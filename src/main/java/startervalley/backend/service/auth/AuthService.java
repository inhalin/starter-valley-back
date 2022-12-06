package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.auth.JwtTokenDto;
import startervalley.backend.dto.auth.SignupRequest;
import startervalley.backend.entity.*;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.TokenNotValidException;
import startervalley.backend.repository.DevpartRepository;
import startervalley.backend.repository.GenerationRepository;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.jwt.JwtTokenProvider;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DevpartRepository devpartRepository;
    private final GenerationRepository generationRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public void signup(User user, Map<String, String> userData, SignupRequest signupRequest) {
        Devpart devpart = devpartRepository.findByName(signupRequest.getDevpart())
                .orElseThrow(() -> new ResourceNotFoundException("Devpart", "name", signupRequest.getDevpart()));
        Generation generation = generationRepository.findById(signupRequest.getGenerationId())
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", String.valueOf(signupRequest.getGenerationId())));

        User newUser = User.builder()
                .provider(user.getProvider())
                .providerId(userData.get("providerId"))
                .role(Role.USER)
                .githubUrl(userData.get("githubUrl"))
                .imageUrl(userData.get("imageUrl"))
                .email(userData.get("email"))
                .username(user.getUsername())
                .name(signupRequest.getName())
                .devpart(devpart)
                .generation(generation)
                .profile(new UserProfile(signupRequest.getIntroduction()))
                .build();
        userRepository.save(newUser);
    }

    public void validateGenerationCode(String code, Long generationId) {
        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", String.valueOf(generationId)));

        if (!code.equals(generation.getCode())) {
            throw new TokenNotValidException("code가 일치하지 않습니다.");
        }
    }

    public JwtTokenDto createJwtToken(Authentication authentication) {
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
