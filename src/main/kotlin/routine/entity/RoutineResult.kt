package routine.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import routine.entity.common.ResultConverter
import routine.entity.common.ResultEnum
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name="routine_result")
@EntityListeners(AuditingEntityListener::class)
class RoutineResult(result: ResultEnum, isDeleted: Boolean, isAlarm: Boolean){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0

    @JoinColumn(name = "routine_id")
    @ManyToOne(cascade = [CascadeType.REMOVE])
    lateinit var routine: Routine

    @Convert(converter = ResultConverter::class)
    var result: ResultEnum = result
        protected set

    @Column(name = "is_deleted")
    var isDeleted: Boolean = isDeleted
        protected set

    @Column(name = "is_alarm")
    var isAlarm: Boolean = isAlarm
        protected set

    @CreatedDate
    @Column(name = "created_dt")
    lateinit var createdDt: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_dt")
    lateinit var updatedDt: LocalDateTime
}