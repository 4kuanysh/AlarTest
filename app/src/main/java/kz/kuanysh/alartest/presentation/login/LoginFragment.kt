package kz.kuanysh.alartest.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kz.kuanysh.alartest.R
import kz.kuanysh.alartest.databinding.FragmentLoginBinding
import kz.kuanysh.alartest.utils.hideKeyboardFrom
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        with(binding) {
            username.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty() && !text.isNullOrEmpty()) {
                    usernameLayout.error = ""
                }
            }
            password.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty() && !text.isNullOrEmpty()) {
                    passwordLayout.error = ""
                }
            }
            loginButton.setOnClickListener {
                val username = username.text.toString()
                val password = password.text.toString()
                if (username.isEmpty() || username.isBlank()) {
                    usernameLayout.error = getString(R.string.empty_username_error)
                }
                if (password.isEmpty() || password.isBlank()) {
                    passwordLayout.error = getString(R.string.empty_password_error)
                }
                if (username.isNotEmpty() && username.isNotBlank() && password.isNotEmpty() || password.isNotBlank()) {
                    viewModel.dispatch(LoginAction.Login(username, password))
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginUIState.observe(viewLifecycleOwner) {
            when (it) {
                is LoginUIState.LoginSuccess -> navigateNext(it.code)

                LoginUIState.InvalidLoginData -> binding.passwordLayout.error =
                    getString(R.string.login_error)

                LoginUIState.RequestError -> showSnackbar()

                LoginUIState.UnknownServerError -> showSnackbar()
            }
        }
    }

    private fun showSnackbar() {
        Snackbar.make(binding.root, "Opps, some error was occurred :(", Snackbar.LENGTH_LONG).show()
    }

    private fun navigateNext(code: String) {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToDataListFragment(code)
        )
        hideKeyboardFrom(requireContext(), binding.root)
    }
}