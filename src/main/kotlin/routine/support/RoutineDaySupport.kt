package routine.support

import routine.entity.RoutineDay

interface RoutineDaySupport {
    fun getRoutineDays(routineId: Long): List<RoutineDay>
}
