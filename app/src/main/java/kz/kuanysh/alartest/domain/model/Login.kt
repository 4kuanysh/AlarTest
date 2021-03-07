package kz.kuanysh.alartest.domain.model

data class Login(
    val status: Status,
    val code: String,
) {

    enum class Status {
        OK, ERROR, UNKNOWN;

        companion object {
            fun create(value: String) =
                when (value) {
                    "ok" -> OK
                    "error" -> ERROR
                    else -> UNKNOWN
                }
        }
    }
}