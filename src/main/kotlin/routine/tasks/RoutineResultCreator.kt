package routine.tasks

import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import routine.entity.Routine
import routine.entity.RoutineResult
import routine.entity.common.DayEnum
import routine.entity.common.ResultEnum
import routine.repository.RoutineRepository
import routine.repository.RoutineResultRepository
import java.time.LocalDateTime
import java.time.Period

@Component
class RoutineResultCreator(
    private val routineRepository: RoutineRepository,
    private val routineResultRepository: RoutineResultRepository
) {

    @Scheduled(cron = "0 * 0 * * ?")
    @Async
    fun createRoutine(): List<RoutineResult>{
        val routines = routineRepository.getRoutinesWithDay()
        val routineResults: MutableList<RoutineResult> = mutableListOf()
        routines.let{
            val routine = it[0] as Routine
            val day = it[1] as DayEnum
            val now = LocalDateTime.now()
            val mPeriod = Period.ofDays(now.dayOfWeek.value)
            val startOfWeek = now - mPeriod
            val resultDate = startOfWeek + Period.ofDays(day.day)
            routineResults.add(RoutineResult(
                routine=routine,
                result=ResultEnum.NOT,
                isAlarm = false,
                isDeleted = false,
                createdDt = resultDate,
                updatedDt = resultDate
            ))
        }
        return routineResultRepository.saveAll(routineResults)
    }
}