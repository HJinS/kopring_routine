package routine.support

import com.querydsl.core.Tuple
import routine.dto.RoutineDayQueryResultDto
import routine.dto.RoutineDetailResultDto
import routine.dto.RoutineListResponseDto

interface RoutineSupport {
    fun getRoutines(userId: Long): List<RoutineListResponseDto>

    fun getRoutineDetail(routineId: Long): List<RoutineDetailResultDto>

    fun getRoutinesWithDay(): List<RoutineDayQueryResultDto>
}
