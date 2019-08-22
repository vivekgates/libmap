//package org.unyde.mapintegrationlib.services
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import android.util.Log
//import org.unyde.mapintegrationlib.MyApplication
//import org.unyde.mapintegrationlib.util.Helper
//import org.unyde.mapintegrationlib.util.Pref_manager
//
//class CheckInCheckOutService : Service(), MainView {
//
//    private var setUserTransactionImpl: SetUserTransactionImpl? = null
//    var checkInCheckOutId: Int = 0
//    var store_name: String = ""
//
//    override fun onCreate() {
//        super.onCreate()
//    }
//
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        try {
//
//            if (intent != null) {
//
//                if (Helper.isConnectionAvailable(applicationContext)) {
//                  //  MyApplication.get()!!.db!!.checkInCheckOut().deleteOtherData()
//                    var storeCheckInCheckOutData = MyApplication.get()!!.db!!.checkInCheckOut().all
//                    if (storeCheckInCheckOutData.size > 0) {
//                        checkInCheckOutId = storeCheckInCheckOutData.get(0).auto_inc
//                        setUserTransactionImpl = SetUserTransactionImpl(this@CheckInCheckOutService, Pref_manager.getUserId(MyApplication.instance.applicationContext), storeCheckInCheckOutData.get(0).type_in_out.toString(), storeCheckInCheckOutData.get(0).beacon_id, storeCheckInCheckOutData.get(0).createDate + " " + storeCheckInCheckOutData.get(0).time_in_out, "1")
//                        setUserTransactionImpl!!.init()
//                    } else {
//                        stopSelf()
//                    }
//                }
//                /*   Thread(Runnable {
//                       if (Helper.isConnectionAvailable(applicationContext)) {
//                           var storeCheckInCheckOutData = MyApplication.get()!!.db!!.checkInCheckOut().all
//                           if (storeCheckInCheckOutData.size > 0) {
//                               checkInCheckOutId = storeCheckInCheckOutData.get(0).auto_inc
//                               setUserTransactionImpl = SetUserTransactionImpl(this@CheckInCheckOutService, Pref_manager.getUserId(MyApplication.instance.applicationContext), storeCheckInCheckOutData.get(0).type_in_out.toString(), storeCheckInCheckOutData.get(0).beacon_id,storeCheckInCheckOutData.get(0).createDate+" "+storeCheckInCheckOutData.get(0).time_in_out,"1")
//                               setUserTransactionImpl!!.init()
//                           } else {
//                               stopSelf()
//
//                           }
//                       }
//                   }).start()*/
//            }
//
//
//        } catch (e: Exception) {
//            Log.e("chekInOutServiceOnStart", e.message)
//        }
//        return Service.START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//
//    override fun onDestroy() {
//        //
//        super.onDestroy()
//    }
//
//
//    override fun stopService(name: Intent?): Boolean {
//        // writeLine("Automate service stop...");
//        stopSelf()
//        return super.stopService(name)
//    }
//
//
//    override fun showLoadingLayout() {
//
//    }
//
//    override fun hideLoadingLayout() {
//
//    }
//
//    override fun errorCapture(error: CaptureAllRequest) {
//        startService(Intent(this@CheckInCheckOutService, CaptureAllErrorService::class.java).putExtra("error", error))
//    }
//
//    override fun showSuccess(`object`: Any) {
//
//        try {
//            if (`object` is ChekIn_OUtResponse) {
//                MyApplication.get()!!.db!!.checkInCheckOut().deleteData(checkInCheckOutId)
//                var storeCheckInCheckOutData = MyApplication.get()!!.db!!.checkInCheckOut().all
//                if (storeCheckInCheckOutData.size > 0) {
//                    startService(Intent(this@CheckInCheckOutService, CheckInCheckOutService::class.java))
//                } else {
//                    stopSelf()
//                }
//
//            }
//        } catch (ex: Exception) {
//            Log.e("checkInOutService", ex.message)
//        }
//    }
//
//    override fun authFailure(error: String) {
//
//        try {
//
//            var storeCheckInCheckOutData = MyApplication.get()!!.db!!.checkInCheckOut().all
//            if (storeCheckInCheckOutData.size > 0) {
//                startService(Intent(this@CheckInCheckOutService, CheckInCheckOutService::class.java))
//            } else {
//                stopSelf()
//            }
//        } catch (e: Exception) {
//            Log.e("InoutService", e.message)
//        }
//
//    }
//
//    override fun showFailure(error: String) {
//        try {
//            MyApplication.get()!!.db!!.checkInCheckOut().deleteData(checkInCheckOutId)
//            var storeCheckInCheckOutData = MyApplication.get()!!.db!!.checkInCheckOut().all
//            if (storeCheckInCheckOutData.size > 0) {
//                startService(Intent(this@CheckInCheckOutService, CheckInCheckOutService::class.java))
//            } else {
//                stopSelf()
//            }
//        } catch (e: Exception) {
//            Log.e("InoutService", e.message)
//        }
//
//    }
//
//
//}
