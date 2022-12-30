package startervalley.backend.repository.adminuser;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, AdminUserRepositoryCustom {
}
