package edu.uw.tcss450.team2.signin;

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
import edu.uw.tcss450.team2.databinding.FragmentSignInBinding;

import static edu.uw.tcss450.team2.utils.PasswordValidator.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel mSignInModel;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());


    public SignInFragment() {
        // Required empty public constructor
    }

    //public static final String Extra_MESSAGE = "com.example.mylapapppech.MESSAGE";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSignInBinding.bind(getView());
        //UserInfoViewModel edu.uw.tcss450.edu.uw.tcss450.model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        //Use a Lamda expression to add the OnClickListener
        binding.signInInfoButton.setOnClickListener(this::attemptSignIn);



        //Use a method reference to add the OnClickListener
        binding.registerInfoButton.setOnClickListener(button ->
                navDirSignToRegister());

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.emailAddress.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.password.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    private void attemptSignIn(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.emailAddress.getText().toString().trim()),
                this::validatePassword,
                result -> binding.emailAddress.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.password.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.password.setError("Please enter a valid Password."));
    }
    private void verifyAuthWithServer() {

        mSignInModel.connect(binding.emailAddress.getText().toString(),
                binding.password.getText().toString());

    }

    private void navDirSignToRegister() {
        //The following object represents the action from sign in to edu.uw.tcss450.ui.register.
        //Use the navigate method to perform the navigation.
        Navigation.findNavController(getView()).navigate(SignInFragmentDirections.
                actionSignInFragmentToRegisterFragment());
    }

    public void navDirSignToSuccess(String email, String password) {
        Navigation.findNavController(getView()).navigate(SignInFragmentDirections.
                actionSignInFragmentToMainActivity(email, password));
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
                try { binding.emailAddress.setError( "Error Authenticating: " + response.getJSONObject("data").
                        getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    navDirSignToSuccess(binding.emailAddress.getText().toString(), response.getString("token") );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}