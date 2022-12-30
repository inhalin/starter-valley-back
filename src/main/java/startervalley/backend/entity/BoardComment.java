package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public BoardComment(Long id, String content, User user, Board board) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.board = board;
    }

    public void setNullBoard() {
        this.board = null;
    }

    public void update(String content) {
        this.content = content;
    }
}
