package routine.support

import routine.dto.RoutineDetailResultDto
import routine.dto.RoutineListResponseDto

interface RoutineSupport {
    fun getRoutines(userId: Long): List<RoutineListResponseDto>

    fun getRoutineDetail(routineId: Long): List<RoutineDetailResultDto>
}
