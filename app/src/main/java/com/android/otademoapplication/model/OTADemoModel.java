package com.android.otademoapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.onyx.android.sdk.api.device.OTAManager;


public class OTADemoModel extends BaseObservable {

    public static final String WAKE_LOCK_TAG = "iwg_ota_update";

    private Context mContext;
    private PowerManager.WakeLock wakeLock = null;
    public ObservableField<String> otaFilePath = new ObservableField<>("/sdcard/update.zip");
    public ObservableField<String> firmwareUpdateResult = new ObservableField<>();

    public OTADemoModel(Context context) {
        mContext = context;
    }

    public void dispose() {
        releaseWakeLock();
    }

    public void clickOTAUpdate() {
        Intent intent = OTAManager.buildFirmwareUpdateIntent(otaFilePath.get());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    //Must add Wakelock with background update
    public void clickCustomOTAUpdate() {
        Intent intent = OTAManager.buildFirmwareUpdateServiceIntent(otaFilePath.get());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent);
        } else {
            mContext.startService(intent);
        }
        acquireWakeLock();
    }

    public void setFirmwareUpdateResult(String info) {
        firmwareUpdateResult.set(info);
    }

    @SuppressLint("InvalidWakeLockTag")
    private synchronized void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, WAKE_LOCK_TAG);
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    private synchronized void releaseWakeLock() {
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
