package ar.com.bia.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
public class CachingConfig {

	@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("queries");
    }
	
	//https://aggarwalarpit.wordpress.com/2017/01/25/setting-ttl-for-cacheable-spring/
	@CacheEvict(allEntries = true, cacheNames = { "queries" })
	@Scheduled(fixedDelay = 30000)
	public void cacheEvict() {
	}
	
}
