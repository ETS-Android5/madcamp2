package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NoLoginSideMenuFragment extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private LoginSideMenuFragment loginSideMenuFragment;
    private NoLoginSideMenuFragment noLoginSideMenuFragment;
    private int signin_state;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_login_state, container, false);



        return view;
    }
    @Override
    public void onResume() {

        super.onResume();

        if (getArguments() != null) {
            signin_state = getArguments().getInt("signin_state");
            username = getArguments().getString("username");
            if (signin_state == 0) {
                Log.e("사이드 헤더 상태", "로그인");
                loginSideMenuFragment = new LoginSideMenuFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                LoginSideMenuFragment fragment = new LoginSideMenuFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                fragment.setArguments(bundle);
                transaction.replace(R.id.userStateContainerview, loginSideMenuFragment).commitAllowingStateLoss();
            }
        } else {
            Log.e("번들 n", "전달실패");
        }

    }
}
