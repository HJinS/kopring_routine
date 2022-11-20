package routine.tasks

import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import routine.entity.Routine
import routine.entity.RoutineResult
import routine.entity.common.DayEnum
import routine.entity.common.ResultEnum
import routine.repository.RoutineRepository
import routine.repository.RoutineResultRepository
import java.time.LocalDateTime
import java.time.Period
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.*
import java.util.concurrent.Future

@Service
class RoutineResultCreator(
    private val routineRepository: RoutineRepository,
    private val routineResultRepository: RoutineResultRepository
) {

    @Scheduled(cron = "0 * 0 * * ?")
    @Async
    fun createRoutine(): CompletableFuture<List<RoutineResult>> {
        val routines = routineRepository.getRoutinesWithDay()
        val routineResults: MutableList<RoutineResult> = mutableListOf()
        routines.map {
            val routine = it.routine
            val day = it.day
            day.let{ dayIt ->
                val now = LocalDateTime.now()
                val mPeriod = Period.ofDays(now.dayOfWeek.value)
                val startOfWeek = now - mPeriod
                val resultDate = startOfWeek + Period.ofDays(dayIt.day)
                routineResults.add(RoutineResult(
                    routine=routine,
                    result=ResultEnum.NOT,
                    isAlarm=false,
                    isDeleted=false,
                    createdDt=resultDate,
                    updatedDt=resultDate
                ))
            }
        }
        return completedFuture(routineResultRepository.saveAll(routineResults))
    }
}