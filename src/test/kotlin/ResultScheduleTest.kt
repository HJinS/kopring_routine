import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import routine.RoutineKopringApplication
import routine.dto.UserRegisterRequestDto
import routine.entity.common.CategoryEnum
import routine.entity.common.DayEnum
import routine.service.UserService
import routine.tasks.RoutineResultCreator
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates


@AutoConfigureMockMvc
@SpringBootTest(classes = [RoutineKopringApplication::class])
@ContextConfiguration(classes = [(KotestConfig::class)])
@ActiveProfiles("test")
class ResultScheduleTest: DescribeSpec() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var scheduleObject: RoutineResultCreator

    private var routineId by Delegates.notNull<Long>()

    private val loginUrl = "/user/users/login"

    private val routineListCreateUrl = "/routine/routines"

    override suspend fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        objectMapper = Jackson2ObjectMapperBuilder.json()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .modules(JavaTimeModule())
            .build()
    }

    init {
        this.describe("RoutineService"){
            context("루틴을 만들면"){
                it("루틴 아이디가 반환된다."){
                    val dto = UserRegisterRequestDto(email=userInfo["email"]!!, password=userInfo["password"]!!, confirmPassword= userInfo["confirmPassword"]!!, name=userInfo["name"]!!)
                    userService.createUser(dto)

                    val content = objectMapper.writeValueAsString(LoginInfo(email=userInfo["email"]!!, password=userInfo["password"]!!))
                    val loginResponse = mockMvc.perform(MockMvcRequestBuilders.post(loginUrl)
                        .content(content)
                        .contentType(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                    val responseCookies = loginResponse
                        .andExpect(status().isOk)
                        .andReturn().response.cookies

                    val routineCreateUrl = "/routine/routines"
                    val routineContent = objectMapper.writeValueAsString(RoutineInfo())
                    val response = mockMvc.perform(MockMvcRequestBuilders.post(routineCreateUrl)
                        .content(routineContent)
                        .contentType(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .cookie(responseCookies[0])
                        .cookie(responseCookies[1]))
                    val routineCreateResponse = response.andExpect(status().isOk).andReturn().response.contentAsString
                    val responseData = objectMapper.readValue(routineCreateResponse, RoutineCreateResult::class.java)
                    responseData shouldNotBe null
                    responseData.data.id shouldBe 1
                    responseData.message.status shouldBe "ROUTINE_CREATE_OK"
                    responseData.message.msg shouldBe "생성 성공"
                    routineId = responseData.data.id
                }
            }
            context("스케줄을 동작시키면"){
                it("루틴 결과가 만들어진다"){
                    val results = scheduleObject.createRoutine()
                    print("")
                }
            }
        }
    }

    companion object{
        private val userInfo = mapOf<String, String>(
            "email" to "test_email@example.com",
            "password" to "test_password",
            "confirmPassword" to "test_password",
            "name" to "test_user"
        )
        data class LoginInfo(val email: String, val password: String)
        data class RoutineInfo(
            val title: String = "title",
            val category:CategoryEnum = CategoryEnum.MIRACLE,
            val goal: String = "test goal",
            val is_alarm: Boolean = true,
            val days: List<DayEnum> = listOf(DayEnum.SUN, DayEnum.MON)
        )
        class RoutineId(@JsonProperty("id") var id: Long)
        class RoutineMessage(
            @JsonProperty("msg")
            var msg: String,
            @JsonProperty("status")
            var status: String
        )
        class RoutineCreateResult(
            @JsonProperty("data")
            var data: RoutineId,
            @JsonProperty("message")
            var message: RoutineMessage
        )
        class RoutineListData(
            @JsonProperty("goal")
            var goal: String,
            @JsonProperty("id")
            var id: Long,
            @JsonProperty("result")
            var result: String?,
            @JsonProperty("title")
            var title: String,
        )
        class RoutineListResult(
            @JsonProperty("data")
            var data: List<RoutineListData>,
            @JsonProperty("message")
            var message: RoutineMessage
        )
        class RoutineDetailData(
            @JsonProperty("goal")
            var goal: String,
            @JsonProperty("result")
            var result: String?,
            @JsonProperty("title")
            var title: String,
            @JsonProperty("category")
            var category: String,
            @JsonProperty("days")
            var days: List<String>?,
            @JsonProperty("createdDate")
            var createdDate: String,
            @JsonProperty("modifiedDate")
            var modifiedDate: String,
        )
        class RoutineDetailResult(
            @JsonProperty("data")
            var data: RoutineDetailData,
            @JsonProperty("message")
            var message: RoutineMessage
        )
    }
}