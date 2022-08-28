package routine.dto


data class UserLoginRequestDto(val email: String, val password: String)

data class UserRegisterRequestDto(val email: String, val password: String, val confirmPassword: String, val name: String)

data class UserRegisterResponseDto(val id: Long, val email: String)