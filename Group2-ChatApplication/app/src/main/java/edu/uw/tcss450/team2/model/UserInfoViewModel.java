package edu.uw.tcss450.team2.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoViewModel extends ViewModel {

    private String mEmail;
    private String jwt;


    public UserInfoViewModel() {

    }

    public UserInfoViewModel(String mEmail, String jwt) {
        this.mEmail = mEmail;
        this.jwt = jwt;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getJwt() {
        return jwt;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {
        private final String email;
        private final String jwt;


        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }



        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == edu.uw.tcss450.team2.model.UserInfoViewModel.class) {
                return (T) new edu.uw.tcss450.team2.model.UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + edu.uw.tcss450.team2.model.UserInfoViewModel.class);
        }
    }

}
