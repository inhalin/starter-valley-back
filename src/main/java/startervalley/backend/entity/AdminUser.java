package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
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

    @Builder
    public AdminUser(Long id, String username, String password, String name, String email, String phone, Role role, UserProfile profile, String refreshToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.profile = profile;
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String token) {
        this.refreshToken = token;
    }
}