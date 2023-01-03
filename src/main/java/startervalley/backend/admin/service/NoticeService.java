package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeListDto;
import startervalley.backend.admin.dto.notice.NoticeRequest;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.AdminUser;
import startervalley.backend.entity.Notice;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.adminuser.AdminUserRepository;
import startervalley.backend.repository.notice.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final AdminUserRepository adminUserRepository;
    private final NoticeRepository noticeRepository;

    public List<NoticeListDto> getAll() {
        return noticeRepository.findAllOrderById(Sort.Direction.DESC.name());
    }

    public NoticeDto getOne(Long id) {
        return noticeRepository.findOneById(id);
    }

    @Transactional
    public BasicResponse update(Long id, NoticeRequest request) {
        noticeRepository.updateById(id, request);

        return BasicResponse.of(id, "공지사항이 정상적으로 수정되었습니다.");
    }

    @Transactional
    public BasicResponse deleteOne(Long id) {
        noticeRepository.deleteOneById(id);

        return BasicResponse.of(id, "공지사항이 정상적으로 삭제되었습니다.");
    }

    public BasicResponse createOne(Long adminUserId, NoticeRequest request) {
        AdminUser adminUser = adminUserRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", adminUserId.toString()));

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .adminUser(adminUser)
                .build();

        noticeRepository.save(notice);

        return BasicResponse.of(notice.getId(), "공지사항이 정상적으로 등록되었습니다.");
    }
}
