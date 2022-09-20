package routine.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
class JwtSecurity {
    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var userDetailService: UserDetailsServiceImpl

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/user/users/login", "/user/users/register", "/user/users/refresh", "/user/users/tmp").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
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
}
