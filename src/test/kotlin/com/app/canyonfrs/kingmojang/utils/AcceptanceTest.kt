package com.app.canyonfrs.kingmojang.utils

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners

@SpringBootTest
@Retention(AnnotationRetention.RUNTIME)
@TestExecutionListeners(value = [AcceptanceTestExecutionListener::class], mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
annotation class AcceptanceTest