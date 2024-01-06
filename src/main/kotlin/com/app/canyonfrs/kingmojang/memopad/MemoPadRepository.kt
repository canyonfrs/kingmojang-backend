package com.app.canyonfrs.kingmojang.memopad

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

interface MemoPadRepository : JpaRepository<MemoPad, Long>, MemoPadCustomRepository

interface MemoPadCustomRepository {
    fun findByMemoPadCursorPageCondition(memoPadCursorPageCondition: MemoPadCursorPageCondition): List<MemoPad>
}

@Repository
@Transactional(readOnly = true)
class MemoPadCustomRepositoryImpl(
    val jdbcClient: JdbcClient
) : MemoPadCustomRepository {

    override fun findByMemoPadCursorPageCondition(memoPadCursorPageCondition: MemoPadCursorPageCondition): List<MemoPad> {
        var sql = "SELECT * FROM memo_pad WHERE 1=1 AND id < :lastCursorId"
        if (memoPadCursorPageCondition.streamerId != null) {
            sql += " AND writer_id = :writerId"
        }
        sql += " ORDER BY id DESC LIMIT :pageSize"

        var client = jdbcClient.sql(sql)
            .param("lastCursorId", memoPadCursorPageCondition.lastCursorId)
            .param("pageSize", memoPadCursorPageCondition.pageSize)

        if (memoPadCursorPageCondition.streamerId != null) {
            client.param("writerId", memoPadCursorPageCondition.streamerId)
        }

        return client.query(MemoPad::class.java).list()
    }
}