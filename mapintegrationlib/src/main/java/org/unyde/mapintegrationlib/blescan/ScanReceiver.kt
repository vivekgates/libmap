package org.unyde.mapintegrationlib.blescan

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.Observable
import org.unyde.mapintegrationlib.MyApplication
import org.unyde.mapintegrationlib.interfaces.OnScanListener
import org.unyde.mapintegrationlib.model.GetClusterDataList
import java.util.concurrent.Callable

private const val SCAN_REQUEST_CODE = 42
var getMutableLiveData = MutableLiveData<String>()
var scanResult: String?=""


class ScanReceiver : BroadcastReceiver() {

    companion object {
        fun newPendingIntent(context: Context): PendingIntent =
            Intent(context, ScanReceiver::class.java).let {
                PendingIntent.getBroadcast(context, SCAN_REQUEST_CODE, it, 0)
            }

        fun getScanObservable(): MutableLiveData<String> {
            getMutableLiveData!!.value= scanResult

            return  getMutableLiveData


        }
    }

    @RequiresApi(26 /* Build.VERSION_CODES.O */)
    override fun onReceive(context: Context, intent: Intent) {
        val backgroundScanner = MyApplication.rxBleClient.backgroundScanner
        val i = try {
            val scanResults = backgroundScanner.onScanResultReceived(intent)
            //scanResult=scanResults.get(0).scanRecord.toString()
            Log.i("ScanReceiver", "Scan results received: $scanResults")
        } catch (exception: BleScanException) {
            Log.e("ScanReceiver", "Failed to scan devices", exception)
        }
    }
}
