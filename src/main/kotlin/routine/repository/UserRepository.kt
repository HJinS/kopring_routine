package routine.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import routine.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String) : User?
    fun existsByEmail(email: String): Boolean?

}