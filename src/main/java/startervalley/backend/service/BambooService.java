package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import startervalley.backend.dto.request.BambooRequestDto;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.response.BambooResponseDto;
import startervalley.backend.dto.response.PageResultDTO;
import startervalley.backend.entity.Bamboo;
import startervalley.backend.entity.User;
import startervalley.backend.repository.BambooRepository;
import startervalley.backend.repository.user.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BambooService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final BambooRepository bambooRepository;

    public BambooResponseDto findBamboo() {
        return null;
    }

    public PageResultDTO<Bamboo, BambooResponseDto> findBambooList(PageRequestDto pageRequestDto) {
        Sort descByCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = pageRequestDto.getPageable(descByCreatedDate);
        Page<Bamboo> page = bambooRepository.findAll(pageable);

        List<Bamboo> bambooList = page.getContent();
        List<BambooResponseDto> dtoList = bambooList.stream()
                .map(b -> BambooResponseDto.builder()
                        .id(b.getId())
                        .content(b.getContent())
                        .randomName(b.getRandomName())
                        .createdDate(b.getCreatedDate())
                        .build())
                .toList();
        return new PageResultDTO<>(page, dtoList);
    }

    public Long createBamboo(Long userId, BambooRequestDto bambooRequestDto) {

        User user = userRepository.findById(userId).orElseThrow();

        Bamboo bamboo = Bamboo.builder()
                .content(bambooRequestDto.getContent())
                .randomName(getRandomNicknameByWebClient())
                .user(user)
                .build();
        bambooRepository.save(bamboo);
        return bamboo.getId();
    }

    public String getRandomNicknameByWebClient() {
        return webClient.get()
                .uri("https://nickname.hwanmoo.kr/?format=text&max_length=20")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
