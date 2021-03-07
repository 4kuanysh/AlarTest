package kz.kuanysh.alartest.data.model

import com.google.gson.annotations.SerializedName
import kz.kuanysh.alartest.domain.model.Login

data class AuthResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("code")
    val code: String
) {

    fun toDomain() = Login(
        code = code,
        status = Login.Status.create(status)
    )
}