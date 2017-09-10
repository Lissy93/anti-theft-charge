package  net.as93;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public  class ConfirmOwner extends DialogFragment {

    static ConfirmOwner newInstance() {
        ConfirmOwner co = new ConfirmOwner();
        return co;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_confirm_owner, container, false);

        Button button = (Button)v.findViewById(R.id.cmdStop);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        return v;
    }
}