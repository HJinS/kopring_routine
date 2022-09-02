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
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider(private val userDetailsService: UserDetailsServiceImpl) {

    val EXP_TIME: Long = 30 * 60 * 1000L
    val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.HS256

    // 토큰생성
    fun createToken(email: String, isAccess: Boolean = true): String {
        val claims: Claims = Jwts.claims().setSubject(email)
        val now = Date()
        var secretKey: PrivateKey
        claims["email"] = email
        if (isAccess){
            secretKey = JWT_ACCESS_PRIVATE
        }else{
            secretKey = JWT_REFRESH_PRIVATE
        }
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + EXP_TIME))
            .signWith(secretKey, SIGNATURE_ALG)
            .compact()
    }

    // 토큰검증
    fun validation(token: String) : Boolean {
        val claims: Claims = getAllClaims(token)
        val exp: Date = claims.expiration
        return exp.before(Date())
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getSubject(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 모든 Claims 조회
    private fun getAllClaims(token: String, isAccess: Boolean = true): Claims{
        val key = if (isAccess) JWT_ACCESS_PUBLIC else JWT_REFRESH_PUBLIC
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body
    }

    fun parseEmail(token: String): String {
        val actualToken: String = getToken(token)
        val claims: Claims? = getAllClaims(actualToken)
        return claims?.get("email").toString()
    }

    private fun getSubject(token: String): String{
        val actualToken: String = getToken(token)
        return getAllClaims(actualToken).subject
    }

    fun getToken(token: String): String{
        val headerAuth: String = token;

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length);
        }
        throw JwtException("Invalid token")
    }

    fun resolveToken(request: HttpServletRequest): Array<Cookie>? = request.cookies

    companion object{

        private const val accessPublicPath = "./public-access-key.pem"
        private const val accessPrivatePath = "./private-access-key.pem"
        private const val refreshPublicPath = "./public-refresh-key.pem"
        private const val refreshPrivatePath = "./private-refresh-key.pem"
        private val JWT_ACCESS_PUBLIC: ECPublicKey = getPublicKey(accessPublicPath)
        private val JWT_ACCESS_PRIVATE: ECPrivateKey = getPrivateKey(accessPrivatePath)
        private val JWT_REFRESH_PUBLIC: ECPublicKey = getPublicKey(refreshPublicPath)
        private val JWT_REFRESH_PRIVATE: ECPrivateKey = getPrivateKey(refreshPrivatePath)

        private fun getPublicKey(path: String): ECPublicKey{
            val keyPairGenerator: KeyFactory = KeyFactory.getInstance("EC")
            return FileInputStream(path).use{
                keyPairGenerator.generatePublic(X509EncodedKeySpec(it.readAllBytes())) as ECPublicKey
            }
        }

        private fun getPrivateKey(path: String): ECPrivateKey{
            val keyPairGenerator: KeyFactory = KeyFactory.getInstance("EC")
            return FileInputStream(path).use{
                keyPairGenerator.generatePrivate(PKCS8EncodedKeySpec(it.readAllBytes())) as ECPrivateKey
            }
        }
    }
}