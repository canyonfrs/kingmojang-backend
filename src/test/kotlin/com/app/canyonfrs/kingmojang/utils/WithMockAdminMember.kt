package com.app.canyonfrs.kingmojang.utils

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockAdminMemberSecurityContextFactory::class)
annotation class WithMockAdminMember