package co.vulcanus.dux.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.w3c.dom.Text;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.DuxButton;
import co.vulcanus.dux.model.PanelModel;

/**
 * Created by ryan_turner on 10/22/15.
 */
public class ButtonSettingsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText button_label_edit_text;
    private LinearLayout layout;
    private DuxButton duxButton;
    private Button save_button;

    public DuxButton getDuxButton() {
        return duxButton;
    }

    public void setDuxButton(DuxButton button) {
        this.duxButton = button;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.button_settings_layout, container, false);
        layout = (LinearLayout) view.findViewById(R.id.button_settings_linear_layout);
        button_label_edit_text = (EditText) view.findViewById(R.id.button_label_edit_text);
        button_label_edit_text.setText(duxButton.getLabel());
        button_label_edit_text.addTextChangedListener(button_label_edit_text_watcher);
        save_button = (Button) view.findViewById(R.id.save_button);
        save_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.save_button): {
                getFragmentManager().popBackStackImmediate();
                break;
            }
        }
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
