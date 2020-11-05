package edu.uw.tcss450.team2.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.team2.utils.PasswordValidator;
import edu.uw.tcss450.team2.databinding.FragmentRegisterBinding;

import static edu.uw.tcss450.team2.utils.PasswordValidator.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {


    private FragmentRegisterBinding binding;
    private RegisterViewModel mRegisterModel;
    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editTextTextPassword.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Use a Lamda expression to add the OnClickListener
        binding.register.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.personFirstName.getText().toString().trim()),
                this::validateLast,
                result -> binding.personFirstName.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.personLastName.getText().toString().trim()),
                this::validateEmail,
                result -> binding.personLastName.setError("Please enter a last name."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editTextTextEmailAddress.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editTextTextEmailAddress.setError("Please enter a valid Email address."));
    }


    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editTextTextPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editTextTextPassword.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editTextTextPassword.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editTextTextPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editTextTextPassword.setError("Please enter a valid Password."));
    }

    public void navigateToLogin() {

        //The following object represents the action from sign in to edu.uw.tcss450.ui.register.
        RegisterFragmentDirections.ActionRegisterFragmentToSignInFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToSignInFragment();
        directions.setEmail(binding.editTextTextEmailAddress.getText().toString());
        directions.setPassword(binding.editTextTextPassword.getText().toString());

        //Use the navigate method to perform the navigation.
        Navigation.findNavController(getView()).navigate(directions);
    }

    private void verifyAuthWithServer() {
        mRegisterModel.connect(binding.personFirstName.getText().toString(),
                binding.personLastName.getText().toString(),
                binding.editTextTextEmailAddress.getText().toString(),
                binding.editTextTextPassword.getText().toString());
        //this is an Asynchronous call no statements after should rely on the result of connect
    }
    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editTextTextEmailAddress.setError("Error Authentication " +
                            response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Erro", e.getMessage());
                }

            } else {
                navigateToLogin();

            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}