package routine.support.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import routine.config.QueryDslSupport
import routine.entity.RoutineDay
import routine.entity.QRoutineDay.routineDay
import routine.support.RoutineDaySupport

class RoutineDaySupportImpl(private val query: JPAQueryFactory): QueryDslSupport(RoutineDay::class.java), RoutineDaySupport {

    override fun getRoutineDays(routineId: Long): List<RoutineDay> =
        query.selectFrom(routineDay)
            .where(routineDay.routine.id.eq(routineId))
            .fetch()
}
