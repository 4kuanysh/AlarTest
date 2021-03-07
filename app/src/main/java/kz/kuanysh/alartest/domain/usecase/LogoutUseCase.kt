package kz.kuanysh.alartest.domain.usecase

import kz.kuanysh.alartest.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {

    operator fun invoke() = repository.saveCode("")
}