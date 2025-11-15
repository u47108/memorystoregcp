package com.latam.bebigquery.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latam.bebigquery.entity.User;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisTemplate redisTemplate;

    private static final String KEY = "user";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserDaoImpl.class);

    public Boolean saveUser(User user) {
        if (user == null || user.getName() == null || user.getName().trim().isEmpty()) {
            logger.warn("Attempted to save null user or user with null/empty name");
            return false;
        }
        
        try {
            Map userHash = OBJECT_MAPPER.convertValue(user, Map.class);
            redisTemplate.opsForHash().put(KEY, user.getName(), userHash);
            return true;
        } catch (Exception e) {
            logger.error("Error saving user: {}", user.getName(), e);
            return false;
        }
    }

    public User findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Attempted to find user with null or empty name");
            return null;
        }
        
        try {
            Map userMap = (Map) redisTemplate.opsForHash().get(KEY, name);
            if (userMap == null) {
                logger.debug("User not found for name: {}", name);
                return null;
            }
            
            return OBJECT_MAPPER.convertValue(userMap, User.class);
        } catch (Exception e) {
            logger.error("Error finding user by name: {}", name, e);
            return null;
        }
    }

}