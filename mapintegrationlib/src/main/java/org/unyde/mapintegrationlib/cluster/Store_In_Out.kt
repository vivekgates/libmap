package woogly.unyde.org.wooglyunyde.cluster

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap
import org.unyde.mapintegrationlib.MyApplication
import org.unyde.mapintegrationlib.R
import org.unyde.mapintegrationlib.database.entity.CheckInCheckOut
import org.unyde.mapintegrationlib.database.entity.PathNode
import org.unyde.mapintegrationlib.model.store_info.StoreInfo
import org.unyde.mapintegrationlib.util.Constants
import org.unyde.mapintegrationlib.util.Helper
import org.unyde.mapintegrationlib.util.NotificationUtils
import org.unyde.mapintegrationlib.util.Pref_manager


class Store_In_Out private constructor()  {

  //  internal var getStoreImpl: GetStoreDetailsImpl? = null
    private var beacon: String? = null
    private var cluster: String? = null
    var store_info: List<PathNode>? = null
    var addCurrentStore: addCurrentStoreCallback? = null
    var addParking: addParkingCallback? = null
   // private var setUserTransactionImpl: SetUserTransactionImpl? = null


    companion object {
        private var instance: Store_In_Out? = Store_In_Out()

        fun getInstance(): Store_In_Out {
            if (instance == null)
                instance = Store_In_Out()
            return instance!!
        }
    }


    fun check_in(beacon_id: String, cluster_id: String):StoreInfo {

        beacon = beacon_id
        cluster = cluster_id
        var storeDetail=StoreInfo()
        try {
            if (!beacon_id.substring(4, 22).equals(Pref_manager.getCheckInStoreId(MyApplication.instance.applicationContext), ignoreCase = true)) {
                //if (!NotificationUtils.isAppIsInBackground(MyApplication.instance.applicationContext)) {

                if (Pref_manager.getCheckInStoreId(MyApplication.instance.applicationContext).isEmpty() || Pref_manager.getCheckInStoreId(MyApplication.instance.applicationContext).equals("", ignoreCase = true)) {
                    Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, beacon_id.substring(4, 22))
                    Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, beacon_id)
                    storeDetail=showcard(beacon_id);
                } else {
                    // if (Helper.isConnectionAvailable(MyApplication.instance.applicationContext)) {
                    check_out(Pref_manager.getCheckInBeaconId(MyApplication.instance.applicationContext), false)
                   // Constants.storeBeaconMap.remove(Pref_manager.getCheckInBeaconId(MyApplication.instance.applicationContext))
                    Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, beacon_id.substring(4, 22))
                    Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, beacon_id)
                    storeDetail=showcard(beacon_id);
                    // }
                }
                //}
            }
        } catch (ex: Exception) {
            Log.e("tag", ex.message.toString())
        }
     return storeDetail
    }


    fun check_out(beacon_id: String, isLosted: Boolean) {
        store_info = MyApplication.get()!!.db!!.pathNodeList().findById(beacon_id, cluster)

        if (store_info!!.size > 0) {

            if (store_info!!.get(0).store_type.toInt() == 1) {
                if (isLosted) {
                    var checkOutData: CheckInCheckOut = CheckInCheckOut(beacon_id, 1, Pref_manager.currentTime, Pref_manager.date, "1", "1")
                    MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkOutData)
                    Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, "")
                    Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, "")
                   // Constants.storeBeaconMap.clear()
                    floaterButtonVisibility(false, "sa", "", "", "", "","")
                    /*  if (addCurrentStore != null) {
                          addCurrentStore!!.addCurrentStoreButton(false, "sa", "")
                      }*/
                    Log.e("CheckoutLosted", "" + beacon_id)
                } else {
                    Log.e("CheckOut", "" + beacon_id)
                    var checkOutData: CheckInCheckOut = CheckInCheckOut(beacon_id, 1, Pref_manager.currentTime, Pref_manager.date, "1", "1")
                    MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkOutData)
                    Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, "")
                    Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, "")
                   // Constants.storeBeaconMap.clear()
                    floaterButtonVisibility(false, "sa", "", "", "", "","")
                }
            } else if (store_info!!.get(0).store_type.toInt() == 4) {
                var checkOutData: CheckInCheckOut = CheckInCheckOut(beacon_id, 1, Pref_manager.currentTime, Pref_manager.date, "1", "4")
                MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkOutData)
                Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, "")
                Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, "")
                //Constants.storeBeaconMap.clear()
                floaterButtonVisibility(false, "sa", "", "", "", "","")

            } else {
                var checkOutData: CheckInCheckOut = CheckInCheckOut(beacon_id, 1, Pref_manager.currentTime, Pref_manager.date, "1", "10")
                MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkOutData)
                Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, "")
                Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, "")
                //Constants.storeBeaconMap.clear()
                floaterButtonVisibility(false, "sa", "", "", "", "","")
            }
        } else {
            var checkOutData: CheckInCheckOut = CheckInCheckOut(beacon_id, 1, Pref_manager.currentTime, Pref_manager.date, "1", "1")
            MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkOutData)
            Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
            Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
            Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, "")
            Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, "")
          //  Constants.storeBeaconMap.clear()
            floaterButtonVisibility(false, "sa", "", "", "", "","")
        }


       /* if (!Helper.isMyServiceRunning(CheckInCheckOutService::class.java, MyApplication.INSTANCE?.applicationContext)) {
            //  MyApplication.INSTANCE?.applicationContext!!.startService(Intent(MyApplication.INSTANCE?.applicationContext, CheckInCheckOutService::class.java))
        }*/

    }


    fun floaterButtonVisibility(isBtnShow: Boolean?, url: String, alphabet: String, store_name: String, store_add: String, store_mobile: String, store_beacon: String) {
        if (addCurrentStore != null) {
            addCurrentStore!!.addCurrentStoreButton(isBtnShow, url, alphabet, store_name, store_add, store_mobile,store_beacon)
        }
    }


    private fun showcard(beacon_id: String):StoreInfo {

        var storeDetail=StoreInfo()

        if (Pref_manager.getOpenClusterSatus(MyApplication.INSTANCE!!.applicationContext)) {
            if (!Pref_manager.isShowCard(MyApplication.instance.applicationContext)) {

               /* if (Helper.isConnectionAvailable(MyApplication.instance.applicationContext)) {
                    getStoreImpl = null
                    getStoreImpl = GetStoreDetailsImpl(this@Store_In_Out, "", beacon_id)
                    getStoreImpl!!.init()
                }*/
            }
        } else {
            store_info = MyApplication.get()!!.db!!.pathNodeList().findById(beacon_id, cluster)
            if (store_info!!.size > 0) {
                var store_name: String = store_info!!.get(0).store_name
                var store_id: String = store_info!!.get(0).store_id
                var address: String = store_info!!.get(0).address
                var phoneNumber: String = store_info!!.get(0).phone
                var mobile: String = store_info!!.get(0).mobile
                var icon: String = store_info!!.get(0).store_logo
                var cluster_id: String = store_info!!.get(0).clustor_id
                if (store_info!!.get(0).store_type.toInt() == 1) {
                    var checkinData: CheckInCheckOut = CheckInCheckOut(beacon_id, 0, Pref_manager.currentTime, Pref_manager.date, "1", "1")
                    MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkinData)
                    var phoneNumber1 = ""

                    storeDetail?.address = store_info!!.get(0).address
                    storeDetail?.clustor_id = store_info!!.get(0).clustor_id
                    storeDetail?.floor_level = store_info!!.get(0).floor_level
                    storeDetail?.floor_name = store_info!!.get(0).floor_name
                    storeDetail?.mobile = store_info!!.get(0).mobile
                    storeDetail?.phone = store_info!!.get(0).phone
                    storeDetail?.store_id = store_info!!.get(0).store_id
                    storeDetail?.store_name = store_info!!.get(0).store_name
                    storeDetail?.store_logo = store_info!!.get(0).store_logo

                 /*   val notifyIntent = Intent(MyApplication.instance.applicationContext, LoginActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("STORE_ID", store_id)

                    notifyIntent.putExtras(bundle)
                    // Set the Activity to start in a new, empty task
                    notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    // Create the PendingIntent
                    val notifyPendingIntent = PendingIntent.getActivity(
                            MyApplication.instance.applicationContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    if (NotificationUtils.isAppIsInBackground(MyApplication.instance.applicationContext)) {
                        if (Pref_manager.isNotificationOnOff(MyApplication.instance.applicationContext)) {
                            if(cluster_id.equals("105"))
                            {
                                handleDataMessage("Welcome to Home Expo", "You are at " + store_name, "", notifyPendingIntent)
                            }
                            else
                            {
                                handleDataMessage("Offer in " + store_name, "New Offer for You!", "", notifyPendingIntent)
                            }


                        }
                    }*/


                    Pref_manager.saveToSharedPreferences(MyApplication.INSTANCE!!.applicationContext, System.currentTimeMillis(), "Offer in " + store_name, "New Offer for You!")


                    Log.e("CheckIn", "" + beacon_id)
                    floaterButtonVisibility(true, icon, store_name.substring(0, 1).toUpperCase(), store_name, address, phoneNumber1,beacon_id)
                    Pref_manager.setStoreTimestamp(MyApplication.instance.applicationContext, System.currentTimeMillis())
                    Pref_manager.setStoreIcon(MyApplication.instance.applicationContext!!, icon)
                    Pref_manager.setStoreBeacon(MyApplication.instance.applicationContext!!, beacon_id)
                    Pref_manager.setStoreBeaconName(MyApplication.instance.applicationContext!!, store_name)
                    Pref_manager.setStoreBeaconAddress(MyApplication.instance.applicationContext!!, address)
                    Pref_manager.setStoreBeaconCallNo(MyApplication.instance.applicationContext!!, phoneNumber1)


                  /*  if (!Cluster3DMap.mActionMode!!.equals(Cluster3DMap.IndoorMode.NAVIGATION)) {
                        *//*var prodIn = Intent(MyApplication.INSTANCE!!.applicationContext, ProductPageWithSmallCard::class.java)
                        prodIn.putExtra("Store_name", store_name);
                        prodIn.putExtra("Store_address", address);
                        prodIn.putExtra("Store_id", store_id);
                        prodIn.putExtra("show_btn", "" + 0);
                        prodIn.putExtra("no_navigation", "" + 1);
                        prodIn.putExtra("allData", "")
                        prodIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*//*
                        // MyApplication.INSTANCE!!.applicationContext.startActivity(prodIn)
                    }
*/
                    Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, store_info!!.get(0).store_id.toInt())
                    Pref_manager.setStoreId(MyApplication.instance.applicationContext, store_info!!.get(0).store_id.toInt())
                } else if (store_info!!.get(0).store_type.toInt() == 4) {
                    var checkinData: CheckInCheckOut = CheckInCheckOut(beacon_id, 0, Pref_manager.currentTime, Pref_manager.date, "1", "4")
                    MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkinData)
                    if (Pref_manager.isParked(MyApplication.instance.applicationContext) == false) {
                        if (addParking != null) {
                            addParking!!.addParking()
                        }
                    }
                    floaterButtonVisibility(false, "sa", "", "", "", "",beacon_id)
                    Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setStoreDetails(MyApplication.instance.applicationContext, "")
                } else {
                    var checkinData: CheckInCheckOut = CheckInCheckOut(beacon_id, 0, Pref_manager.currentTime, Pref_manager.date, "1", "10")
                    MyApplication.get()!!.db!!.checkInCheckOut().addCheckinCheckOutData(checkinData)
                    floaterButtonVisibility(false, "sa", "", "", "", "",beacon_id)
                    /* if (addCurrentStore != null) {
                         addCurrentStore!!.addCurrentStoreButton(false, "sa", "")
                     }*/
                    Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setStoreDetails(MyApplication.instance.applicationContext, "")
                }


            } else {

                if (Helper.isConnectionAvailable(MyApplication.instance.applicationContext)) {
                   /* getStoreImpl = null
                    getStoreImpl = GetStoreDetailsImpl(this@Store_In_Out, "", beacon_id)
                    getStoreImpl!!.init()*/
                } else {
                    Pref_manager.setStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setCardStoreId(MyApplication.instance.applicationContext, 0)
                    Pref_manager.setCheckInStoreId(MyApplication.instance.applicationContext, "")
                    Pref_manager.setCheckInBeaconId(MyApplication.instance.applicationContext, "")
                }


            }
        }

     /*   if (!Helper.isMyServiceRunning(CheckInCheckOutService::class.java, MyApplication.INSTANCE?.applicationContext)) {
            MyApplication.INSTANCE?.applicationContext!!.startService(Intent(MyApplication.INSTANCE?.applicationContext, CheckInCheckOutService::class.java))
        }*/
    return storeDetail
    }


    interface addCurrentStoreCallback {
        fun addCurrentStoreButton(isBtnShow: Boolean?, url: String, alphabet: String, store_name: String, store_add: String, store_mobile: String, store_beacon: String)
    }

    fun registerClientForAddCurrentStoreButton(fragment: Fragment) {
        try {
            addCurrentStore = fragment as addCurrentStoreCallback
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    interface addParkingCallback {
        fun addParking()
    }

    fun registerClientForAddParking(fragment: Fragment) {
        try {
            addParking = fragment as addParkingCallback
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    fun handleDataMessage(title: String, message: String, timestamp: String, pendingIntent: PendingIntent) {

        try {
            val notification = NotificationUtils.getNotificationBuilder(MyApplication.instance.applicationContext)
                    .setSmallIcon(R.drawable.ic_my_location_black_24dp)
                    .setContentTitle(if (TextUtils.isEmpty(title)) "Woogly" else title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setAutoCancel(true)
                    .build()
            val id = System.currentTimeMillis().toInt()
            NotificationUtils.showNotification(MyApplication.instance.applicationContext, notification, timestamp, R.id.text_notification_id)
        } catch (e: Exception) {
            Log.e("Store_in_out", "Exception: " + e.message)
        }

    }


}
