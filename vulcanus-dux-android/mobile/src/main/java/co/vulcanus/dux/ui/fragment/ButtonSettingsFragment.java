package co.vulcanus.dux.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.DuxButton;
import co.vulcanus.dux.model.Pin;
import co.vulcanus.dux.util.Constants;

/**
 * Created by ryan_turner on 10/22/15.
 */
public class ButtonSettingsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText button_label_edit_text;
    private LinearLayout layout;
    private ListView pinsTurnOn;
    private ListView pinsTurnOff;
    private DuxButton duxButton;
    private ArrayList<DuxButton> buttons;
    private SharedPreferences SP;

    private DeviceState deviceState;
    private Button save_button, delete_button;

    public DuxButton getDuxButton() {
        return duxButton;
    }

    public void setDuxButton(DuxButton button) {
        this.duxButton = button;
    }

    public DeviceState getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    public ArrayList<DuxButton> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<DuxButton> buttons) {
        this.buttons = buttons;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            duxButton = savedInstanceState.getParcelable("duxButton");
            deviceState = savedInstanceState.getParcelable("deviceState");
        }
        SP = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        List<Pin> pinList = Pin.createCompletePinListWithPartial(duxButton.getPins(),
                Integer.valueOf(SP.getString(Constants.EMBEDDED_NUMBER_OF_RELAYS,
                        Constants.EMBEDDED_NUMBER_OF_RELAYS_DEFAULT)),
                Integer.valueOf(SP.getString(Constants.EMBEDDED_FIRST_PIN,
                        Constants.EMBEDDED_FIRST_PIN_DEFAULT)));
        view = inflater.inflate(R.layout.button_settings_layout, container, false);
        layout = (LinearLayout) view.findViewById(R.id.button_settings_linear_layout);
        pinsTurnOn = (ListView) view.findViewById(R.id.pins_turn_on);
        pinsTurnOff = (ListView) view.findViewById(R.id.pins_turn_off);
        button_label_edit_text = (EditText) view.findViewById(R.id.button_label_edit_text);
        if(duxButton == null) {
            Log.e(Constants.LOG_TAG, "duxButton was somehow null");
        } else {
            button_label_edit_text.setText(duxButton.getLabel());
        }
        button_label_edit_text.addTextChangedListener(button_label_edit_text_watcher);
        save_button = (Button) view.findViewById(R.id.save_button);
        save_button.setOnClickListener(this);
        delete_button = (Button) view.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(this);
        List<String> pin_list = new ArrayList<String>();
        for(Pin pin : deviceState.getPins()) {
            pin_list.add(Integer.toString(pin.getNumber()));
        }

        ArrayAdapter<Pin> pinsTurnOnAdapter = new ArrayAdapter<Pin>(this.getContext(),
                android.R.layout.simple_list_item_multiple_choice, pinList);
        pinsTurnOn.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        pinsTurnOn.setAdapter(pinsTurnOnAdapter);
        for(int i = 0 ; i < pinsTurnOnAdapter.getCount() ; i++){
            Pin pin = pinsTurnOnAdapter.getItem(i);
            if(duxButton.containsPinWithNumber(pin.getNumber(), true)) {
                pinsTurnOn.setItemChecked(i, true);
            }
        }

        ArrayAdapter<Pin> pinsTurnOffAdapter = new ArrayAdapter<Pin>(this.getContext(),
                android.R.layout.simple_list_item_multiple_choice, pinList);
        pinsTurnOff.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        pinsTurnOff.setAdapter(pinsTurnOffAdapter);
        for(int i = 0 ; i < pinsTurnOffAdapter.getCount() ; i++){
            Pin pin = pinsTurnOffAdapter.getItem(i);
            if(duxButton.containsPinWithNumber(pin.getNumber(), false)) {
                pinsTurnOff.setItemChecked(i, true);
            }
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.save_button): {
                List<Pin> pins = new ArrayList<Pin>();
                SparseBooleanArray pinsTurnedOnChecked = pinsTurnOn.getCheckedItemPositions();
                for (int i = 0; i < pinsTurnOn.getAdapter().getCount(); i++) {
                    if (pinsTurnedOnChecked.get(i)) {
                        Pin newTurnedOnPin = (Pin)pinsTurnOn.getAdapter().getItem(i);
                        newTurnedOnPin.setIsHigh(true);
                        pins.add(newTurnedOnPin);
                    }
                }
                SparseBooleanArray pinsTurnedOffChecked = pinsTurnOff.getCheckedItemPositions();
                for (int i = 0; i < pinsTurnOff.getAdapter().getCount(); i++) {
                    if (pinsTurnedOffChecked.get(i)) {
                        Pin newTurnedOffPin = (Pin)pinsTurnOff.getAdapter().getItem(i);
                        newTurnedOffPin.setIsHigh(false);
                        pins.add(newTurnedOffPin);
                    }
                }
                //Log.e(Constants.LOG_TAG, pins.toString());
                duxButton.setPins(pins);

                getFragmentManager().popBackStackImmediate();
                break;
            }
            case(R.id.delete_button): {
                Gson gson = new GsonBuilder().create();
                buttons.remove(this.duxButton);
                SP.edit().putString(Constants.BUTTON_OBJECT, gson.toJson(buttons)).apply();
                getFragmentManager().popBackStackImmediate();
                break;
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("duxButton", duxButton);
        savedInstanceState.putParcelable("deviceState", deviceState);
    }

    private final TextWatcher button_label_edit_text_watcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            duxButton.setLabel(button_label_edit_text.getText().toString());
        }
    };
}
