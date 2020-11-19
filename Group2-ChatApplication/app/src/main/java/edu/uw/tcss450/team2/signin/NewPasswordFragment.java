package edu.uw.tcss450.team2.signin;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentNewPasswordBinding;
import edu.uw.tcss450.team2.databinding.FragmentSetPasswordBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.utils.PasswordValidator;

import static edu.uw.tcss450.team2.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdSpecialChar;

public class NewPasswordFragment extends Fragment {

    private FragmentNewPasswordBinding binding;
    private NewPasswordViewModel mNewPasswordViewModel;
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewPasswordViewModel = new ViewModelProvider(getActivity())
                .get(NewPasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPasswordBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cancelButton.setOnClickListener(this::navigateBackToSignIn);
        binding.sendEmailButton.setOnClickListener(this::attemptToResetPassword);

        mNewPasswordViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse);

        NewPasswordFragmentArgs args = NewPasswordFragmentArgs.fromBundle(getArguments());
        binding.emailTextField.setText(args.getEmail());
    }

    private void observeSignInResponse(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.emailTextField.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateBackToSignIn(null);
                Toast.makeText(getActivity(),
                        "Email sent to: " + binding.emailTextField.getText().toString(),
                        Toast.LENGTH_LONG).show();

                // The following hides the keyboard (bc it's annoying that it stays up)
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void attemptToResetPassword(View view) {
        String emailText = binding.emailTextField.getText().toString();

        mEmailValidator.processResult(
                mEmailValidator.apply(emailText),
                this::validEmailResetPassword,
                result -> binding.emailTextField.setError("Please enter a valid Email address."));
    }

    private void validEmailResetPassword() {
        mNewPasswordViewModel.connect(binding.emailTextField.getText().toString());
    }

    private void navigateBackToSignIn(View view) {
        Navigation.findNavController(getView()).navigate(
                NewPasswordFragmentDirections
                        .actionNewPasswordFragment4ToSignInFragment());

        Navigation.findNavController(requireView()).popBackStack(
                R.id.newPasswordFragment, true);
    }
}