package routine.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
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
class Routine(goal: String, category: CategoryEnum, title: String, user: User){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0

    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User = user

    @Column(name = "title", length = 200)
    var title: String = title

    @Convert(converter = CategoryConverter::class)
    @Column(name = "category")
    var category: CategoryEnum = category

    @Column(name = "goal")
    var goal: String = goal

    @CreatedDate
    @Column(name = "created_dt")
    lateinit var createdDt: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_dt")
    lateinit var updatedDt: LocalDateTime
}