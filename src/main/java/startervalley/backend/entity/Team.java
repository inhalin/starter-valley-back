package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
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

    @ManyToOne(fetch = LAZY)
    private Generation generation;

    @Builder
    public Team(Long id, String name, String description, String notionUrl, String releaseUrl, List<User> users, Generation generation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.notionUrl = notionUrl;
        this.releaseUrl = releaseUrl;
        this.users = users;
        this.generation = generation;
    }
}
