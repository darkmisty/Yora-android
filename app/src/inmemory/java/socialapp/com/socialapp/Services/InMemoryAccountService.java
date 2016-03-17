package socialapp.com.socialapp.Services;


import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.infrastructure.SocialApplication;

public class InMemoryAccountService extends BaseMemoryService {



    public InMemoryAccountService(SocialApplication application) {
        super(application);
    }

    @Subscribe
    public void updateProfile(Account.UpdateProfileRequest request) {

        Account.UpdateProfileResponse response = new Account.UpdateProfileResponse();

        if (request.displayName.equalsIgnoreCase("Samar")) {
            response.setPropertyError("displayName", "You may not be named Samar");
        }

        postDelayed(response, 4000);
    }

    @Subscribe
    public void updateAvatar(Account.ChangeAvatarRequest request) {

        postDelayed(new Account.ChangeAvatarResponse());

    }


    @Subscribe
    public void changePassword(Account.ChangePasswordRequest request) {

        Account.ChangePasswordResponse response = new Account.ChangePasswordResponse();


        if (!request.newPassword.equals(request.confirmNewPassword)) {
            response.setPropertyError("Password", "Password Must Match");
        }

        if (request.newPassword.length() < 3) {
            response.setPropertyError("newPassword", "Password must be larger than 3 characters");
        }


        postDelayed(response);

    }
}
