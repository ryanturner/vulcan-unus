package co.vulcanus.dux.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import co.vulcanus.dux.R;
import co.vulcanus.dux.ui.fragment.MainActivityFragment;
import co.vulcanus.dux.util.Constants;

public class MainActivity extends AppCompatActivity {
    private MainActivityFragment mainActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainActivityFragment = new MainActivityFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.linear_layout_parent, mainActivityFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, DuxPreferenceActivity.class);
            startActivity(i);
            return true;
        } else if(id == R.id.action_edit_layout) {
            if(item.getTitle().toString().equals(getString(R.string.edit_layout))) {
                item.setTitle(R.string.set_layout);
            } else {
                item.setTitle(R.string.edit_layout);
            }
            mainActivityFragment.editLayoutPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
