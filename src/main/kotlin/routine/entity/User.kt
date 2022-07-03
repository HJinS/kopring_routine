package routine.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
class User (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "email")
    val email: String,
    @Column(name = "password")
    val password: String,
    @Column(name = "name")
    val name: String,

): Base()
