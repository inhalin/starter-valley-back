package startervalley.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.user.UserCardDto;
import startervalley.backend.dto.user.UserCardListDto;
import startervalley.backend.dto.user.UserProfileReadDto;
import startervalley.backend.dto.user.UserProfileUpdateDto;
import startervalley.backend.entity.User;
import startervalley.backend.entity.UserProfile;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.UserNotValidException;
import startervalley.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public UserCardListDto findUsersByGeneration(Long userId, Long generationId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
        List<UserCardDto> meDto = new ArrayList<>();
        meDto.add(mapToUserCardDto(me));

        List<User> users = userRepository.findAllByGenerationId(generationId);
        List<UserCardDto> dtoList = new ArrayList<>();
        for (User user : users) {
            dtoList.add(mapToUserCardDto(user));
        }

        return UserCardListDto.builder()
                .me(meDto)
                .list(dtoList)
                .build();
    }

    public UserProfileReadDto showUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return mapToUserProfileReadDto(user);
    }

    @Transactional
    public UserProfileUpdateDto updateUserProfile(Long id, UserProfileUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));

        UserProfile userProfile = user.getProfile();

        if (userProfile == null) {
            userProfile = user.setProfile(new UserProfile());
        }

        userProfile.updateProfile(dto);

        return mapper.convertValue(userProfile, UserProfileUpdateDto.class);
    }

    public Long validateUser(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", String.valueOf(username)));

        if (!user.getId().equals(id)) {
            throw new UserNotValidException("회원 정보가 일치하지 않습니다.");
        }

        return user.getId();
    }

    private UserProfileReadDto mapToUserProfileReadDto(User user) {
        UserProfile profile = user.getProfile() != null ? user.getProfile() : new UserProfile();

        List<UserProfileReadDto.InfoMap> mainInfo = new ArrayList<>();
        mainInfo.add(UserProfileReadDto.InfoMap.of("Team", user.getTeam() != null ? user.getTeam().getName() : null));
        mainInfo.add(UserProfileReadDto.InfoMap.of("MBTI", profile.getMbti()));
        mainInfo.add(UserProfileReadDto.InfoMap.of("Email", getUserContactEmail(user)));

        List<UserProfileReadDto.InfoMap> subInfo = new ArrayList<>();
        subInfo.add(UserProfileReadDto.InfoMap.of("I am", profile.getIntro()));
        subInfo.add(UserProfileReadDto.InfoMap.of("I like", profile.getLikes()));
        subInfo.add(UserProfileReadDto.InfoMap.of("I dislike", profile.getDislikes()));
        subInfo.add(UserProfileReadDto.InfoMap.of("My interests art", profile.getInterests()));

        return UserProfileReadDto.builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .isLeader(user.getIsLeader())
                .devpart(user.getDevpart().getName())
                .generationId(user.getGeneration().getId())
                .mainInfo(mainInfo)
                .subInfo(subInfo)
                .build();
    }

    private String getUserContactEmail(User user) {
        if (user.getProfile() != null && user.getProfile().getContact() != null) {
            return user.getProfile().getContact();
        }
        return user.getEmail();
    }

    private UserCardDto mapToUserCardDto(User user) {
        return UserCardDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .devpart(user.getDevpart().getName())
                .generationId(user.getGeneration().getId())
                .team(user.getTeam() != null ? user.getTeam().getName() : null)
                .email(user.getEmail())
                .intro(user.getProfile() != null ? user.getProfile().getIntro() : null)
                .imageUrl(user.getImageUrl())
                .githubUrl(user.getGithubUrl())
                .isLeader(user.getIsLeader())
                .mbti(user.getProfile() != null ? user.getProfile().getMbti() : null)
                .build();
    }
}