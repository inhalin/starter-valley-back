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

        return BasicResponse.of(adminUser.getId(), "???????????? ???????????????.");
    }

    @Transactional
    public BasicResponse register(AuthRegisterRequest request) {

        if (isExistingUsername(request.getUsername())) {
            throw new ResourceDuplicateException("?????? ???????????? ??????????????????.");
        }

        AdminUser adminUser = AdminUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
        adminUserRepository.save(adminUser);

        return BasicResponse.of(adminUser.getId(), "????????? ???????????? ??????????????? ?????????????????????.");
    }

    @Transactional
    public BasicResponse changePassword(Long userId, AuthPasswordRequest request) {
        AdminUser adminUser = adminUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin User", "id", userId.toString()));

        validatePassword(request.getPassword(), adminUser.getPassword());

        if (request.getPassword().equals(request.getNewPassword())) {
            throw new PasswordNotValidException("?????? ??????????????? ????????? ??????????????? ????????? ??? ????????????.");
        }

        if (!request.getNewPassword().equals(request.get_newPassword())) {
            throw new PasswordNotValidException("???????????? ????????? ??????????????? ?????? ???????????? ????????????.");
        }

        adminUserRepository.changePassword(adminUser.getId(), passwordEncoder.encode(request.getNewPassword()));

        return BasicResponse.of(adminUser.getId(), "??????????????? ??????????????? ?????????????????????.");
    }

    private boolean isExistingUsername(String username) {
        return adminUserRepository.existsByUsername(username);
    }

    private void validatePassword(String inputPassword, String originalPassword) {
        if (!passwordEncoder.matches(inputPassword, originalPassword)) {
            throw new PasswordNotValidException("?????? ??????????????? ???????????? ????????????.");
        }
    }

    public JwtTokenDto refreshToken(String refreshToken) {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new TokenNotValidException("?????? ????????? ???????????? ????????????.");
        }

        String username = tokenProvider.getUsername(refreshToken);
        AdminUser adminUser = adminUserRepository.findByUsername(username);

        if (!adminUser.getRefreshToken().equals(refreshToken)) {
            throw new TokenNotValidException("?????? ????????? ???????????? ????????????.");
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
