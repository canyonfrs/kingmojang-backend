package com.app.canyonfrs.kingmojang.utils

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomStreamerSecurityContextFactory::class)
annotation class WithMockCustomStreamer()
