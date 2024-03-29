package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.auth.AvailableDevpart;
import startervalley.backend.dto.auth.JwtTokenDto;
import startervalley.backend.dto.auth.SignupRequest;
import startervalley.backend.entity.*;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.TokenNotValidException;
import startervalley.backend.repository.devpart.DevpartRepository;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.repository.user.UserRepository;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.security.jwt.JwtTokenProvider;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DevpartRepository devpartRepository;
    private final GenerationRepository generationRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public JwtTokenDto signup(Map<String, String> userData, SignupRequest signupRequest) {

        Devpart devpart = devpartRepository.findByName(signupRequest.getDevpart())
                .orElseThrow(() -> new ResourceNotFoundException("Devpart", "name", signupRequest.getDevpart()));
        Generation generation = generationRepository.findById(signupRequest.getGenerationId())
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", String.valueOf(signupRequest.getGenerationId())));

        User user = User.builder()
                .provider(AuthProvider.GITHUB)
                .providerId(userData.get("provider") + "_" + userData.get("id"))
                .role(Role.USER)
                .githubUrl(userData.get("githubUrl"))
                .imageUrl(userData.get("imageUrl"))
                .email(userData.get("email"))
                .username(userData.get("username"))
                .name(signupRequest.getName())
                .devpart(devpart)
                .generation(generation)
                .profile(new UserProfile(signupRequest.getIntroduction()))
                .build();
        userRepository.save(user);

        return createJwtToken(user);
    }

    public void validateGenerationCode(String code, Long generationId) {
        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", String.valueOf(generationId)));

        if (!code.equals(generation.getCode())) {
            throw new TokenNotValidException("코드가 일치하지 않습니다.");
        }
    }

    private JwtTokenDto createJwtToken(User user) {
        String accessToken = tokenProvider.createAccessToken(user, new HashMap<>());
        Date expiration = tokenProvider.getExpiration(accessToken);
        String refreshToken = tokenProvider.createRefreshToken(tokenProvider.getAuthentication(accessToken));

        return JwtTokenDto.of(accessToken, refreshToken, expiration);
    }

    public User getLoginUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId().toString()));
    }

    @Transactional
    public void logout(String username) {
        userRepository.deleteRefreshToken(username);
        SecurityContextHolder.clearContext();
        log.debug("{username} 회원 로그아웃 처리 및 refresh token 삭제 완료");
    }

    public Role fetchRole(UserDetails userDetails) {
        String authority = new ArrayList<>(userDetails.getAuthorities()).get(0).getAuthority();

        if (authority.equalsIgnoreCase(Role.ADMIN.getRole())) return Role.ADMIN;

        return Role.USER;
    }

    public List<Long> getAvailableGenerations() {
        return generationRepository.findAll().stream()
                .map(Generation::getId)
                .toList();
    }

    public List<AvailableDevpart> getAvailableDevparts(Long generationId) {

        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", generationId.toString()));

        return devpartRepository.findAllByGenerationId(generation.getId()).stream()
                .map(AvailableDevpart::mapToResponse)
                .toList();
    }

    public JwtTokenDto refreshToken(String refreshToken) {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new TokenNotValidException("토큰 정보가 확인되지 않습니다.");
        }

        String username = tokenProvider.getUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new TokenNotValidException("유저 토큰과 일치하지 않습니다.");
        }

        return createJwtToken(user);
    }
}
