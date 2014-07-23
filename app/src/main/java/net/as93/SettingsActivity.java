package net.as93;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SettingsActivity extends Activity {
	public Context c = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
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

	public void showTimeDialog(View v) {
		final CharSequence[] items = { "None", "2 Seconds", "5 Seconds", "10 Seconds" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select A Time Delay");
		builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		int seconds = UserSettings.getTimeDelay();
		int pos = 0;
		if (seconds == 0) {
			pos = 0;
		} else if (seconds == 2) {
			pos = 1;
		} else if (seconds == 5) {
			pos = 2;
		} else if (seconds == 10) {
			pos = 3;
		}
		builder.setSingleChoiceItems(items, pos, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if ("None".equals(items[which])) {
					UserSettings.setTimeDelay(0);
				} else if ("2 Seconds".equals(items[which])) {
					UserSettings.setTimeDelay(2);
				} else if ("5 Seconds".equals(items[which])) {
					UserSettings.setTimeDelay(5);
				} else if ("10 Seconds".equals(items[which])) {
					UserSettings.setTimeDelay(10);
				}
			}
		});
		builder.show();

	}


	public void showToneDialog(View v) {
		final CharSequence[] items = { "Alarm 1", "Alarm 2", "Alarm 3", "Alarm 4" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select an Alarm Tone");
		builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}

		});

		String tone = UserSettings.getAlarmTone();
		int pos = 0;
		if (tone.equals("a.1")) {
			pos = 0;
		} else if (tone.equals("alarm2")) {
			pos = 1;
		} else if (tone.equals("alarm3")) {
			pos = 2;
		} else if (tone.equals("alarm4")) {
			pos = 3;
		}
		builder.setSingleChoiceItems(items, pos, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if ("Alarm 1".equals(items[which])) {
					UserSettings.setAlarmTone("alarm1");
				} else if ("Alarm 2".equals(items[which])) {
					UserSettings.setAlarmTone("alarm2");
				} else if ("Alarm 3".equals(items[which])) {
					UserSettings.setAlarmTone("alarm3");
				} else if ("Alarm 4".equals(items[which])) {
					UserSettings.setAlarmTone("alarm4");
				}
			}
		});
		builder.show();

	}


}
