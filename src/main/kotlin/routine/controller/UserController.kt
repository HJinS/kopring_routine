package routine.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import routine.dto.UserLoginRequestDto
import routine.dto.UserRegisterRequestDto
import routine.service.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/user/users")
class UserController(private val userService: UserService){
    @PostMapping("/login")
    fun login(@RequestBody loginDto: UserLoginRequestDto, response: HttpServletResponse): ResponseEntity<Any> {
        val cookieValue: Pair<String, String> = userService.login(loginDto)

        val accessTokenCookie: ResponseCookie = ResponseCookie.from("accessToken", cookieValue.first)
            .path("/")
            .secure(true)
            .httpOnly(true)
            .build()
        val refreshTokenCookie: ResponseCookie = ResponseCookie.from("refreshToken", cookieValue.second)
            .path("/")
            .secure(true)
            .httpOnly(true)
            .build()
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).build()
    }

    @PostMapping("/register")
    fun register(@RequestBody registerDto: UserRegisterRequestDto) = ResponseEntity.ok().body(userService.createUser(registerDto))

    @GetMapping("/refresh")
    fun refresh(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Any> {
        val tokens: Pair<String, String> = userService.refresh(request)
        val accessTokenCookie: ResponseCookie = ResponseCookie.from("accessToken", tokens.first)
            .path("/")
            .secure(true)
            .httpOnly(true)
            .build()

        val refreshTokenCookie: ResponseCookie = ResponseCookie.from("refreshToken", tokens.second)
            .path("/")
            .secure(true)
            .httpOnly(true)
            .build()
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).build()
    }
    // Todo token blacklist 구현 -> redis  이용
}
