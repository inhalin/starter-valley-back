package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    private String notionUrl;
    private String releaseUrl;

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    @Builder
    public Team(String name, String description, String notionUrl, String releaseUrl, List<User> users) {
        this.name = name;
        this.description = description;
        this.notionUrl = notionUrl;
        this.releaseUrl = releaseUrl;
        this.users = users;
    }
}
