package example.quiz.shared.quizlist.dialogQuizCreate

data class QuizDialogItem(
    val id: Long = 0L,
    val dialogType: DialogType
)

enum class QuizBase {
    Default
}

enum class DialogType {
    PickBase, PickName, PickThemes
}

val dialogItems = listOf<QuizDialogItem>(
    QuizDialogItem(0, DialogType.PickBase),
    QuizDialogItem(1, DialogType.PickName),
    QuizDialogItem(2, DialogType.PickThemes)
)

const val SIZE_OF_DIALOG_ITEMS = 3