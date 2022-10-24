package routine.dto

import routine.entity.Routine
import routine.entity.RoutineDay
import routine.entity.common.DayEnum

data class RoutineDayCreateDto(val routine: Routine, val days: List<DayEnum>){
    fun toEntity(): MutableList<RoutineDay>{
        val entities: MutableList<RoutineDay> = mutableListOf()
        days.map{
            entities.add(RoutineDay(day = it, routine = routine))
        }
        return entities
    }
}

data class RoutineDayUpdateDto(val routine: Routine, val days: List<DayEnum>?){
    fun toEntity(): MutableList<RoutineDay>{
        val entities: MutableList<RoutineDay> = mutableListOf()
        days?.map{
            entities.add(RoutineDay(day = it, routine = routine))
        }
        return entities
    }
}
