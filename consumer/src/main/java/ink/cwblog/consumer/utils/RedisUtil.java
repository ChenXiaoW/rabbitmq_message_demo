package ink.cwblog.consumer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description: redis 工具简单封装
 * @author: ChenWei
 * @create: 2020/5/16 - 22:15
 **/

@Slf4j
@Component
public class RedisUtil {
	@Autowired
	RedisTemplate redisTemplate;

	/**
	 * 批量删除数据
	 *
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 删除数据
	 *
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断数据是否存在
	 *
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取数据
	 *
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 写入数据
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			result = true;
		} catch (Exception e) {
			log.error("redis写入数据异常:{}", e);
		}
		return result;
	}

	/**
	 * 写入数据 - 可设置有效时间
	 *
	 * @param key        键
	 * @param value      值
	 * @param expireTime 过期时间 秒
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			log.error("redis写入数据并设置有效时间异常:{}", e);
		}
		return result;
	}
}
