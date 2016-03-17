package socialapp.com.socialapp.Services;


import android.net.Uri;

import socialapp.com.socialapp.infrastructure.ServiceResponse;

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


}
