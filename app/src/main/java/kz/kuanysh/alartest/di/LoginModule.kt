package kz.kuanysh.alartest.di

import android.content.Context
import kz.kuanysh.alartest.data.repository.AuthRepositoryImpl
import kz.kuanysh.alartest.domain.repository.AuthRepository
import kz.kuanysh.alartest.domain.usecase.GetCodeUseCase
import kz.kuanysh.alartest.domain.usecase.LoginUseCase
import kz.kuanysh.alartest.domain.usecase.LogoutUseCase
import kz.kuanysh.alartest.presentation.login.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val PREF_NAME = "pref_name"

val loginModule = module {

    single {
        androidApplication().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    factory<AuthRepository> {
        AuthRepositoryImpl(apiService = get(), sharedPreferences = get())
    }

    factory {
        LoginUseCase(repository = get())
    }

    factory {
        LogoutUseCase(repository = get())
    }

    factory {
        GetCodeUseCase(repository = get())
    }

    viewModel {
        LoginViewModel(loginUseCase = get(), getCodeUseCase = get())
    }

}