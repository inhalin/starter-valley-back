package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "tinyint(1)")
    @ColumnDefault(value = "0")
    private boolean anonymous;

    @Enumerated(value = STRING)
    private InquiryTarget target;

    @Builder
    public Inquiry(Long id, Long userId, String title, String content, boolean anonymous, InquiryTarget target) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.anonymous = anonymous;
        this.target = target;
    }
}
