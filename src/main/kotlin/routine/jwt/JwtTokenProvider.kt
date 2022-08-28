package routine.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import routine.service.UserDetailsServiceImpl
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider(private val userDetailsService: UserDetailsServiceImpl) {

    val EXP_TIME: Long = 30 * 60 * 1000L
    final val JWT_SECRET: String = "12bcdef662a1deeee90d69df6881a9f7eb7d2c6ee09404553ea8edbff478179dbfe1997481f7944f0c2a8e1998c69c64a4f90b02e7294b8bb41aa9f06d79bf83"
    val key : Key = Keys.hmacShaKeyFor(JWT_SECRET.toByteArray(StandardCharsets.UTF_8))
    val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.HS256

    // 토큰생성
    fun createToken(email: String): String {
        val claims: Claims = Jwts.claims().setSubject(email)
        val now = Date()
        claims["email"] = email

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + EXP_TIME))
            .signWith(key, SIGNATURE_ALG)
            .compact()
    }

    // 토큰검증
    fun validation(token: String) : Boolean {
        val claims: Claims = getAllClaims(token)
        val exp: Date = claims.expiration
        return exp.before(Date())
    }

    // 토큰에서 username 파싱
    fun parseEmail(token: String): String {
        val claims: Claims = getAllClaims(token)
        return claims["email"].toString()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getSubject(token))

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 모든 Claims 조회
    private fun getAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSubject(token: String): String{
        return getAllClaims(token).subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }
}