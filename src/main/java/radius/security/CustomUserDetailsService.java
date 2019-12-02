package radius.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import radius.User;
import radius.data.repository.UserRepository;

@Service("postgresUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
    public CustomUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }
	
	@Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            final User user = userRepository.findUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            final List<String> roles = userRepository.findAuthoritiesByEmail(email);
            for(String role : roles) {
            	grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
        } catch (final Exception e) {
            throw new AuthenticationServiceException("error");
        }
	}
}
