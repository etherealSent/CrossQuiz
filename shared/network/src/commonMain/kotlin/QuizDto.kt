import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name_course")
    val title: String,
    @SerialName("id_subject_id")
    val subjectId: Int,
    @SerialName("id_expert")
    val expertId: Int,
    @SerialName("description")
    val description: String?
)