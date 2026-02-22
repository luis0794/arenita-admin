package com.arenita.admin.application

import com.arenita.admin.api.dto.*
import com.arenita.admin.domain.model.*
import com.arenita.admin.domain.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PermissionService(
    private val permissionRepo: PermissionRepository,
    private val auditRepo: AuditLogRepository
) {
    fun getPermissions(userId: UUID): PermissionsResponse {
        val perms = permissionRepo.findByUserId(userId)
        return PermissionsResponse(userId, perms.map { it.toEntry() })
    }

    @Transactional
    fun setPermissions(userId: UUID, req: PermissionsRequest, adminId: String): PermissionsResponse {
        permissionRepo.deleteByUserId(userId)
        val perms = req.permissions.map { entry ->
            Permission(
                userId = userId,
                permission = PermissionType.valueOf(entry.permission.uppercase()),
                granted = entry.granted,
                activeFrom = entry.activeFrom,
                activeUntil = entry.activeUntil
            )
        }
        val saved = permissionRepo.saveAll(perms)
        auditRepo.save(AuditLog(
            adminId = adminId, targetUserId = userId,
            actionType = AuditAction.PERMISSION_GRANTED,
            details = "Updated permissions: ${req.permissions.map { it.permission }}"
        ))
        return PermissionsResponse(userId, saved.map { it.toEntry() })
    }
}
