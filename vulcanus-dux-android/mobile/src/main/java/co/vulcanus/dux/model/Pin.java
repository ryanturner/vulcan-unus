package co.vulcanus.dux.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ryan_turner on 10/17/15.
 */
public class Pin implements Parcelable {
    private int number;
    private boolean isHigh;
    public Pin() {
        number = 0;
        isHigh = false;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isHigh() {
        return isHigh;
    }

    public void setIsHigh(boolean high) {
        this.isHigh = high;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Pin createFromParcel(Parcel in) {
            return new Pin(in);
        }

        public ButtonState[] newArray(int size) {
            return new ButtonState[size];
        }
    };

    private Pin(Parcel in) {
        number = in.readInt();
        isHigh = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeByte((byte) (isHigh ? 1 : 0));
    }
}
