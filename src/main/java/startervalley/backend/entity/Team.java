package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
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
    @JoinColumn(name = "generation_id")
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

    public void update(String name, String description, String notionUrl, String releaseUrl) {
        this.name = name;
        this.description = description;
        this.notionUrl = notionUrl;
        this.releaseUrl = releaseUrl;
    }
}
