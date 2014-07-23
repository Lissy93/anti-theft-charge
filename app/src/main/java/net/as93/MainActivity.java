package net.as93;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


    Switch mainOnOff;
    Button btnStopSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        Intent i = new Intent();
        i.setAction("net.as93.ScreenReciever");
        startService(i);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mainOnOff = (Switch) findViewById(R.id.switchOnOff);
        mainOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkAppState();
				UserSettings.setEnabled(isChecked);
                if(UserSettings.getMp().isPlaying()){
					UserSettings.getMp().stop();
                }
            }
        });
        Switch swchMovement = (Switch) findViewById(R.id.switchMovement);
        swchMovement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MainActivity.this, "Movement Feature Coming Soon!", Toast.LENGTH_SHORT).show();

            }
        });


        Button btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mDialog = new Dialog(MainActivity.this);
                mDialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog .setContentView(R.layout.fragment_info_message);
                mDialog .show();
            }
        });

        Switch swchPower = (Switch) findViewById(R.id.switchPower);
        swchPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				UserSettings.setCharge(false);
            }
        });


    }





    private void checkAppState(){
        mainOnOff = (Switch) findViewById(R.id.switchOnOff);
        Switch swchMovement = (Switch) findViewById(R.id.switchMovement);
        Switch swchPower = (Switch) findViewById(R.id.switchPower);

        if(!mainOnOff.isChecked()){
            swchMovement.setEnabled(false);
            swchPower.setEnabled(false);
        }
        else{
            swchMovement.setEnabled(true);
            swchPower.setEnabled(true);
        }
    }


    public void showDialogToStop(){

//        Button btnStopSound = (Button) findViewById(R.id.btnStop);

    }

}
