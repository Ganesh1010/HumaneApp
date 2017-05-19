package vuram_test_2.vuram.com.vuram_test_2;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPageFragment extends Fragment{

    View v;
    EditText emailEditText,passwordEditText;
    Button loginButton,signupButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(v == null)
            v = inflater.inflate(R.layout.login_fragment,container,false);
        emailEditText = (EditText)v.findViewById(R.id.email_login);
        passwordEditText = (EditText)v.findViewById(R.id.password_login);
        loginButton = (Button)v.findViewById(R.id.btn_login);
        signupButton = (Button)v.findViewById(R.id.link_login);


        return v;
    }
}
