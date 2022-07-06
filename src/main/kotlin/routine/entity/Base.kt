package routine.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Base (

    @CreatedDate
    @Column(name="created_dt", updatable = false)
    var created_dt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name="updated_dt")
    var updated_dt: LocalDateTime = LocalDateTime.now(),
)
