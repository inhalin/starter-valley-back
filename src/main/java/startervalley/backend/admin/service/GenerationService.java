package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .recruitUrl(request.getRecruitUrl())
                .submitUrl(request.getSubmitUrl())
                .build();

        generationRepository.save(generation);

        log.info("기수 생성 완료: {}기", generation.getId());

        request.getDevparts().stream()
                .map(DevpartDto::validate)
                .forEach(devpart -> devpartRepository.save(Devpart.mapToEntity(devpart.getName(), devpart.getKoname(), generation.getId())));

        log.info("파트 생성 완료: {}", request.getDevparts().stream().map(devpartDto -> "{" + devpartDto.getName() + ", " + devpartDto.getKoname() + "}").toList());

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
                request.getAddress1() != null ? request.getAddress1() : generation.getAddress1(),
                request.getAddress2() != null ? request.getAddress2() : generation.getAddress2(),
                request.getLatitude() != null ? request.getLatitude() : generation.getLatitude(),
                request.getLongitude() != null ? request.getLongitude() : generation.getLongitude(),
                request.getRecruitUrl() != null ? request.getRecruitUrl() : generation.getRecruitUrl(),
                request.getSubmitUrl() != null ? request.getSubmitUrl() : generation.getSubmitUrl(),
                request.getSubmitResultUrl() != null ? request.getSubmitResultUrl() : generation.getSubmitResultUrl());

        if (request.getDevparts() != null) {
            request.getDevparts().forEach(devpartDto -> updateDevpart(id, devpartDto));
        }

        return BasicResponse.of(generation.getId(), "기수 상세 내용이 수정되었습니다.");
    }

    public void deleteOne(Long id) {
        generationRepository.deleteById(id);

        log.info("기수 삭제 완료: {}기", id);
    }

    public BasicResponse updateDevpart(Long generationId, DevpartDto devpartDto) {

        Devpart devpart = devpartRepository.findByNameAndGenerationId(devpartDto.getName(), generationId);
        String oldKoname = devpart.getKoname();
        devpart.updateKoname(devpartDto.getKoname());

        String updated = "[" + oldKoname + " -> " + devpart.getKoname() + "]";

        log.info("{}기 파트 한글명 수정 {}", generationId, updated);

        return BasicResponse.of(generationId, "해당 기수의 파트 한글명이 수정되었습니다. " + updated);
    }

    public void deleteDevpart(Long generationId, DevpartDto devpartDto) {

        String deleted = "[" + devpartDto.getName() + ", " + devpartDto.getKoname() + "]";
        devpartRepository.deleteByNameAndGenerationId(devpartDto.getName(), generationId);

        log.info("{}기 파트 삭제 {}", generationId, deleted);
    }
}
