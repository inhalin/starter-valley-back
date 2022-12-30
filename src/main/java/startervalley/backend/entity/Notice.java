package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "admin_user_id")
    private AdminUser adminUser;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "notice", cascade = ALL, orphanRemoval = true)
    private List<NoticeImage> images = new ArrayList<>();

    @Builder
    public Notice(Long id, AdminUser adminUser, String title, String content, List<NoticeImage> images) {
        this.id = id;
        this.adminUser = adminUser;
        this.title = title;
        this.content = content;
        this.images = images;
    }
}
