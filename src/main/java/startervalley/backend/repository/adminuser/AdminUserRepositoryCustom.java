package startervalley.backend.repository.adminuser;

public interface AdminUserRepositoryCustom {

    void changePassword(Long id, String newPassword);
}
