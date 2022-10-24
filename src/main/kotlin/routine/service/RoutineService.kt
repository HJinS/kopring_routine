package routine.service

import org.springframework.stereotype.Service
import routine.dto.*
import routine.entity.Routine
import routine.repository.RoutineRepository
import routine.repository.UserRepository

@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val userRepository: UserRepository
) {
    fun createRoutine(routineCreateDto: RoutineCreateRequestDto, userId: Long): Routine {
        val user = userRepository.getReferenceById(userId)
        return routineRepository.save(routineCreateDto.toEntity(user=user))
    }

    fun updateRoutine(routineUpdateDto: RoutineUpdateRequestDto, routineId: Long): Routine{
        val routine = routineRepository.getReferenceById(routineId)
        return routineRepository.save(routineUpdateDto.toEntity(routine))
    }

    fun listRoutine(userId: Long): CommonDataResponseDto {
        val routines = routineRepository.getRoutines(userId = userId)
        val routineDtoList = routines.map{RoutineListResponseDto(goal = it.goal, id = it.id, result = it.result, title = it.title)}
        return CommonDataResponseDto(data = routineDtoList, message = MessageResponseDto(msg = "조회 성공", status = "ROUTINE_LIST_OK"))
    }

    fun detailRoutine(routineId: Long): CommonDatumResponseDto {
        val routineInfo = routineRepository.getRoutineDetail(routineId = routineId)
        val dayList = routineInfo.map(RoutineDetailResultDto::day)
        val routineResult = routineInfo[0]
        return CommonDatumResponseDto(data = RoutineDetailResponseDto(
            goal = routineResult.goal,
            result = routineResult.result,
            title = routineResult.title,
            days = dayList,
            category = routineResult.category,
            createdDate = routineResult.createdDate,
            modifiedDate = routineResult.modifiedDate
        ), message = MessageResponseDto(msg = "조회 성공", status = "ROUTINE_DETAIL_OK"))
    }

    fun deleteRoutine(routineId: Long): CommonDatumResponseDto{
        routineRepository.deleteById(routineId)
        return CommonDatumResponseDto(data = RoutineDeleteResponseDto(id = routineId), message = MessageResponseDto(msg = "삭제 성공", status = "ROUTINE_DELETE_OK"))
    }
}