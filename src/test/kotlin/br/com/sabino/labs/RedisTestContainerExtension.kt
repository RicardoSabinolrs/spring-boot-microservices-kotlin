
package br.com.sabino.labs

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.GenericContainer
import java.util.concurrent.atomic.AtomicBoolean

class RedisTestContainerExtension : BeforeAllCallback {
    private val started = AtomicBoolean(false)

    companion object {
        @JvmStatic
        val redis = KGenericContainer("redis:6.2.1").withExposedPorts(6379)
    }

    @Throws(Exception::class)
    override fun beforeAll(extensionContext: ExtensionContext) {
        if (!started.get()) {
            redis.start()
            System.setProperty("cache.redis.server", "redis://" + redis.containerIpAddress + ":" + redis.getMappedPort(6379))
            started.set(true)
        }
    }

    // Workaround for Testcointainers issue https://github.com/testcontainers/testcontainers-java/issues/318#issuecomment-290692749
    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
}
