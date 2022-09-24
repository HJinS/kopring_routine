package routine.dto

import routine.entity.Routine
import routine.entity.common.CategoryEnum
import routine.entity.common.DayEnum
import routine.entity.common.ResultEnum

data class RoutineCreateRequestDto(val title: String, val category: CategoryEnum, val goal: String, val isAlarm: Boolean, val day: DayEnum){
    fun toEntity(): Routine = Routine(goal = goal, category = category)
}

data class RoutineListResponseDto(val goal: String, val id: Long, val result: ResultEnum, val title: String)

data class MessageResponseEntity(val msg: String, val status: String)

data class CommonResponseDto(val data: List<Any>, val message: MessageResponseEntity)