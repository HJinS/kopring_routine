package routine.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import routine.entity.common.CategoryConverter
import routine.entity.common.CategoryEnum
import javax.persistence.*


@Entity
@Table(name = "routine")
class Routine(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = [CascadeType.REMOVE])
    val user: User,

    @Convert(converter = CategoryConverter::class)
    @Column(name = "category")
    val category: CategoryEnum,

    @Column(name = "goal")
    val goal: String,
): Base()