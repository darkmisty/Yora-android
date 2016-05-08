package socialapp.com.socialapp.Services;

import com.squareup.otto.Subscribe;

import java.io.File;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import socialapp.com.socialapp.infrastructure.RetrofitCallbackPost;
import socialapp.com.socialapp.infrastructure.YoraApplication;

/**
 * Created by SAMAR on 5/3/2016.
 */
public class LiveMessagesService extends BaseLiveService {
    public LiveMessagesService(YoraWebService api, YoraApplication application) {
        super(api, application);
    }


    @Subscribe
    public void sendMessage(Messages.SendMessageRequest request) {
        api.sendMessage(
                new TypedString(request.getMessage()),
                new TypedString(Integer.toString(request.getRecipient().getId())),
                new TypedFile("image/jpeg", new File(request.getImagePath().getPath())),
                new RetrofitCallbackPost<>(Messages.SendMessageResponse.class, bus)
        );
    }

    @Subscribe
    public void searchMessages(Messages.SearchMessagesRequest request) {
        if (request.FromContactId != -1) {

            api.searchMessages(
                    request.FromContactId,
                    request.IncludeSendMessages,
                    request.IncludeReceivedMessages,
                    new RetrofitCallbackPost<>(Messages.SearchMessagesResponse.class, bus));
        } else {
            api.searchMessages(
                    request.IncludeReceivedMessages,
                    request.IncludeReceivedMessages,
                    new RetrofitCallbackPost<>(Messages.SearchMessagesResponse.class, bus)
            );
        }

    }

    @Subscribe
    public void deleteMessages(Messages.DeleteMessageRequest request) {
        api.deleteMessage(request.MessageId, new RetrofitCallbackPost<>(Messages.GetMessageDetailsRespons.class, bus));
    }

    @Subscribe
    public void markMessgeAsRead(Messages.MardMessageAsReadRequest request) {
        api.markMessageAsRead(request.MessageId, new RetrofitCallbackPost<>(Messages.MarkMessageAsReadResponse.class, bus));
    }


    @Subscribe
    public void getMessageDetails(Messages.GetMessageDetailsRequest request) {
        api.getMessageDetails(request.id, new RetrofitCallbackPost<>(Messages.GetMessageDetailsRespons.class, bus));
    }
}

