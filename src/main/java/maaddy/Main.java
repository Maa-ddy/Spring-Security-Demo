package maaddy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import maaddy.util.JWTprovider;

@SpringBootApplication
public class Main {
	@Autowired static PasswordEncoder encoder;
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		System.out.println("ok!");
	}
}
