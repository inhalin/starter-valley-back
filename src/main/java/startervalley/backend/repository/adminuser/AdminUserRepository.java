package startervalley.backend.repository.adminuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, AdminUserRepositoryCustom {

    boolean existsByUsername(String username);

    AdminUser findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update AdminUser u set u.refreshToken = :token where u.id = :id")
    void updateRefreshToken(@Param("id") Long id, @Param("token") String token);

    @Transactional
    @Modifying
    @Query("update AdminUser u set u.refreshToken = null where u.username = :username")
    void deleteRefreshToken(@Param("username") String username);
}
