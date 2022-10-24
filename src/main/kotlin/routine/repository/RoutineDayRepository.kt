package routine.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import routine.entity.RoutineDay
import routine.support.RoutineDaySupport

@Repository
interface RoutineDayRepository: JpaRepository<RoutineDay, Long>, RoutineDaySupport {
}
