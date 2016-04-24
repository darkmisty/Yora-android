package socialapp.com.socialapp.Services.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by SAMAR on 4/17/2016.
 */
public class Message implements Parcelable {

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    private int id;
    private Calendar createdAt;
    private String shortMessage;
    private String longMessage;
    private String imageUrl;
    private UserDetails otherUser;
    private boolean isFromUs;
    private boolean isRead;
    private boolean isSelected;

    public Message(int id, Calendar createdAt, String shortMessage, String longMessage, String imageUrl, UserDetails otherUser, boolean isFromUs, boolean isRead) {
        this.id = id;
        this.createdAt = createdAt;
        this.shortMessage = shortMessage;
        this.longMessage = longMessage;
        this.imageUrl = imageUrl;
        this.otherUser = otherUser;
        this.isFromUs = isFromUs;
        this.isRead = isRead;
    }

    protected Message(Parcel in) {
        id = in.readInt();
        shortMessage = in.readString();
        longMessage = in.readString();
        imageUrl = in.readString();
        otherUser = in.readParcelable(UserDetails.class.getClassLoader());
        isFromUs = in.readByte() != 0;
        isRead = in.readByte() != 0;
        isSelected = in.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getLongMessage() {
        return longMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserDetails getOtherUser() {
        return otherUser;
    }

    public boolean isFromUs() {
        return isFromUs;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortMessage);
        dest.writeString(longMessage);
        dest.writeString(imageUrl);
        dest.writeParcelable(otherUser, flags);
        dest.writeByte((byte) (isFromUs ? 1 : 0));
        dest.writeByte((byte) (isRead ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
