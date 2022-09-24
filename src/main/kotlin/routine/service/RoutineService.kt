package routine.service

import org.springframework.stereotype.Service
import routine.dto.RoutineCreateRequestDto
import routine.entity.Routine
import routine.repository.RoutineRepository

@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
) {
    fun createRoutine(routineCreateDto: RoutineCreateRequestDto) = routineRepository.save(routineCreateDto.toEntity())

    fun getRoutineList(userId: Long) {
        val routine = routineRepository.findByUser(userId = userId)

    }
}