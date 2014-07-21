package net.as93;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsActivity extends Activity {
    public Context c= this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        Button btnChooseTime = (Button) findViewById(R.id.btnChooseTime);
        btnChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showTimeDialog(v);
            }
        });

        Button btnChooseTone = (Button) findViewById(R.id.btnChooseTone);
        btnChooseTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToneDialog(v);
            }
        });

    }

    public void showTimeDialog(View v)
    {
        final CharSequence[] items={"None","2 Seconds","5 Seconds", "10 Seconds"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select A Time Delay");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {}

        });

        UserSettings us = new UserSettings();
        int seconds = us.getTimeDelay();
        int pos = 0;
        if(seconds==0){ pos = 0; } else if(seconds==2){pos = 1;} else if(seconds==5){pos = 2;} else if(seconds==10){pos = 3;}
        builder.setSingleChoiceItems(items, pos, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                LinearLayout ll = (LinearLayout) findViewById(R.id.linear);

                UserSettings us = new UserSettings();

                if ("None".equals(items[which])) {
                    us.setTimeDelay(0);
                    Toast.makeText(getApplicationContext(), "No Time Delay Set",  Toast.LENGTH_SHORT).show();
                } else if ("2 Seconds".equals(items[which])) {
                    us.setTimeDelay(2);
                    Toast.makeText(getApplicationContext(), "2 Second Time Delay Set",  Toast.LENGTH_SHORT).show();
                } else if ("5 Seconds".equals(items[which])) {
                    us.setTimeDelay(5);
                    Toast.makeText(getApplicationContext(), "5 Second Time Delay Set",  Toast.LENGTH_SHORT).show();
                } else if ("10 Seconds".equals(items[which])) {
                    us.setTimeDelay(10);
                    Toast.makeText(getApplicationContext(), "10 Second Time Delay Set",  Toast.LENGTH_SHORT).show();
                }

            }
        });
            builder.show();

    }


    public void showToneDialog(View v)
    {
        final CharSequence[] items={"Alarm 1","Alarm 2","Alarm 3", "Alarm 4"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select an Alarm Tone");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {}

        });

        UserSettings us = new UserSettings();
        String tone = us.getAlarmTone();
        int pos = 0;
        if(tone=="alarm1"){ pos = 0; } else if(tone=="alarm2"){pos = 1;} else if(tone=="alarm3"){pos = 2;} else if(tone=="alarm4"){pos = 3;}
        builder.setSingleChoiceItems(items, pos, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserSettings us = new UserSettings();
                LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
                if ("Alarm 1".equals(items[which])) {
                    us.setAlarmTone("alarm1");
                    Toast.makeText(getApplicationContext(), "Alarm 1",  Toast.LENGTH_SHORT).show();
                } else if ("Alarm 2".equals(items[which])) {
                    us.setAlarmTone("alarm2");
                    Toast.makeText(getApplicationContext(), "Alarm 2",  Toast.LENGTH_SHORT).show();
                } else if ("Alarm 3".equals(items[which])) {
                    us.setAlarmTone("alarm3");
                    Toast.makeText(getApplicationContext(), "Alarm 3",  Toast.LENGTH_SHORT).show();
                } else if ("Alarm 4".equals(items[which])) {
                    us.setAlarmTone("alarm4");
                    Toast.makeText(getApplicationContext(), "Alarm 4",  Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();

    }


}
