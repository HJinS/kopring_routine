package routine.service

import org.springframework.stereotype.Service
import routine.dto.RoutineDayCreateDto
import routine.repository.RoutineDayRepository

@Service
class RoutineDayService(private val routineDayRepository: RoutineDayRepository) {
    fun createRoutineDay(routineDayCreateDto: RoutineDayCreateDto) = routineDayRepository.save(routineDayCreateDto.toEntity())
}