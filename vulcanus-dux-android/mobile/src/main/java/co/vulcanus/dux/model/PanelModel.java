package co.vulcanus.dux.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.vulcanus.dux.util.Constants;

/**
 * Created by ryan_turner on 10/21/15.
 */
public class PanelModel implements Serializable {
    private List<DuxButton> buttons = new ArrayList<>();

    public List<DuxButton> getButtons() {
        return buttons;
    }

    public DuxButton getButtonForId(int id) {
        for(DuxButton duxButton : buttons) {
            if(duxButton.getId() == id) {
                return duxButton;
            }
        }
        Log.e(Constants.LOG_TAG, "Failed to find button for ID " + id);
        return null;
    }

    public void setButtons(List<DuxButton> buttons) {
        this.buttons = buttons;
    }

    public void addButton(DuxButton button) {
        this.buttons.add(button);
    }

    public void setButtonWithId(int id, DuxButton newButton) {
        for(int i = 0; i < buttons.size(); i++) {
            DuxButton duxButton = buttons.get(i);
            if(duxButton.getId() == id) {
                buttons.set(i, newButton);
            }
        }
        Log.e(Constants.LOG_TAG, "Failed to set button for ID " + id);
    }
}
