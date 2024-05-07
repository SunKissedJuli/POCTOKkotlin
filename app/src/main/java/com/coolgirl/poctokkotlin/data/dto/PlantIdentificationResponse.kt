package com.coolgirl.poctokkotlin.data.dto

data class PlantIdentificationResponse(
    val access_token: String,
    val model_version: String,
    val custom_id: String?,
    val input: Input,
    val result: Result,
    val status: String,
    val sla_compliant_client: Boolean,
    val sla_compliant_system: Boolean,
    val created: Double,
    val completed: Double
)

data class Input(
    val latitude: Double?,
    val longitude: Double?,
    val images: List<String>,
    val datetime: String
)

data class Result(
    val is_plant: IsPlantResult,
    val classification: ClassificationResult
)

data class IsPlantResult(
    val probability: Double,
    val threshold: Double,
    val binary: Boolean
)

data class ClassificationResult(
    val suggestions: List<SuggestionResult>
)

data class SuggestionResult(
    val id: String,
    val name: String,
    val probability: Double,
    val details: DetailsResult
)

data class DetailsResult(
    val common_names: List<String>?,
    val language: String,
    val entity_id: String
)