package maaddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import maaddy.model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {
	public Person findByName(String name);
	public boolean existsByName(String name);
	public boolean existsByEmail(String email);
}
