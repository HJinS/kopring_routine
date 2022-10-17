package routine.jwt

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.server.ResponseStatusException
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@Component
class JwtFilter(
    private val jwtUtils: JwtTokenProvider,
    private val redisTemplate: RedisTemplate<String, String>
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain){
        val resolvedToken: List<Cookie>? = jwtUtils.resolveToken(request as HttpServletRequest)
        val valueOperations = redisTemplate.opsForValue()
        resolvedToken?.let { itToken ->
            if(itToken.isNotEmpty()){
                val accessToken: String = itToken.first{ it.name == "accessToken"}.value
                if(valueOperations.get(accessToken) != null){
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Authenticated")
                }
                val authentication = jwtUtils.getAuthentication(accessToken)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(request, response)
    }
}