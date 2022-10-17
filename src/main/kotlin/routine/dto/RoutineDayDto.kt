package routine.dto

import routine.entity.Routine
import routine.entity.RoutineDay
import routine.entity.common.DayEnum

data class RoutineDayCreateDto(val routine: Routine, val day: List<DayEnum>){
    fun toEntity(): MutableList<RoutineDay>{
        val entities: MutableList<RoutineDay> = mutableListOf()
        day.map{
            entities.add(RoutineDay(day = it, routine = routine))
        }
        return entities
    }
}
