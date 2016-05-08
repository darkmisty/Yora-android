package socialapp.com.socialapp.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Account;


public class LoginFragment extends BaseFragment implements View.OnClickListener {


    private Button btnLogin;
    private CallBacks callBacks;
    private View progressBar;
    private EditText txtUserName, txtPassword;
    private String defaultLoginButtonText = "";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        View v =  inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = (Button) v.findViewById(R.id.fragment_login_btnLogin);
        btnLogin.setOnClickListener(this);

        progressBar = v.findViewById(R.id.fragment_login_ProgressBar);
        txtUserName = (EditText) v.findViewById(R.id.fragment_login_userName);
        txtPassword = (EditText) v.findViewById(R.id.fragment_login_password);

        defaultLoginButtonText = btnLogin.getText().toString();


        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {

            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setText("Loading...");
            txtUserName.setEnabled(false);
            txtPassword.setEnabled(false);
            btnLogin.setEnabled(false);


            bus.post(new Account.LoginWithUsernameRequest(
                    txtUserName.getText().toString(),
                    txtPassword.getText().toString()));

        }
    }

    @Subscribe
    public void OnLoginWithUserName(Account.LoginWithUsernameResponse response) {

        response.showErrorToast(getActivity());

        if (response.didSucceed()) {
            callBacks.onLoggedIn();
            return;
        }

        txtUserName.setError(response.getPropertyError("username"));
        txtPassword.setError(response.getPropertyError("password"));

        txtUserName.setEnabled(true);
        txtPassword.setEnabled(true);

        progressBar.setVisibility(View.GONE);
        btnLogin.setText(defaultLoginButtonText);

        btnLogin.setEnabled(true);




    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        if (context instanceof Activity) {
//
//            callbacks = (Callbacks) context;
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {

            callBacks = (CallBacks) context;
        }
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
