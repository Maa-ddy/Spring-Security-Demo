package maaddy.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Person {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private long id;
	private String name;
	private String password;
	private boolean active;
	private String email;
	@ManyToMany @JsonBackReference @JoinTable(
			name = "person_authority",
			joinColumns = @JoinColumn(name="person_id"),
			inverseJoinColumns = @JoinColumn(name="authority_id")
	) private List<Authority> authorities;
}
