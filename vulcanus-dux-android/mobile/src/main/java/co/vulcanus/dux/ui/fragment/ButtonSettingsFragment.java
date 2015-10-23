package co.vulcanus.dux.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.PanelModel;

/**
 * Created by ryan_turner on 10/22/15.
 */
public class ButtonSettingsFragment extends Fragment {
    View view;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.button_settings_layout, container, false);
        //layout = (LinearLayout)view.findViewById(R.id.button_grid_layout);
        return view;
    }
}
