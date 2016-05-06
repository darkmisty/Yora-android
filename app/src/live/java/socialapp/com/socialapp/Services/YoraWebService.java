package socialapp.com.socialapp.Services;

import com.google.gson.annotations.SerializedName;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import socialapp.com.socialapp.infrastructure.ServiceResponse;

/**
 * Created by SAMAR on 5/3/2016.
 */
public interface YoraWebService {

    //------------------------------------------------------------------------------------------------------
    //Accounts
    @POST("/token")
    @FormUrlEncoded
    void login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String clientId,
            @Field("grant_type") String grandType,
            Callback<LoginResponse> callback);

    @POST("/api/v1/account/external/token")
    void LoginWithExternalToken(@Body Account.LoginWithExternalTokenRequest request, Callback<Account.LoginWithExternalTokenResponse> callback);


    @POST("/api/v1/account")
    void register(@Body Account.RegisterRequest request, Callback<Account.RegisterResponse> callback);

    @POST("/api/v1/account/external")
    void registerExternal(@Body Account.RegisterWithExternalTokenRequest request, Callback<Account.RegisterWithExternalTokenResponse> callback);


    @GET("/api/v1/account")
    void getAccount(Callback<Account.LoginWithLocalTokenResponse> callback);

    @PUT("/api/v1/account")
    void updateProfile(@Body Account.UpdateProfileRequest request, Callback<Account.UpdateProfileResponse> callback);

    @Multipart
    @PUT("/apit/v1/account/avatar")
    void updateAvatar(@Part("avatar") TypedFile avatar, Callback<Account.ChangeAvatarResponse> callback);

    @PUT("/api/v1/account/password")
    void updatePassword(@Body Account.ChangePasswordRequest request, Callback<Account.ChangePasswordResponse> callback);

    @PUT("/api/v1/account/gcm-registration")
    void updateGcmRegistration(@Body Account.UpdateGcmRegistrationRequest request, Callback<Account.UpdateGcmRegistrationResponse> callback);

    //------------------------------------------------------------------------------------------------------
    //Contacts
    @GET("/api/v1/users")
    void searchUser(@Query("query") String query, Callback<Contacts.SearchUsersResponse> callback);

    @POST("/api/v1/contact-requests/{user}")
    void sendContactRequest(@Path("user") int userId, Callback<Contacts.SendContactRequestResponse> callback);

    @PUT("/api/v1/contact-requests/{user}")
    void respondToContactRequest(@Path("user") int userId, @Body RespondToContactRequest request, Callback<Contacts.RepsondToContactRequestResponse> callback);

    @DELETE("/api/v1/contacts/{user}")
    void removeContact(@Path("user") int userId, Callback<Contacts.RemoveContactResponse> callback);

    @GET("/api/v1/contact-requests/sent")
    void getContactRequestsFromUs(Callback<Contacts.GetContactRequestResponse> callback);

    @GET("/api/v1/contact-request/received")
    void getContactRequestsToUs(Callback<Contacts.GetContactRequestResponse> callback);

    @GET("api/v1/contacts")
    void getContacts(Callback<Contacts.GetContactResponse> callback);


    //------------------------------------------------------------------------------------------------------
    // Messages

    @Multipart
    @POST("/api/v1/messages")
    void sendMessage(
            @Part("message") TypedString message,
            @Part("to") TypedString to,
            @Part("photo")TypedFile photo,
            Callback<Messages.SendMessageResponse> callback
            );

    @DELETE("/api/v1/messages/{id}")
    void deleteMessage(@Path("id") int messageId, Callback<Messages.GetMessageDetailsRespons> callback);

    @PUT("/api/v1/messages{id}/is-read")
    void markMessageAsRead(@Path("id") int messageId, Callback<Messages.MarkMessageAsReadResponse> callback);

    @GET("/api/v1/messages")
    void searchMessages(
            @Query("contactid") int from,
            @Query("includeSent") boolean includeSent,
            @Query("includeReceived") boolean includeReceived,
            Callback<Messages.SearchMessagesResponse> callback
    );

    @GET("/api/v1/messages")
    void searchMessages(
            @Query("includeSent") boolean includeSent,
            @Query("includeReceived") boolean includeReceived,
            Callback<Messages.SearchMessagesResponse> callback
    );


    @GET("api/v1/messages/{id}")
    void getMessageDetails(@Path("id") int id, Callback<Messages.GetMessageDetailsRespons> callback);


    //------------------------------------------------------------------------------------------------------
    //DTOs


    public class RespondToContactRequest {
        public String Response;

        public RespondToContactRequest(String response) {
            Response = response;
        }
    }


    public class LoginResponse extends ServiceResponse {
        @SerializedName(".expires")
        public String expires;

        @SerializedName(".issued")
        public String Issued;

        @SerializedName("access_token")
        public String Token;

        @SerializedName("error")
        public String Error;

        @SerializedName("error_description")
        public String ErrorDescription;
    }
}
