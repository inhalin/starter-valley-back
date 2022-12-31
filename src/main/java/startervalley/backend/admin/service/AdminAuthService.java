package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.auth.AuthRegisterRequest;
import startervalley.backend.admin.dto.auth.AuthLoginRequest;
import startervalley.backend.admin.dto.auth.AuthLoginResponse;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.AdminUser;
import startervalley.backend.exception.ResourceDuplicateException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.UserNotValidException;
import startervalley.backend.repository.adminuser.AdminUserRepository;
import startervalley.backend.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public AuthLoginResponse login(AuthLoginRequest request) {

        if (!adminUserRepository.existsByUsername(request.getUsername())) {
            throw new ResourceNotFoundException("AdminUser", "username", request.getUsername());
        }

        AdminUser adminUser = adminUserRepository.findByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), adminUser.getPassword())) {
            throw new UserNotValidException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(adminUser.getUsername());

        return AuthLoginResponse.of(accessToken);
    }

    @Transactional
    public BasicResponse register(AuthRegisterRequest request) {

        if (adminUserRepository.existsByUsername(request.getUsername())) {
            throw new ResourceDuplicateException("이미 사용중인 아이디입니다.");
        }

        AdminUser adminUser = AdminUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
        adminUserRepository.save(adminUser);

        return BasicResponse.of(adminUser.getId(), "새로운 관리자를 정상적으로 등록하였습니다.");
    }
}
