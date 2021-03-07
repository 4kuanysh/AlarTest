package kz.kuanysh.alartest.domain.usecase

import kz.kuanysh.alartest.domain.exception.AuthorizeException
import kz.kuanysh.alartest.domain.exception.UnknownAuthException
import kz.kuanysh.alartest.domain.model.Login
import kz.kuanysh.alartest.domain.repository.AuthRepository
import kz.kuanysh.alartest.utils.Result

class LoginUseCase(
    private val repository: AuthRepository,
) {

    suspend operator fun invoke(username: String, password: String): Result<String> =
        when (val result = repository.login(username, password)) {
            is Result.Success -> when (result.data.status) {
                Login.Status.OK -> {
                    repository.saveCode(result.data.code)
                    Result.Success(result.data.code)
                }

                Login.Status.ERROR -> Result.Error(AuthorizeException())

                else -> Result.Error(UnknownAuthException())
            }

            is Result.Error -> Result.Error(result.exception)
        }

}