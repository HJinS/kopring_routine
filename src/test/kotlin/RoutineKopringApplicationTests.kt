import com.tistory.eclipse4j.domain.persist.dic.KotestConfig
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.server.ResponseStatusException
import routine.RoutineKopringApplication
import routine.dto.UserLoginRequestDto
import routine.dto.UserRegisterRequestDto
import routine.service.UserService


@SpringBootTest(classes = [RoutineKopringApplication::class])
@ContextConfiguration(classes = [(KotestConfig::class)])
class UserServiceTest: DescribeSpec() {

    @Autowired
    private lateinit var userService: UserService

    init {
        this.describe("UserService"){
            context("유저를 만들면"){
                it("유저 정보가 반환된다"){
                    userInfo.invoke().forAll {
                        val dto = UserRegisterRequestDto(email=it["email"]!!, password=it["password"]!!, confirmPassword=it["confirmPassword"]!!, name=it["name"]!!)
                        val createdUserInfo = userService.createUser(dto)
                        createdUserInfo.email shouldBe it["email"]
                    }
                }
            }
            context("유저가 로그인을 하면"){
                it("토큰이 발급 된다"){
                    val userData = userInfo.invoke()[0]
                    val dto = UserLoginRequestDto(email=userData["email"]!!, password=userData["password"]!!)
                    val result = userService.login(dto)
                    result shouldNotBe null
                }
            }
            context("유저가 회원가입을 하지 않고 로그인을 하면"){
                it("에러가 반환 된다"){
                    val exception = shouldThrow<ResponseStatusException>{
                        val dto = UserLoginRequestDto(email="non-user@#example.com", password="non-user-password")
                        userService.login(dto)
                    }
                }
            }
        }
    }

    companion object{
        private val userInfo = {
            listOf(
                mapOf<String, String>("email" to "test_email1@example.com", "password" to "test_password1", "confirmPassword" to "test_password1", "name" to "test_user1"),
                mapOf<String, String>("email" to "test_email2@example.com", "password" to "test_password2", "confirmPassword" to "test_password2", "name" to "test_user2"),
                mapOf<String, String>("email" to "test_email3@example.com", "password" to "test_password3", "confirmPassword" to "test_password3", "name" to "test_user3")
            )
        }
    }

}
