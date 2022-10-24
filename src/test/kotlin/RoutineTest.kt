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
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates


@AutoConfigureMockMvc
@SpringBootTest(classes = [RoutineKopringApplication::class])
@ContextConfiguration(classes = [(KotestConfig::class)])
@ActiveProfiles("test")
class RoutineTest: DescribeSpec() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var routineId by Delegates.notNull<Long>()

    private val loginUrl = "/user/users/login"

    private val routineListCreateUrl = "/routine/routines"

    private val routineDetailDeleteUrl = "/routine/"

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
            context("루틴을 조회하면"){
                it("루틴의 정보들이 반환된다"){
                    val content = objectMapper.writeValueAsString(LoginInfo(email=userInfo["email"]!!, password=userInfo["password"]!!))
                    var response = mockMvc.perform(MockMvcRequestBuilders.post(loginUrl)
                        .content(content)
                        .contentType(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                    val responseCookies = response
                        .andExpect(status().isOk)
                        .andReturn().response.cookies

                    response = mockMvc.perform(MockMvcRequestBuilders.get(routineListCreateUrl)
                        .accept(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .cookie(responseCookies[0])
                        .cookie(responseCookies[1]))
                    val routineListResponse = response.andExpect(status().isOk).andReturn().response.contentAsString
                    val responseData = objectMapper.readValue(routineListResponse, RoutineListResult::class.java)
                    val routineInfo = RoutineInfo()
                    responseData.data.size shouldBe 1
                    responseData.data[0].goal shouldBe routineInfo.goal
                    responseData.data[0].title shouldBe routineInfo.title
                    responseData.data[0].result shouldBe null
                    responseData.message.msg shouldBe "조회 성공"
                    responseData.message.status shouldBe "ROUTINE_LIST_OK"
                }
            }
            context("루틴 디테일 정보를 조회하면"){
                it("루틴 1개의 자세한 정보가 반환된다"){
                    val content = objectMapper.writeValueAsString(LoginInfo(email=userInfo["email"]!!, password=userInfo["password"]!!))
                    var response = mockMvc.perform(MockMvcRequestBuilders.post(loginUrl)
                        .content(content)
                        .contentType(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                    val responseCookies = response
                        .andExpect(status().isOk)
                        .andReturn().response.cookies

                    response = mockMvc.perform(MockMvcRequestBuilders.get(routineDetailDeleteUrl + routineId.toString())
                        .accept(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .cookie(responseCookies[0])
                        .cookie(responseCookies[1]))
                    val routineDetailResponse = response.andExpect(status().isOk).andReturn().response.contentAsString
                    val responseData = objectMapper.readValue(routineDetailResponse, RoutineDetailResult::class.java)
                    val routineInfo = RoutineInfo()
                    responseData.data.goal shouldBe routineInfo.goal
                    responseData.data.title shouldBe routineInfo.title
                    responseData.data.result shouldBe null
                    val daySet = mutableSetOf<DayEnum>()
                    val tmp = mutableListOf<DayEnum>()
                    responseData.data.days?.forEach{
                        day -> daySet.add(DayEnum.valueOf(day))
                    }
                    daySet shouldBe routineInfo.days.toSet()
                    responseData.message.msg shouldBe "조회 성공"
                    responseData.message.status shouldBe "ROUTINE_DETAIL_OK"
                }
            }
            context("루틴을 삭제하면"){
                it("루틴이 삭제된다"){
                    val content = objectMapper.writeValueAsString(LoginInfo(email= userInfo["email"]!!, password = userInfo["password"]!!))
                    var response = mockMvc.perform(MockMvcRequestBuilders.post(loginUrl)
                        .content(content)
                        .contentType(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                    val responseCookies = response
                        .andExpect(status().isOk)
                        .andReturn().response.cookies

                    response = mockMvc.perform(MockMvcRequestBuilders.delete(routineDetailDeleteUrl + routineId.toString())
                        .accept(MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .cookie(responseCookies[0])
                        .cookie(responseCookies[1]))
                    val routineDeleteResponse = response.andExpect(status().isNoContent).andReturn().response.contentAsString
                    routineDeleteResponse shouldBe ""
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