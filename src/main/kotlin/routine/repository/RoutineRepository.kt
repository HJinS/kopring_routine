package routine.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import routine.entity.Routine
import routine.support.RoutineSupport

@Repository
interface RoutineRepository: JpaRepository<Routine, Long>, RoutineSupport {
    fun findByUser(userId: Long): List<Routine>
}
