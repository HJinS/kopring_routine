package routine.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import routine.entity.common.ResultConverter
import routine.entity.common.ResultEnum
import javax.persistence.*



@Entity
@Table(name="routine_result")
class RoutineResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @JoinColumn(name = "routine_id")
    @ManyToOne(cascade = [CascadeType.REMOVE])
    val routine: Routine,

    @Convert(converter = ResultConverter::class)
    val result: ResultEnum,

    @Column(name = "is_deleted")
    val is_deleted: Boolean,
    @Column(name = "is_alarm")
    val is_alaram: Boolean,

): Base()