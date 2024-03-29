package routine.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import routine.jwt.JwtFilter
import routine.jwt.JwtTokenProvider
import routine.service.UserDetailsServiceImpl


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class JwtSecurity(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailService: UserDetailsServiceImpl,
    private val redisTemplate: RedisTemplate<String, String>
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                *authWhitelist
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(JwtFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authManager(http: HttpSecurity, bCryptPasswordEncoder: BCryptPasswordEncoder): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userDetailService)
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build()
    }

    private val authWhitelist = arrayOf(
        "/user/users/login",
        "/user/users/register",
        "/user/users/refresh",
        "/swagger-ui/index.html",
        "/authenticate",
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/v3/api-docs",
        "/webjars/**",
        "/v3/api-docs/**"
    )

}
