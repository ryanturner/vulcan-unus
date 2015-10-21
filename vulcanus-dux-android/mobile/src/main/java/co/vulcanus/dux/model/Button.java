package co.vulcanus.dux.model;

import java.util.List;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class Button {
    private int id;
    private int sortOrder;
    private String label;
    private List<ButtonState> buttonStateList;
    private ButtonState currentState;

    public List<ButtonState> getButtonStateList() {
        return buttonStateList;
    }

    public void setButtonStateList(List<ButtonState> buttonStateList) {
        this.buttonStateList = buttonStateList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ButtonState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ButtonState currentState) {
        this.currentState = currentState;
    }
}
