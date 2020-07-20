package maaddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import maaddy.model.Person;
import maaddy.model.UserDetailsImpl;
import maaddy.repository.PersonRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired private PersonRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		Person user = this.userRepo.findByName(username);
		if (user == null) {
			System.out.println("exception here");
			throw new UsernameNotFoundException("User" + username + "is not found!");
		}
		UserDetails userDetails = new UserDetailsImpl(user);
		return userDetails;
	}
}
