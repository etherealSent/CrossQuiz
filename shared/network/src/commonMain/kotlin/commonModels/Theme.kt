package commonModels

import kotlinx.serialization.Serializable

@Serializable
class Theme(
    val id: Int,
    val name_theme: String,
    val id_subject: Int,
)