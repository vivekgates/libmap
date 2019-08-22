package org.unyde.mapintegration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import org.unyde.mapintegrationlib.MapIntgrationMain

class MainActivity : FragmentActivity() {

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



           Log.i("mainActivity",""+clusterDetail.toString())*/
       // MapIntgrationMain.downloadmap(this,"101")
//        var store= MapIntgrationMain.getStoreDetails("FF11011C000650000003910011022800","101")
     //  var store= MapIntgrationMain.getStoreDetails("FF11011C0006500000030B0011022800","101")
     // Log.i("Main",store.address)
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

