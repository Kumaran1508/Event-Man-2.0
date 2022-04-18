package com.kofze.eventman.datamodels;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.List;

public class Event implements Parcelable {
    @PropertyName("title")
    private String title;
    @PropertyName("eventCategory")
    private EventCategory eventCategory;
    @PropertyName("isPublic")
    private boolean isPublic;
    @PropertyName("owner")
    private String owner;
    @PropertyName("owner_profile_ur")
    private String ownerProfileUrl;
    @PropertyName("description")
    private String description;
    @PropertyName("start_time")
    private Timestamp start_time;
    @PropertyName("end_time")
    private Timestamp end_time;

    @PropertyName("image_url")
    private String image_url;

    private String Id;

    @PropertyName("eventMode")
    private EventMode eventMode;

    @PropertyName("location")
    private GeoPoint location;

    private List<User> attendees;

    protected Event(Parcel in) {
        title = in.readString();
        isPublic = in.readByte() != 0;
        owner = in.readString();
        ownerProfileUrl = in.readString();
        description = in.readString();
        start_time = in.readParcelable(Timestamp.class.getClassLoader());
        end_time = in.readParcelable(Timestamp.class.getClassLoader());
        image_url = in.readString();
        Id = in.readString();
        eventMode = EventMode.valueOf(in.readString());
        eventCategory = EventCategory.valueOf(in.readString());
        location = new GeoPoint(in.readDouble(), in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (isPublic ? 1 : 0));
        dest.writeString(owner);
        dest.writeString(ownerProfileUrl);
        dest.writeString(description);
        dest.writeParcelable(start_time, flags);
        dest.writeParcelable(end_time, flags);
        dest.writeString(image_url);
        dest.writeString(Id);
        dest.writeString(eventMode.toString());
        dest.writeString(eventCategory.toString());
        dest.writeDouble(location.getLatitude());
        dest.writeDouble(location.getLongitude());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }



    public Event(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }

    public EventMode getEventMode() {
        return eventMode;
    }

    public void setEventMode(EventMode eventMode) {
        this.eventMode = eventMode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOwnerProfileUrl() {
        return ownerProfileUrl;
    }

    public void setOwnerProfileUrl(String ownerProfileUrl) {
        this.ownerProfileUrl = ownerProfileUrl;
    }


}
