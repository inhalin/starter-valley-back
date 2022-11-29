package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.AuthProvider;
import startervalley.backend.entity.Devpart;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndProvider(String email, AuthProvider provider);
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.refreshToken = :token where u.username = :username")
    void updateRefreshToken(@Param("username") String username, @Param("token") String token);

    @Transactional
    @Modifying
    @Query("update User u set u.name = :name, u.devpart = :devpart, u.generation = :generation where u.username = :username")
    void signUpWithAdditionalInfo(
            @Param("username") String username,
            @Param("name") String name,
            @Param("devpart") Devpart devpart,
            @Param("generation") Generation generation);
}
