package com.example.farewelltaxi.infra.security.jwt
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtPlugin: JwtPlugin) : OncePerRequestFilter() {

    private val pathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        // permitAll()로 설정된 경로 리스트
        val permitAllPaths = listOf(
            "/api/drivers/create",
            "/api/Passengers/create",
            "/api/admin/create",
            "/user/signup",
            "/user/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/call/**"
        )

        // 현재 요청이 permitAll()로 설정된 경로와 일치하는지 검사
        val isPermitAll = permitAllPaths.any { path ->
            pathMatcher.match(path, requestURI)
        }

        val token = getTokenFromRequest(request)
        if (token != null && jwtPlugin.validateToken(token).isSuccess) {
            val claims = jwtPlugin.validateToken(token).getOrNull()?.body
            val userEmail = claims?.get("email", String::class.java)  // 사용자의 이메일을 추출합니다.
            val role = claims?.get("role", String::class.java)
            val authorities = if (role != null) listOf(SimpleGrantedAuthority("ROLE_$role")) else listOf<GrantedAuthority>()
            val authentication = UsernamePasswordAuthenticationToken(userEmail, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        } else if (isPermitAll) {
            // permitAll 경로이지만 토큰이 없거나 유효하지 않은 경우, 인증 절차를 건너뛰고 허용
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }


    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}
