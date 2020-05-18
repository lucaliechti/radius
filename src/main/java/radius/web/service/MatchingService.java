package radius.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedMatching;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.util.SupplierUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import radius.*;
import radius.data.form.MeetingFeedbackForm;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.MatchingRepository;
import radius.data.repository.UserRepository;
import radius.web.components.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching.DEFAULT_OPTIONS;
import static org.jgrapht.alg.matching.blossom.v5.ObjectiveSense.MAXIMIZE;

@Slf4j
@RequiredArgsConstructor
@Service
@PropertySource("classpath:config/profile.properties")
public class MatchingService {

    private final UserRepository userRepo;
    private final MatchingRepository matchRepo;
    private final MessageSource messageSource;
    private final EmailService emailService;
    private final JavaMailSenderImpl matchingMailSender;
    private final CountrySpecificProperties countryProperties;
    private final ConfigService configService;
    private final ProfileDependentProperties profileProperties;

    private final String DELETED = "DELETED";

    @Scheduled(cron = "${matching.schedule}")
    public void matchUsers() {
        if(configService.matchingActive()) {
            if(configService.specialActive()) {
                match(MatchingMode.SPECIAL);
            }
            match(MatchingMode.REGULAR);
        }
    }

    private void match(MatchingMode mode) {
        List<User> usersToMatch = userRepo.matchableUsers();
        List<Edge> edges = allEdges(usersToMatch, mode);
        MatchingAlgorithm.Matching<String, DefaultWeightedEdge> matching = calculateMatching(edges);
        matching.getEdges().forEach(e -> applyMatch(e, mode));
        log.info("Matched  " + matching.getEdges().size() * 2 + " out of " + usersToMatch.size() + " waiting users."
            + " (Mode = " + mode + ")");
    }

    private void applyMatch(DefaultWeightedEdge edge, MatchingMode mode) {
        String[] matchedUsers = edge.toString().substring(1, edge.toString().length()-1).split(" : ");
        User user1 = userRepo.findUserByEmail(matchedUsers[0]);
        User user2 = userRepo.findUserByEmail(matchedUsers[1]);
        persistMatch(new UserPair(user1, user2), mode);
        if(profileProperties.isSendEmails()) {
            emailUserAboutMatch(user1, user2, mode);
            emailUserAboutMatch(user2, user1, mode);
        } else {
            log.info("Suppressing email to " + user1.getEmail() + " and " + user2.getEmail() + ".");
        }
    }

    private MatchingAlgorithm.Matching<String, DefaultWeightedEdge> calculateMatching(List<Edge> edges) {
        DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> matchingGraph = new DefaultUndirectedWeightedGraph<>
                (DefaultWeightedEdge.class);
        matchingGraph.setVertexSupplier(SupplierUtil.createStringSupplier());
        edges.forEach(e -> Graphs.addEdgeWithVertices(matchingGraph, e.getStart(), e.getEnd(), e.getWeight()));
        KolmogorovWeightedMatching<String, DefaultWeightedEdge> weightedMatching = new KolmogorovWeightedMatching<>
                (matchingGraph, DEFAULT_OPTIONS, MAXIMIZE);
        return weightedMatching.getMatching();
    }

    private List<Edge> allEdges(List<User> users, MatchingMode mode) {
        Instant now = Instant.now();
        Map<String, Set<String>> existingMatches = alreadyMatched();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (existingMatches.containsKey(users.get(i).getEmail()) &&
                        existingMatches.get(users.get(i).getEmail()).contains(users.get(j).getEmail())) {
                    continue;
                }
                Edge.optionalFromUserPair(
                        new UserPair(users.get(i), users.get(j)),
                        now,
                        configService.matchingFactorWaitingTime(),
                        configService.matchingMinimumDisagreementsRegular(),
                        configService.matchingMinimumDisagreementsSpecial(),
                        mode)
                    .ifPresent(edges::add);
            }
        }
        return edges;
    }

    private Map<String, Set<String>> alreadyMatched() {
        Map<String, Set<String>> result = new HashMap<>();
        for (HalfEdge halfEdge : allMatches()) {
            if (!result.containsKey(halfEdge.getEmail1())) {
                result.put(halfEdge.getEmail1(), new HashSet<>());
            }
            result.get(halfEdge.getEmail1()).add(halfEdge.getEmail2());
        }
        return result;
    }

    public void deactivateOldMatchesFor(String email) {
        matchRepo.deactivateOldMatchesFor(email);
    }

    public void updateMatchesAfterMeeting(User user, MeetingFeedbackForm feedbackForm) {
        if(feedbackForm.isConfirmed()) {
            confirmHalfEdge(user.getEmail());
        } else {
            unconfirmHalfEdge(user.getEmail());
        }
        deactivateOldMatchesFor(user.getEmail());
    }

    private void confirmHalfEdge(String email) {
        matchRepo.confirmHalfEdge(email);
    }

    private void unconfirmHalfEdge(String email) {
        matchRepo.unconfirmHalfEdge(email);
    }

    public List<HalfEdge> allMatches() {
        try {
            return matchRepo.allMatches();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<HalfEdge> uniqueOrderedMatches() {
        return allMatches().stream().
                filter(m -> ((!DELETED.equals(m.getEmail1()) && DELETED.equals(m.getEmail2()))
                        || (m.getEmail1().compareToIgnoreCase(m.getEmail2()) < 0 &&
                        !(DELETED.equals(m.getEmail1()) && !DELETED.equals(m.getEmail2())))
                )).collect(Collectors.toList());
    }

    public void persistMatch(UserPair userPair, MatchingMode mode) {
        userPair.getUser1().setStatus(User.UserStatus.MATCHED);
        userPair.getUser2().setStatus(User.UserStatus.MATCHED);
        userRepo.updateExistingUser(userPair.getUser1());
        userRepo.updateExistingUser(userPair.getUser2());
        deactivateOldMatchesFor(userPair.getUser1().getEmail());
        deactivateOldMatchesFor(userPair.getUser2().getEmail());
        matchRepo.createMatch(userPair, mode);
    }

    public void emailUserAboutMatch(User user, User match, MatchingMode mode) {
        try {
            UserPair up = new UserPair(user, match);
            String matchingLanguages = matchingLanguages(up, user);
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    messageSource.getMessage("email.match.title", new Object[]{}, user.preferredLocale()),
                    messageSource.getMessage("email.match.content", new Object[]{
                            user.getFirstname(),
                            user.getLastname(),
                            match.getFirstname(),
                            match.getLastname(),
                            String.join(", ", countryProperties.prettyLocations(new ArrayList<>(up.commonLocations()))),
                            matchingLanguages,
                            match.getEmail(),
                            disagreedUponQuestions(user, match, mode, user.preferredLocale())},
                            user.preferredLocale()),
                    matchingMailSender
            );
            log.info("Successfully sent matching email to " + user.getEmail());
        } catch (Exception e) {
            log.error("Could not send matching email to " + user.getEmail());
        }
    }

    public List<HalfEdge> allMatchesForUser(String email) {
        return matchRepo.allMatchesForUser(email);
    }

    private String matchingLanguages(UserPair up, User user) {
        ArrayList<String> languages = new ArrayList<>();
        for(String lang : up.commonLanguages()){
            languages.add(messageSource.getMessage("language." + lang, new Object[]{}, user.preferredLocale()));
        }
        return String.join(", ", languages);
    }

    private String disagreedUponQuestions(User user, User match, MatchingMode mode, Locale loc) {
        int position = loc.getLanguage().equals("de") ? 0 : loc.getLanguage().equals("fr") ? 1 : 2;
        List<String> questions = new ArrayList<>();
        List<Integer> disagreed = disagreedQuestions(user, match, mode);
        List<List<String>> questionTexts = mode == MatchingMode.REGULAR ? configService.getQuestionForm().getRegularQuestions() : configService.getQuestionForm().getSpecialQuestions();
        disagreed.forEach(q -> questions.add("- " + questionTexts.get(position).get(q)));
        return String.join("\n", questions);
    }

    private List<Integer> disagreedQuestions(User user1, User user2, MatchingMode mode) {
        ArrayList<Integer> disagree = new ArrayList();
        List<User.TernaryAnswer> answers1 = mode.equals(MatchingMode.REGULAR) ? user1.getRegularanswers() :
                user1.getSpecialanswers();
        List<User.TernaryAnswer> answers2 = mode.equals(MatchingMode.REGULAR) ? user2.getRegularanswers() :
                user2.getSpecialanswers();
        for(int i = 0; i < answers1.size(); i++) {
            if((answers1.get(i) == User.TernaryAnswer.TRUE && answers2.get(i) == User.TernaryAnswer.FALSE)
            || (answers1.get(i) == User.TernaryAnswer.FALSE && answers2.get(i) == User.TernaryAnswer.TRUE)) {
                disagree.add(i);
            }
        }
        return disagree;
    }
}
