package routine.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@Component
class JwtFilter(private val jwtUtils: JwtTokenProvider) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain){
        val resolvedToken: List<Cookie> = jwtUtils.resolveToken((request as HttpServletRequest))
        if (resolvedToken.isNotEmpty()){
            val token: String = resolvedToken.first { it.name == "access_token " }.value
            val authentication = jwtUtils.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }
}