package com.example.client;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginSideMenuFragment extends Fragment {

    private TextView username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_state, container, false);
        username = view.findViewById(R.id.username);
        if (getArguments() != null) {
            username.setText(getArguments().getString("username","0"));
        } else {
            Log.e("번들 l", "전달실패");
        }
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();



    }
}
