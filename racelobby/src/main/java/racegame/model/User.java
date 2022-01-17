package racegame.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user1")
@Data
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Column(nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;
}