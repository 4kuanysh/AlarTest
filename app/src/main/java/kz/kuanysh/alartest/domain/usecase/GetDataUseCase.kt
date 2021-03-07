package kz.kuanysh.alartest.domain.usecase

import kz.kuanysh.alartest.domain.exception.UnknownGetDataException
import kz.kuanysh.alartest.domain.model.Data
import kz.kuanysh.alartest.domain.repository.DataRepository
import kz.kuanysh.alartest.utils.Result

class GetDataUseCase(
    private val repository: DataRepository
) {

    suspend operator fun invoke(code: String, page: Int) =
        when (val result = repository.getData(code, page)) {
            is Result.Success -> when (result.data.status) {
                Data.Status.OK -> Result.Success(result.data)
                Data.Status.UNKNOWN -> Result.Error(UnknownGetDataException())
            }
            is Result.Error -> Result.Error(result.exception)
        }
}