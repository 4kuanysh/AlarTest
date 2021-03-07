package kz.kuanysh.alartest.domain.repository

import kz.kuanysh.alartest.domain.model.Data
import kz.kuanysh.alartest.utils.Result

interface DataRepository {

    suspend fun getData(code: String, page: Int): Result<Data>
}