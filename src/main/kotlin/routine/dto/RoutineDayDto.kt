package routine.dto

import routine.entity.Routine
import routine.entity.RoutineDay
import routine.entity.common.DayEnum

data class RoutineDayCreateDto(val routine: Routine, val day: DayEnum){
    fun toEntity(): RoutineDay = RoutineDay(day = day, routine = routine)
}
