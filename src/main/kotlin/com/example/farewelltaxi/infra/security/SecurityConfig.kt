package com.example.farewelltaxi.infra.security
import com.example.farewelltaxi.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig (private val jwtAuthenticationFilter: JwtAuthenticationFilter ){

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .formLogin { formLogin ->
                formLogin.disable()
            }
            .httpBasic { httpBasic ->
                httpBasic.disable()
            }
            .csrf { csrf ->
                csrf.disable()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/api/drivers/create","/api/Passengers/create","/api/admin/create").permitAll()
                    .requestMatchers("/user/signup","/user/login").permitAll()
                    .requestMatchers("/api/Passengers/**","/api/call/by-passenger/").hasRole("PASSENGER")
                    .requestMatchers("/api/drivers/**","/api/call/by-driver/").hasRole("DRIVER")
                    .requestMatchers("/api/taxis/**").hasRole("ADMIN")
                    .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/api/call/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}

        return http.build()
    }
}
