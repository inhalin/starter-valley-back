package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Bamboo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String randomName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Bamboo(Long id, String content, String randomName, User user) {
        this.id = id;
        this.content = content;
        this.randomName = randomName;
        this.user = user;
    }
}
