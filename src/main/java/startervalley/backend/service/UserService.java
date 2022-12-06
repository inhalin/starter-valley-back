package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import startervalley.backend.dto.user.*;

import startervalley.backend.entity.User;
import startervalley.backend.entity.UserProfile;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.UserInvalidException;
import startervalley.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

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

        return mapToUserProfileUpdateDto(userRepository.findById(id).orElseThrow().getProfile());
    }

    public void validateUser(Long id, String username) {
        User updateUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", String.valueOf(username)));

        if (!updateUser.getId().equals(id)) {
            throw new UserInvalidException("회원 정보가 일치하지 않습니다.");
        }
    }

    private UserProfileReadDto mapToUserProfileReadDto(User user) {
        UserProfile profile = user.getProfile() != null ? user.getProfile() : new UserProfile();

        List<UserProfileInfoMap> mainInfo = new ArrayList<>();
        mainInfo.add(UserProfileInfoMap.of("Team", user.getTeam() != null ? user.getTeam().getName() : null));
        mainInfo.add(UserProfileInfoMap.of("MBTI", profile.getMbti()));
        mainInfo.add(UserProfileInfoMap.of("Email", getUserContactEmail(user)));

        List<UserProfileInfoMap> subInfo = new ArrayList<>();
        subInfo.add(UserProfileInfoMap.of("I am", profile.getIntro()));
        subInfo.add(UserProfileInfoMap.of("I like", profile.getLikes()));
        subInfo.add(UserProfileInfoMap.of("I dislike", profile.getDislikes()));
        subInfo.add(UserProfileInfoMap.of("My interests art", profile.getInterests()));

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

    public UserSelfProfileDto getSelfProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return UserSelfProfileDto.builder()
                .name(user.getName())
                .generationId(user.getGeneration().getId())
                .consecutiveDays(user.getConsecutiveDays())
                .imageUrl(user.getImageUrl())
                .devpart(user.getDevpart().getName())
                .build();
    }
}
