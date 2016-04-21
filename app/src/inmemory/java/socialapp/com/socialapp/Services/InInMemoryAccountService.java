package socialapp.com.socialapp.Services;


import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.infrastructure.Auth;
import socialapp.com.socialapp.infrastructure.MyApplication;
import socialapp.com.socialapp.infrastructure.User;

public class InInMemoryAccountService extends BaseInMemoryService {


    public InInMemoryAccountService(MyApplication application) {
        super(application);
    }

    @Subscribe
    public void updateProfile(final Account.UpdateProfileRequest request) {

        final Account.UpdateProfileResponse response = new Account.UpdateProfileResponse();

        if (request.displayName.equalsIgnoreCase("Samar")) {
            response.setPropertyError("displayName", "You may not be named Samar");
        }

        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                User user = application.getAuth().getUser();
                user.setDisplayName(request.displayName);
                user.setEmail(request.email);

                bus.post(response);
                bus.post(new Account.UserDetailUpdatesEvent(user));
            }
        }, 2000, 3000);

    }

    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request) {

        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                User user = application.getAuth().getUser();
                user.setAvatarUrl(request.newAvatarUri.toString());

                bus.post(new Account.ChangeAvatarResponse());
                bus.post(new Account.UserDetailUpdatesEvent(user));
            }
        }, 4000, 5000);

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

    @Subscribe
    public void loginWithUserName(final Account.LoginWithUsernameRequest request) {

        invokeDelayed(new Runnable() {
            @Override
            public void run() {

                Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();

                if (request.userName.equals("username-test"))
                    response.setPropertyError("username", "invalid username or password");

                loginUser(response);
                bus.post(response);


            }
        }, 3000, 4000);
    }

    @Subscribe
    public void loginWithExternalToken(Account.LoginWithExternalTokenRequest request) {

        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.LoginWithExternalTokenResponse response = new Account.LoginWithExternalTokenResponse();
                loginUser(response);
                bus.post(response);
            }
        }, 1000, 2000);
    }


    @Subscribe
    public void register(Account.RegisterRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {

                Account.RegisterResponse response = new Account.RegisterResponse();
                loginUser(response);
                bus.post(response);

            }
        }, 1000, 2000);
    }

    @Subscribe
    public void externalRegister(Account.RegisterWithExternalTokenRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.RegisterWithExternalTokenResponse response = new Account.RegisterWithExternalTokenResponse();
                loginUser(response);
                bus.post(response);
            }
        }, 1000, 2000);
    }


    @Subscribe
    public void loginWithLocalToken(Account.LoginWithLocalTokenRequest request) {

        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.LoginWithLocalTokenResponse response = new Account.LoginWithLocalTokenResponse();
                loginUser(response);
                bus.post(response);
            }
        }, 2000, 3000);

    }


    private void loginUser(Account.UserResponse response) {
        Auth auth = application.getAuth();
        User user = auth.getUser();

        user.setDisplayName("Samar Ali");
        user.setUsername("sammieb1");
        user.setEmail("samarali@live.com");
        user.setAvatarUrl("http://www.gravatar.com/avatar/1?id=identicon");
        user.setLoggedIn(true);
        user.setId(123);

        bus.post(new Account.UserDetailUpdatesEvent(user));

        auth.setAuthToke("fakeAuthToken");

        response.displayName = user.getDisplayName();
        response.userName = user.getUsername();
        response.email = user.getEmail();
        response.id = user.getId();
        response.avatarUri = user.getAvatarUrl();
        response.authToken = auth.getAuthToken();
    }
}
