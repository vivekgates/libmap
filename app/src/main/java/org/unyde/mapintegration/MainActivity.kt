package org.unyde.mapintegration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.unyde.mapintegrationlib.ClusterMapActivity
import org.unyde.mapintegrationlib.MapIntgrationMain
import org.unyde.mapintegrationlib.blescan.BleScan
import org.unyde.mapintegrationlib.blescan.BleScan.Companion.onScanStartClick
import org.unyde.mapintegrationlib.blescan.ScanReceiver
import org.unyde.mapintegrationlib.blescan.ScanReceiver.Companion.getScanObservable
import org.unyde.mapintegrationlib.viewmodel.BleScandataViewModel
import org.unyde.mapintegrationlib.viewmodel.ClusterDetailViewModel

class MainActivity : FragmentActivity() {

    private var mViewModel_cluster: BleScandataViewModel? = null
  //  var ble = BleScan(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*  var opencluster = Intent(this@MainActivity, ClusterMapActivity::class.java)
          startActivity(opencluster)*/


     /*   ble.registerBroadcastManager(this@MainActivity)
        ble.run {
            onScanStartClick()
        }


        mViewModel_cluster = ViewModelProviders.of(this@MainActivity).get(BleScandataViewModel::class.java!!)
        mViewModel_cluster!!.init(this@MainActivity)
        mViewModel_cluster!!.clusterDetails.observeForever { clusterDetail ->

           Log.i("mainActivity",""+clusterDetail.toString())*/
      //  MapIntgrationMain.downloadmap(this,"101")
      var store= MapIntgrationMain.getStoreDetails("FF11011C000650000003910011022800","101")
      Log.i("Main",store.address)
  }



        // observable

        // observable
        /*  val scanObservable = getScanObservable()
          val scanObserver = getScanObserver()

          scanObservable
              .observeOn(Schedulers.io())
              .subscribeOn(AndroidSchedulers.mainThread())
              .subscribe(scanObserver);*/


    }

  /*  private fun getScanObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(s: String) {
                Log.d("MainActivity", "Name: " + s);
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {

            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        ble.unregisterBroadcastManager(this@MainActivity)
    }
*/

