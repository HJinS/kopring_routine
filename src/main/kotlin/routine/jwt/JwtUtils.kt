package routine.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import routine.service.UserDetailsServiceImpl
import java.util.*


@Component
class JwtUtils(private val userDetailsService: UserDetailsServiceImpl) {

    val EXP_TIME: Long = 1000L * 60 * 3
    val JWT_SECRET: String = "12bcdef662a1deeee90d69df6881a9f7eb7d2c6ee09404553ea8edbff478179dbfe1997481f7944f0c2a8e1998c69c64a4f90b02e7294b8bb41aa9f06d79bf83"
    val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.HS256

    // 토큰생성
    fun createToken(username: String): String {
        val claims: Claims = Jwts.claims()
        claims["email"] = username

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(Date(System.currentTimeMillis()+ EXP_TIME))
            .signWith(SIGNATURE_ALG, JWT_SECRET)
            .compact()
    }

    // 토큰검증
    fun validation(token: String) : Boolean {
        val claims: Claims = getAllClaims(token)
        val exp: Date = claims.expiration
        return exp.after(Date())
    }

    // 토큰에서 username 파싱
    fun parseEmail(token: String): String {
        val claims: Claims = getAllClaims(token)
        return claims["email"].toString()
    }

    fun getAuthentication(email: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    // 모든 Claims 조회
    private fun getAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(JWT_SECRET)
            .parseClaimsJws(token)
            .body
    }
}