package routine.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import routine.dto.UserRegisterRequestDto
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
class User (email: String, password: String, name: String){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0

    @Column(name = "email", unique = true)
    var email: String = email
        protected set

    @Column(name = "password")
    var password: String = password
        protected set

    @Column(name = "name")
    var name: String = name
        protected set

    @CreatedDate
    @Column(name = "created_dt")
    lateinit var createdDt: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_dt")
    lateinit var updatedDt: LocalDateTime


    companion object{
        fun createUser(userRegisterRequestDto: UserRegisterRequestDto, passwordEncoder: BCryptPasswordEncoder): User{
            return User(
                email=userRegisterRequestDto.email,
                password=passwordEncoder.encode(userRegisterRequestDto.password),
                name=userRegisterRequestDto.name
            )
        }
    }
}
