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

import edu.uw.tcss450.team2.MainActivityArgs;
import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentNewPasswordBinding;
import edu.uw.tcss450.team2.databinding.FragmentSetPasswordBinding;
import edu.uw.tcss450.team2.utils.PasswordValidator;

import static edu.uw.tcss450.team2.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.team2.utils.PasswordValidator.checkPwdUpperCase;

/**
 * Fragment to set a new password for a user
 */
public class SetPasswordFragment extends Fragment {

    private FragmentSetPasswordBinding binding;
    private SetPasswordViewModel mSetPasswordViewModel;

    private String mEmail;
    private String mPassword;

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.passwordField2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetPasswordViewModel = new ViewModelProvider(getActivity())
                .get(SetPasswordViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetPasswordBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSetPasswordViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse);

        binding.cancelButton2.setOnClickListener(this::navigateBackToSignIn);
        binding.submitPasswordsButton.setOnClickListener(this::checkPasswords);

        SetPasswordFragmentArgs args = SetPasswordFragmentArgs.fromBundle(getArguments());
        mEmail = args.getEmail();
        mPassword = args.getPassword();

        Log.d("temp", "here1");
    }

    /**
     * Observes a chance in the json object
     * @param response The new json object
     */
    private void observeSignInResponse(JSONObject response) {
        Log.d("temp", "here2");
        if (response.length() > 0) {
            Log.d("temp", "here3");
            if (response.has("code")) {
                try {
                    binding.passwordField1.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Log.d("temp", "here4");
                navigateBackToSignIn(null);
                Toast.makeText(getActivity(),
                        "New password set!",
                        Toast.LENGTH_LONG).show();

                // The following hides the keyboard (bc it's annoying that it stays up)
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

    /**
     * Checks that the password is valid
     * @param view The view required to make this be an action listener
     */
    private void checkPasswords(View view) {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.passwordField1.getText().toString()),
                this::attemptToSetPassword,
                result -> binding.passwordField1.setError("Please enter a valid Password."));
    }

    /**
     * Sends a request to set the given user with the given temporary password to the given new password
     */
    private void attemptToSetPassword() {
        mSetPasswordViewModel.connect(mEmail, mPassword, binding.passwordField1.getText().toString());
    }

    /**
     * Navigates safely back to sign in page
     * @param view See above
     */
    private void navigateBackToSignIn(View view) {
        Navigation.findNavController(getView()).navigate(
                SetPasswordFragmentDirections.actionSetPasswordFragmentToSignInFragment());

        Navigation.findNavController(requireView()).popBackStack(
                R.id.setPasswordFragment, true);
    }
}