package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
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

import java.util.HashMap;
import java.util.Map;

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
                .provider(AuthProvider.valueOf(userData.get("provider")))
                .providerId(userData.get("provider") + "_" + userData.get("id"))
                .role(Role.valueOf(userData.get("role")))
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

        Map<String, String> attributes = new HashMap<>();
        attributes.put("role", user.getRole().name());

        return createJwtToken(user, attributes);
    }

    public void validateGenerationCode(String code, Long generationId) {
        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", String.valueOf(generationId)));

        if (!code.equals(generation.getCode())) {
            throw new TokenNotValidException("코드가 일치하지 않습니다.");
        }
    }

    private JwtTokenDto createJwtToken(User user, Map<String, String> attributes) {
        String accessToken = tokenProvider.createAccessToken(user, attributes);
        String refreshToken = tokenProvider.createRefreshToken(tokenProvider.getAuthentication(accessToken));

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
