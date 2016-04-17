package socialapp.com.socialapp.Services;

import java.util.List;

import socialapp.com.socialapp.Services.entities.Message;
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
        public String FromContactId;
        public boolean IncludeSendMessages;
        public boolean IncludeReceivedMessages;

        public SearchMessagesRequest(String fromContactId, boolean includeSendMessages, boolean includeReceivedMessages) {
            FromContactId = fromContactId;
            IncludeSendMessages = includeSendMessages;
            IncludeReceivedMessages = includeReceivedMessages;
        }

        public SearchMessagesRequest(boolean includeSendMessages, boolean includeReceivedMessages) {
            IncludeSendMessages = includeSendMessages;
            IncludeReceivedMessages = includeReceivedMessages;
        }
    }

    public static class SearchMessagesResponse extends ServiceResponse {
        public List<Message> Message;
    }


}
