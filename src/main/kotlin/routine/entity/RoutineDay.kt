package routine.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import routine.entity.common.DayConverter
import routine.entity.common.DayEnum
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "routine_day")
@EntityListeners(AuditingEntityListener::class)
class RoutineDay(day: DayEnum, routine: Routine){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0

    @JoinColumn(name = "routine_id")
    @ManyToOne(cascade = [CascadeType.REMOVE])
    var routine: Routine = routine

    @Convert(converter = DayConverter::class)
    @Column(name = "day")
    var day: DayEnum = day
        protected set

    @CreatedDate
    @Column(name = "created_dt")
    lateinit var createdDt: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_dt")
    lateinit var updatedDt: LocalDateTime
}
