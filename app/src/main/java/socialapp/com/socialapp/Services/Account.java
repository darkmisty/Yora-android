package socialapp.com.socialapp.Services;


import android.net.Uri;

import socialapp.com.socialapp.infrastructure.ServiceResponse;
import socialapp.com.socialapp.infrastructure.User;


public class Account {

    private Account() {

    }


    public static class UserResponse extends ServiceResponse {

        public int id;
        public String avatarUri;
        public String displayName;
        public String userName;
        public String email;
        public String authToken;
        public boolean hasPassword;
    }


    public static class LoginWithUsernameRequest {
        public String userName;
        public String password;

        public LoginWithUsernameRequest(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }
    }

    public static class LoginWithUsernameResponse extends UserResponse {

    }


    public static class LoginWithLocalTokenRequest {
        public String AuthToken;

        public LoginWithLocalTokenRequest(String authToken) {
            AuthToken = authToken;
        }
    }


    public static class LoginWithLocalTokenResponse extends UserResponse {
    }

    public static class LoginWithExternalTokenRequest {
        public String Provider;
        public String Token;
        public String CliendId;

        public LoginWithExternalTokenRequest(String provider, String token) {
            Provider = provider;
            Token = token;
            CliendId = "android";
        }
    }

    public static class LoginWithExternalTokenResponse extends UserResponse {

    }


    public static class RegisterRequest {
        public String Username;
        public String Email;
        public String Password;
        public String CliendId;

        public RegisterRequest(String username, String email, String password) {
            Username = username;
            Email = email;
            Password = password;
            CliendId = "android";
        }
    }

    public static class RegisterResponse extends UserResponse {

    }

    public static class RegisterWithExternalTokenRequest {
        public String Username;
        public String Email;
        public String Provider;
        public String Token;
        public String ClientId;

        public RegisterWithExternalTokenRequest(String username, String email, String provider, String token) {
            Username = username;
            Email = email;
            Provider = provider;
            Token = token;
            ClientId = "android";
        }
    }

    public static class RegisterWithExternalTokenResponse extends UserResponse {

    }




    public static class ChangeAvatarRequest {
        public Uri newAvatarUri;


        public ChangeAvatarRequest(Uri newAvatarUri) {
            this.newAvatarUri = newAvatarUri;
        }
    }

    public static class ChangeAvatarResponse extends ServiceResponse {
    }


    public static class UpdateProfileRequest {

        public String displayName;
        public String email;

        public UpdateProfileRequest(String displayName, String email) {
            this.displayName = displayName;
            this.email = email;
        }
    }

    public static class UpdateProfileResponse extends ServiceResponse {

    }


    public static class ChangePasswordRequest {
        public String newPassword;
        public String confirmNewPassword;
        public String currentPassword;

        public ChangePasswordRequest(String newPassword, String confirmNewPassword, String currentPassword) {
            this.newPassword = newPassword;
            this.confirmNewPassword = confirmNewPassword;
            this.currentPassword = currentPassword;
        }
    }


    public static class ChangePasswordResponse extends ServiceResponse {
    }


    public static class UserDetailUpdatesEvent {
        public User user;

        public UserDetailUpdatesEvent(User user) {
            this.user = user;
        }
    }


}
