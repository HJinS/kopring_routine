import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.test.AssertionMode
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode

class KotestConfig : AbstractProjectConfig() {
    override val parallelism = 4
    override val assertionMode = AssertionMode.Error
    override val globalAssertSoftly = true
    override val failOnIgnoredTests = false
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

}