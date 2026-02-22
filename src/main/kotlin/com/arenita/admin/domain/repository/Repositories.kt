package com.arenita.admin.domain.repository

import com.arenita.admin.domain.model.*
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByExternalId(externalId: String): User?
    fun findByStatus(status: UserStatus): List<User>
    fun findByRole(role: UserRole): List<User>
}

interface PermissionRepository : JpaRepository<Permission, UUID> {
    fun findByUserId(userId: UUID): List<Permission>
    fun findByUserIdAndPermission(userId: UUID, permission: PermissionType): Permission?
    fun deleteByUserId(userId: UUID)
}

interface PersonalizationRepository : JpaRepository<Personalization, UUID> {
    fun findByUserId(userId: UUID): Personalization?
}

interface ModerationRuleRepository : JpaRepository<ModerationRule, UUID> {
    fun findByEnabled(enabled: Boolean): List<ModerationRule>
    fun findByUserId(userId: UUID?): List<ModerationRule>
    fun findByRuleType(ruleType: RuleType): List<ModerationRule>
}

interface AuditLogRepository : JpaRepository<AuditLog, UUID> {
    fun findByTargetUserId(userId: UUID): List<AuditLog>
    fun findByAdminId(adminId: String): List<AuditLog>
    fun findByActionType(actionType: AuditAction): List<AuditLog>
    fun findByCreatedAtBetween(from: Instant, to: Instant): List<AuditLog>
}
