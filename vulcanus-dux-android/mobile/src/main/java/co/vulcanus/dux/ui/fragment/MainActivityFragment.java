package co.vulcanus.dux.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.ArrayList;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.ButtonState;
import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.DuxButton;
import co.vulcanus.dux.model.PanelModel;
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
    PanelModel panelModel;
    SharedPreferences SP;
    View view;

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
        panelModel = new PanelModel();
        SP = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        initialCreateButtons();
        this.setDeviceState();
        return view;
    }

    /**
     * This method gets called when the app needs to start from scratch and create all of the buttons
     * This generally should only be called on the first run
     */
    private void initialCreateButtons() {
        for (int i = 0; i < Integer.valueOf(SP.getString(Constants.EMBEDDED_NUMBER_OF_RELAYS, Constants.EMBEDDED_NUMBER_OF_RELAYS_DEFAULT)); i++) {
            DuxButton duxButton = new DuxButton();
            Button button = new Button(getContext());
            button.setText("Button " + (i + 1));
            int buttonId = i +
                    Integer.valueOf(SP.getString(Constants.EMBEDDED_FIRST_PIN,
                            Constants.EMBEDDED_FIRST_PIN_DEFAULT));
            button.setTag(buttonId);
            duxButton.setId(buttonId);

            ButtonState offState = new ButtonState();
            offState.setStateName("Off");
            ArrayList<Pin> offStateStates = new ArrayList<Pin>();
            Pin offStateStatesPin = new Pin();
            offStateStatesPin.setIsHigh(false);
            offStateStatesPin.setNumber(buttonId);
            offStateStates.add(offStateStatesPin);
            offState.setPinStates(offStateStates);
            offState.setIsDefault(true);
            duxButton.getButtonStateList().add(offState);

            ButtonState onState = new ButtonState();
            onState.setStateName("On");
            ArrayList<Pin> onStateStates = new ArrayList<Pin>();
            Pin onStateStatesPin = new Pin();
            onStateStatesPin.setIsHigh(true);
            onStateStatesPin.setNumber(buttonId);
            onStateStates.add(onStateStatesPin);
            onState.setPinStates(onStateStates);
            duxButton.getButtonStateList().add(onState);

            offState.setPinStates(offStateStates);
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);
            panel.addView(button);
            duxButton.setView(button);
            panelModel.getButtons().add(duxButton);
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
        RestClient.destroyInstance();
        RestClient.setBaseUrl(host);
        RestClient.setUsername(username);
        RestClient.setPassword(password);
    }

    @Override
    public void onClick(View v) {
        int id = Integer.valueOf(v.getTag().toString());
        DuxButton button = panelModel.getButtonForId(id);
        if(button != null) {
            this.togglePin(v, id);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ButtonSettingsFragment nextFrag= new ButtonSettingsFragment();
        this.getFragmentManager().beginTransaction()
                .replace(this.getId(), nextFrag, null)
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
        Pin pin = this.deviceState.getPin(pinNumber);
        boolean reverseLogic = SP.getBoolean(Constants.EMBEDDED_REVERSE_LOGIC, Constants.EMBEDDED_REVERSE_LOGIC_DEFAULT);
        pin.setIsHigh(!pin.isHigh());
        if(pin.isHigh() ^ reverseLogic) { //Good 'ol xor!
            v.getBackground().setColorFilter(getResources().getColor(R.color.material_red_500), PorterDuff.Mode.MULTIPLY);
        } else {
            v.getBackground().setColorFilter(null);
        }
        this.deviceState.setPin(pin.getNumber(), pin);
        Call<String> call = RestClient.getInstance().getService().sendMailbox(this.deviceState);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constants.LOG_TAG, "Response: " + response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constants.LOG_TAG, "Error!");
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
