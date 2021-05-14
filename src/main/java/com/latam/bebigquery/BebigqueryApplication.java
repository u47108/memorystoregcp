package com.latam.bebigquery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
@EnableCaching
@EnableRedisRepositories
public class BebigqueryApplication {

  @Value("spring.redis.host")
  String hostName;
  @Value("spring.redis.port")
  String port;

  public static void main(String[] args) {
    SpringApplication.run(BebigqueryApplication.class, args);
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, 6379);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);

  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

    template.setConnectionFactory(redisConnectionFactory());

    template.setKeySerializer(stringSerializer);
    template.setHashKeySerializer(stringSerializer);

    template.setValueSerializer(jdkSerializationRedisSerializer);
    template.setHashValueSerializer(jdkSerializationRedisSerializer);

    template.setEnableTransactionSupport(true);
    template.afterPropertiesSet();

    return template;

  }
}
