package org.unyde.mapintegrationlib.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Pref_manager(internal var context: Context) {



    companion object {
        val USER_NAME = "user_name"
        val SET_ANSWER = "user_answer"
        val STORE_DETAIL = "store_details"
        val MOBILE_NO = "mobile_no"
        val OTP = "otp"
        val GET_QUESTION = "get_question"
        val GET_QUESTIONID = "get_questionID"
        val DEVICE_NAme = "device_name"
        val FCMID = "fcm_id"
        val SPLASH_VIEW = "splash_view"
        val SPLASH_MAINTAIN = "splash_mainatain"
        val SUBPANEL_MAINTAIN = "sub_panle"
        val DEVICE_LIST = "device_list"
        val USER_ID = "user_id"
        val USER_AUTHTOKEN = "user_token"
        val STORE_DETALIS = "store_details"
        val USER_MAINID = "user_mainID"
        val COUPON = "COUPON"
        val STORE_ICON = "store_iconMain"
        val SHOW_CARD = "show_card"
        val LATITUDE = "latitud"
        val LONGITUDE = "longitude"
        val CLUSTER_TIMESTAMP = "cluster_ts"
        val STORE_TIMESTAMP = "store_ts"
        val SCAN_TIMESTAMP = "scan_ts"
        val FLOOR_LEVEL = "floor_level"
        val STORE_ID = "store_id"
        val CARD_STORE_ID = "card_store_id"
        val PARK_BEACON_ID = "PARK_BEACON_ID"
        val CHECKIN_STORE_BEACON_ID = "CHECKIN_STORE_BEACON_ID"
        val CHECKIN_CLUSTER_BEACON_ID = "CHECKIN_CLUSTER_BEACON_ID"
        val CLUSTER_ORIENTATION = "CLUSTER_ORIENTATION"
        val CHECKIN_BEACON_ID = "CHECKIN_BEACON_ID"
        val PARK_BEACON_NOTE = "PARK_BEACON_NOTE"
        val PARK_BEACON_FLOOR = "PARK_BEACON_FLOOR"
        val BECONID = "becon_id"
        val NOTIFICATION = "payloads"
        val PARK_STATUS = "park_status"
        val OPEN_CLUSTER_STATUS = "open_cluster_status"
        val CLUSTER_IN_STATUS = "cluster_in_status"
        val IS_AVAILABLE_DOWNLOAD = "is_available_download"
        val APP_STATUS = "app_status"
        val KEYWORDS_IN_DB = "keywords_in_db"
        val UPDATE_KEYWORDS_IN_DB = "update_keywords_in_db"
        val UPDATE_NEW_KEYWORDS_IN_DB = "update_new_keywords_in_db"
        val CLUSTER_DATA_IN_DB = "cluster_data_in_db"


        val All_OFFER = "all_offer"
        val STORE_ICOn = "store_iconM"
        val CLAIM_OFFER = "claim_offer"
        val UPDATE_LOCATION = "update_location"
        val TUTORIAL = "update_tutorial"
        val TUTORIAL_TRENDING = "update_tutorial_trending"
        val NOTIFICATION_ON_OFF = "notification_on_off"
        val MAINPANEL_OFFER = "main_offers"
        val CLUSTER_OFFER = "cluster_offers"
        val OUTSIDE_PARKING_LOCATION = "outside_parking_location"
        val SEARCH_KEYWORDS_DATE = "search_keywords_date"
        val PARKING_FROM = "parking_from"


        var type = ""
        var type_id = ""
        var range_data = ""
        var exclusive_offers: Boolean = false


        fun setUserName(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_NAME, editable).commit()
        }

        fun getUserName(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_NAME, "")!!
        }

        fun setStoreCount(context: Context, editable:ArrayList<String>) {
            var sharedPreferences = context.getSharedPreferences("storeCount", Context.MODE_PRIVATE)
            var json = Gson().toJson(editable)
            sharedPreferences.edit().putString("storeCount", json).commit()
        }

        fun getStoreCount(context: Context):String {
            var sharedPreferences = context.getSharedPreferences("storeCount", Context.MODE_PRIVATE)
            return sharedPreferences.getString("storeCount","")!!
        }

        fun setStoreICon(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(STORE_ICOn, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_NAME, editable).commit()
        }

        fun getStoreICon(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(STORE_ICOn, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_NAME, "")!!
        }


        fun setUserId(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_ID, editable).commit()
        }

        fun getUserId(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_ID, "")!!
        }


        fun setMobileNo(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(MOBILE_NO, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(MOBILE_NO, editable).commit()
        }

        fun getMobileNo(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(MOBILE_NO, Context.MODE_PRIVATE)
            return sharedPreferences.getString(MOBILE_NO, "")!!
        }



        fun setQuestion(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(GET_QUESTION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(GET_QUESTION, editable).commit()
        }

        fun getQuestion(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(GET_QUESTION, Context.MODE_PRIVATE)
            return sharedPreferences.getString(GET_QUESTION, "")!!
        }

        fun setQuestionID(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(GET_QUESTIONID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(GET_QUESTIONID, editable).commit()
        }

        fun getQuestionID(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(GET_QUESTIONID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(GET_QUESTIONID, "")!!
        }


        fun setDeviceName(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(DEVICE_NAme, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(DEVICE_NAme, editable).commit()
        }

        fun getGetDeviceName(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(DEVICE_NAme, Context.MODE_PRIVATE)
            return sharedPreferences.getString(DEVICE_NAme, "")!!
        }

        fun setBECONID(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(BECONID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(BECONID, editable).commit()
        }

        fun getBECONID(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(BECONID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(BECONID, "")!!
        }


        fun setFloor_Level(context: Context, editable: Int) {
            var sharedPreferences = context.getSharedPreferences(FLOOR_LEVEL, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(FLOOR_LEVEL, editable).commit()
        }

        fun getFloor_Level(context: Context): Int {
            var sharedPreferences = context.getSharedPreferences(FLOOR_LEVEL, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(FLOOR_LEVEL, 0)
        }


        fun setStoreId(context: Context, editable: Int) {
            var sharedPreferences = context.getSharedPreferences(STORE_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(STORE_ID, editable).commit()
        }

        fun getStoreId(context: Context): Int {
            val sharedPreferences = context.getSharedPreferences(STORE_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(STORE_ID, 0)
        }

        fun setCardStoreId(context: Context, editable: Int) {
            var sharedPreferences = context.getSharedPreferences(CARD_STORE_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(CARD_STORE_ID, editable).commit()
        }

        fun getCardStoreId(context: Context): Int {
            var sharedPreferences = context.getSharedPreferences(CARD_STORE_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(CARD_STORE_ID, 0)
        }


        fun setParkBeaconId(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(PARK_BEACON_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(PARK_BEACON_ID, editable).commit()
        }

        fun getParkBeaconId(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(PARK_BEACON_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(PARK_BEACON_ID, "")!!
        }

        fun setParkBeaconNote(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(PARK_BEACON_NOTE, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(PARK_BEACON_NOTE, editable).commit()
        }

        fun getParkBeaconNote(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(PARK_BEACON_NOTE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(PARK_BEACON_NOTE, "")!!
        }

        fun setParkBeacon_Floor_Level(context: Context, editable: Int) {
            var sharedPreferences = context.getSharedPreferences(PARK_BEACON_FLOOR, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(PARK_BEACON_FLOOR, editable).commit()
        }

        fun getParkBeacon_Floor_Level(context: Context): Int {
            var sharedPreferences = context.getSharedPreferences(PARK_BEACON_FLOOR, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(PARK_BEACON_FLOOR, 0)
        }


        fun setCheckInStoreId(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(CHECKIN_STORE_BEACON_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(CHECKIN_STORE_BEACON_ID, editable).commit()
        }

        fun getCheckInStoreId(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(CHECKIN_STORE_BEACON_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(CHECKIN_STORE_BEACON_ID, "")!!
        }


        fun setCheckInBeaconId(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(CHECKIN_BEACON_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(CHECKIN_BEACON_ID, editable).commit()
        }

        fun getCheckInBeaconId(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(CHECKIN_BEACON_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(CHECKIN_BEACON_ID, "")!!
        }


        fun setClusterOrientation(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_ORIENTATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(CLUSTER_ORIENTATION, editable).commit()
        }

        fun getClusterOrientation(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_ORIENTATION, Context.MODE_PRIVATE)
            return sharedPreferences.getString(CLUSTER_ORIENTATION, "")!!
        }




        fun setCheckInClusterId(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(CHECKIN_CLUSTER_BEACON_ID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(CHECKIN_CLUSTER_BEACON_ID, editable).commit()
        }

        fun getCheckInClusterStoreId(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(CHECKIN_CLUSTER_BEACON_ID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(CHECKIN_CLUSTER_BEACON_ID, "")!!
        }


        fun setUpdateAvaialableDownload(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(IS_AVAILABLE_DOWNLOAD, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(IS_AVAILABLE_DOWNLOAD, editable).commit()
        }

        fun getUpdateAvaialableDownload(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(IS_AVAILABLE_DOWNLOAD, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(IS_AVAILABLE_DOWNLOAD, false)
        }


        fun setKeywordsEnterInDB(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(KEYWORDS_IN_DB, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(KEYWORDS_IN_DB, editable).commit()
        }

        fun getKeywordsEnterInDB(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(KEYWORDS_IN_DB, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(KEYWORDS_IN_DB, false)
        }


        fun setUpdatedKeywordsEnterInDB(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_KEYWORDS_IN_DB, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(UPDATE_KEYWORDS_IN_DB, editable).commit()
        }

        fun getUpdatedKeywordsEnterInDB(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_KEYWORDS_IN_DB, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(UPDATE_KEYWORDS_IN_DB, false)
        }

        fun setNewUpdatedKeywordsEnterInDB(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_NEW_KEYWORDS_IN_DB, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(UPDATE_NEW_KEYWORDS_IN_DB, editable).commit()
        }

        fun getNewUpdatedKeywordsEnterInDB(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_NEW_KEYWORDS_IN_DB, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(UPDATE_NEW_KEYWORDS_IN_DB, false)
        }



        fun setOpenClusterStatus(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(OPEN_CLUSTER_STATUS, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(OPEN_CLUSTER_STATUS, editable).commit()
        }

        fun getOpenClusterSatus(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(OPEN_CLUSTER_STATUS, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(OPEN_CLUSTER_STATUS, false)
        }

        fun setParkFrom(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(PARKING_FROM, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(PARKING_FROM, editable).commit()
        }

        fun getParkFrom(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(PARKING_FROM, Context.MODE_PRIVATE)
            return sharedPreferences.getString(PARKING_FROM, "")!!
        }



        fun setParkStatus(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(PARK_STATUS, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(PARK_STATUS, editable).commit()
        }

        fun isParked(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(PARK_STATUS, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(PARK_STATUS, false)
        }

        fun setClusterIn(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_IN_STATUS, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(CLUSTER_IN_STATUS, editable).commit()
        }

        fun isCluster(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_IN_STATUS, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(CLUSTER_IN_STATUS, false)
        }


        fun setFirstTimeAppRun(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(APP_STATUS, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(APP_STATUS, editable).commit()
        }

        fun isFirstTimeRunApp(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(APP_STATUS, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(APP_STATUS, false)
        }


        fun setAuthToken(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(USER_AUTHTOKEN, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_AUTHTOKEN, editable).commit()
        }

        fun getAuthToken(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(USER_AUTHTOKEN, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_AUTHTOKEN, "")!!
        }


        fun setStoreDetailes(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(STORE_DETALIS, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(STORE_DETALIS, editable).commit()
        }

        fun getStoreDetailes(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(STORE_DETALIS, Context.MODE_PRIVATE)
            return sharedPreferences.getString(STORE_DETALIS, "")!!
        }


        fun setAnswer(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(SET_ANSWER, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(SET_ANSWER, editable).commit()
        }

        fun getAnswer(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(SET_ANSWER, Context.MODE_PRIVATE)
            return sharedPreferences.getString(SET_ANSWER, "")!!
        }




        fun setStoreDetails(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(STORE_DETAIL, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(STORE_DETAIL, editable).commit()
        }

        fun getStoreDetails(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(STORE_DETAIL, Context.MODE_PRIVATE)
            return sharedPreferences.getString(STORE_DETAIL, "")!!
        }






        fun seLoginView(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(SPLASH_MAINTAIN, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(SPLASH_MAINTAIN, editable).commit()
        }

        fun isLoginView(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(SPLASH_MAINTAIN, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(SPLASH_MAINTAIN, false)
        }


        fun setFCMID(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(FCMID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(FCMID, editable).commit()
        }

        fun getFCMID(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(FCMID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(FCMID, "")!!
        }


        fun setPanelPanelList(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(SUBPANEL_MAINTAIN, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(SUBPANEL_MAINTAIN, editable).commit()
        }

        fun isPanelList(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(SUBPANEL_MAINTAIN, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(SUBPANEL_MAINTAIN, false)
        }


        fun setGetDeviceList(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(DEVICE_LIST, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(DEVICE_LIST, editable).commit()
        }

        fun isGetDeviceList(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(DEVICE_LIST, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(DEVICE_LIST, false)
        }


        fun setShowCard(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(SHOW_CARD, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(SHOW_CARD, editable).commit()
        }

        fun isShowCard(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(SHOW_CARD, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(SHOW_CARD, false)
        }

        fun setLatitudu(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(LATITUDE, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(LATITUDE, editable).commit()
        }

        fun getLatitudu(context: Context?): String {
            var sharedPreferences: SharedPreferences? = null
            if (context != null) {
                sharedPreferences = context.getSharedPreferences(LATITUDE, Context.MODE_PRIVATE)

            }
            return sharedPreferences!!.getString(LATITUDE, "0")!!

        }


        fun setLongitude(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(LONGITUDE, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(LONGITUDE, editable).commit()
        }

        fun getLongitude(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(LONGITUDE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(LONGITUDE, "0")!!
        }


        fun setClusterTimestamp(context: Context, editable: Long?) {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_TIMESTAMP, Context.MODE_PRIVATE)
            sharedPreferences.edit().putLong(CLUSTER_TIMESTAMP, editable!!).commit()
        }

        fun getClusterTimestamp(context: Context): Long {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_TIMESTAMP, Context.MODE_PRIVATE)
            return sharedPreferences.getLong(CLUSTER_TIMESTAMP, 0)
        }


        fun setStoreTimestamp(context: Context, editable: Long?) {
            var sharedPreferences = context.getSharedPreferences(STORE_TIMESTAMP, Context.MODE_PRIVATE)
            sharedPreferences.edit().putLong(STORE_TIMESTAMP, editable!!).commit()
        }

        fun getStoreTimestamp(context: Context): Long {
            var sharedPreferences = context.getSharedPreferences(STORE_TIMESTAMP, Context.MODE_PRIVATE)
            return sharedPreferences.getLong(STORE_TIMESTAMP, 0)
        }


        fun setScanTimestamp(context: Context, editable: Long?) {
            var sharedPreferences = context.getSharedPreferences(SCAN_TIMESTAMP, Context.MODE_PRIVATE)
            sharedPreferences.edit().putLong(SCAN_TIMESTAMP, editable!!).commit()
        }

        fun getScanTimestamp(context: Context): Long {
            var sharedPreferences = context.getSharedPreferences(SCAN_TIMESTAMP, Context.MODE_PRIVATE)
            return sharedPreferences.getLong(SCAN_TIMESTAMP, 0)
        }


        fun setMainUserID(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(USER_MAINID, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_MAINID, editable).commit()
        }

        fun getMainUserID(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(USER_MAINID, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_MAINID, "")!!
        }

        fun setStoreIcon(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(STORE_ICON, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_MAINID, editable).commit()
        }

        fun getStoreIcon(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(STORE_ICON, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_MAINID, "")!!
        }

        fun setStoreBeaconName(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences("BeaconName", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("BeaconName", editable).commit()
        }

        fun getStoreBeaconName(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("BeaconName", Context.MODE_PRIVATE)
            return sharedPreferences.getString("BeaconName", "")!!
        }


        fun setStoreBeaconAddress(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences("BeaconAddress", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("BeaconAddress", editable).commit()
        }

        fun getStoreBeaconAddress(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("BeaconAddress", Context.MODE_PRIVATE)
            return sharedPreferences.getString("BeaconAddress", "")!!
        }

        fun setStoreBeaconCallNo(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences("BeaconCallNo", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("BeaconCallNo", editable).commit()
        }

        fun getStoreBeaconCallNo(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("BeaconCallNo", Context.MODE_PRIVATE)
            return sharedPreferences.getString("BeaconCallNo", "")!!
        }


        fun setStoreBeacon(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences("BeaconNo", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("BeaconNo", editable).commit()
        }

        fun getStoreBeacon(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("BeaconNo", Context.MODE_PRIVATE)
            return sharedPreferences.getString("BeaconNo", "")!!
        }


        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(NOTIFICATION, Context.MODE_PRIVATE)
        }


        fun saveToSharedPreferences(context: Context, timesatamp: Long, action: String, message: String) {
            getSharedPreferences(context).edit().putString(timesatamp.toString() + "|" + action, message.toString()).apply()

        }

        fun removeAllNotification(context: Context) {
            getSharedPreferences(context).edit().clear().apply()
        }


        fun setAllOffers(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(All_OFFER, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(All_OFFER, editable).commit()
        }

        fun isAllOffers(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(All_OFFER, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(All_OFFER, false)
        }

        fun setClaimOffer(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(CLAIM_OFFER, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(CLAIM_OFFER, editable).commit()
        }

        fun isClaimOffer(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(CLAIM_OFFER, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(CLAIM_OFFER, false)
        }

        fun setMainPanelOffers(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(MAINPANEL_OFFER, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(MAINPANEL_OFFER, editable).commit()
        }

        fun isMainPanelOffers(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(MAINPANEL_OFFER, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(MAINPANEL_OFFER, false)
        }

        fun setClusterePanelOffers(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_OFFER, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(CLUSTER_OFFER, editable).commit()
        }

        fun isClusterePanelOffers(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_OFFER, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(CLUSTER_OFFER, false)
        }


        fun setNotification(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(NOTIFICATION_ON_OFF, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(NOTIFICATION_ON_OFF, editable).commit()
        }

        fun isNotificationOnOff(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(NOTIFICATION_ON_OFF, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(NOTIFICATION_ON_OFF, true)
        }


        fun setLocationUpdate(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(UPDATE_LOCATION, editable).commit()
        }

        fun isLocationUpdate(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(UPDATE_LOCATION, false)
        }







        fun setTutorialUpdate(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(TUTORIAL, editable).commit()
        }

        fun isTutorialUpdate(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(TUTORIAL, false)
        }

        fun setTutorialTrending(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(TUTORIAL_TRENDING, editable).commit()
        }

        fun isTutorialTrending(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(TUTORIAL_TRENDING, false)
        }

        fun setTutorialMap(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(TUTORIAL_TRENDING, editable).commit()
        }

        fun isTutorialMap(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(TUTORIAL_TRENDING, false)
        }

        fun setTutorialParking(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(TUTORIAL_TRENDING, editable).commit()
        }

        fun isTutorialParking(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences(UPDATE_LOCATION, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(TUTORIAL_TRENDING, false)
        }

        fun setShowTrending(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("showTrending", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("showTrending", editable).commit()
        }

        fun getShowTrending(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("showTrending", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("showTrending", false)
        }

        fun setCategoryCodeForOffer(context: Context, code : String){
            var sharedPreferences = context.getSharedPreferences("cat_code_cat", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("cat_code_cat", code).commit()
        }

        fun getCatCodeOffers(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("cat_code_cat", Context.MODE_PRIVATE)
            return sharedPreferences.getString("cat_code_cat","")!!
        }
        fun setClusterIdForOffer(context: Context, code : String){
            var sharedPreferences = context.getSharedPreferences("cat_cluster_cat", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("cat_cluster_cat", code).commit()
        }

        fun getClusterIdForOffers(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("cat_cluster_cat", Context.MODE_PRIVATE)
            return sharedPreferences.getString("cat_cluster_cat","")!!
        }

        fun setFromMall(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("setFrommall", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("setFrommall", editable).commit()
        }

        fun getFromMall(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("setFrommall", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("setFrommall", false)
        }

        fun OkClicked(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("OkClicked", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("OkClicked", editable).commit()
        }

        fun getOkClicked(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("OkClicked", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("OkClicked", false)
        }

        fun OkClickedMall(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("OkClickedMall", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("OkClickedMall", editable).commit()
        }

        fun getOkClickedMall(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("OkClickedMall", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("OkClickedMall", false)
        }

        fun OkClickedMarket(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("OkClickedMarket", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("OkClickedMarket", editable).commit()
        }

        fun getOkClickedMarket(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("OkClickedMarket", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("OkClickedMarket", false)
        }

        fun OkClickedBrand(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("OkClickedBrand", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("OkClickedBrand", editable).commit()
        }

        fun getOkClickedBrand(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("OkClickedBrand", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("OkClickedBrand", false)
        }

        fun setFromMarket(context: Context, editable: Boolean) {
            var sharedPreferences = context.getSharedPreferences("FromMarket", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("FromMarket", editable).commit()
        }

        fun getFromMarket(context: Context): Boolean {
            var sharedPreferences = context.getSharedPreferences("FromMarket", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("FromMarket", false)
        }

        fun setOfferJson(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences("OfferJson", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("OfferJson", editable).commit()
        }

        fun getOfferJson(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("OfferJson", Context.MODE_PRIVATE)
            return sharedPreferences.getString("OfferJson", "")!!
        }

        fun setOfferJsonList(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences("OfferJsonList", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("OfferJsonList", editable).commit()
        }

        fun getOfferJsonList(context: Context): String {
            var sharedPreferences = context.getSharedPreferences("OfferJsonList", Context.MODE_PRIVATE)
            return sharedPreferences.getString("OfferJsonList", "")!!
        }

        val currentTime: String
            get() {
                var sdf = SimpleDateFormat("HH:mm:ss")
                var date = Date()
                return sdf.format(date)

            }

        val date: String
            get() {

                var c = Calendar.getInstance().time
                // println("Current time => $c")
                var df = SimpleDateFormat("yyyy-MM-dd")
                return df.format(c)
            }

        fun dateConvert(normal_date: String): String {
            var startDateStrNewFormat = ""

            //        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            var inputFormat = SimpleDateFormat("yyyy-MM-dd ")
            var outputFormat = SimpleDateFormat("dd MMM, yyyy")
            var date: Date? = null
            try {
                date = inputFormat.parse(normal_date)
                startDateStrNewFormat = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return startDateStrNewFormat
        }



        fun hideKeyboard(activity: Activity) {
            var imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun deleteCache(context: Context) {
            try {
                val dir = context.cacheDir
                deleteDir(dir)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun setParkingLocation(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(OUTSIDE_PARKING_LOCATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(OUTSIDE_PARKING_LOCATION, editable).commit()
        }

        fun getParkingLocation(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(OUTSIDE_PARKING_LOCATION, Context.MODE_PRIVATE)
            return sharedPreferences.getString(OUTSIDE_PARKING_LOCATION, "")!!
        }

        fun setCouponCode(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(COUPON, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(COUPON, editable).commit()
        }

        fun getCouponCode(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(COUPON, Context.MODE_PRIVATE)
            return sharedPreferences.getString(COUPON, "")!!
        }

        fun setStoreKeywordsFileDate(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(SEARCH_KEYWORDS_DATE, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(SEARCH_KEYWORDS_DATE, editable).commit()
        }

        fun getStoreKeywordsFileDate(context: Context): String {
            var sharedPreferences = context.getSharedPreferences(SEARCH_KEYWORDS_DATE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(SEARCH_KEYWORDS_DATE, "")!!
        }


        fun deleteDir(dir: File?): Boolean {
            if (dir != null && dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
                return dir.delete()
            } else return if (dir != null && dir.isFile) {
                dir.delete()
            } else {
                false
            }
        }
    }

    private fun isDiscountActive(delivery_date: String?): Boolean {

        var current_date = Pref_manager.date

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var date1: Date
        var date2: Date
        if (delivery_date != null) {
            try {
                date1 = sdf.parse(current_date)
                date2 = sdf.parse(delivery_date)
                if (date1.after(date2)) {
                    return false
                } else if (date1 == date2 || date1.before(date2)) {
                    return true
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }


        }
        return false
    }




}
