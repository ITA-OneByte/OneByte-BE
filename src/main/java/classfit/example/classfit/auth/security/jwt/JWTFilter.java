package classfit.example.classfit.auth.security.jwt;

import classfit.example.classfit.auth.security.custom.CustomUserDetailService;
import classfit.example.classfit.auth.security.custom.CustomUserDetails;
import classfit.example.classfit.common.ApiResponse;
import classfit.example.classfit.common.exception.ClassfitAuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN_CATEGORY = "access";
    private static final String SECURITY_SCHEMA_TYPE = "Bearer ";

    private final CustomUserDetailService customUserDetailService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.extractToken(request);

        if (accessToken == null || accessToken.startsWith(SECURITY_SCHEMA_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtUtil.isExpired(accessToken)) {
                throw new ClassfitAuthException("액세스 토큰이 만료되었습니다.", HttpStatus.REQUEST_TIMEOUT);
            }

            if (!ACCESS_TOKEN_CATEGORY.equals(jwtUtil.getCategory(accessToken))) {
                throw new ClassfitAuthException("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }

            String email = jwtUtil.getEmail(accessToken);
            CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(email);

            Authentication authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (ClassfitAuthException ex) {
            ApiResponse.errorResponse(response, ex.getMessage(), ex.getHttpStatusCode());
        }
    }
}
