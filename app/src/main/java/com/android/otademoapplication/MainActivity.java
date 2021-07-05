package com.android.otademoapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.otademoapplication.databinding.ActivityOtaDemoBinding;
import com.android.otademoapplication.model.OTADemoModel;

public class MainActivity extends AppCompatActivity {


    private static final String ACTION_OTA_UPDATE_RESULT = "action_ota_update_result";
    private static final String EXTRA_OTA_UPDATE_RESULT_PROGRESS = "extra_ota_update_result_Progress";
    private static final String EXTRA_OTA_UPDATE_RESULT_ERROR = "extra_ota_update_result_error";
    private static final String EXTRA_OTA_UPDATE_RESULT_MSG = "extra_ota_update_result_msg";
    private static final int NO_ERROR = 0;

    private ActivityOtaDemoBinding binding;
    private OTADemoModel otaDemoModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ota_demo);
        otaDemoModel = new OTADemoModel(this);
        binding.setModel(otaDemoModel);
        registerOtaReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        otaDemoModel.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterOtaReceiver();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_OTA_UPDATE_RESULT:
                    long progress = intent.getLongExtra(EXTRA_OTA_UPDATE_RESULT_PROGRESS, 0);
                    int error = intent.getIntExtra(EXTRA_OTA_UPDATE_RESULT_ERROR, 0);
                    String msg = intent.getStringExtra(EXTRA_OTA_UPDATE_RESULT_MSG);
                    if (error != NO_ERROR) {
                        otaDemoModel.setFirmwareUpdateResult(msg);
                    } else {
                        otaDemoModel.setFirmwareUpdateResult("进度：" + progress + "/100");
                    }
                    break;
            }
        }
    };

    private void registerOtaReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_OTA_UPDATE_RESULT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterOtaReceiver() {
        unregisterReceiver(broadcastReceiver);
    }
}