package de.gs.api.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static de.gs.api.auth.LoginEndpoint.LOGIN_PATH;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRequestInterceptor extends OncePerRequestFilter {

    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = Optional.ofNullable(request.getHeader("Authorization")).orElse("");
        if (jwt.isBlank() || !authenticationProvider.isValid(jwt)) {
            log.info("Request denied, invalid JWT: {}", jwt);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(HttpServletRequest request) {
        final String path = request.getServletPath();
        return path.equals("/") || path.startsWith(LOGIN_PATH);
    }
}
