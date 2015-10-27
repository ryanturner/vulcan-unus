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

import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.ArrayList;
import java.util.List;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.DuxButton;
import co.vulcanus.dux.model.Pin;
import co.vulcanus.dux.service.RestClient;
import co.vulcanus.dux.util.Constants;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener, View.OnLongClickListener {
    DragLinearLayout panel;
    DeviceState deviceState;
    SharedPreferences SP;
    View view;
    ArrayList<DuxButton> buttons;

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
                initialCreateDuxButtons();
            }
        } else {
            buttons = savedInstanceState.getParcelableArrayList("buttons");
            deviceState = savedInstanceState.getParcelable("deviceState");
        }
        createButtons();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("buttons", buttons);
        savedInstanceState.putParcelable("deviceState", deviceState);
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
    private void createButtons() {
        for (DuxButton duxButton : buttons) {
            ToggleButton button = new ToggleButton(getContext());
            button.setText(duxButton.getLabel());
            button.setTextOn(duxButton.getLabel());
            button.setTextOff(duxButton.getLabel());
            button.setTag(duxButton.getId());
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);
            if(duxButton.isOn()) {
                button.getBackground().setColorFilter(getResources().getColor(R.color.material_red_300), PorterDuff.Mode.MULTIPLY);
                button.setChecked(true);
            }
            panel.addView(button);
        }
    }
    public void setDeviceState() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String firstPin = SP.getString(Constants.EMBEDDED_FIRST_PIN,
                Constants.EMBEDDED_FIRST_PIN_DEFAULT);
        String numberOfRelays = SP.getString(Constants.EMBEDDED_NUMBER_OF_RELAYS,
                Constants.EMBEDDED_NUMBER_OF_RELAYS_DEFAULT);
        String host = SP.getString(Constants.EMBEDDED_HOST, Constants.EMBEDDED_HOST_DEFAULT);
        String username = SP.getString(Constants.EMBEDDED_USER, Constants.EMBEDDED_USER_DEFAULT);
        String password = SP.getString(Constants.EMBEDDED_PASSWORD, Constants.EMBEDDED_PASSWORD_DEFAULT);
        this.deviceState = new DeviceState(Integer.parseInt(firstPin), Integer.parseInt(firstPin) +
                Integer.parseInt(numberOfRelays));
        deviceState.setReverseLogic(SP.getBoolean(Constants.EMBEDDED_REVERSE_LOGIC, Constants.EMBEDDED_REVERSE_LOGIC_DEFAULT));
        RestClient.destroyInstance();
        RestClient.setBaseUrl(host);
        RestClient.setUsername(username);
        RestClient.setPassword(password);
    }

    @Override
    public void onClick(View v) {
        int id = Integer.valueOf(v.getTag().toString());
        this.togglePin(v, id);
    }

    @Override
    public boolean onLongClick(View v) {
        ButtonSettingsFragment nextFrag= new ButtonSettingsFragment();
        nextFrag.setDuxButton(DuxButton.getDuxButtonWithId(buttons, (int) v.getTag()));
        this.getFragmentManager().beginTransaction()
                .replace(this.getId(), nextFrag)
                .addToBackStack(null)
                .commit();
        return true;
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
        List<Pin> pins = duxButton.getButtonState(button.isChecked()).getPinStates();
        this.deviceState.setPins(pins);

        final ColorFilter oldColorFilter = v.getBackground().getColorFilter();
        v.getBackground().setColorFilter(getResources().getColor(R.color.material_red_100), PorterDuff.Mode.MULTIPLY);
        Call<String> call = RestClient.getInstance().getService().sendMailbox(this.deviceState);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                duxButton.setIsOn(!duxButton.isOn());
                if (duxButton.isOn()) {
                    button.getBackground().setColorFilter(getResources().getColor(R.color.material_red_300), PorterDuff.Mode.MULTIPLY);
                } else {
                    button.getBackground().setColorFilter(null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constants.LOG_TAG, "Error!");
                button.getBackground().setColorFilter(oldColorFilter);
                button.setChecked(!button.isChecked());
            }
        });
    }
    @Override
    public void onResume() {
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
