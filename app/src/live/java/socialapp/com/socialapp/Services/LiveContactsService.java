package socialapp.com.socialapp.Services;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.infrastructure.RetrofitCallbackPost;
import socialapp.com.socialapp.infrastructure.YoraApplication;

/**
 * Created by SAMAR on 5/3/2016.
 */
public class LiveContactsService extends BaseLiveService {

    public LiveContactsService(YoraWebService api, YoraApplication application) {
        super(api, application);
    }


    @Subscribe
    public void searchUsers(Contacts.SearchUsersRequest request) {
        api.searchUser(request.Query, new RetrofitCallbackPost<>(Contacts.SearchUsersResponse.class, bus));
    }

    @Subscribe
    public void sendContactRequest(Contacts.SendContactRequestRequest request) {
        api.sendContactRequest(request.UserId, new RetrofitCallbackPost<>(Contacts.SendContactRequestResponse.class, bus));
    }

    @Subscribe
    public void getContactRequests(Contacts.GetContactRequestsRequest request) {
        if (request.FromUs){
            api.getContactRequestsFromUs(new RetrofitCallbackPost<>(Contacts.GetContactRequestResponse.class, bus));
        } else {
            api.getContactRequestsToUs(new RetrofitCallbackPost<>(Contacts.GetContactRequestResponse.class, bus));
        }
    }



    @Subscribe
    public void respondToContactRequest(Contacts.RespondToContactRequestRequest request) {
        String response;
        if (request.Accept){
            response = "accept";
        } else {
            response = "reject";
        }

        api.respondToContactRequest(
                request.ContactRequestId,
                new YoraWebService.RespondToContactRequest(response),
                new RetrofitCallbackPost<>(Contacts.RepsondToContactRequestResponse.class, bus)
        );
    }

    @Subscribe
    public void getContacts(Contacts.GetContactRequest request) {
        api.getContacts(new RetrofitCallbackPost<>(Contacts.GetContactResponse.class, bus));
    }


    @Subscribe
    public void removeContact(Contacts.RemoveContactRequest request) {
        api.removeContact(request.Contactid, new RetrofitCallbackPost<>(Contacts.RemoveContactResponse.class, bus));
    }
}
