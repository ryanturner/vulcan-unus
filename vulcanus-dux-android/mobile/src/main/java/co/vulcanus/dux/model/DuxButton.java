package co.vulcanus.dux.model;

import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class DuxButton implements Serializable {
    private int id;
    private int left;
    private int top;
    private String label;
    private transient Button view = null;
    private List<ButtonState> buttonStateList;
    private ButtonState currentState = null;

    public DuxButton() {
        this.buttonStateList = new ArrayList<ButtonState>();
    }

    public List<ButtonState> getButtonStateList() {
        return buttonStateList;
    }

    public void setButtonStateList(List<ButtonState> buttonStateList) {
        this.buttonStateList = buttonStateList;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ButtonState getCurrentState() {
        if(currentState == null && buttonStateList.size() > 0) {
            for(ButtonState buttonState : buttonStateList) {
                if(buttonState.isDefault()) {
                    currentState = buttonState;
                    break;
                }
            }
        }
        return currentState;
    }

    public void setCurrentState(ButtonState currentState) {
        this.currentState = currentState;
    }
}
