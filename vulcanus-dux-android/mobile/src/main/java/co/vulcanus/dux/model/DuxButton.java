package co.vulcanus.dux.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.List;

import co.vulcanus.dux.util.Constants;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class DuxButton implements Parcelable {
    private int id;
    private ButtonState buttonStateOn;
    private ButtonState buttonStateOff;

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    private boolean isOn;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DuxButton createFromParcel(Parcel in) {
            return new DuxButton(in);
        }

        public ButtonState[] newArray(int size) {
            return new ButtonState[size];
        }
    };

    private DuxButton(Parcel in) {
        id = in.readInt();
        buttonStateOn = in.readParcelable(ButtonState.class.getClassLoader());
        buttonStateOn = in.readParcelable(ButtonState.class.getClassLoader());
        isOn = in.readByte() != 0;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String label;

    public DuxButton(int id) {
        this.id = id;
        ButtonState offState = new ButtonState();
        offState.setStateName("Off");
        ArrayList<Pin> offStateStates = new ArrayList<Pin>();
        Pin offStateStatesPin = new Pin();
        offStateStatesPin.setIsHigh(false);
        offStateStatesPin.setNumber(id);
        offStateStates.add(offStateStatesPin);
        offState.setPinStates(offStateStates);
        offState.setIsDefault(true);
        setButtonStateOff(offState);

        ButtonState onState = new ButtonState();
        onState.setStateName("On");
        ArrayList<Pin> onStateStates = new ArrayList<Pin>();
        Pin onStateStatesPin = new Pin();
        onStateStatesPin.setIsHigh(true);
        onStateStatesPin.setNumber(id);
        onStateStates.add(onStateStatesPin);
        onState.setPinStates(onStateStates);
        setButtonStateOn(onState);
    }

    public ButtonState getButtonStateOn() {
        return buttonStateOn;
    }

    public void setButtonStateOn(ButtonState buttonStateOn) {
        this.buttonStateOn = buttonStateOn;
    }

    public ButtonState getButtonState(boolean isChecked) {
        if(isChecked) {
            return getButtonStateOn();
        } else {
            return getButtonStateOff();
        }
    }

    public ButtonState getButtonStateOff() {
        return buttonStateOff;
    }

    public void setButtonStateOff(ButtonState buttonStateOff) {
        this.buttonStateOff = buttonStateOff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static DuxButton getDuxButtonWithId(List<DuxButton> buttons, int id) {
        for(DuxButton button : buttons) {
            if(button.getId() == id) {
                return button;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(buttonStateOn, flags);
        dest.writeParcelable(buttonStateOff, flags);
        dest.writeByte((byte) (isOn ? 1 : 0));
    }
}
