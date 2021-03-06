package com.foundy.hansungnotification.utils

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RetryTestRule(private val retryCount: Int = 5) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return statement {
            for (i in 1..retryCount) {
                try {
                    base.evaluate()
                    break
                } catch (t: Throwable) {
                    if (i == retryCount) {
                        throw t
                    }
                }
            }
        }
    }

    private inline fun statement(crossinline eval: () -> Unit): Statement {
        return object : Statement() {
            override fun evaluate() = eval()
        }
    }
}