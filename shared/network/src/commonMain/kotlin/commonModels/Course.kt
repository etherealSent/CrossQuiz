package commonModels

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: Int,
    val name_course: String,
    val id_expert: String,
    val description: String,
    val id_subject: String,
)