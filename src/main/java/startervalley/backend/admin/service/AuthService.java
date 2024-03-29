package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.auth.AuthLoginRequest;
import startervalley.backend.admin.dto.auth.AuthPasswordRequest;
import startervalley.backend.admin.dto.auth.AuthRegisterRequest;
import startervalley.backend.dto.auth.JwtTokenDto;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.AdminUser;
import startervalley.backend.exception.PasswordNotValidException;
import startervalley.backend.exception.ResourceDuplicateException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.TokenNotValidException;
import startervalley.backend.repository.adminuser.AdminUserRepository;
import startervalley.backend.security.jwt.JwtTokenProvider;

import java.util.Date;

@Service(value = "AuthServiceBO")
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public JwtTokenDto login(AuthLoginRequest request) {

        if (!isExistingUsername(request.getUsername())) {
            throw new ResourceNotFoundException("Admin User", "username", request.getUsername());
        }

        AdminUser adminUser = adminUserRepository.findByUsername(request.getUsername());

        validatePassword(request.getPassword(), adminUser.getPassword());

        return createJwtToken(adminUser);
    }

    @Transactional
    public BasicResponse logout(Long id) {
        AdminUser adminUser = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", id.toString()));

        adminUser.setRefreshToken(null);

        return BasicResponse.of(adminUser.getId(), "로그아웃 하였습니다.");
    }

    @Transactional
    public BasicResponse register(AuthRegisterRequest request) {

        if (isExistingUsername(request.getUsername())) {
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

    @Transactional
    public BasicResponse changePassword(Long userId, AuthPasswordRequest request) {
        AdminUser adminUser = adminUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin User", "id", userId.toString()));

        validatePassword(request.getPassword(), adminUser.getPassword());

        if (request.getPassword().equals(request.getNewPassword())) {
            throw new PasswordNotValidException("기존 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.");
        }

        if (!request.getNewPassword().equals(request.get_newPassword())) {
            throw new PasswordNotValidException("입력하신 새로운 비밀번호가 서로 일치하지 않습니다.");
        }

        adminUserRepository.changePassword(adminUser.getId(), passwordEncoder.encode(request.getNewPassword()));

        return BasicResponse.of(adminUser.getId(), "비밀번호가 정상적으로 변경되었습니다.");
    }

    private boolean isExistingUsername(String username) {
        return adminUserRepository.existsByUsername(username);
    }

    private void validatePassword(String inputPassword, String originalPassword) {
        if (!passwordEncoder.matches(inputPassword, originalPassword)) {
            throw new PasswordNotValidException("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    public JwtTokenDto refreshToken(String refreshToken) {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new TokenNotValidException("토큰 정보가 확인되지 않습니다.");
        }

        String username = tokenProvider.getUsername(refreshToken);
        AdminUser adminUser = adminUserRepository.findByUsername(username);

        if (!adminUser.getRefreshToken().equals(refreshToken)) {
            throw new TokenNotValidException("유저 토큰과 일치하지 않습니다.");
        }

        return createJwtToken(adminUser);
    }

    private JwtTokenDto createJwtToken(AdminUser adminUser) {
        String accessToken = tokenProvider.createAccessToken(adminUser);
        Date expiration = tokenProvider.getExpiration(accessToken);
        String refreshToken = tokenProvider.createRefreshToken(tokenProvider.getAuthentication(accessToken));

        return JwtTokenDto.of(accessToken, refreshToken, expiration);
    }
}
