package socialapp.com.socialapp.Services.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SAMAR on 4/10/2016.
 */
public class UserDetails implements Parcelable {

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };
    private int id;
    private boolean isContact;
    private String displayName;
    private String userName;
    private String avatarUrl;

    public UserDetails(int id, boolean isContact, String displayName, String userName, String avatarUrl) {
        this.id = id;
        this.isContact = isContact;
        this.displayName = displayName;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    protected UserDetails(Parcel in) {
        id = in.readInt();
        isContact = in.readByte() != 0;
        displayName = in.readString();
        userName = in.readString();
        avatarUrl = in.readString();
    }

    public int getId() {
        return id;
    }

    public boolean isContact() {
        return isContact;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isContact ? 1 : 0));
        dest.writeString(displayName);
        dest.writeString(userName);
        dest.writeString(avatarUrl);


    }


}
