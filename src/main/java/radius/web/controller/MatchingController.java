package radius.web.controller;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radius.HalfEdge;
import radius.User;
import radius.UserPair;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUserRepository;
import radius.data.StaticResourceRepository;
import radius.web.components.EmailService;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/")
public class MatchingController {
	private final JDBCUserRepository userRepo;
	private final EmailService emailService;
	private final StaticResourceRepository staticRepo;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	public MatchingController(JDBCUserRepository _userRepo, EmailService _emailService, JDBCStaticResourceRepository _staticRepo) {
		this.userRepo = _userRepo;
		this.emailService = _emailService;
		this.staticRepo = _staticRepo;
	}

	/**
	 * REST class representing a pair of users which could be matched, together with a weight
	 */
	public static class Edge {
		private final String start;
		private final String end;
		private final double weight;

		public Edge(String start, String end, double weight) {
			this.start = start;
			this.end = end;
			this.weight = weight;
		}

		public String getStart() {
			return start;
		}

		public String getEnd() {
			return end;
		}

		public double getWeight() {
			return weight;
		}

		public static Optional<MatchingController.Edge> optFromUserPair(UserPair userPair, Instant now) {
			if (userPair.compatible()) {
				return Optional.of(new MatchingController.Edge(userPair.user1().getEmail(), userPair.user2().getEmail(),
						userPair.numberOfDisagreements() + userPair.totalDaysUnmodified(now)));
			} else {
				return Optional.empty();
			}
		}
	}

	/**
	 * REST class containing the request to match two users
	 */
	public static class Match {
		private String start;
		private String end;

		public Match() {
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

		public String toString() {
			return String.format("%s - %s", this.getStart(), this.getEnd());
		}
	}

	/**
	 * REST class containing the response for a match request
	 */
	public static class MatchResponse {
		private final int code;
		private final String description;
		private final String start;
		private final String end;

		public MatchResponse(Match match, int code, String description) {
			this.start = match.getStart();
			this.end = match.getEnd();
			this.code = code;
			this.description = description;
		}

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public String getStart() {
			return start;
		}

		public String getEnd() {
			return end;
		}
	}

	@RequestMapping(value = "/graph", method = GET)
	public ResponseEntity<List<Edge>> graph() {
		List<User> usersToMatch = ImmutableList.copyOf(userRepo.usersToMatch());
		Instant now = Instant.now();

		List<Edge> edges = allEdges(usersToMatch, now);
		return new ResponseEntity<>(edges, HttpStatus.OK);
	}

	private Map<String, Set<String>> alreadyMatched() {
		Map<String, Set<String>> result = new HashMap<>();

		for (HalfEdge halfEdge : userRepo.allMatches()) {
			if (!result.containsKey(halfEdge.email1())) {
				result.put(halfEdge.email1(), new HashSet<>());
			}

			result.get(halfEdge.email1()).add(halfEdge.email2());
		}

		return result;
	}

	private List<Edge> allEdges(List<User> users, Instant now) {
		Map<String, Set<String>> existingMatches = alreadyMatched();

		List<Edge> edges = new ArrayList<>();
		for (int i = 0; i < users.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (existingMatches.containsKey(users.get(i).getEmail()) &&
						existingMatches.get(users.get(i).getEmail()).contains(users.get(j).getEmail())) {
					// users were already matched before
					continue;
				}

				Edge.optFromUserPair(UserPair.of(users.get(i), users.get(j)), now).ifPresent(edges::add);
			}
		}
		return ImmutableList.copyOf(edges);
	}

	@RequestMapping(value = "/match", method = POST)
	public ResponseEntity<List<MatchResponse>> match(@RequestBody List<Match> matches) {

		List<MatchResponse> responses = matches
				.stream().map(this::singleMatch).collect(Collectors.toList());

		return new ResponseEntity<>(responses, HttpStatus.OK);
	}

	/**
	 * Process a single matching request.
	 */
	private MatchResponse singleMatch(final Match match) {
		User user1 = userRepo.findUserByEmail(match.getStart());
		User user2 = userRepo.findUserByEmail(match.getEnd());

		if (user1 == null || user2 == null) {
			return new MatchResponse(match, 404, "One of the users was not found.");
		}

		if (user1.isBanned() || user2.isBanned()) {
			return new MatchResponse(match, 403, "One of the users is banned.");
		}

		if (user1.getStatus() != User.userStatus.WAITING || user2.getStatus() != User.userStatus.WAITING) {
			return new MatchResponse(match, 409, "One of the users is not in WAITING state.");
		}

		UserPair userPair = UserPair.of(user1, user2);

		if (!userPair.compatible()) {
			return new MatchResponse(match, 409, "Conflict: The users are incompatible.");
		}

		userRepo.match(userPair);

		////////TEST////////////
		emailUserAboutMatch(user1, user2, usersLocale(user1));
		emailUserAboutMatch(user2, user1, usersLocale(user1));
		////////END OF TEST/////
		
//		emailUserAboutMatch(user1, user2);
//		emailUserAboutMatch(user2, user1);

		return new MatchResponse(match, 201, "The users were successfully matched.");
	}
	
	////////////////////////
	////////TEST////////////
	////////////////////////
	@SuppressWarnings("static-access")
	private void emailUserAboutMatch(User user, User match, Locale locale) {
		try {
			UserPair up = UserPair.of(user, match);
			String matchingLanguages = matchingLanguages(up, user);
			emailService.sendSimpleMessage("info", 
					user.getEmail(), 
					messageSource.getMessage("email.match.title", new Object[]{}, usersLocale(user)), 
					messageSource.getMessage("email.match.content", new Object[]{user.getFirstname(), user.getLastname(), match.getFirstname(), match.getLastname(), String.join(", ", staticRepo.prettyLocations(new ArrayList<Integer>(up.commonLocations()))), matchingLanguages, messageSource.getMessage("status.modi." + User.convertModusToString(up.commonModus(user.getModus(), match.getModus()).get()), new Object[]{}, usersLocale(user)), match.getEmail()}, usersLocale(user)));
		} catch (Exception ignored) {
		}
	}
	
	private String matchingLanguages(UserPair up, User user) {
		ArrayList<String> languages = new ArrayList<String>();
		for(String lang : up.commonLanguages()){
			languages.add(messageSource.getMessage("language." + lang, new Object[]{}, usersLocale(user)));
		}
		return String.join(", ", languages);
	}

	private Locale usersLocale(User u) {
		if(u.getLanguages().contains("DE")){
			return new Locale("de");
		}
		else if(u.getLanguages().contains("FR")){
			return new Locale("fr");
		}
		else {
			return new Locale("en");
		}
	}
}
