package radius.web.service;

import com.google.common.collect.ImmutableList;
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

import static org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching.DEFAULT_OPTIONS;
import static org.jgrapht.alg.matching.blossom.v5.ObjectiveSense.MAXIMIZE;

@Slf4j
@Service
@PropertySource("classpath:config/profile.properties")
public class MatchingService {

    private UserRepository userRepo;
    private MatchingRepository matchRepo;
    private MessageSource messageSource;
    private EmailService emailService;
    private JavaMailSenderImpl matchingMailSender;
    private CountrySpecificProperties countryProperties;
    private ConfigurationProperties configProperties;
    private ProfileDependentProperties profileProperties;
    private RealWorldProperties realWorld;

    public MatchingService(JDBCUserRepository userRepo, MatchingRepository matchRepo, MessageSource messageSource,
                           EmailService emailService, JavaMailSenderImpl matchingMailSender,
                           CountrySpecificProperties countryProperties, ConfigurationProperties configProperties,
                           ProfileDependentProperties profileProperties, RealWorldProperties realWorld) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.messageSource = messageSource;
        this.emailService = emailService;
        this.matchingMailSender = matchingMailSender;
        this.countryProperties = countryProperties;
        this.configProperties = configProperties;
        this.profileProperties = profileProperties;
        this.realWorld = realWorld;
    }

    @Scheduled(cron = "${matching.schedule}")
    public void matchUsers() {
        if(configProperties.isActive()) {
            if(realWorld.isSpecialIsActive()) {
                match(MatchingMode.SPECIAL);
            }
            match(MatchingMode.REGULAR);
        }
    }

    private void match(MatchingMode mode) {
        List<User> usersToMatch = userRepo.matchableUsers();
        List<Edge> edges = allEdges(usersToMatch, mode);
        MatchingAlgorithm.Matching<String, DefaultWeightedEdge> matching = calculateMatching(edges);
        matching.getEdges().forEach(this::applyMatch);
        log.info("Matched  " + matching.getEdges().size() * 2 + " out of " + usersToMatch.size() + " waiting users."
            + " (Mode = " + mode + ")");
    }

    private void applyMatch(DefaultWeightedEdge edge) {
        String[] matchedUsers = edge.toString().substring(1, edge.toString().length()-1).split(" : ");
        User user1 = userRepo.findUserByEmail(matchedUsers[0]);
        User user2 = userRepo.findUserByEmail(matchedUsers[1]);
        persistMatch(UserPair.of(user1, user2));
        if(profileProperties.isSendEmails()) {
            emailUserAboutMatch(user1, user2);
            emailUserAboutMatch(user2, user1);
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
                        UserPair.of(users.get(i), users.get(j)),
                        now,
                        configProperties.isWaitingTime(),
                        configProperties.getMinDisagreementsRegular(),
                        configProperties.getMinDisagreementsSpecial(),
                        mode)
                    .ifPresent(edges::add);
            }
        }
        return ImmutableList.copyOf(edges);
    }

    private Map<String, Set<String>> alreadyMatched() {
        Map<String, Set<String>> result = new HashMap<>();
        for (HalfEdge halfEdge : allMatches()) {
            if (!result.containsKey(halfEdge.email1())) {
                result.put(halfEdge.email1(), new HashSet<>());
            }
            result.get(halfEdge.email1()).add(halfEdge.email2());
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

    public void persistMatch(UserPair userPair) {
        userPair.user1().setStatus(User.UserStatus.MATCHED);
        userPair.user2().setStatus(User.UserStatus.MATCHED);
        userRepo.updateExistingUser(userPair.user1());
        userRepo.updateExistingUser(userPair.user2());
        matchRepo.createMatch(userPair);
    }

    public void emailUserAboutMatch(User user, User match) {
        try {
            UserPair up = UserPair.of(user, match);
            String matchingLanguages = matchingLanguages(up, user);
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    messageSource.getMessage("email.match.title", new Object[]{}, user.preferredLocale()),
                    messageSource.getMessage("email.match.content", new Object[]{
                            user.getFirstname(),
                            user.getLastname(),
                            match.getFirstname(),
                            match.getLastname(),
                            String.join(", ", countryProperties.prettyLocations(new ArrayList<Integer>(up.commonLocations()))),
                            matchingLanguages,
                            match.getEmail()},
                            user.preferredLocale()),
                    matchingMailSender
            );
        } catch (Exception ignored) { }
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
}
