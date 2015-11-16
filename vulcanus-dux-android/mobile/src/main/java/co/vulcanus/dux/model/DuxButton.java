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
    private List<Pin> pins;
    private boolean isOn;
    private String label;

    public List<Pin> getPins() {
        return pins;
    }

    public boolean containsPinWithNumber(int number) {
        if(pins == null) {
            return false;
        }
        for(Pin pin : pins) {
            if(pin.getNumber() == number) {
                return true;
            }
        }
        return false;
    }
    public boolean containsPinWithNumber(int number, boolean isHigh) {
        if(pins == null) {
            return false;
        }
        for(Pin pin : pins) {
            if(pin.getNumber() == number && pin.isHigh() == isHigh) {
                return true;
            }
        }
        return false;
    }

    public void setPins(List<Pin> pins) {
        this.pins = pins;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }


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
        pins = in.readArrayList(Pin.class.getClassLoader());
        isOn = in.readByte() != 0;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DuxButton() {
        pins = new ArrayList<Pin>();
    }
    public DuxButton(int id) {
        this.id = id;
        pins = new ArrayList<Pin>();
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

    public ButtonState getButtonState() {
        ButtonState buttonState = new ButtonState();
        buttonState.setPinStates(pins);
        return buttonState;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeList(pins);
        dest.writeByte((byte) (isOn ? 1 : 0));
    }
}
