package startervalley.backend.repository.notice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeListDto;
import startervalley.backend.admin.dto.notice.NoticeRequest;
import startervalley.backend.entity.Notice;
import startervalley.backend.entity.QAdminUser;
import startervalley.backend.entity.QNotice;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QNotice notice = QNotice.notice;
    private final QAdminUser adminUser = QAdminUser.adminUser;

    @Override
    public NoticeDto findOneById(Long id) {
        Notice noticeEntity = queryFactory.selectFrom(notice)
                .leftJoin(notice.adminUser, adminUser).fetchJoin()
                .where(notice.id.eq(id))
                .fetchFirst();

        return NoticeDto.builder()
                .id(noticeEntity.getId())
                .title(noticeEntity.getTitle())
                .content(noticeEntity.getContent())
                .postedBy(noticeEntity.getAdminUser().getName())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }

    @Override
    public List<NoticeListDto> findAllOrderById(String orderBy) {

        List<Notice> notices = queryFactory.selectFrom(notice)
                .leftJoin(notice.adminUser, adminUser).fetchJoin()
                .orderBy(notice.id.desc())
                .fetch();

        return notices.stream().map(notice -> NoticeListDto.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .postedBy(notice.getAdminUser().getName())
                        .createdDate(notice.getCreatedDate())
                        .build())
                .toList();
    }

    @Override
    public void updateById(Long id, NoticeRequest request) {
        queryFactory.update(notice)
                .set(notice.title, request.getTitle())
                .set(notice.content, request.getContent())
                .where(notice.id.eq(id))
                .execute();
    }

    @Override
    public void deleteOneById(Long id) {
        queryFactory.delete(notice)
                .where(notice.id.eq(id))
                .execute();
    }
}
