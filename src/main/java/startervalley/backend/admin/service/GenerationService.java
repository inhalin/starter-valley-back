package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.generation.GenerationRequest;
import startervalley.backend.admin.dto.generation.GenerationResponse;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.Generation;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.util.CodeGenerator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenerationService {

    private final GenerationRepository generationRepository;
    private final UserRepository userRepository;

    @Transactional
    public BasicResponse createOne(GenerationRequest request) {

        Generation generation = Generation.builder()
                .id(request.getId())
                .code(CodeGenerator.generateRandom())
                .courseStartDate(request.getCourseStartDate())
                .courseEndDate(request.getCourseEndDate())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .location(request.getLocation())
                .recruitUrl(request.getRecruitUrl())
                .submitUrl(request.getSubmitUrl())
                .build();

        generationRepository.save(generation);

        return BasicResponse.of(generation.getId(), "새로운 기수가 생성되었습니다.");
    }

    public List<GenerationResponse> listAll() {
        return generationRepository.findAll().stream()
                .map(generation -> GenerationResponse.mapToResponse(generation, userRepository.findAllByGenerationId(generation.getId())))
                .toList();
    }
}
