package routine.entity

import routine.entity.common.CategoryConverter
import routine.entity.common.CategoryEnum
import javax.persistence.*


@Entity
@Table(name = "routine")
class Routine(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user_id: User,

    @Convert(converter = CategoryConverter::class)
    @Column(name = "category")
    val category: CategoryEnum,

    @Column(name = "goal")
    val goal: String,

    ): Base()