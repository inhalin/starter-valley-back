package startervalley.backend.repository.inquiry;

import startervalley.backend.entity.Inquiry;

import java.util.List;

public interface InquiryRepositoryCustom {

    List<Inquiry> findAllOrderBy(String dir);
}
