package com.arenita.admin.application

import com.arenita.admin.api.dto.*
import com.arenita.admin.domain.model.*
import com.arenita.admin.domain.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    private val userRepo: UserRepository,
    private val auditRepo: AuditLogRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createUser(req: CreateUserRequest, adminId: String): UserResponse {
        val user = User(
            externalId = req.externalId,
            platform = Platform.valueOf(req.platform.uppercase()),
            name = req.name,
            displayName = req.displayName,
            role = UserRole.valueOf(req.role.uppercase()),
            dailyMessageLimit = req.dailyMessageLimit,
            monthlyCostLimitCents = req.monthlyCostLimitCents
        )
        val saved = userRepo.save(user)
        audit(adminId, saved.id, AuditAction.USER_CREATED, "Created user ${req.name} (${req.externalId})")
        log.info("User created: ${saved.id} ${req.name}")
        return saved.toResponse()
    }

    fun getUser(id: UUID): UserResponse = userRepo.findById(id).orElseThrow { NoSuchElementException("User not found") }.toResponse()

    fun getUserByExternalId(externalId: String): UserResponse =
        (userRepo.findByExternalId(externalId) ?: throw NoSuchElementException("User not found")).toResponse()

    fun listUsers(status: String?, role: String?): List<UserResponse> {
        val users = when {
            status != null -> userRepo.findByStatus(UserStatus.valueOf(status.uppercase()))
            role != null -> userRepo.findByRole(UserRole.valueOf(role.uppercase()))
            else -> userRepo.findAll()
        }
        return users.map { it.toResponse() }
    }

    @Transactional
    fun updateUser(id: UUID, req: UpdateUserRequest, adminId: String): UserResponse {
        val user = userRepo.findById(id).orElseThrow { NoSuchElementException("User not found") }
        val updated = user.copy(
            name = req.name ?: user.name,
            displayName = req.displayName ?: user.displayName,
            role = req.role?.let { UserRole.valueOf(it.uppercase()) } ?: user.role,
            dailyMessageLimit = req.dailyMessageLimit ?: user.dailyMessageLimit,
            monthlyCostLimitCents = req.monthlyCostLimitCents ?: user.monthlyCostLimitCents,
            updatedAt = Instant.now()
        )
        val saved = userRepo.save(updated)
        audit(adminId, id, AuditAction.USER_UPDATED, "Updated user fields")
        return saved.toResponse()
    }

    @Transactional
    fun suspendUser(id: UUID, adminId: String): UserResponse {
        val user = userRepo.findById(id).orElseThrow { NoSuchElementException("User not found") }
        val updated = userRepo.save(user.copy(status = UserStatus.SUSPENDED, updatedAt = Instant.now()))
        audit(adminId, id, AuditAction.USER_SUSPENDED, "User suspended", user.status.name, "SUSPENDED")
        return updated.toResponse()
    }

    @Transactional
    fun activateUser(id: UUID, adminId: String): UserResponse {
        val user = userRepo.findById(id).orElseThrow { NoSuchElementException("User not found") }
        val updated = userRepo.save(user.copy(status = UserStatus.ACTIVE, updatedAt = Instant.now()))
        audit(adminId, id, AuditAction.USER_ACTIVATED, "User activated", user.status.name, "ACTIVE")
        return updated.toResponse()
    }

    private fun audit(adminId: String, userId: UUID?, action: AuditAction, details: String, prev: String? = null, new: String? = null) {
        auditRepo.save(AuditLog(adminId = adminId, targetUserId = userId, actionType = action, details = details, previousValue = prev, newValue = new))
    }
}
