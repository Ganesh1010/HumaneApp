package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

public class OrganisationRegistrationFragment extends Fragment {

    View v;
    EditText orgNoEditText,orgNameEditText,addressEditText,orgEmailEditText,orgMobNoEditText,orgDescEditText;
    Spinner orgTypeFromSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(v==null)
            v = inflater.inflate(R.layout.layout_org_form,container,false);

        orgNoEditText = (EditText)v.findViewById(R.id.org_register_num_editText_org_form);
        orgNameEditText = (EditText)v.findViewById(R.id.org_name_editText_org_form);

        return v;
    }
}
