package maaddy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import maaddy.model.AuthenticationRequest;
import maaddy.model.AuthenticationResponse;
import maaddy.model.Authority;
import maaddy.model.AuthorityDTO;
import maaddy.model.Person;
import maaddy.model.PersonDTO;
import maaddy.model.UserDetailsImpl;
import maaddy.repository.AuthorityRepo;
import maaddy.repository.PersonRepo;
import maaddy.util.JWTprovider;
import maaddy.util.PasswordStorage;
import maaddy.util.PasswordStorage.CannotPerformOperationException;

@RestController
public class EndPoint {
	@Autowired private PersonRepo personRepo;
	@Autowired private AuthorityRepo authorityRepo;
	@Autowired private JWTprovider jwtUtil;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private UserDetailsService userDetailsService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@PostMapping("/authenticate")
	private AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getUsername(), request.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			System.out.println("failed login attempt.");
			throw new BadCredentialsException("Incorrect Username and/or Password.");
		}
		UserDetails userDetails = 
				this.userDetailsService.loadUserByUsername(request.getUsername());
		String jwt = this.jwtUtil.generateToken(userDetails);
		return new AuthenticationResponse(jwt);
	}
	
	@PostMapping("/register")
	private ResponseEntity<String> register(@RequestBody PersonDTO user) {
		if (this.personRepo.existsByName(user.getName()))
			return new ResponseEntity<>("Username already taken.", HttpStatus.BAD_REQUEST);
		if (this.personRepo.existsByEmail(user.getEmail()))
			return new ResponseEntity<>("e-mail already taken.", HttpStatus.BAD_REQUEST);
		Person newUser = new Person();
		newUser.setName(user.getName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
		newUser.setActive(true);
		List<Authority> authorities = new ArrayList<>();
		for (AuthorityDTO authority: user.getAuthorities()) {
			Authority auth = this.authorityRepo.findByName(authority.getName());
			authorities.add(auth);
		}
		newUser.setAuthorities(authorities);
		this.personRepo.save(newUser);
		return new ResponseEntity<>("Successfully registered!", HttpStatus.ACCEPTED);
	}

	@GetMapping("/admin/all")
	private List<Person> getAll() {
		return this.personRepo.findAll();
	}
	
	@GetMapping("/roles")
	private List<Authority> getRoles() {
		return this.authorityRepo.findAll();
	}
	
	@PostMapping("/admin")
	private Person add(@RequestBody Person user) {
		return this.personRepo.save(user);
	}
	
	@DeleteMapping("/admin")
	private void delete(@RequestBody Person user) {
		this.personRepo.delete(user);
	}
	
	@GetMapping("/public")
	private String publicView() {
		System.out.println(passwordEncoder.encode("pass"));
		return "<h2>Public things everyone can see</h2>";
	}
	
	@GetMapping("/admin")
	private String adminView() {
		return "<h2>Admin stuff</h2>";
	}
	
	@GetMapping("/user")
	private String userView() {
		return "<h2>User thingies</h2>";
	}
}
