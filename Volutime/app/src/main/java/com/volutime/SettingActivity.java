package com.volutime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingActivity extends AppCompatActivity {
    public static final String KEY_PREF_LINK_SWITCH = "Switch_L";
    public static final String KEY_PREF_NOTE_SWITCH = "Switch_Note";
    public static final String KEY_PREF_CB_SWITCH = "Switch_CB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class); // Default is Main
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle extras = getIntent().getExtras();
                if(extras !=null) {
                    String fromClass = extras.getString("FromClass");
                    switch (fromClass) {
                        case "Main":
                            intent = new Intent(this, MainActivity.class);
                            break;
                        case "Profile":
                            intent = new Intent(this, ProfileActivity.class);
                            break;
                        case "Stats":
                            intent = new Intent(this, StatsActivity.class);
                            break;

                    }
                }
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
