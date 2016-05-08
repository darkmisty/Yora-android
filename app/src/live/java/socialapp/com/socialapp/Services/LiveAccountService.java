package socialapp.com.socialapp.Services;

import com.squareup.otto.Subscribe;

import java.io.File;

import retrofit.mime.TypedFile;
import socialapp.com.socialapp.infrastructure.Auth;
import socialapp.com.socialapp.infrastructure.RetrofitCallback;
import socialapp.com.socialapp.infrastructure.RetrofitCallbackPost;
import socialapp.com.socialapp.infrastructure.User;
import socialapp.com.socialapp.infrastructure.YoraApplication;

/**
 * Created by SAMAR on 5/3/2016.
 */
public class LiveAccountService extends BaseLiveService {

    private final Auth auth;

    public LiveAccountService(YoraWebService api, YoraApplication application) {
        super(api, application);
        auth = application.getAuth();
    }


    @Subscribe
    public void register(Account.RegisterRequest request) {
        api.register(request, new RetrofitCallback<Account.RegisterResponse>(Account.RegisterResponse.class) {
            @Override
            protected void onResponse(Account.RegisterResponse registerResponse) {
                if (registerResponse.didSucceed()) {
                    loginUser(registerResponse);
                }

                bus.post(registerResponse);
            }
        });
    }


    @Subscribe
    public void LoginWithUsername(final Account.LoginWithUsernameRequest request) {
        api.login(
                request.userName,
                request.password,
                "android",
                "password",
                new RetrofitCallback<YoraWebService.LoginResponse>(YoraWebService.LoginResponse.class) {
                    @Override
                    protected void onResponse(YoraWebService.LoginResponse loginResponse) {
                        if (!loginResponse.didSucceed()) {
                            Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                            response.setPropertyError("userName", loginResponse.ErrorDescription);
                            bus.post(response);
                            return;
                        }

                        auth.setAuthToke(loginResponse.Token);
                        api.getAccount(new RetrofitCallback<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class) {

                            @Override
                            protected void onResponse(Account.LoginWithLocalTokenResponse loginWithLocalTokenResponse) {
                                if (!loginWithLocalTokenResponse.didSucceed()) {
                                    Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                                    response.setOperationError(loginWithLocalTokenResponse.getOperationError());
                                    bus.post(response);
                                    return;
                                }

                                loginUser(loginWithLocalTokenResponse);
                                bus.post(new Account.LoginWithUsernameResponse());
                            }
                        });
                    }
                }
        );
    }


    @Subscribe
    public void loginWithLocalToken(final Account.LoginWithLocalTokenResponse response) {

        api.getAccount(new RetrofitCallbackPost<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class, bus) {
            @Override
            protected void onResponse(Account.LoginWithLocalTokenResponse loginWithLocalTokenResponse) {
                loginUser(loginWithLocalTokenResponse);
                super.onResponse(loginWithLocalTokenResponse);
            }
        });
    }

    @Subscribe
    public void updateProfile(final Account.UpdateProfileRequest request) {
        api.updateProfile(request, new RetrofitCallbackPost<Account.UpdateProfileResponse>(Account.UpdateProfileResponse.class, bus){
            @Override
            protected void onResponse(Account.UpdateProfileResponse response) {
                User user = auth.getUser();
                user.setDisplayName(response.DisplayName);
                user.setEmail(response.Email);
                super.onResponse(response);
                bus.post(new Account.UserDetailUpdatesEvent(user));
            }
        });
    }

    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request) {
        api.updateAvatar(
                new TypedFile("image/jpeg", new File(request.newAvatarUri.getPath())),
                new RetrofitCallbackPost<Account.ChangeAvatarResponse>(Account.ChangeAvatarResponse.class, bus){
                    @Override
                    protected void onResponse(Account.ChangeAvatarResponse response) {
                        User user = auth.getUser();
                        user.setAvatarUrl(response.AvatarUrl);
                        super.onResponse(response);
                        bus.post(new Account.UserDetailUpdatesEvent(user));
                    }
                }
        );
    }


    @Subscribe
    public void changePassword(Account.ChangePasswordRequest request) {
        api.updatePassword(request, new RetrofitCallbackPost<Account.ChangePasswordResponse>(Account.ChangePasswordResponse.class, bus) {
            @Override
            protected void onResponse(Account.ChangePasswordResponse response) {
                if (response.didSucceed()) {
                    auth.getUser().setHasPassword(true);
                }
                super.onResponse(response);
            }
        });
    }


    @Subscribe
    public void loginWithExternalToken(final Account.LoginWithExternalTokenRequest request) {
        api.LoginWithExternalToken(request, new RetrofitCallbackPost<Account.LoginWithExternalTokenResponse>(Account.LoginWithExternalTokenResponse.class, bus){
            @Override
            protected void onResponse(Account.LoginWithExternalTokenResponse response) {
                if (response.didSucceed()) {

                    loginUser(response);
                }
                super.onResponse(response);
            }
        });
    }


    @Subscribe
    public void registerWithExternalToken(Account.RegisterWithExternalTokenRequest request) {
        api.registerExternal(request, new RetrofitCallbackPost<Account.RegisterWithExternalTokenResponse>(Account.RegisterWithExternalTokenResponse.class, bus){
            @Override
            protected void onResponse(Account.RegisterWithExternalTokenResponse respone) {
                if (respone.didSucceed()) {

                    loginUser(respone);
                }
                super.onResponse(respone);
            }
        });
    }


    @Subscribe
    public void registerGcm(Account.UpdateGcmRegistrationRequest request){
        api.updateGcmRegistration(request, new RetrofitCallbackPost<Account.UpdateGcmRegistrationResponse>(Account.UpdateGcmRegistrationResponse.class, bus));
    }

    private void loginUser(Account.UserResponse response) {
        if (response.authToken != null && !response.authToken.isEmpty()) {
            auth.setAuthToke(response.authToken);
        }

        User user = auth.getUser();
        user.setId(response.id);
        user.setDisplayName(response.displayName);
        user.setUsername(response.userName);
        user.setEmail(response.email);
        user.setAvatarUrl(response.avatarUri);
        user.setHasPassword(response.hasPassword);
        user.setHasPassword(true);

        bus.post(new Account.UserDetailUpdatesEvent(user));
    }
}
