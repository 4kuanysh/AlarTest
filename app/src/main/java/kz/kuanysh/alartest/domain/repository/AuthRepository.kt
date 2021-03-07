package kz.kuanysh.alartest.domain.repository

import kz.kuanysh.alartest.domain.model.Login
import kz.kuanysh.alartest.utils.Result

interface AuthRepository {

    suspend fun login(username: String, password: String): Result<Login>

    fun saveCode(code: String)

    fun getCode(): String

}