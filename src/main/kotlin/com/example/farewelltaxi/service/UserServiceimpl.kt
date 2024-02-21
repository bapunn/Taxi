package com.example.farewelltaxi.service
import com.example.farewelltaxi.dto.USER.LoginRequest
import com.example.farewelltaxi.dto.USER.LoginResponse
import com.example.farewelltaxi.infra.security.jwt.JwtPlugin
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import com.example.farewelltaxi.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service


import java.util.regex.Pattern

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.security.special-key}") private val specialKey: String,
    private val jwtPlugin: JwtPlugin
) : UserService {



//     이메일과 비밀번호에 대한 정규식 패턴
    private val emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    private val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$") // 최소 8자, 하나 이상의 숫자 및 대소문자

    override fun login(loginRequest: LoginRequest): LoginResponse {
        validateEmail(loginRequest.email)

        val user = userRepository.findByEmail(loginRequest.email)
            .orElseThrow { UsernameNotFoundException("User with email ${loginRequest.email} not found") }

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw BadCredentialsException("Invalid password")
        }

        val token = jwtPlugin.generateAccessToken(user.id.toString(), user.email, user.role)
        return LoginResponse(email = user.email, token = token)
    }

    private fun validateEmail(email: String) {
        if (!emailPattern.matcher(email).matches()) {
            throw IllegalArgumentException("Invalid email format")
        }
    }

    private fun validatePassword(password: String) {
        if (!passwordPattern.matcher(password).matches()) {
            throw IllegalArgumentException("Password must be at least 8 characters long, include numbers, uppercase and lowercase letters.")
        }
    }
}
