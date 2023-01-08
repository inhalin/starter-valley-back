package startervalley.backend.repository.inquiry;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.Inquiry;
import startervalley.backend.entity.QInquiry;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QInquiry inquiry = QInquiry.inquiry;

    @Override
    public List<Inquiry> findAllOrderBy(String dir) {
        return queryFactory.selectFrom(inquiry)
                .orderBy(inquiry.id.desc())
                .fetch();
    }
}
