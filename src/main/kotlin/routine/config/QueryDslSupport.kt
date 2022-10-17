package routine.config

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
abstract class QueryDslSupport (domainClass: Class<*>): QuerydslRepositorySupport(domainClass){
    /**
     * Creates a new [QuerydslRepositorySupport] instance for the given domain type.}}"}}}
     * @param domainClass must not be null.
     */
    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }
}
