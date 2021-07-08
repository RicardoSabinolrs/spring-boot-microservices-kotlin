package br.com.sabino.labs.infra.config

import org.redisson.Redisson
import org.redisson.config.Config
import org.redisson.jcache.configuration.RedissonConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.jhipster.config.JHipsterProperties
import tech.jhipster.config.cache.PrefixedKeyGenerator
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.cache.configuration.MutableConfiguration
import javax.cache.expiry.CreatedExpiryPolicy
import javax.cache.expiry.Duration

@Configuration
@EnableCaching
class CacheConfiguration(@Autowired val gitProperties: GitProperties?, @Autowired val buildProperties: BuildProperties?) {

    @Bean
    fun jcacheConfiguration(jHipsterProperties: JHipsterProperties): javax.cache.configuration.Configuration<Any, Any> {
        val jcacheConfig = MutableConfiguration<Any, Any>()
        val redisUri = URI.create(jHipsterProperties.cache.redis.server[0])
        val config = Config()
        if (jHipsterProperties.cache.redis.isCluster) {
            var clusterServersConfig = config
                .useClusterServers()
                .setMasterConnectionPoolSize(jHipsterProperties.cache.redis.connectionPoolSize)
                .setMasterConnectionMinimumIdleSize(jHipsterProperties.cache.redis.connectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(jHipsterProperties.cache.redis.subscriptionConnectionPoolSize)
                .addNodeAddress(*jHipsterProperties.cache.redis.server)
            if (redisUri.userInfo != null) {
                clusterServersConfig.setPassword(redisUri.userInfo.substring(redisUri.userInfo.indexOf(':') + 1))
            }
        } else {
            var singleServerConfig = config
                .useSingleServer()
                .setConnectionPoolSize(jHipsterProperties.cache.redis.connectionPoolSize)
                .setConnectionMinimumIdleSize(jHipsterProperties.cache.redis.connectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(jHipsterProperties.cache.redis.subscriptionConnectionPoolSize)
                .setAddress(jHipsterProperties.cache.redis.server[0])

            if (redisUri.userInfo != null) {
                singleServerConfig.setPassword(redisUri.userInfo.substring(redisUri.userInfo.indexOf(':') + 1))
            }
        }
        jcacheConfig.isStatisticsEnabled = true
        jcacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration(TimeUnit.SECONDS, jHipsterProperties.cache.redis.expiration.toLong())))
        return RedissonConfiguration.fromInstance(Redisson.create(config), jcacheConfig)
    }

    @Bean
    fun cacheManagerCustomizer(jcacheConfiguration: javax.cache.configuration.Configuration<Any, Any>): JCacheManagerCustomizer {
        return JCacheManagerCustomizer {
        }
    }

    private fun createCache(cm: javax.cache.CacheManager, cacheName: String, jcacheConfiguration: javax.cache.configuration.Configuration<Any, Any>) {
        val cache = cm.getCache<Any, Any>(cacheName)
        if (cache != null) {
            cache.clear()
        } else {
            cm.createCache(cacheName, jcacheConfiguration)
        }
    }

    @Bean
    fun keyGenerator() = PrefixedKeyGenerator(gitProperties, buildProperties)
}
