import com.tistory.eclipse4j.domain.persist.dic.KotestConfig
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import routine.RoutineKopringApplication
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
                        val dto = UserRegisterRequestDto(email = it["email"]!!, password = it["password"]!!, confirmPassword = it["confirmPassword"]!!, name = it["name"]!!)
                        val createdUserInfo = userService.createUser(dto)
                        createdUserInfo.email shouldBe it["email"]
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
