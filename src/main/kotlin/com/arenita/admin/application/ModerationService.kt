package com.arenita.admin.application

import com.arenita.admin.api.dto.*
import com.arenita.admin.domain.model.*
import com.arenita.admin.domain.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ModerationService(
    private val ruleRepo: ModerationRuleRepository,
    private val auditRepo: AuditLogRepository
) {
    fun getRules(userId: UUID?): List<RuleResponse> {
        val rules = if (userId != null) ruleRepo.findByUserId(userId) else ruleRepo.findByEnabled(true)
        return rules.map { it.toResponse() }
    }

    @Transactional
    fun createRule(req: CreateRuleRequest, adminId: String): RuleResponse {
        val rule = ModerationRule(
            name = req.name, description = req.description,
            userId = req.userId,
            ruleType = RuleType.valueOf(req.ruleType.uppercase()),
            value = req.value, action = req.action
        )
        val saved = ruleRepo.save(rule)
        auditRepo.save(AuditLog(
            adminId = adminId, targetUserId = req.userId,
            actionType = AuditAction.RULE_CREATED,
            details = "Created rule: ${req.name} (${req.ruleType})"
        ))
        return saved.toResponse()
    }

    @Transactional
    fun deleteRule(id: UUID, adminId: String) {
        val rule = ruleRepo.findById(id).orElseThrow { NoSuchElementException("Rule not found") }
        ruleRepo.delete(rule)
        auditRepo.save(AuditLog(
            adminId = adminId, targetUserId = rule.userId,
            actionType = AuditAction.RULE_DELETED,
            details = "Deleted rule: ${rule.name}"
        ))
    }
}
