package startervalley.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class NoticeImage extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;
}
