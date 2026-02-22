package com.arenita.admin.domain.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "audit_logs")
data class AuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val adminId: String,

    @Column(nullable = false)
    val targetUserId: UUID? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val actionType: AuditAction,

    @Column(nullable = false, length = 2000)
    val details: String,

    val previousValue: String? = null,
    val newValue: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)

enum class AuditAction {
    USER_CREATED, USER_UPDATED, USER_SUSPENDED, USER_ACTIVATED, USER_BANNED,
    PERMISSION_GRANTED, PERMISSION_REVOKED,
    PERSONALIZATION_UPDATED,
    RULE_CREATED, RULE_UPDATED, RULE_DELETED
}
