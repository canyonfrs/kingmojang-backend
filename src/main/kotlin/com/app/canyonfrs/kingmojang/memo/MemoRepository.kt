package com.app.canyonfrs.kingmojang.memo

import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long>