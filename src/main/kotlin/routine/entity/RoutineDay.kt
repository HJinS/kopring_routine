package routine.entity

import routine.entity.common.DayConverter
import routine.entity.common.DayEnum
import javax.persistence.*

@Table(name = "routine_day")
@Entity
class RoutineDay(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "routine_id")
    val routine_id: Routine,


    @Convert(converter = DayConverter::class)
    @Column(name = "day")
    val day: List<DayEnum>,
)
