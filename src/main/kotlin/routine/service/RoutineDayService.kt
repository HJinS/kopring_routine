package routine.service

import org.springframework.stereotype.Service
import routine.dto.*
import routine.entity.Routine
import routine.repository.RoutineDayRepository

@Service
class RoutineDayService(private val routineDayRepository: RoutineDayRepository) {
    fun createRoutineDay(routine: Routine, routineDayCreateDto: RoutineDayCreateDto): CommonDatumResponseDto{
        routineDayRepository.saveAll(routineDayCreateDto.toEntity())
        val routineDto =  RoutineCreateResponseDto(id = routine.id)
        return CommonDatumResponseDto(data = routineDto, message = MessageResponseDto(msg = "생성 성공", status = "ROUTINE_CREATE_OK"))
    }
}