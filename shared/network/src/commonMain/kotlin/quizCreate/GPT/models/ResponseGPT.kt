package quizCreate.GPT.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseGPT(
    val is_success: Boolean,
    val response: String,
    val used_words_count: Int,
    val used_tokens_count: Int,
)