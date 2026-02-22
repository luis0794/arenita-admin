package com.arenita.admin.api.dto

import com.arenita.admin.domain.model.*
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.time.LocalTime
import java.util.UUID

// User DTOs
data class CreateUserRequest(
    @field:NotBlank val externalId: String,
    @field:NotBlank val platform: String,
    @field:NotBlank val name: String,
    val displayName: String? = null,
    val role: String = "USER",
    val dailyMessageLimit: Int? = null,
    val monthlyCostLimitCents: Int? = null
)

data class UpdateUserRequest(
    val name: String? = null,
    val displayName: String? = null,
    val role: String? = null,
    val dailyMessageLimit: Int? = null,
    val monthlyCostLimitCents: Int? = null
)

data class UserResponse(
    val id: UUID, val externalId: String, val platform: String,
    val name: String, val displayName: String?, val role: String,
    val status: String, val dailyMessageLimit: Int?,
    val monthlyCostLimitCents: Int?, val createdAt: Instant
)

// Permission DTOs
data class PermissionEntry(
    val permission: String, val granted: Boolean = true,
    val activeFrom: LocalTime? = null, val activeUntil: LocalTime? = null
)

data class PermissionsRequest(val permissions: List<PermissionEntry>)

data class PermissionsResponse(val userId: UUID, val permissions: List<PermissionEntry>)

// Personalization DTOs
data class PersonalizationRequest(
    val tone: String? = null, val language: String? = null,
    val formalityLevel: String? = null, val customGreeting: String? = null,
    val responseFormat: String? = null, val featureFlags: Set<String>? = null
)

data class PersonalizationResponse(
    val userId: UUID, val tone: String, val language: String,
    val formalityLevel: String, val customGreeting: String?,
    val responseFormat: String, val featureFlags: Set<String>
)

// Moderation DTOs
data class CreateRuleRequest(
    @field:NotBlank val name: String, val description: String? = null,
    val userId: UUID? = null, @field:NotBlank val ruleType: String,
    @field:NotBlank val value: String, val action: String = "DENY"
)

data class RuleResponse(
    val id: UUID, val name: String, val description: String?,
    val userId: UUID?, val ruleType: String, val value: String,
    val action: String, val enabled: Boolean, val createdAt: Instant
)

// Audit DTOs
data class AuditLogResponse(
    val id: UUID, val adminId: String, val targetUserId: UUID?,
    val actionType: String, val details: String,
    val previousValue: String?, val newValue: String?, val createdAt: Instant
)

// Mappers
fun User.toResponse() = UserResponse(id!!, externalId, platform.name, name, displayName, role.name, status.name, dailyMessageLimit, monthlyCostLimitCents, createdAt)
fun Permission.toEntry() = PermissionEntry(permission.name, granted, activeFrom, activeUntil)
fun Personalization.toResponse() = PersonalizationResponse(userId, tone.name, language, formalityLevel.name, customGreeting, responseFormat.name, featureFlags)
fun ModerationRule.toResponse() = RuleResponse(id!!, name, description, userId, ruleType.name, value, action, enabled, createdAt)
fun AuditLog.toResponse() = AuditLogResponse(id!!, adminId, targetUserId, actionType.name, details, previousValue, newValue, createdAt)
