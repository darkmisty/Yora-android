package socialapp.com.socialapp.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import socialapp.com.socialapp.R;


public class LoginFragment extends BaseFragment implements View.OnClickListener {


    private Button btnLogin;
    private CallBacks callBacks;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        View v =  inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = (Button) v.findViewById(R.id.fragment_login_btnLogin);
        btnLogin.setOnClickListener(this);



        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            application.getAuth().getUser().setLoggedIn(true);
            application.getAuth().getUser().setDisplayName("Samar Ali");
            callBacks.onLoggedIn();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBacks = (CallBacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBacks = null;
    }

    public interface CallBacks {
        void onLoggedIn();
    }
}
