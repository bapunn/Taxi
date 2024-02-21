package com.example.farewelltaxi.infra.security.jwt

import com.example.farewelltaxi.enum.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*
import io.jsonwebtoken.Jws
import javax.crypto.SecretKey
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value

@Component
class JwtPlugin(@Value("\${auth.jwt.secret}") private val jwtSecret: String) {

    companion object {
        const val ISSUER = "team.sparta.com"
        const val ACCESS_TOKEN_EXPIRATION_HOUR: Long = 168
    }

    private val key: SecretKey = generateKey()

    private fun generateKey(): SecretKey {
        return Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
    }

    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
        }
    }

    fun generateAccessToken(subject: String, email: String, role: Role): String {
        // Role 열거형 값을 String으로 변환하여 generateToken 메소드에 전달
        return generateToken(subject, email, role.name, Duration.ofHours(ACCESS_TOKEN_EXPIRATION_HOUR))
    }

    private fun generateToken(subject: String, email: String, role: String, expirationPeriod: Duration): String {
        val now = Instant.now()

        return Jwts.builder()
            .setSubject(subject)
            .setIssuer(ISSUER)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(expirationPeriod)))
            .claim("email", email)
            .claim("role", role)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

}
