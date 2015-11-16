package co.vulcanus.dux.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

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
    public String toString() {
        return Integer.toString(this.getNumber());
    }

    // This method takes both of the pin lists and performs an
    public static List<Pin> createCompletePinListWithPartial(List<Pin> partialPinList, int numberOfPins, int firstPinNumber) {
        List<Pin> pins = new ArrayList<Pin>();
        for (int i = firstPinNumber; i < numberOfPins + firstPinNumber; i++) {
            Pin existingPin = Pin.findPinNumbered(partialPinList, i);
            if(existingPin != null) {
                pins.add(existingPin);
            } else {
                Pin pin = new Pin();
                pin.setNumber(i);
                pin.setIsHigh(false);
                pins.add(pin);
            }
        }
        return pins;
    }

    public static Pin findPinNumbered(List<Pin> pinList, int number) {
        for(Pin pin : pinList) {
            if(pin.getNumber() == number) {
                return pin;
            }
        }
        return null;
    }
}
