package commonModels

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Int,
    val name_subject: String,
)