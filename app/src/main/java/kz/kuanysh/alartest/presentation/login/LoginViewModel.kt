package kz.kuanysh.alartest.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.kuanysh.alartest.domain.exception.AuthorizeException
import kz.kuanysh.alartest.domain.exception.UnknownAuthException
import kz.kuanysh.alartest.domain.usecase.GetCodeUseCase
import kz.kuanysh.alartest.domain.usecase.LoginUseCase
import kz.kuanysh.alartest.utils.Result

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getCodeUseCase: GetCodeUseCase,
) : ViewModel() {

    private val _loginUIState = MutableLiveData<LoginUIState>()
    val loginUIState: LiveData<LoginUIState> get() = _loginUIState

    fun dispatch(action: LoginAction) {
        when (action) {
            is LoginAction.Login -> action.run { login(username, password) }
        }
    }

    init {
        val code = getCodeUseCase()
        if (code.isNotEmpty()) {
            _loginUIState.value = LoginUIState.LoginSuccess(code)
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUIState.value = when (val result = loginUseCase(username, password)) {
                is Result.Success -> LoginUIState.LoginSuccess(code = result.data)
                is Result.Error -> {
                    Log.d("taaag", result.exception.toString())
                    handleError(result.exception)
                }
            }
        }
    }

    private fun handleError(exception: Exception) = when (exception) {
        is AuthorizeException -> LoginUIState.InvalidLoginData
        is UnknownAuthException -> LoginUIState.UnknownServerError
        else -> LoginUIState.RequestError
    }

}

sealed class LoginAction {
    data class Login(val username: String, val password: String) : LoginAction()
}

sealed class LoginUIState {
    data class LoginSuccess(val code: String) : LoginUIState()
    object InvalidLoginData : LoginUIState()
    object UnknownServerError : LoginUIState()
    object RequestError : LoginUIState()
}
