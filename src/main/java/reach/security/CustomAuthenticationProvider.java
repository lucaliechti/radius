package reach.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import reach.User;
import reach.data.UserRepository;

//@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
      String username = auth.getName();
      String password = auth.getCredentials().toString();
      
      UserDetails u = this.getUserDetailsService().loadUserByUsername(username); //userRepository.findUserByEmail(username);
      if(u == null) {
    	  return null;
  	  }
      List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
      final List<String> roles = userRepository.findAuthoritiesByEmail(username); //checken, ob 1 Resultat. Bereits im Repo - throw new BadCredentialsException("Invalid username or password");
      for(String role : roles) {
      	grantedAuthorities.add(new SimpleGrantedAuthority(role));
      }
      
      System.out.println("in the custom authentication provider");
      System.out.println("name: " + auth.getName());
      System.out.println("principal: " + auth.getPrincipal());
      System.out.println("cred: " + auth.getCredentials());
      System.out.println("details: " + auth.getDetails().toString());
      System.out.println("---------------");
      System.out.println("name from db: " + u.getUsername());
      System.out.println("pw from db: " + u.getPassword());
      System.out.println("authorities from db: " + roles);

      //final Authentication result = super.authenticate(auth);
      return new UsernamePasswordAuthenticationToken(u, password, grantedAuthorities); //new User(username, password) instead of u
  }

  @Override
  public boolean supports(Class<?> authentication) {
      return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}