package com.arenita.admin.domain.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val externalId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val platform: Platform,

    @Column(nullable = false)
    val name: String,

    val displayName: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: UserStatus = UserStatus.ACTIVE,

    val dailyMessageLimit: Int? = null,
    val monthlyCostLimitCents: Int? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    val updatedAt: Instant = Instant.now()
)

enum class Platform { TELEGRAM, WHATSAPP, DISCORD, SLACK, WEB }
enum class UserRole { ADMIN, USER, RESTRICTED }
enum class UserStatus { ACTIVE, SUSPENDED, BANNED }
