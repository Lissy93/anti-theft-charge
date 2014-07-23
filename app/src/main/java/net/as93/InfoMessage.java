package net.as93;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

public class InfoMessage extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_info_message);
	}


}
