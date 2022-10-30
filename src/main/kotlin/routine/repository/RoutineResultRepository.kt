package routine.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import routine.entity.RoutineResult

@Repository
interface RoutineResultRepository: JpaRepository<RoutineResult, Long> {

}

