package routine.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import routine.dto.*
import routine.repository.UserDetailsImpl
import routine.service.RoutineDayService
import routine.service.RoutineService


@RestController
@RequestMapping("/routine")
class RoutineController(
    private val routineService: RoutineService,
    private val routineDayService: RoutineDayService
) {

    @PostMapping("/routines")
    fun createRoutine(@RequestBody routineCreateRequestDto: RoutineCreateRequestDto, authentication: Authentication): ResponseEntity<CommonDatumResponseDto> {
        val userAuth = authentication.principal as UserDetailsImpl
        val routine = routineService.createRoutine(routineCreateRequestDto, userAuth.userId)
        val responseDto = routineDayService.createRoutineDay(routine, RoutineDayCreateDto(routine = routine, day = routineCreateRequestDto.days))
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/routines")
    fun listRoutine(authentication: Authentication): ResponseEntity<CommonDataResponseDto>{
        val userAuth = authentication.principal as UserDetailsImpl
        val responseDto = routineService.listRoutine(userAuth.userId)
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/{routineId}")
    fun detailRoutine(@PathVariable routineId: Long): ResponseEntity<CommonDatumResponseDto>{
        val responseDto = routineService.detailRoutine(routineId = routineId)
        return ResponseEntity.ok(responseDto)
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