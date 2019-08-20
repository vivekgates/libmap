package org.unyde.mapintegrationlib.blescan

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.unyde.mapintegrationlib.MyApplication
import org.unyde.mapintegrationlib.permission.TedPermissionBase.getDeniedPermissions
import org.unyde.mapintegrationlib.permission.TedPermissionBase.isGranted
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.work.impl.utils.ForceStopRunnable
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanSettings
import org.unyde.mapintegrationlib.R
import org.unyde.mapintegrationlib.permission.TedRx2Permission
import org.unyde.mapintegrationlib.permission.TedPermission
import org.unyde.mapintegrationlib.permission.PermissionListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.unyde.mapintegrationlib.MyApplication.Companion.rxBleClient


class BleScan(context: Context) {

    private val rxBleClient = MyApplication.rxBleClient
    private var hasClickedScan = false


    init {
        callbackIntent = ScanReceiver.newPendingIntent(context)
        val permissionlistener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                onScanStartClick()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    context,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


        TedPermission.with(context)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("we need permission for read contact, find your location and system alert window")
            .setDeniedMessage("If you reject permission,you can not use this BLE service")
            .setGotoSettingButtonText("setting")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()


       // registerBroadcastManager(context)
    }

companion object
{
    lateinit var callbackIntent: PendingIntent



    fun scanBleDeviceInBackground() {
        if (Build.VERSION.SDK_INT >= 26 /* Build.VERSION_CODES.O */) {
            try {
                val scanSettings = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .build()

                val scanFilter = ScanFilter.Builder()
//                    .setDeviceAddress("5C:31:3E:BF:F7:34")
                    // add custom filters if needed
                    .build()

                rxBleClient.backgroundScanner.scanBleDeviceInBackground(callbackIntent, scanSettings, scanFilter)

            } catch (scanException: BleScanException) {
                Log.e("BackgroundScanActivity", "Failed to start background scan", scanException)
            }
        } else {
        }
    }

    fun onScanStartClick() {

        scanBleDeviceInBackground()
    }

    fun onScanStopClick() {
        if (Build.VERSION.SDK_INT >= 26 /* Build.VERSION_CODES.O */) {
            rxBleClient.backgroundScanner.stopBackgroundBleScan(callbackIntent)
        } else {
        }
    }



}

    fun registerBroadcastManager(context: Context) {
        val manager = LocalBroadcastManager.getInstance(context)
        val filter1 = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(mBluetoothStatusChangedReceiver!!, filter1)
    }


    fun unregisterBroadcastManager(context: Context) {
        val manager = LocalBroadcastManager.getInstance(context)
        context.unregisterReceiver(mBluetoothStatusChangedReceiver)
    }



    var mBluetoothStatusChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val extras = intent.extras
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                var bluetoothState = extras!!.getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                when (bluetoothState) {
                    BluetoothAdapter.STATE_OFF -> {
                        onScanStopClick()
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                    }
                    BluetoothAdapter.STATE_ON -> {
                        onScanStartClick()
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                    }
                }
            }


        }





    }
}