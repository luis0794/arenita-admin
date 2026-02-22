package com.arenita.admin.application

import com.arenita.admin.api.dto.*
import com.arenita.admin.domain.model.*
import com.arenita.admin.domain.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PersonalizationService(
    private val personalizationRepo: PersonalizationRepository,
    private val auditRepo: AuditLogRepository
) {
    fun getPersonalization(userId: UUID): PersonalizationResponse {
        val p = personalizationRepo.findByUserId(userId) ?: Personalization(userId = userId)
        return p.toResponse()
    }

    @Transactional
    fun updatePersonalization(userId: UUID, req: PersonalizationRequest, adminId: String): PersonalizationResponse {
        val existing = personalizationRepo.findByUserId(userId) ?: Personalization(userId = userId)
        val updated = existing.copy(
            tone = req.tone?.let { Tone.valueOf(it.uppercase()) } ?: existing.tone,
            language = req.language ?: existing.language,
            formalityLevel = req.formalityLevel?.let { FormalityLevel.valueOf(it.uppercase()) } ?: existing.formalityLevel,
            customGreeting = req.customGreeting ?: existing.customGreeting,
            responseFormat = req.responseFormat?.let { ResponseFormat.valueOf(it.uppercase()) } ?: existing.responseFormat,
            featureFlags = req.featureFlags ?: existing.featureFlags
        )
        val saved = personalizationRepo.save(updated)
        auditRepo.save(AuditLog(
            adminId = adminId, targetUserId = userId,
            actionType = AuditAction.PERSONALIZATION_UPDATED,
            details = "Updated personalization settings"
        ))
        return saved.toResponse()
    }
}
