package com.example.chatapp.view.signIn


import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.SignUpBinding
import com.example.chatapp.model.UserModel
import com.example.chatapp.view.CommonFragment
import com.example.chatapp.viewModel.signUpViewModel.SignUpViewModel
import com.example.chatapp.viewModel.signUpViewModel.UiEvent

class SignUp : CommonFragment<SignUpBinding>() {
    private val viewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java]
    }
    private lateinit var user: UserModel
    private var imageUri: String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->
        imageUri = it.toString()
        binding.setUserImage.setImageURI(it)
    }

    override fun getMyBinding(): SignUpBinding {
        return SignUpBinding.inflate(layoutInflater)
    }


    override fun setup() {
        with(binding) {
            setUserImageRoot.setOnClickListener {
                launcher.launch("image/*")
            }

            signUp.setOnClickListener {
                val name = etName.text.toString()
                val lastName = etLastName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val passwordRepeat = etPasswordRepeat.text.toString()
                if (name.isNotBlank() &&
                    lastName.isNotBlank() && email.isNotBlank()
                    && password.isNotBlank() &&
                    passwordRepeat.isNotBlank() && password == passwordRepeat
                ) {
                    viewModel.addUserForAuth(email, password)
                    user = UserModel(name, lastName, email, password, imageUri)
                }
            }
            signIn.setOnClickListener {
                findNavController().navigate(R.id.action_signUp_to_signIn)
            }
        }

        viewModel.liveDataAuth.observe(viewLifecycleOwner) {
            when (it) {

                is UiEvent.Success -> {
                    if (::user.isInitialized) {
                        user.token = it.token
                        // image uri is incorrect
                        viewModel.addUserForDatabase(user)
                        it.token?.let { t ->
                            viewModel.addSharePref(t)
                            viewModel.addMyName(user.name!!)
                        }
                    }
                }

                is UiEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
            }


            viewModel.liveDataStore.observe(viewLifecycleOwner) {
                when (it) {
                    is UiEvent.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }

                    is UiEvent.Success -> {
                        findNavController().setGraph(R.navigation.user_nav)
                    }
                }
            }


        }

    }
}