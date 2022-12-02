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

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndProvider(String email, AuthProvider provider);
    Optional<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.refreshToken = :token where u.id = :userId")
    void updateRefreshToken(@Param("userId") Long userId, @Param("token") String token);

    @Transactional
    @Modifying
    @Query("update User u set u.name = :name, u.devpart = :devpart, u.generation = :generation, u.profile.intro = :intro where u.id = :userId")
    void signup(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("devpart") Devpart devpart,
            @Param("generation") Generation generation,
            @Param("intro") String intro);

    @Query("select u from User u inner join u.generation g where g.id = :generationId")
    List<User> findAllByGenerationId(@Param("generationId") Long generationId);
}
