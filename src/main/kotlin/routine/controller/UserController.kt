package routine.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import routine.dto.UserLoginRequestDto
import routine.dto.UserRegisterRequestDto
import routine.service.UserService


@RestController
@RequestMapping("/user/users")
class UserController(private val userService: UserService){
    @PostMapping("/login")
    fun login(@RequestBody loginDto: UserLoginRequestDto): ResponseEntity<Any>{
        val cookieValue: String = "Bearer ${userService.login(loginDto)}"
        val cookie: ResponseCookie = ResponseCookie.from("accessToken", cookieValue)
            .path("/")
            .secure(true)
            .httpOnly(true)
            .build()
        val headers = HttpHeaders()
        headers.add("Set-Cookie", cookie.toString())
        return ResponseEntity<Any>(headers, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun register(@RequestBody registerDto: UserRegisterRequestDto) = ResponseEntity.ok().body(userService.createUser(registerDto))
}
