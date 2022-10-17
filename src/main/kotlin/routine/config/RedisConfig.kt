package routine.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Value("\${spring.redis.host}")
    val host: String = ""

    @Value("\${spring.redis.port}")
    val port: Int = 0

    @Bean
    fun redisConnectFactory() = LettuceConnectionFactory(host, port)

    @Bean
    fun redisTemplate(): RedisTemplate<String, Unit>{
        val redisTemplate = RedisTemplate<String, Unit>()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.setConnectionFactory(redisConnectFactory())
        return redisTemplate
    }

    fun stringRedisTemplate(): StringRedisTemplate{
        val stringTemplate = StringRedisTemplate()
        stringTemplate.keySerializer = StringRedisSerializer()
        stringTemplate.valueSerializer = StringRedisSerializer()
        stringTemplate.setConnectionFactory(redisConnectFactory())
        return stringTemplate
    }
}