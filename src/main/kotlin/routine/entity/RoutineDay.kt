package routine.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import routine.entity.common.DayConverter
import routine.entity.common.DayEnum
import javax.persistence.*

@Entity
@Table(name = "routine_day")
class RoutineDay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @JoinColumn(name = "routine_id")
    @OneToOne
    val routine: Routine,

    @Convert(converter = DayConverter::class)
    @Column(name = "day")
    val day: List<DayEnum>,

): Base()
