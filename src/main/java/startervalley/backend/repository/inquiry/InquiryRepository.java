package startervalley.backend.repository.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Inquiry;
import startervalley.backend.entity.InquiryTarget;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryRepositoryCustom {

    List<Inquiry> findByTarget(InquiryTarget target);

    List<Inquiry> findByUserIdAndTarget(Long userId, InquiryTarget target);

    List<Inquiry> findByUserIdOrderByIdDesc(Long userId);
}
