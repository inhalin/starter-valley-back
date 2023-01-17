package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "admin_user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AdminUser adminUser;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Notice(Long id, AdminUser adminUser, String title, String content) {
        this.id = id;
        this.adminUser = adminUser;
        this.title = title;
        this.content = content;
    }
}
