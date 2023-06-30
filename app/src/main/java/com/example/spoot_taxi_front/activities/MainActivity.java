package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


import com.example.spoot_taxi_front.databinding.ActivityMainBinding;
import com.example.spoot_taxi_front.fragments.ChatFragment;
import com.example.spoot_taxi_front.fragments.MatchingFragment;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.fragments.RallyFragment;
import com.example.spoot_taxi_front.fragments.SettingsFragment;


import com.kakao.util.maps.helper.Utility;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_RALLY = "RallyFragment";
    private static final String TAG_CHAT = "ChatFragment";
    private static final String TAG_SETTINGS = "SettingsFragment";
    private static final String TAG_MATCHING = "MatchingFragment";

    private ActivityMainBinding binding;
    Button matchingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        String keyHash = Utility.getKeyHash(this);
        Log.d("카카오키해시", keyHash);



        setFragment(TAG_RALLY, new RallyFragment());

        binding.navigationView.setSelectedItemId(R.id.homeFragment);

        binding.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Log.d("libienz", "libienz");
            if (itemId == R.id.homeFragment) {
                setFragment(TAG_RALLY, new RallyFragment());
            } else if (itemId == R.id.chatFragment) {
                setFragment(TAG_CHAT, new ChatFragment());
            } else if (itemId == R.id.settingsFragment) {
                setFragment(TAG_SETTINGS, new SettingsFragment());
            } else if (itemId == R.id.matchingFragment) {
                setFragment(TAG_MATCHING, new MatchingFragment());
        }


            return true;
        });

    }

    protected void setFragment(String tag, Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.mainFrameLayout, fragment, tag);
        }

        Fragment home = fragmentManager.findFragmentByTag(TAG_RALLY);
        Fragment chat = fragmentManager.findFragmentByTag(TAG_CHAT);
        Fragment settings = fragmentManager.findFragmentByTag(TAG_SETTINGS);
        Fragment matching = fragmentManager.findFragmentByTag(TAG_MATCHING);


        if (home != null){
            fragmentTransaction.hide(home);
        }

        if (chat != null){
            fragmentTransaction.hide(chat);
        }

        if (settings != null){
            fragmentTransaction.hide(settings);
        }
        if (matching != null) {
            fragmentTransaction.hide(matching);
        }

        if (tag.equals(TAG_RALLY)) {
            if (home != null){
                fragmentTransaction.show(home);
            }
        } else if (tag.equals(TAG_CHAT)) {
            if (chat != null){
                fragmentTransaction.show(chat);
            }
        } else if (tag.equals(TAG_SETTINGS)) {
            if (settings != null) {
                fragmentTransaction.show(settings);
            }
        } else {
            if (matching != null) {
                fragmentTransaction.show(matching);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}