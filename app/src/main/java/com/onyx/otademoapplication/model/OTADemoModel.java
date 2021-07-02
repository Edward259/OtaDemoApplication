package com.onyx.otademoapplication.model;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.onyx.android.sdk.api.device.OTAManager;


public class OTADemoModel extends BaseObservable {

    private Context mContext;
    public ObservableField<String> otaFilePath = new ObservableField<>("/sdcard/update.zip");
    public ObservableField<String> firmwareUpdateResult = new ObservableField<>();

    public OTADemoModel(Context context) {
        mContext = context;
    }

    public void clickOTAUpdate() {
        Intent intent = OTAManager.buildFirmwareUpdateIntent(otaFilePath.get());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void clickCustomOTAUpdate() {
        Intent intent = OTAManager.buildFirmwareUpdateServiceIntent(otaFilePath.get());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent);
        } else {
            mContext.startService(intent);
        }
    }

    public void setFirmwareUpdateResult(String info) {
        firmwareUpdateResult.set(info);
    }
}
