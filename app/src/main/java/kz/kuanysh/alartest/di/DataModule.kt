package kz.kuanysh.alartest.di

import kz.kuanysh.alartest.data.repository.DataRepositoryImpl
import kz.kuanysh.alartest.domain.repository.DataRepository
import kz.kuanysh.alartest.domain.usecase.GetDataUseCase
import kz.kuanysh.alartest.presentation.data_list.DataListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {

    factory<DataRepository> {
        DataRepositoryImpl(apiService = get())
    }

    factory {
        GetDataUseCase(repository = get())
    }

    viewModel { (code: String) ->
        DataListViewModel(code = code, getDataUseCase = get(), logoutUseCase = get())
    }
}