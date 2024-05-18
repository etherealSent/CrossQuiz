package quizCreate.GPT.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestGPT(
    val message: String,
    val api_key: String,
)