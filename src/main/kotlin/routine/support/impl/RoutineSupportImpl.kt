package routine.support.impl

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import routine.config.QueryDslSupport
import routine.dto.RoutineDetailResultDto
import routine.dto.RoutineListResponseDto
import routine.entity.Routine
import routine.entity.QRoutine.routine
import routine.entity.QRoutineResult.routineResult
import routine.entity.QRoutineDay.routineDay
import routine.support.RoutineSupport

class RoutineSupportImpl(private val query: JPAQueryFactory): QueryDslSupport(Routine::class.java), RoutineSupport {
    override fun getRoutines(userId: Long): List<RoutineListResponseDto> =
        query.select(
            Projections.constructor(
                RoutineListResponseDto::class.java, routine.goal, routine.id, routineResult.result, routine.title))
        .from(routine)
        .where(routine.user.id.eq(userId))
        .leftJoin(routineResult).on(routine.id.eq(routineResult.routine.id))
        .fetchJoin().fetch()

    override fun getRoutineDetail(routineId: Long): List<RoutineDetailResultDto> =
        query.select(
            Projections.constructor(
                RoutineDetailResultDto::class.java, routine.goal, routineResult.result, routine.title, routineDay.day, routine.category, routine.createdDt, routine.updatedDt,
            )
        )
            .from(routine)
            .where(routine.id.eq(routineId))
            .leftJoin(routineResult).on(routine.id.eq(routineResult.routine.id))
            .fetchJoin()
            .leftJoin(routineDay).on(routine.id.eq(routineDay.routine.id))
            .fetchJoin()
            .fetch()
}