package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.user.UserCardDto;
import startervalley.backend.dto.user.UserProfileUpdateDto;
import startervalley.backend.dto.user.UserProfileReadDto;
import startervalley.backend.entity.User;
import startervalley.backend.entity.UserProfile;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public BaseResponseDto<List<UserCardDto>> findUsersByGenerationId(Long generationId) {
        List<User> users = userRepository.findAllByGenerationId(generationId);
        List<UserCardDto> dtoList = new ArrayList<>();

        for (User user : users) {
            dtoList.add(mapToUserCardDto(user));
        }

        return new BaseResponseDto<>(null, dtoList);
    }

    public BaseResponseDto<UserProfileReadDto> showUserProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));

        UserProfileReadDto dto = mapToUserProfileReadDto(user);
        return new BaseResponseDto<>(null, dto);
    }

    private String getUserContactEmail(User user) {
        if (user.getProfile() != null && user.getProfile().getContact() != null) {
            return user.getProfile().getContact();
        }
        return user.getEmail();
    }

    @Transactional
    public BaseResponseDto<UserProfileUpdateDto> updateUserProfile(Long id, UserProfileUpdateDto dto) {
        UserProfile userProfile = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)))
                .getProfile();
        userProfile.updateProfile(dto);

        UserProfileUpdateDto updatedUserProfile = mapToUserProfileUpdateDto(userRepository.findById(id).orElseThrow().getProfile());

        return new BaseResponseDto<>("Uer profile updated successfully", updatedUserProfile);
    }

    private UserProfileReadDto mapToUserProfileReadDto(User user) {
        UserProfile userProfile = user.getProfile() != null ? user.getProfile() : new UserProfile();

        List<String> mainInfo = new ArrayList<>();
        mainInfo.add(user.getDevpart().getName());
        mainInfo.add(user.getTeam() != null ? user.getTeam().getName() : null);
        mainInfo.add(userProfile.getMbti());
        mainInfo.add(getUserContactEmail(user));

        List<String> subInfo = new ArrayList<>();
        subInfo.add(userProfile.getLikes());
        subInfo.add(userProfile.getDislikes());
        subInfo.add(userProfile.getInterests());

        return UserProfileReadDto.builder()
                .imageUrl(user.getImageUrl())
                .mainInfo(mainInfo)
                .intro(userProfile.getIntro())
                .subInfo(subInfo)
                .build();
    }

    private UserCardDto mapToUserCardDto(User user) {
        return UserCardDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .devpart(user.getDevpart().getName())
                .generationId(user.getGeneration().getId())
                .team(user.getTeam())
                .email(user.getEmail())
                .intro(user.getProfile() != null ? user.getProfile().getIntro() : null)
                .imageUrl(user.getImageUrl())
                .githubUrl(user.getGithubUrl())
                .isLeader(user.getIsLeader())
                .mbti(user.getProfile() != null ? user.getProfile().getMbti() : null)
                .build();
    }

    private UserProfileUpdateDto mapToUserProfileUpdateDto(UserProfile userProfile) {
        return UserProfileUpdateDto.builder()
                .contact(userProfile.getContact())
                .mbti(userProfile.getMbti())
                .intro(userProfile.getIntro())
                .likes(userProfile.getLikes())
                .dislikes(userProfile.getDislikes())
                .interests(userProfile.getInterests())
                .build();
    }
}
