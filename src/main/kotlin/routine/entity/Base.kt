package routine.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Base (

    @CreationTimestamp
    var created_dt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    var updated_dt: LocalDateTime = LocalDateTime.now(),
)
