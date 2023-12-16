package com.app.canyonfrs.kingmojang.utils

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class AcceptanceTestExecutionListener : AbstractTestExecutionListener() {

    override fun beforeTestMethod(testContext: TestContext) {
        val jdbcTemplate = getJdbcTemplate(testContext)
        val truncateQueries = getTruncateQueries(jdbcTemplate)
        truncateTables(jdbcTemplate, truncateQueries)
    }

    // h2
    private fun getTruncateQueries(jdbcTemplate: JdbcTemplate): MutableList<String> {
        return jdbcTemplate.queryForList(
            "SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
            String::class.java
        )
    }

    private fun getJdbcTemplate(testContext: TestContext): JdbcTemplate {
        return testContext.applicationContext.getBean(JdbcTemplate::class.java)
    }

    private fun truncateTables(jdbcTemplate: JdbcTemplate, truncateQueries: List<String>) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE")
        truncateQueries.forEach { query -> execute(jdbcTemplate, query) }
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE")
    }

    private fun execute(jdbcTemplate: JdbcTemplate, query: String) {
        jdbcTemplate.execute(query)
    }
}