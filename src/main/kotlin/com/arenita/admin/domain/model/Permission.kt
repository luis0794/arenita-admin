package com.arenita.admin.domain.model

import jakarta.persistence.*
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "permissions")
data class Permission(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val permission: PermissionType,

    val granted: Boolean = true,

    val activeFrom: LocalTime? = null,
    val activeUntil: LocalTime? = null
)

enum class PermissionType {
    SEND_MESSAGES, RECEIVE_RESPONSES, UPLOAD_FILES,
    USE_IMAGE_GEN, USE_VOICE, ACCESS_DRIVE,
    ACCESS_EMAIL, ACCESS_CALENDAR
}
