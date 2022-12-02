package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.auth.SignupRequest;
import startervalley.backend.entity.Devpart;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.User;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.DevpartRepository;
import startervalley.backend.repository.GenerationRepository;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.auth.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DevpartRepository devpartRepository;
    private final GenerationRepository generationRepository;

    @Transactional
    public void signup(Authentication authentication, SignupRequest signupRequest) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Devpart devpart = devpartRepository.findByName(signupRequest.getDevpart())
                .orElseThrow(() -> new ResourceNotFoundException("Devpart", "name", signupRequest.getDevpart()));
        Generation generation = generationRepository.findById(signupRequest.getGenerationId())
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", String.valueOf(signupRequest.getGenerationId())));

        userRepository.signup(user.getId(), signupRequest.getName(), devpart, generation, signupRequest.getIntroduction());
    }
}
