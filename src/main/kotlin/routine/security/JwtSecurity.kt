package routine.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
class JwtSecurity(
    private val userDetailService: UserDetailsServiceImpl,
    private val jwtTokenProvider: JwtTokenProvider
){
    @Bean
    public fun filterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/members/signup", "/api/members/signin").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}