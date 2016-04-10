package socialapp.com.socialapp.Services.entities;

import java.util.Calendar;

/**
 * Created by SAMAR on 4/10/2016.
 */
public class ContactRequest {

    private int id;
    private boolean isFromUs;
    private UserDetails user;
    private Calendar createdAt;

    public ContactRequest(int id, boolean isFromUs, UserDetails user, Calendar createdAt) {
        this.id = id;
        this.isFromUs = isFromUs;
        this.user = user;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
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
