package startervalley.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lunchbus extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "lunchbus_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User driver;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer occupancy;
    private Integer count;
    private String storeName;
    private String storeUrl;

    @Column(columnDefinition = "tinyint(1)")
    private Boolean active;
}
