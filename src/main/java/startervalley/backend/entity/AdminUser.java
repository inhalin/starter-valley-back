package startervalley.backend.entity;

import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@DynamicInsert
public class AdminUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'ADMIN'", nullable = false)
    private Role role = Role.ADMIN;

    @Embedded
    private UserProfile profile = new UserProfile();

    private String refreshToken;
}