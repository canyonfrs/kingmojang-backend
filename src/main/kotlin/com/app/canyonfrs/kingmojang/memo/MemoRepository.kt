package com.app.canyonfrs.kingmojang.memo

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

interface MemoRepository : JpaRepository<Memo, Long>, MemoCustomRepository

interface MemoCustomRepository {
    fun findByMemoCursorPageCondition(memoCursorPageCondition: MemoCursorPageCondition): List<Memo>
}

@Repository
@Transactional(readOnly = true)
class MemoCustomRepositoryImpl(
    val jdbcClient: JdbcClient
) : MemoCustomRepository {

    override fun findByMemoCursorPageCondition(memoCursorPageCondition: MemoCursorPageCondition): List<Memo> {
        var sql = "SELECT * FROM memo WHERE 1=1 AND id < :lastCursorId"
        if (memoCursorPageCondition.streamerId != null) {
            sql += " AND writer_id = :writerId"
        }
        sql += " ORDER BY id DESC LIMIT :pageSize"

        var client = jdbcClient.sql(sql)
            .param("lastCursorId", memoCursorPageCondition.lastCursorId)
            .param("pageSize", memoCursorPageCondition.pageSize)

        if (memoCursorPageCondition.streamerId != null) {
            client.param("writerId", memoCursorPageCondition.streamerId)
        }

        return client.query(Memo::class.java).list()
    }
}