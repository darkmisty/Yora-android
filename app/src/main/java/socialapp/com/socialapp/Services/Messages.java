package socialapp.com.socialapp.Services;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import socialapp.com.socialapp.Services.entities.Message;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.infrastructure.ServiceResponse;

/**
 * Created by SAMAR on 4/17/2016.
 */
public final class Messages {
    private Messages() {

    }

    public static class DeleteMessageRequest {
        public int MessageId;

        public DeleteMessageRequest(int messageId) {
            MessageId = messageId;
        }
    }

    public static class DeleteMessageResponse extends ServiceResponse {
        public int MessageId;
    }

    public static class SearchMessagesRequest {
        public int FromContactId;
        public boolean IncludeSendMessages;
        public boolean IncludeReceivedMessages;

        public SearchMessagesRequest(int fromContactId, boolean includeSendMessages, boolean includeReceivedMessages) {
            FromContactId = fromContactId;
            IncludeSendMessages = includeSendMessages;
            IncludeReceivedMessages = includeReceivedMessages;
        }

        public SearchMessagesRequest(boolean includeSendMessages, boolean includeReceivedMessages) {
            FromContactId = -1;
            IncludeSendMessages = includeSendMessages;
            IncludeReceivedMessages = includeReceivedMessages;
        }
    }

    public static class SearchMessagesResponse extends ServiceResponse {
        public List<Message> Message;
    }

    public static class SendMessageRequest implements Parcelable {

        public static final Creator<SendMessageRequest> CREATOR = new Creator<SendMessageRequest>() {
            @Override
            public SendMessageRequest createFromParcel(Parcel in) {
                return new SendMessageRequest(in);
            }

            @Override
            public SendMessageRequest[] newArray(int size) {
                return new SendMessageRequest[size];
            }
        };
        private UserDetails recipient;
        private Uri imagePath;
        private String message;


        public SendMessageRequest () {

        }

        protected SendMessageRequest(Parcel in) {
            recipient = in.readParcelable(UserDetails.class.getClassLoader());
            imagePath = in.readParcelable(Uri.class.getClassLoader());
            message = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(recipient, flags);
            dest.writeParcelable(imagePath, flags);
            dest.writeString(message);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public UserDetails getRecipient() {
            return recipient;
        }

        public void setRecipient(UserDetails recipient) {
            this.recipient = recipient;
        }

        public Uri getImagePath() {
            return imagePath;
        }

        public void setImagePath(Uri imagePath) {
            this.imagePath = imagePath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class SendMessageResponse extends ServiceResponse {
        public Message message;

    }

    public static class MardMessageAsReadRequest {
        public int MessageId;

        public MardMessageAsReadRequest(int messageId) {
            MessageId = messageId;
        }
    }

    public static class MarkMessageAsReadResponse extends ServiceResponse {

    }

    public static class GetMessageDetailsRequest {
        public int id;

        public GetMessageDetailsRequest(int id) {
            this.id = id;
        }
    }


    public static class GetMessageDetailsRespons {
        public Message message;
    }


}
