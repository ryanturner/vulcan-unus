package co.vulcanus.dux.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.ArrayList;
import java.util.List;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.DuxButton;
import co.vulcanus.dux.model.Pin;
import co.vulcanus.dux.ui.activity.MainActivity;
import co.vulcanus.dux.util.Constants;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    DragLinearLayout panel;
    DeviceState deviceState;
    SharedPreferences SP;
    View view;
    ArrayList<DuxButton> buttons;
    private Gson gson = new GsonBuilder().create();

    public boolean isLayoutChangeable() {
        return layoutChangeable;
    }

    public void setLayoutChangeable(boolean layoutChangeable) {
        this.layoutChangeable = layoutChangeable;
    }

    boolean layoutChangeable = false;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        panel = (DragLinearLayout)view.findViewById(R.id.button_grid_layout);
        SP = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        this.setDeviceState();
        if(savedInstanceState == null) {
            if(buttons == null) {
                String savedValue = SP.getString(Constants.BUTTON_OBJECT, "");
                if (!savedValue.equals("")) {
                    buttons = gson.fromJson(savedValue, new TypeToken<ArrayList<DuxButton>>() {}.getType());
                } else {
                    initialCreateDuxButtons();
                }
            }
        } else {
            buttons = savedInstanceState.getParcelableArrayList("buttons");
            deviceState = savedInstanceState.getParcelable("deviceState");
        }
        createPanel();
        panel.setOnViewSwapListener(new DragLinearLayout.OnViewSwapListener() {
            @Override
            public void onSwap(View firstView, int firstPosition,
                               View secondView, int secondPosition) {
                DuxButton firstButton = null;
                DuxButton secondButton = null;
                for (DuxButton dButton : buttons) {
                    if (dButton.getId() == Integer.parseInt(firstView.getTag().toString())) {
                        firstButton = dButton;
                    } else if (dButton.getId() == Integer.parseInt(secondView.getTag().toString())) {
                        secondButton = dButton;
                    }
                }
                buttons.set(firstPosition, secondButton);
                buttons.set(secondPosition, firstButton);
                SP.edit().putString(Constants.BUTTON_OBJECT, gson.toJson(buttons)).apply();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("buttons", buttons);
        savedInstanceState.putParcelable("deviceState", deviceState);
    }

    public ToggleButton addButton() {
        int id = 0;
        for(DuxButton button : buttons) {
            if(button.getId() >= id) id = button.getId() + 1;
        }
        DuxButton duxButton = new DuxButton(id);
        duxButton.setLabel("Button");
        buttons.add(duxButton);
        ToggleButton button = new ToggleButton(getContext());
        button.setText(duxButton.getLabel());
        button.setTextOn(duxButton.getLabel());
        button.setTextOff(duxButton.getLabel());
        button.setTag(duxButton.getId());
        button.setOnClickListener(this);
        panel.addView(button);
        return button;
    }


    /**
     * This method gets called when the app needs to start from scratch and create all of the buttons
     * This generally should only be called on the first run
     */
    private void initialCreateDuxButtons() {
        buttons = new ArrayList<DuxButton> ();
        for (int i = 0; i < Integer.valueOf(SP.getString(Constants.EMBEDDED_NUMBER_OF_RELAYS, Constants.EMBEDDED_NUMBER_OF_RELAYS_DEFAULT)); i++) {
            int buttonId = i +
                    Integer.valueOf(SP.getString(Constants.EMBEDDED_FIRST_PIN,
                            Constants.EMBEDDED_FIRST_PIN_DEFAULT));

            DuxButton duxButton = new DuxButton(buttonId);
            duxButton.setLabel("Button " + (i + 1));
            buttons.add(duxButton);
        }
    }
    private void createPanel() {
        if(buttons == null) {
            Log.wtf(Constants.LOG_TAG, "Somehow buttons was null?");
            return;
        }
        panel.removeAllViews();
        for (DuxButton duxButton : buttons) {
            ToggleButton button = new ToggleButton(getContext());
            button.setText(duxButton.getLabel());
            button.setTextOn(duxButton.getLabel());
            button.setTextOff(duxButton.getLabel());
            button.setTag(duxButton.getId());
            button.setOnClickListener(this);
            button.setChecked(duxButton.isOn());
            if(duxButton.isOn()) {
                button.getBackground().setColorFilter(getResources().getColor(R.color.material_red_300), PorterDuff.Mode.MULTIPLY);
                button.setChecked(true);
            }
            panel.addView(button);

        }
    }
    public void setDeviceState() {
        String firstPin = SP.getString(Constants.EMBEDDED_FIRST_PIN,
                Constants.EMBEDDED_FIRST_PIN_DEFAULT);
        String numberOfRelays = SP.getString(Constants.EMBEDDED_NUMBER_OF_RELAYS,
                Constants.EMBEDDED_NUMBER_OF_RELAYS_DEFAULT);
        this.deviceState = new DeviceState(Integer.parseInt(firstPin), Integer.parseInt(firstPin) +
                Integer.parseInt(numberOfRelays));
        deviceState.setReverseLogic(SP.getBoolean(Constants.EMBEDDED_REVERSE_LOGIC, Constants.EMBEDDED_REVERSE_LOGIC_DEFAULT));
    }

    @Override
    public void onClick(View v) {
        if(isLayoutChangeable()) {
            ButtonSettingsFragment nextFrag = new ButtonSettingsFragment();
            nextFrag.setDuxButton(DuxButton.getDuxButtonWithId(buttons, (int) v.getTag()));
            nextFrag.setDeviceState(deviceState);
            nextFrag.setButtons(buttons);
            this.getFragmentManager().beginTransaction()
                    .replace(this.getId(), nextFrag)
                    .addToBackStack(null)
                    .commit();
        } else {
            if (v instanceof ToggleButton) {
                int id = Integer.valueOf(v.getTag().toString());
                this.togglePin(v, id);
            }
        }
    }

    public void editLayoutPressed() {
        if(isLayoutChangeable()) {
            for(int i = 0; i < panel.getChildCount(); i++){
                View child = panel.getChildAt(i);
                child.setOnTouchListener(null);
            }
        } else {
            for(int i = 0; i < panel.getChildCount(); i++){
                View child = panel.getChildAt(i);
                // the child will act as its own drag handle
                panel.setViewDraggable(child, child);
            }
        }
        this.setLayoutChangeable(!isLayoutChangeable());
    }

    private void togglePin(View v, int pinNumber) {
        final ToggleButton button = (ToggleButton) v;
        final DuxButton duxButton = DuxButton.getDuxButtonWithId(buttons, (Integer) button.getTag());
        List<Pin> pins = duxButton.getButtonState().getPinStates();
        this.deviceState.setPins(pins, button.isChecked());
        if(writeDeviceState()) { //Update worked, so reflect the change in our data model.
            duxButton.setIsOn(!duxButton.isOn());
        }

        ToggleButton toggleButton = (ToggleButton) v;
        if (duxButton.isOn()) {
            button.getBackground().setColorFilter(getResources().getColor(R.color.material_red_300), PorterDuff.Mode.MULTIPLY);
            toggleButton.setChecked(true);
        } else {
            button.getBackground().setColorFilter(null);
            toggleButton.setChecked(false);

        }
    }
    private boolean writeDeviceState() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity.bluetoothSerial.isConnected()) {
            String command = this.deviceState.toString();
            activity.bluetoothSerial.write(command);
            Log.i(Constants.LOG_TAG, command);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onResume() {
        createPanel();
        writeDeviceState();
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.setDeviceState();
    }
}
