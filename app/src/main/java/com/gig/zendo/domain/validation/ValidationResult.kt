package com.gig.zendo.domain.validation


sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val message: String) : ValidationResult
}

object EmailValidator {
    private val re = Regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", RegexOption.IGNORE_CASE)
    fun validate(s: String): ValidationResult =
        when {
            s.isBlank() -> ValidationResult.Invalid("Email không được trống")
            !re.matches(s) -> ValidationResult.Invalid("Email không hợp lệ")
            else -> ValidationResult.Valid
        }
}

object PasswordValidator {
    fun validate(s: String): ValidationResult =
        when {
            s.length < 8 -> ValidationResult.Invalid("Mật khẩu tối thiểu 8 ký tự")
            !s.any(Char::isUpperCase) -> ValidationResult.Invalid("Cần ít nhất 1 ký tự in hoa")
            !s.any(Char::isDigit) -> ValidationResult.Invalid("Cần ít nhất 1 chữ số")
            else -> ValidationResult.Valid
        }
}

object MoneyValidator {
    fun validate(rawDigits: String, min: Long = 0L, max: Long = 10_000_000_000L): ValidationResult {
        val amount = rawDigits.toLongOrNull() ?: 0L
        return when {
            amount < min -> ValidationResult.Invalid("Số tiền tối thiểu là $min")
            amount > max -> ValidationResult.Invalid("Số tiền tối đa là $max")
            else -> ValidationResult.Valid
        }
    }
}


data class FieldState(
    val text: String = "",
    val error: String? = null
)

fun ValidationResult.errorOrNull(): String? = when (this) {
    is ValidationResult.Valid -> null
    is ValidationResult.Invalid -> this.message
}

object TextFieldValidator {
    fun validate(s: String): ValidationResult =
        when {
            s.isBlank() -> ValidationResult.Invalid("Thông tin không được trống")
            else -> ValidationResult.Valid
        }
}


data class RoomFormUiState(
    val name: FieldState = FieldState(),
    val canSubmit: Boolean = false
)

data class HouseFormUiState(
    val name: FieldState = FieldState(),
    val address: FieldState = FieldState(),
    val canSubmit: Boolean = false
)