package com.arenita.admin.infrastructure.web

import com.arenita.admin.api.dto.*
import com.arenita.admin.application.ModerationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/moderation/rules")
@Tag(name = "Moderation", description = "Content moderation rules")
class ModerationController(private val moderationService: ModerationService) {

    @GetMapping
    @Operation(summary = "List moderation rules")
    fun list(@RequestParam(required = false) userId: UUID?) = moderationService.getRules(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create moderation rule")
    fun create(@Valid @RequestBody req: CreateRuleRequest, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        moderationService.createRule(req, adminId)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete moderation rule")
    fun delete(@PathVariable id: UUID, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        moderationService.deleteRule(id, adminId)
}
