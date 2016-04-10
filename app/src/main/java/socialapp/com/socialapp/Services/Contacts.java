package socialapp.com.socialapp.Services;

import java.util.List;

import socialapp.com.socialapp.Services.entities.ContactRequest;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.infrastructure.ServiceResponse;

/**
 * Created by SAMAR on 4/10/2016.
 */
public final class Contacts {

    private Contacts() {
    }

    public static class GetContactRequestsRequest {
        public boolean FromUs;

        public GetContactRequestsRequest(boolean fromUs) {
            FromUs = fromUs;
        }
    }

    public static class GetContactRequestResponse extends ServiceResponse {
        public List<ContactRequest> Request;
    }

    public static class GetContactRequest {
        public boolean IncludePendingContacts;

        public GetContactRequest(boolean includePendingContacts) {
            IncludePendingContacts = includePendingContacts;
        }
    }

    public static class GetContactResponse extends ServiceResponse {
        public List<UserDetails> Contacts;
    }

    public static class SendContactRequestRequest {
        public int UserId;

        public SendContactRequestRequest(int userId) {
            UserId = userId;
        }
    }

    public static class SendContactRequestResponse extends ServiceResponse {

    }

    public static class RespondToContactRequestRequest {
        public int ContactRequestId;
        public boolean Accept;

        public RespondToContactRequestRequest(int contactRequestId, boolean accept) {
            ContactRequestId = contactRequestId;
            Accept = accept;
        }
    }

    public static class RepsondToContactRequestResponse extends ServiceResponse {

    }


}
