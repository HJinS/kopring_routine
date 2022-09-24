package routine.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import routine.entity.Routine

@Repository
interface RoutineRepository: JpaRepository<Routine, Long> {
    //TODO queryDSL 사용하여 조인 처리 할 것
    fun findByUser(userId: Long): List<Routine>
}
