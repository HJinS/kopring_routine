package routine.dto

import routine.entity.Routine
import routine.entity.User
import routine.entity.common.CategoryEnum
import routine.entity.common.DayEnum
import routine.entity.common.ResultEnum
import java.time.LocalDateTime

data class RoutineCreateRequestDto(val title: String, val category: CategoryEnum, val goal: String, val isAlarm: Boolean, val days: List<DayEnum>){
    fun toEntity(user: User): Routine = Routine(goal = goal, category = category, title = title, user = user)
}

data class RoutineCreateResponseDto(val id: Long)

data class RoutineListResponseDto(val goal: String, val id: Long, val result: ResultEnum?, val title: String)

data class RoutineDetailResponseDto(val goal: String, val result: ResultEnum?, val title: String, val days: List<DayEnum>?, val category: CategoryEnum?, val createdDate: LocalDateTime, val modifiedDate: LocalDateTime)

data class RoutineDetailResultDto(val goal: String, val result: ResultEnum?, val title: String, val day: DayEnum, val category: CategoryEnum?, val createdDate: LocalDateTime, val modifiedDate: LocalDateTime)

data class MessageResponseDto(val msg: String, val status: String)

data class CommonDataResponseDto(val data: List<Any>?, val message: MessageResponseDto)

data class CommonDatumResponseDto(val data: Any?, val message: MessageResponseDto)