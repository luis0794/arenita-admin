package com.arenita.admin.domain.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "moderation_rules")
data class ModerationRule(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    val description: String? = null,

    val userId: UUID? = null, // null = global rule

    @Enumerated(EnumType.STRING)
    val ruleType: RuleType,

    @Column(nullable = false)
    val value: String, // e.g., purpose name, content keyword

    val action: String = "DENY", // DENY, WARN, LOG

    val enabled: Boolean = true,

    val createdAt: Instant = Instant.now()
)

enum class RuleType { PURPOSE_BLOCK, USAGE_LIMIT, CONTENT_POLICY, TIME_RESTRICTION }
