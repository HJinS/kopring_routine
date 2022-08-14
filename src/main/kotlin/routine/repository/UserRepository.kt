package routine.repository

import org.springframework.data.jpa.repository.JpaRepository
import routine.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String) : User?
}