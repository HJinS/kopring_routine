package com.tistory.eclipse4j.domain.persist.dic

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.AssertionMode
import io.kotest.extensions.spring.SpringExtension

object KotestConfig : AbstractProjectConfig() {
    override val parallelism = 3
    override val assertionMode = AssertionMode.Error
    override val globalAssertSoftly = true
    override val failOnIgnoredTests = false
    override val isolationMode = IsolationMode.SingleInstance

    override fun extensions() = listOf(SpringExtension)
}