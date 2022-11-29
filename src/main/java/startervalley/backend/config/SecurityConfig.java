package startervalley.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import startervalley.backend.security.auth.CookieAuthorizationRequestRepository;
import startervalley.backend.security.auth.CustomOAuth2UserService;
import startervalley.backend.security.auth.OAuth2AuthenticationFailureHandler;
import startervalley.backend.security.auth.OAuth2AuthenticationSuccessHandler;
import startervalley.backend.security.jwt.JwtAccessDeniedHandler;
import startervalley.backend.security.jwt.JwtAuthenticationEntryPoint;
import startervalley.backend.security.jwt.JwtAuthenticationFilter;
import startervalley.backend.security.jwt.JwtLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        http.oauth2Login()
                .authorizationEndpoint().baseUri("/auth/login")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                .userInfoEndpoint().userService(customOauth2UserService)
                .and()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http.logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler);

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 401
                .accessDeniedHandler(jwtAccessDeniedHandler);   // 403

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
