package startervalley.backend.repository.notice;

import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeListDto;
import startervalley.backend.admin.dto.notice.NoticeRequest;

import java.util.List;

public interface NoticeRepositoryCustom {

    NoticeDto findOneById(Long id);

    List<NoticeListDto> findAllOrderById(String orderBy);

    void updateById(Long id, NoticeRequest request);

    void deleteOneById(Long id);
}
