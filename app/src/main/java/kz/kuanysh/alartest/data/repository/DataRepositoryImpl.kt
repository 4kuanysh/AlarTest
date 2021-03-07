package kz.kuanysh.alartest.data.repository

import kz.kuanysh.alartest.data.network.AlarApiService
import kz.kuanysh.alartest.domain.model.Data
import kz.kuanysh.alartest.domain.repository.DataRepository
import kz.kuanysh.alartest.utils.Result
import kz.kuanysh.alartest.utils.apiCall

class DataRepositoryImpl(
    private val apiService: AlarApiService
): DataRepository {

    override suspend fun getData(code: String, page: Int): Result<Data> = apiCall {
        apiService.getData(code, page).toDomain()
    }
}