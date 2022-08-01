package routine.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import routine.entity.common.CategoryConverter
import routine.entity.common.CategoryEnum
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "routine")
@EntityListeners(AuditingEntityListener::class)
class Routine(goal: String, category: CategoryEnum){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    lateinit var user: User

    @Convert(converter = CategoryConverter::class)
    @Column(name = "category")
    var category: CategoryEnum = category
        protected set

    @Column(name = "goal")
    var goal: String = goal
        protected set

    @CreatedDate
    @Column(name = "created_dt")
    lateinit var createdDt: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_dt")
    lateinit var updatedDt: LocalDateTime
}