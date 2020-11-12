/*
   Author: Kim, Hyeong Suk, Spillers, Sam D, Tran, Anh Tu, Sambath Pech
   Class: TCSS 450
 */

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

import edu.uw.tcss450.team2.model.PushyTokenViewModel;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.utils.PasswordValidator;
import edu.uw.tcss450.team2.databinding.FragmentSignInBinding;

import static edu.uw.tcss450.team2.utils.PasswordValidator.*;


public class SignInFragment extends Fragment {

    private PushyTokenViewModel mPushyTokenViewModel;
    private UserInfoViewModel mUserViewModel;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.registerInfoButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToRegisterFragment()
                ));

        binding.signInInfoButton.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.emailAddress.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.password.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        binding.emailAddress.setText("test1@test.com");
        binding.password.setText("test12345");



        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                binding.signInInfoButton.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);

    }

//    private void attemptSignIn(final View button) {
//        navigateToSuccess("", "", "", "");
//        //validateEmail();
//    }

    /*
     * helper method to verify the log in
     *
     * @param: View is a button
     */
    private void attemptSignIn(final View button) {
        //navigateToSuccess("", "");
        validateEmail();
    }

    /*
     *  helper method to verify the email
     *
     * @param: none
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.emailAddress.getText().toString().trim()),
                this::validatePassword,
                result -> binding.emailAddress.setError("Please enter a valid Email address."));
    }

    /*
     *  helper method to verify the paswords whether it is met all the conditions
     *
     * @param: none
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.password.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.password.setError("Please enter a valid Password."));
    }

    /*
     *  helper method to check the valid user information
     *
     * @param: none
     */
    private void verifyAuthWithServer() {
        Log.i("EMAIL: ", binding.emailAddress.getText().toString());
        Log.i("PASSWORd: ", binding.password.getText().toString());
        mSignInModel.connect(
                binding.emailAddress.getText().toString(),
                binding.password.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().

    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
//    private void navigateToSuccess(final String email, final String jwt, String fName, String lName) {
//        Navigation.findNavController(getView())
//                .navigate(SignInFragmentDirections
//                        .actionSignInFragmentToMainActivity(email, jwt, fName, lName));
//    }

    private void navigateToSuccess(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(email, jwt));
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeSignInResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.emailAddress.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    binding.emailAddress.getText().toString(),
                                    response.getString("token")
                            )).get(UserInfoViewModel.class);
                    //navigateToSuccess(mUserViewModel.getEmail(), mUserViewModel.getJwt());
                    sendPushyToken();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJwt());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.emailAddress.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        binding.emailAddress.getText().toString(),
                        mUserViewModel.getJwt()
                );
            }
        }
    }

}