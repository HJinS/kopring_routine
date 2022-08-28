package routine.controller

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
    fun login(@RequestBody loginDto: UserLoginRequestDto) = ResponseEntity.ok().body(userService.login(loginDto))

    @PostMapping("/register")
    fun register(@RequestBody registerDto: UserRegisterRequestDto) = ResponseEntity.ok().body(userService.createUser(registerDto))
}
