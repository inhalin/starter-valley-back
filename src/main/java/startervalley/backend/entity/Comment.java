package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import startervalley.backend.dto.request.CommentRequestDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentTag> commentTagList = new ArrayList<>();

    @Builder
    public Comment(Long id, String description, User user, Store store) {
        this.id = id;
        this.description = description;
        this.user = user;
        this.store = store;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.description = commentRequestDto.getContent();
    }
}
