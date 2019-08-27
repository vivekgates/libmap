package org.unyde.mapintegration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import org.unyde.mapintegrationlib.MapIntgrationMain
import org.unyde.mapintegrationlib.worker.helper.LiveDataHelper

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
        //MapIntgrationMain.downloadmap(this,"101",this@MainActivity)
       var store= MapIntgrationMain.getStoreDetails("FF11011C000650000007000011032800","101",this@MainActivity)

      LiveDataHelper.getInstance().observePercentage()
          .observeForever {
              //Toast.makeText(c, it.toString(), Toast.LENGTH_LONG).show()
          }

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

