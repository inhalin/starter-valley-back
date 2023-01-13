package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    @Builder
    public Team(String name, String description, String notionUrl, String releaseUrl) {
        this.name = name;
        this.description = description;
        this.notionUrl = notionUrl;
        this.releaseUrl = releaseUrl;
    }
}
