package routine.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import routine.dto.UserLoginRequestDto
import routine.dto.UserRegisterRequestDto
import routine.service.UserService
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/user/users")
class UserController(private val userService: UserService){
    @PostMapping("/login")
    fun login(@RequestBody loginDto: UserLoginRequestDto, response: HttpServletResponse): ResponseEntity<Any>{
        val cookie = Cookie("access_token", "Bearer ${userService.login(loginDto)}")
        cookie.path = "/"
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/register")
    fun register(@RequestBody registerDto: UserRegisterRequestDto) = ResponseEntity.ok().body(userService.createUser(registerDto))
}
