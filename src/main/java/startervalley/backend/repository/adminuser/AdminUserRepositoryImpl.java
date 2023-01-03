package startervalley.backend.repository.adminuser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.QAdminUser;

@Repository
@RequiredArgsConstructor
public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QAdminUser adminUser = QAdminUser.adminUser;

    @Override
    public void changePassword(Long id, String newPassword) {
        queryFactory.update(adminUser)
                .set(adminUser.password, newPassword)
                .where(adminUser.id.eq(id))
                .execute();
    }
}
