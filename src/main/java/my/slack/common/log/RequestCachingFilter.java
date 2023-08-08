package my.slack.common.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class RequestCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        log.info("Request URI : {}", request.getRequestURI());
        log.debug("Request Data: {}", new String(cachedHttpServletRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        filterChain.doFilter(cachedHttpServletRequest, response);
    }
}
