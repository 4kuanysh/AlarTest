package kz.kuanysh.alartest.data.repository

import android.content.SharedPreferences
import kz.kuanysh.alartest.data.network.AlarApiService
import kz.kuanysh.alartest.domain.model.Login
import kz.kuanysh.alartest.domain.repository.AuthRepository
import kz.kuanysh.alartest.utils.Result
import kz.kuanysh.alartest.utils.apiCall

private const val KEY_CODE = "key_code"

class AuthRepositoryImpl(
    private val apiService: AlarApiService,
    private val sharedPreferences: SharedPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Login> = apiCall {
        apiService.login(username, password).toDomain()
    }

    override fun saveCode(code: String) {
        sharedPreferences.edit().putString(KEY_CODE, code).apply()
    }

    override fun getCode(): String = sharedPreferences.getString(KEY_CODE, "") ?: ""

}