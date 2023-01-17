package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeListDto;
import startervalley.backend.admin.dto.notice.NoticeRequest;
import startervalley.backend.admin.dto.notice.NoticeResponse;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.AdminUser;
import startervalley.backend.entity.Notice;
import startervalley.backend.event.alert.WebsocketAlertEventPublisher;
import startervalley.backend.event.alert.dto.AlertDto;
import startervalley.backend.event.alert.dto.AlertType;
import startervalley.backend.event.webhook.SlackWebhookDto;
import startervalley.backend.event.webhook.SlackWebhookEventPublisher;
import startervalley.backend.exception.CustomValidationException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.adminuser.AdminUserRepository;
import startervalley.backend.repository.notice.NoticeRepository;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

import static startervalley.backend.util.PaginationConstants.ALLOWED_PAGE_SIZE;
import static startervalley.backend.util.PaginationConstants.MINIMUM_PAGE_NUMBER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final AdminUserRepository adminUserRepository;
    private final NoticeRepository noticeRepository;
    private final WebsocketAlertEventPublisher alertEventPublisher;
    private final SlackWebhookEventPublisher webhookEventPublisher;

    public NoticeResponse getAll(int page, int size, String sort, String dir) {
        try {
            validatePaginationOption(page, size, sort, dir);
        } catch (ValidationException e) {
            throw new CustomValidationException(e.getMessage());
        }

        Sort sorting = dir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sorting);

        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        List<NoticeListDto> dtos = noticePage.getContent()
                .stream()
                .map(notice -> NoticeListDto.builder()
                        .id(notice.getId()).
                        title(notice.getTitle())
                        .postedBy(notice.getAdminUser().getName())
                        .createdDate(notice.getCreatedDate())
                        .build())
                .toList();

        return NoticeResponse.builder()
                .notice(dtos)
                .page(noticePage.getNumber() + 1)
                .size(noticePage.getSize())
                .totalElement(noticePage.getTotalElements())
                .totalPages(noticePage.getTotalPages())
                .last(noticePage.isLast())
                .build();
    }

    private void validatePaginationOption(int page, int size, String sort, String dir) throws ValidationException {
        if (page < MINIMUM_PAGE_NUMBER) {
            throw new ValidationException("페이지 번호는 1 이상어야 합니다.");
        }

        if (Arrays.stream(ALLOWED_PAGE_SIZE).noneMatch(s -> s == size)) {
            throw new ValidationException("페이지당 보여줄 개수는 5, 10, 30, 50, 100 중에서 지정해야 합니다.");
        }

        if (!(sort.equalsIgnoreCase("id") || sort.equalsIgnoreCase("title"))) {
            throw new ValidationException("정렬 기준은 id, title 중에서 지정해야 합니다.");
        }

        if (!(dir.equalsIgnoreCase(Sort.Direction.ASC.name()) || dir.equalsIgnoreCase(Sort.Direction.DESC.name()))) {
            throw new ValidationException("정렬 순서는 asc, desc 중에서 지정해야 합니다.");
        }
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

    @Transactional
    public BasicResponse createOne(Long adminUserId, NoticeRequest request) {
        AdminUser adminUser = adminUserRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", adminUserId.toString()));

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .adminUser(adminUser)
                .build();

        noticeRepository.save(notice);
        AlertDto alertDto = new AlertDto(AlertType.NOTICE, notice.getId(), notice.getTitle());
        alertEventPublisher.publishEvent(alertDto);
        webhookEventPublisher.publishEvent(SlackWebhookDto.of(notice));

        return BasicResponse.of(notice.getId(), "공지사항이 정상적으로 등록되었습니다.");
    }
}
