package routine.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import routine.dto.RoutineCreateRequestDto
import routine.dto.RoutineDayCreateDto
import routine.service.RoutineDayService
import routine.service.RoutineService


@RestController
@RequestMapping("/routine/routines")
class RoutineController(
    private val routineService: RoutineService,
    private val routineDayService: RoutineDayService
) {

    @PostMapping("/")
    fun createRoutine(@RequestBody routineCreateRequestDto: RoutineCreateRequestDto): Map<String, Any>{
        val routine = routineService.createRoutine(routineCreateRequestDto)
        routineDayService.createRoutineDay(RoutineDayCreateDto(routine = routine, day = routineCreateRequestDto.day))
        return mapOf("data" to mapOf("routine_id" to routine.id), "message" to mapOf("msg" to "  .", "status" to "ROUTINE_CREATE_OK"))
    }

    @GetMapping("/")
    fun listRoutine(@RequestParam id: Long){
        routineService.getRoutineList(id)
    }
}

/*
*
*
routine CRUD
{
  'title': 제목,
  'category': "MIRACLE" or "HOMEWORK",
  'goal': 목표,
  'is_alarm': 알람 여부,
  'days': 루틴의 요일, list형태
}
*
*
* */