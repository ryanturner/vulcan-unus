package co.vulcanus.dux.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.Pin;
import co.vulcanus.dux.util.ThreadPreconditions;

/**
 * Created by ryan_turner on 11/11/15.
 */
public class PinListAdapter extends BaseAdapter {
    private List<Pin> bananaPhones = Collections.emptyList();

    private final Context context;

    public PinListAdapter(Context context) {
        this.context = context;
    }

    public void updateBananas(List<Pin> bananaPhones) {
        ThreadPreconditions.checkOnMainThread();
        this.bananaPhones = bananaPhones;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bananaPhones.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public Pin getItem(int position) {
        return bananaPhones.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
//            convertView = LayoutInflater.from(context)
//                    .inflate(R.layout.banana_phone, parent, false);
        }

//        TextView phoneView = (TextView) convertView.findViewById(R.id.phone);

        Pin bananaPhone = getItem(position);
        //phoneView.setText(Integer.toString(bananaPhone.getNumber()));

        return convertView;
    }
}
