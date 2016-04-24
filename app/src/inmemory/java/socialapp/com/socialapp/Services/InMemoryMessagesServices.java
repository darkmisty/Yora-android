package socialapp.com.socialapp.Services;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import socialapp.com.socialapp.Services.entities.Message;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.infrastructure.MyApplication;

/**
 * Created by SAMAR on 4/17/2016.
 */
public class InMemoryMessagesServices extends BaseInMemoryService {

    protected InMemoryMessagesServices(MyApplication application) {
        super(application);
    }


    @Subscribe
    public void deleteMessage(Messages.DeleteMessageRequest request) {
        Messages.DeleteMessageResponse response = new Messages.DeleteMessageResponse();
        response.MessageId = request.MessageId;
        postDelayed(response);
    }

    @Subscribe
    public void searchMessages(Messages.SearchMessagesRequest request) {
        Messages.SearchMessagesResponse response = new Messages.SearchMessagesResponse();
        response.Message = new ArrayList<>();

        UserDetails[] users = new UserDetails[10];

        for (int i = 0; i < users.length; i++) {
            String stringId = Integer.toString(i);
            users[i] = new UserDetails(
                    i,
                    true,
                    "Users " + stringId,
                    "user_ " + stringId,
                    "http://www.gravatar.com/avatar/" + stringId + "?d=identicon");
        }

        Random random = new Random();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, -100);

        for (int i = 0; i < 100; i++) {
            boolean isFromUs;

            if (request.IncludeReceivedMessages && request.IncludeSendMessages) {
                isFromUs = random.nextBoolean();
            } else {
                isFromUs = !request.IncludeReceivedMessages;
            }

            date.set(Calendar.MINUTE, random.nextInt(60 * 24));

            String numberString = Integer.toString(i);

            response.Message.add(new Message(
                    i,
                    (Calendar) date.clone(),
                    "Short Message " + numberString,
                    "Long Message " + numberString,
                    "",
                    users[random.nextInt(users.length)],
                    isFromUs,
                    i > 4));
        }

        postDelayed(response, 2000);

    }

    @Subscribe
    public void sendMessage(Messages.SendMessageRequest request) {
        Messages.SendMessageResponse response = new Messages.SendMessageResponse();


        if (request.getMessage().equals("error")) {
            response.setOperationError("Something bad happened");
        } else if (request.getMessage().equals("error-message")) {
            response.setPropertyError("message", "Invalid message");
        }



        postDelayed(response, 1500, 3000);
    }

    @Subscribe
    public void MardMessageAsRead(Messages.MardMessageAsReadRequest request) {
        postDelayed(new Messages.MarkMessageAsReadResponse());
    }

    @Subscribe
    public void getMessgeDetails(Messages.GetMessageDetailsRequest request) {
        Messages.GetMessageDetailsRespons respons = new Messages.GetMessageDetailsRespons();

        respons.message = new Message(
                1,
                Calendar.getInstance(),
                "Short Message",
                "Long Message",
                null,
                new UserDetails(1, true, "Display Name", "UserName", ""),
                false,
                false);

        postDelayed(respons);
    }
}
