package routine.entity

import routine.entity.common.ResultConverter
import routine.entity.common.ResultEnum
import javax.persistence.*


@Table(name="routine_result")
@Entity
class RoutineResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "routine_id")
    val routine_id: Routine,

    @Convert(converter = ResultConverter::class)
    val result: ResultEnum,

    @Column(name = "is_deleted")
    val is_deleted: Boolean,
    @Column(name = "is_alarm")
    val is_alaram: Boolean,

    ): Base()