package maaddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import maaddy.model.Authority;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long> {
	public Authority findByName(String name);
}
