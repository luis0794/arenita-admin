package com.arenita.admin.infrastructure.web

import com.arenita.admin.api.dto.*
import com.arenita.admin.domain.repository.AuditLogRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/v1/audit")
@Tag(name = "Audit", description = "Audit log")
class AuditController(private val auditRepo: AuditLogRepository) {

    @GetMapping
    @Operation(summary = "Search audit logs")
    fun search(
        @RequestParam(required = false) userId: UUID?,
        @RequestParam(required = false) adminId: String?,
        @RequestParam(required = false) from: Instant?,
        @RequestParam(required = false) to: Instant?
    ): List<AuditLogResponse> {
        val logs = when {
            userId != null -> auditRepo.findByTargetUserId(userId)
            adminId != null -> auditRepo.findByAdminId(adminId)
            from != null && to != null -> auditRepo.findByCreatedAtBetween(from, to)
            else -> auditRepo.findAll()
        }
        return logs.map { it.toResponse() }
    }
}
