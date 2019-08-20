package org.unyde.mapintegrationlib.blescan

import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.polidea.rxandroidble2.exceptions.BleScanException
import org.unyde.mapintegrationlib.MyApplication

private const val SCAN_REQUEST_CODE = 43

class BluetoothReceiver : BroadcastReceiver() {

    companion object {
        fun newPendingIntent(context: Context): PendingIntent =
            Intent(context, BluetoothReceiver::class.java).let {
                PendingIntent.getBroadcast(context, SCAN_REQUEST_CODE, it, 0)
            }
    }

    override fun onReceive(context: Context, intent: Intent) {

        val extras = intent.extras
        var bluetoothState = extras!!.getInt(1.toString());
        when (bluetoothState) {
            BluetoothAdapter.STATE_OFF -> {
                BleScan.onScanStopClick()
            }
            BluetoothAdapter.STATE_TURNING_OFF -> {
            }
            BluetoothAdapter.STATE_ON -> {
                BleScan.onScanStartClick()
            }
            BluetoothAdapter.STATE_TURNING_ON -> {
            }
        }
    }
}
