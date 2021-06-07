package ca.sheridancollege.doricha.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.doricha.database.DatabaseAccess;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Find user based on username (read email)
		ca.sheridancollege.doricha.beans.User user = da.findUserAccount(username);
		
		// If user doesn't exist, throw an exception
		if (user == null) {
			System.out.println("User not found:" + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		
		// Get list of roles for that user
		List<String> roleNames = da.getRolesById(user.getUserId());
		
		// Change the list of user's roles into a list of GrantedAuthority
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				grantList.add(new SimpleGrantedAuthority(role));
			}
		}
		
		// Create user based on the info above
		UserDetails userDetails = (UserDetails)new User(user.getEmail(), user.getEncryptedPassword(), grantList);
		return userDetails;
	}
	
}
