package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.generation.DevpartDto;
import startervalley.backend.admin.dto.generation.GenerationRequest;
import startervalley.backend.admin.dto.generation.GenerationResponse;
import startervalley.backend.admin.dto.generation.GenerationUpdateRequest;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.Devpart;
import startervalley.backend.entity.Generation;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.ResourceNotValidException;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.repository.devpart.DevpartRepository;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.util.CodeGenerator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerationService {

    private final GenerationRepository generationRepository;
    private final UserRepository userRepository;
    private final DevpartRepository devpartRepository;

    public BasicResponse createOne(GenerationRequest request) {

        if (generationRepository.existsById(request.getGeneration())) {
            throw new ResourceNotValidException("기수 내역이 이미 존재합니다.");
        }

        Generation generation = Generation.builder()
                .id(request.getGeneration())
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

        request.getDevparts().stream()
                .map(DevpartDto::validate)
                .forEach(devpart -> devpartRepository.save(Devpart.mapToEntity(devpart.getName(), devpart.getKoname(), generation.getId())));

        return BasicResponse.of(generation.getId(), "새로운 기수가 생성되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<GenerationResponse> listAll() {
        return generationRepository.findAll().stream()
                .map(generation -> GenerationResponse.mapToResponse(generation, userRepository.findAllByGenerationId(generation.getId()), devpartRepository.findAllByGenerationId(generation.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public GenerationResponse getOne(Long id) {

        Generation generation = generationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", id.toString()));

        return GenerationResponse.mapToResponse(generation, userRepository.findAllByGenerationId(generation.getId()), devpartRepository.findAllByGenerationId(generation.getId()));
    }

    public BasicResponse updateOne(Long id, GenerationUpdateRequest request) {

        Generation generation = generationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", id.toString()));

        generation.update(request.getCourseStartDate(),
                request.getCourseEndDate(),
                request.getDescription() != null ? request.getDescription() : generation.getDescription(),
                request.getLocation() != null ? request.getLocation() : generation.getLocation(),
                request.getLatitude() != null ? request.getLatitude() : generation.getLatitude(),
                request.getLongitude() != null ? request.getLongitude() : generation.getLongitude(),
                request.getRecruitUrl() != null ? request.getRecruitUrl() : generation.getRecruitUrl(),
                request.getSubmitUrl() != null ? request.getSubmitUrl() : generation.getSubmitUrl(),
                request.getSubmitResultUrl() != null ? request.getSubmitResultUrl() : generation.getSubmitResultUrl());

        return BasicResponse.of(generation.getId(), "기수 상세 내용이 수정되었습니다.");
    }

    public BasicResponse deleteOne(Long id) {
        generationRepository.deleteById(id);
        return BasicResponse.of(id, "기수가 정상적으로 삭제되었습니다.");
    }

    public BasicResponse updateDevpart(Long generationId, DevpartDto devpartDto) {

        Devpart devpart = devpartRepository.findByNameAndGenerationId(devpartDto.getName(), generationId);
        String oldKoname = devpart.getKoname();
        devpart.updateKoname(devpartDto.getKoname());

        String updated = "[" + oldKoname + " -> " + devpart.getKoname() + "]";

        return BasicResponse.of(generationId, "해당 기수의 파트 한글명이 수정되었습니다. " + updated);
    }

    public BasicResponse deleteDevpart(Long generationId, DevpartDto devpartDto) {

        String deleted = "[" + devpartDto.getName() + ", " + devpartDto.getKoname() + "]";
        devpartRepository.deleteByNameAndGenerationId(devpartDto.getName(), generationId);

        return BasicResponse.of(generationId, "해당 기수의 개발 파트를 삭제하였습니다. " + deleted);
    }
}
