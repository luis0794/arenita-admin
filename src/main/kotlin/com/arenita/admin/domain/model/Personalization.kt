package com.arenita.admin.domain.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "personalizations")
data class Personalization(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val userId: UUID,

    @Enumerated(EnumType.STRING)
    val tone: Tone = Tone.CASUAL,

    val language: String = "es",

    @Enumerated(EnumType.STRING)
    val formalityLevel: FormalityLevel = FormalityLevel.INFORMAL,

    val customGreeting: String? = null,

    @Enumerated(EnumType.STRING)
    val responseFormat: ResponseFormat = ResponseFormat.BALANCED,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_feature_flags", joinColumns = [JoinColumn(name = "personalization_id")])
    val featureFlags: Set<String> = emptySet()
)

enum class Tone { FORMAL, CASUAL, SARCASTIC, WARM, PROFESSIONAL }
enum class FormalityLevel { FORMAL, INFORMAL, MIXED }
enum class ResponseFormat { CONCISE, BALANCED, DETAILED }
