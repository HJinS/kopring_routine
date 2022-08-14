package routine.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import routine.jwt.JwtFilter
import routine.repository.UserDetailsImpl


@Configuration
@EnableWebSecurity
class JwtSecurity(
    private val userDetailsService: UserDetailsImpl,
    private val jwtFilter: JwtFilter
){

    @Bean
    public fun filterChain(http: HttpSecurity): SecurityFilterChain{
        http.authorizeRequests()
            .antMatchers("/api/members/signup", "/api/members/signin").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    public fun webSecurityCustomizer(web: WebSecurity) = web.ignoring().antMatchers("/api/signin", "/api/siginup");

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}