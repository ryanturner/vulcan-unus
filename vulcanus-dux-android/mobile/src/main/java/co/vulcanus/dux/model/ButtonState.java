package co.vulcanus.dux.model;

import java.util.List;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class ButtonState {
    private boolean isDefault;
    private String stateName;
    private List<Pin> pinStates;

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
}
