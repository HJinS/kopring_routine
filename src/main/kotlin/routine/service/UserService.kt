package routine.service

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import routine.dto.*
import routine.entity.User
import routine.jwt.JwtTokenProvider
import routine.repository.UserRepository


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional(readOnly = true)
    fun login(userLoginDTO: UserLoginRequestDto): String {
        var user: User? = null
        if (existsUser(userLoginDTO.email)){
            user = findUser(userLoginDTO.email)
        }
        if(user != null && !passwordEncoder.matches(userLoginDTO.password, user.password)){
            throw BadCredentialsException("로그인 실패")
        }
        return jwtTokenProvider.createToken(userLoginDTO.email)
    }

    fun existsUser(email: String): Boolean{
        return userRepository.existsByEmail(email)
    }

    fun findUser(email: String): User {
        print("-------------")
        return userRepository.getByEmail(email)
    }

    fun createUser(userRegisterDto: UserRegisterRequestDto): UserRegisterResponseDto{
        if(userRegisterDto.password != userRegisterDto.confirmPassword){
            print("error")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Different passwords")
        }
        val user = User(userRegisterDto.email, userRegisterDto.password, userRegisterDto.name)
        userRepository.save(user)

        return UserRegisterResponseDto(user.id, user.email)
    }
}