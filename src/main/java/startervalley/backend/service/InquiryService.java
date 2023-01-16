package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.dto.inquiry.InquiryRequest;
import startervalley.backend.dto.inquiry.InquiryResponse;
import startervalley.backend.entity.Inquiry;
import startervalley.backend.entity.InquiryTarget;
import startervalley.backend.entity.Role;
import startervalley.backend.entity.User;
import startervalley.backend.event.inquiry.InquiryEventPublisher;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.inquiry.InquiryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserService userService;
    private final InquiryEventPublisher eventPublisher;

    @Transactional
    public BasicResponse createOne(Long userId, InquiryRequest request) {

        if (request.getTarget().equals(InquiryTarget.DEVELOPERS) && request.isAnonymous()) {
            throw new IllegalArgumentException("개발자 문의는 실명만 가능합니다.");
        }

        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .anonymous(request.isAnonymous())
                .target(request.getTarget())
                .userId(request.isAnonymous() ? null : userId)
                .build();

        inquiryRepository.save(inquiry);

        if (inquiry.getTarget().equals(InquiryTarget.DEVELOPERS)) {
            eventPublisher.publishEvent(constructSlackMessage(inquiry));
        }

        return BasicResponse.of(inquiry.getId(), "문의가 정상적으로 등록되었습니다.");
    }

    private String constructSlackMessage(Inquiry inquiry) {

        String name = userService.findUserOrThrow(inquiry.getUserId()).getName();

        return "🔔 개발자 문의글이 등록되었습니다.\n\n"
                + "이름: " + name + "\n"
                + "제목: " + inquiry.getTitle() + "\n"
                + "내용: " + inquiry.getContent();
    }

    public InquiryResponse getOne(Long inquiryId, Long userId, Role role) {

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry", "id", inquiryId.toString()));

        if (role.equals(Role.ADMIN)) {
            return InquiryResponse.mapToResponse(inquiry, fetchInquiryOwnerName(inquiry));
        }

        User user = userService.findUserOrThrow(userId);

        if (inquiry.isAnonymous()) {
            throw new IllegalArgumentException("익명 문의는 조회할 수 없습니다.");
        }

        if (!user.getId().equals(inquiry.getUserId())) {
            throw new ResourceAccessException("접근 권한이 없습니다.");
        }

        return InquiryResponse.mapToResponse(inquiry, user.getName());
    }

    public List<InquiryResponse> listWithoutAnonymous(Long userId) {

        User user = userService.findUserOrThrow(userId);

        List<Inquiry> inquiries = inquiryRepository.findByUserIdOrderByIdDesc(user.getId());

        return inquiries.stream()
                .map(inquiry -> InquiryResponse.mapToResponse(inquiry, user.getName()))
                .toList();
    }

    public List<InquiryResponse> listAll() {

        List<Inquiry> inquiries = inquiryRepository.findAllOrderBy(Sort.Direction.DESC.name());

        return inquiries.stream()
                .map(inquiry -> InquiryResponse.mapToResponse(inquiry, fetchInquiryOwnerName(inquiry)))
                .toList();
    }

    private String fetchInquiryOwnerName(Inquiry inquiry) {
        if (inquiry.isAnonymous()) return "익명";

        return userService.findUserOrThrow(inquiry.getUserId()).getName();
    }

    @Transactional
    public BasicResponse deleteOne(Long id, Role role) {
        if (!role.equals(Role.ADMIN)) {
            throw new IllegalArgumentException("관리자만 문의 삭제가 가능합니다.");
        }

        inquiryRepository.deleteById(id);

        return BasicResponse.of(id, "문의가 정상적으로 삭제되었습니다.");
    }
}
