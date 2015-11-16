package co.vulcanus.dux.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import co.vulcanus.dux.model.serializer.DeviceStateSerializer;
import co.vulcanus.dux.util.Constants;

/**
 * Created by ryan_turner on 10/19/15.
 */
public class DeviceState implements Parcelable {

    private List<Pin> pins;
    private int firstPin;
    private int lastPin;
    private boolean reverseLogic = false;

    public boolean isReverseLogic() {
        return reverseLogic;
    }

    public void setReverseLogic(boolean reverseLogic) {
        this.reverseLogic = reverseLogic;
    }

    public void setPin(int pin, Pin pinValue) {
        pins.set(pin, pinValue);
    }

    public Pin getPin(int pinNumber) {

        for(Pin pin : pins) {
            if(pin.getNumber() == pinNumber) return pin;
        }
        Log.e(Constants.LOG_TAG, "Failed to git pin " + pinNumber);
        return null;
    }

    public List<Pin> createPins(int firstPin, int lastPin) {
        List<Pin> pins = new ArrayList<>();
        for(int i = firstPin; i < lastPin; i++) {
            Pin pin = new Pin();
            pin.setNumber(i);
            pins.add(pin);
        }
        return pins;
    }

    public DeviceState(int firstPin, int lastPin) {
        this.firstPin = firstPin;
        this.lastPin = lastPin;
        this.pins = this.createPins(firstPin, lastPin);
    }
    public int getFirstPin() {
        return this.firstPin;
    }
    public int getLastPin() {
        return this.lastPin;
    }
    public List<Pin> getPins() {
        return this.pins;
    }
    public String toString() {
        com.google.gson.Gson gson = new GsonBuilder().registerTypeAdapter(DeviceState.class, new
                DeviceStateSerializer()).create();
        return gson.toJson(this);
    }
    public void setPins(List<Pin> pins, boolean isOn) {
        for(Pin pin : pins) {
            for(int i = 0; i < this.pins.size(); i++) {
                if(pin.getNumber() == this.pins.get(i).getNumber()) {
                    if(isOn)
                        this.pins.set(i, pin);
                    else {
                        Pin pinCopy = new Pin();
                        pinCopy.setIsHigh(!pin.isHigh());
                        pinCopy.setNumber(pin.getNumber());
                        this.pins.set(i, pinCopy);
                    }
                    break;
                }
            }
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DeviceState createFromParcel(Parcel in) {
            return new DeviceState(in);
        }

        public ButtonState[] newArray(int size) {
            return new ButtonState[size];
        }
    };

    private DeviceState(Parcel in) {
        pins = in.readArrayList(Pin.class.getClassLoader());
        firstPin = in.readInt();
        lastPin = in.readInt();
        reverseLogic = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(pins);
        dest.writeInt(firstPin);
        dest.writeInt(lastPin);
        dest.writeByte((byte) (reverseLogic ? 1 : 0));
    }

    public static List<Pin> getEmptyListOfPins(int numberOfRelays, int firstPinNumber) {
        List<Pin> pins = new ArrayList<Pin>();
        for (int i = firstPinNumber; i < numberOfRelays + firstPinNumber; i++) {
            Pin pin = new Pin();
            pin.setNumber(i);
            pin.setIsHigh(false);
            pins.add(pin);
        }
        return pins;
    }
}
