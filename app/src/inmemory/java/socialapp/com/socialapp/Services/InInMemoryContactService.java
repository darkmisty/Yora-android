package socialapp.com.socialapp.Services;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import socialapp.com.socialapp.Services.entities.ContactRequest;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.infrastructure.MyApplication;

/**
 * Created by SAMAR on 4/10/2016.
 */

public class InInMemoryContactService extends BaseInMemoryService {


    public InInMemoryContactService(MyApplication application) {
        super(application);
    }

    @Subscribe
    public void getContactRequest(Contacts.GetContactRequestsRequest request) {

        Contacts.GetContactRequestResponse response = new Contacts.GetContactRequestResponse();

        response.Request = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            response.Request.add(new ContactRequest(i, request.FromUs, createFakeUser(i, false), new GregorianCalendar()));
        }

        postDelayed(response);
    }

    @Subscribe
    public void getContacts(Contacts.GetContactRequest request) {
        Contacts.GetContactResponse response = new Contacts.GetContactResponse();
        response.Contacts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            response.Contacts.add(createFakeUser(i, true));
        }

        postDelayed(response, 5000);
    }


    @Subscribe
    public void sendContactsRequest(Contacts.SendContactRequestRequest request) {

        if (request.UserId == 2) {
            Contacts.SendContactRequestResponse response = new Contacts.SendContactRequestResponse();
            response.setOperationError("Something bad Happend");
            postDelayed(response);
        }
        postDelayed(new Contacts.SendContactRequestResponse());
    }


    @Subscribe
    public void respondToContactRequest(Contacts.RespondToContactRequestRequest request) {

        postDelayed(new Contacts.RepsondToContactRequestResponse());
    }

    private UserDetails createFakeUser(int id, boolean isContact) {
        String idString = Integer.toString(id);
        return new UserDetails(
                id,
                isContact,
                "Contact " + idString,
                "Contact " + idString,
                "http://www.gravatar.com/avatar/" + idString + "?d=identicon&s=64");
    }
}
