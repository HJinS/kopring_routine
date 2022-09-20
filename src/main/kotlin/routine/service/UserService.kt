package routine.service

import config.RedisConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import routine.dto.*
import routine.entity.User
import routine.jwt.JwtTokenProvider
import routine.repository.UserRepository
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: RedisTemplate<String, String>
) {

    @Transactional(readOnly = true)
    fun login(userLoginDTO: UserLoginRequestDto): Pair<String, String> {
        var user: User? = null
        if (existsUser(userLoginDTO.email)){
            user = findUser(userLoginDTO.email)
        }
        if(user == null || !passwordEncoder.matches(userLoginDTO.password, user.password)){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않거나 비밀번호가 틀렸습니다..")
        }
        val accessToken = jwtTokenProvider.createToken(userLoginDTO.email, isAccess = true)
        val refreshToken = jwtTokenProvider.createToken(userLoginDTO.email, isAccess = false)
        return Pair(accessToken, refreshToken)
    }

    fun refresh(request: HttpServletRequest): Pair<String, String>{
        val tokens: List<Cookie>? = jwtTokenProvider.resolveToken(request)
        tokens?.let { itToken ->
            if(itToken.isNotEmpty() && itToken.size == 2){
                val accessToken = itToken[0]
                val refreshToken = itToken[1]
                if(jwtTokenProvider.validation(accessToken.toString(), isAccess = true)){
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token")
                }
                if(!jwtTokenProvider.validation(refreshToken.toString(), isAccess = false)){
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token")
                }
                val valueOperations = redisTemplate.opsForValue()
                val userInfo = jwtTokenProvider.parseToken(refreshToken.toString(), isAccess = false)
                valueOperations.set(refreshToken.toString(), "refresh_token")
                redisTemplate.expire(refreshToken.toString(), userInfo.expiration.time-Date().time, TimeUnit.MILLISECONDS)
                val newAccessToken = jwtTokenProvider.createToken(email = userInfo["email"] as String, isAccess = true)
                val newRefreshToken = jwtTokenProvider.createToken(email = userInfo["email"] as String, isAccess = false)
                return Pair(newAccessToken, newRefreshToken)
            }else{
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰은 필수 입니다.")
            }
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰은 필수 입니다.")
    }

    fun existsUser(email: String): Boolean{
        return userRepository.existsByEmail(email)
    }

    fun findUser(email: String): User {
        try{
            return userRepository.getByEmail(email)
        } catch (e: IncorrectResultSizeDataAccessException){
            if(e.actualSize == -1){
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다.")
            }else{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "두개 이상의 유저가 존재합니다.")
            }

        }

    }

    fun createUser(userRegisterDto: UserRegisterRequestDto): UserRegisterResponseDto{
        if(userRegisterDto.password != userRegisterDto.confirmPassword){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Different passwords")
        }
        val user = User.createUser(userRegisterDto, passwordEncoder)
        userRepository.save(user)

        return UserRegisterResponseDto(user.id, user.email)
    }

}