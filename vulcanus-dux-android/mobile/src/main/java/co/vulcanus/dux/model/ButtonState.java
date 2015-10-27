package co.vulcanus.dux.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class ButtonState implements Parcelable {
    private boolean isDefault;
    private String stateName;
    private List<Pin> pinStates;

    public ButtonState() {
        this.pinStates = new ArrayList<Pin>();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public List<Pin> getPinStates() {
        return pinStates;
    }

    public void setPinStates(List<Pin> pinStates) {
        this.pinStates = pinStates;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ButtonState createFromParcel(Parcel in) {
            return new ButtonState(in);
        }

        public ButtonState[] newArray(int size) {
            return new ButtonState[size];
        }
    };

    private ButtonState(Parcel in) {
        stateName = in.readString();
        pinStates = in.readArrayList(Pin.class.getClassLoader());
        isDefault = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stateName);
        dest.writeList(pinStates);
        dest.writeByte((byte) (isDefault ? 1 : 0));
    }
}
