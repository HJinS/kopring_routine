package routine.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import routine.service.UserDetailsServiceImpl
import java.security.PrivateKey

import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider(private val userDetailsService: UserDetailsServiceImpl) {
    // 토큰생성
    fun createToken(email: String, isAccess: Boolean = true): String {
        val claims: Claims = Jwts.claims().setSubject(email)
        val now = Date()
        val secretKey: PrivateKey = if(isAccess) accessKeyPair.private else refreshKeyPair.private
        claims["email"] = email
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + EXP_TIME))
            .signWith(secretKey, SIGNATURE_ALG)
            .compact()
    }

    // 토큰검증
    fun validation(token: String, isAccess: Boolean) : Boolean {
        val claims: Claims = getAllClaims(token, isAccess)
        val exp: Date = claims.expiration
        return exp.before(Date())
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getSubject(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 모든 Claims 조회
    private fun getAllClaims(token: String, isAccess: Boolean = true): Claims{
        val key = if (isAccess) accessKeyPair.public else refreshKeyPair.public
        println(token)
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body
    }

    fun parseToken(token: String, isAccess: Boolean = true): Claims {
        val actualToken: String = getToken(token)
        return getAllClaims(actualToken, isAccess)
    }

    private fun getSubject(token: String): String{
        val actualToken: String = getToken(token)
        return getAllClaims(actualToken).subject
    }

    fun getToken(token: String): String{
        val headerAuth: String = token;

        if (StringUtils.hasText(headerAuth)) {
            return headerAuth
        }
        throw JwtException("Invalid token")
    }

    fun resolveToken(request: HttpServletRequest): List<Cookie>? = request.cookies?.filter { it.name.endsWith("Token") }

    companion object{
        private val accessKeyPair = Keys.keyPairFor(SignatureAlgorithm.ES256)
        private val refreshKeyPair = Keys.keyPairFor(SignatureAlgorithm.ES256)
        private const val EXP_TIME: Long = 30 * 60 * 1000L
        private val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.ES256
    }
}