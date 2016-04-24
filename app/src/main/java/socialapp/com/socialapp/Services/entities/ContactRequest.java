package socialapp.com.socialapp.Services.entities;

import java.util.Calendar;

/**
 * Created by SAMAR on 4/10/2016.
 */
public class ContactRequest {

    private boolean isFromUs;
    private UserDetails user;
    private Calendar createdAt;

    public ContactRequest(boolean isFromUs, UserDetails user, Calendar createdAt) {
        this.isFromUs = isFromUs;
        this.user = user;
        this.createdAt = createdAt;
    }


    public boolean isFromUs() {
        return isFromUs;
    }

    public UserDetails getUser() {
        return user;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

}
