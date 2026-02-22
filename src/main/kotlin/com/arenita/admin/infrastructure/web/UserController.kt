package com.arenita.admin.infrastructure.web

import com.arenita.admin.api.dto.*
import com.arenita.admin.application.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management")
class UserController(
    private val userService: UserService,
    private val permissionService: PermissionService,
    private val personalizationService: PersonalizationService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    fun create(@Valid @RequestBody req: CreateUserRequest, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        userService.createUser(req, adminId)

    @GetMapping
    @Operation(summary = "List users")
    fun list(@RequestParam(required = false) status: String?, @RequestParam(required = false) role: String?) =
        userService.listUsers(status, role)

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    fun get(@PathVariable id: UUID) = userService.getUser(id)

    @GetMapping("/external/{externalId}")
    @Operation(summary = "Get user by external ID")
    fun getByExternal(@PathVariable externalId: String) = userService.getUserByExternalId(externalId)

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    fun update(@PathVariable id: UUID, @Valid @RequestBody req: UpdateUserRequest, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        userService.updateUser(id, req, adminId)

    @PostMapping("/{id}/suspend")
    @Operation(summary = "Suspend user")
    fun suspend(@PathVariable id: UUID, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        userService.suspendUser(id, adminId)

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate user")
    fun activate(@PathVariable id: UUID, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        userService.activateUser(id, adminId)

    @GetMapping("/{id}/permissions")
    @Operation(summary = "Get user permissions")
    fun getPermissions(@PathVariable id: UUID) = permissionService.getPermissions(id)

    @PutMapping("/{id}/permissions")
    @Operation(summary = "Set user permissions")
    fun setPermissions(@PathVariable id: UUID, @Valid @RequestBody req: PermissionsRequest, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        permissionService.setPermissions(id, req, adminId)

    @GetMapping("/{id}/personalization")
    @Operation(summary = "Get user personalization")
    fun getPersonalization(@PathVariable id: UUID) = personalizationService.getPersonalization(id)

    @PutMapping("/{id}/personalization")
    @Operation(summary = "Update user personalization")
    fun updatePersonalization(@PathVariable id: UUID, @Valid @RequestBody req: PersonalizationRequest, @RequestHeader("X-Admin-Id", defaultValue = "system") adminId: String) =
        personalizationService.updatePersonalization(id, req, adminId)

    @GetMapping("/{id}/audit")
    @Operation(summary = "Get user audit log")
    fun getAudit(@PathVariable id: UUID): List<AuditLogResponse> {
        // Injected directly for simplicity
        return listOf() // TODO: wire audit repo
    }
}
