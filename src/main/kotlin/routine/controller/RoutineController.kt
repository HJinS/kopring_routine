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
        val responseDto = routineDayService.createRoutineDay(routine, RoutineDayCreateDto(routine = routine, days = routineCreateRequestDto.days))
        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("/{routineId}")
    fun updateRoutine(@PathVariable routineId: Long, @RequestBody routineUpdateRequestDto: RoutineUpdateRequestDto): ResponseEntity<CommonDatumResponseDto>{
        val routine = routineService.updateRoutine(routineUpdateRequestDto, routineId)
        val responseDto = routineDayService.updateRoutineDay(routine, RoutineDayUpdateDto(routine = routine, days = routineUpdateRequestDto.days))
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

    @DeleteMapping("/{routineId}")
    fun deleteRoutine(@PathVariable routineId: Long): ResponseEntity<CommonDatumResponseDto>{
        routineService.deleteRoutine(routineId)
        return ResponseEntity.noContent().build()
    }
}
