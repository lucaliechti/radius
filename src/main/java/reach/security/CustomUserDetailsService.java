package reach.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import reach.User;
import reach.data.UserRepository;

@Service//("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
    public CustomUserDetailsService() {
        super();
    }
	
	@Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            final User user = userRepository.findUserByEmail(email);
            System.out.println(user);
            if (user == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }
            
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            final List<String> roles = userRepository.findAuthoritiesByEmail(email);
            for(String role : roles) {
            	grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            System.out.println("in the customUserDetailsService");
            System.out.println("email: " + user.getEmail());
            System.out.println("pass: " + user.getPassword());
            System.out.println("auth: " + grantedAuthorities);
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
	}
}
