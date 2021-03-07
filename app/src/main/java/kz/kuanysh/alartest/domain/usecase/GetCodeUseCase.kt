package kz.kuanysh.alartest.domain.usecase

import kz.kuanysh.alartest.domain.repository.AuthRepository

class GetCodeUseCase(
    private val repository: AuthRepository
) {

    operator fun invoke() = repository.getCode()
}