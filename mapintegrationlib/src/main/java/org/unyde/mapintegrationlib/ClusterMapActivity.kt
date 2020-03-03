package org.unyde.mapintegrationlib

import android.Manifest
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.InsetDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balysv.materialripple.MaterialRippleLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.steps_bottom_sheet.*
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap
import org.unyde.mapintegrationlib.InternalNavigation.demo.SceneLoader
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.Marker_Internal_Nav
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView
import org.unyde.mapintegrationlib.adapter.FlorsRecAdapter
import org.unyde.mapintegrationlib.adapter.StepsInstructionAdapter
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.database.entity.MallMapMain
import org.unyde.mapintegrationlib.database.entity.PathNode
import org.unyde.mapintegrationlib.interfaces.FloorClickListner
import org.unyde.mapintegrationlib.util.Constants
import org.unyde.mapintegrationlib.util.Pref_manager
import org.unyde.mapintegrationlib.util.viewpagerAdsCard.AnchorBottomSheetBehavior
import java.lang.Float
import java.util.*
import kotlin.collections.HashMap

class ClusterMapActivity : AppCompatActivity(), FloorClickListner, SceneLoader.Callback,
    Cluster3DMap.CalorieStepsCallback {


    private var gLView: ModelSurfaceView? = null
    private var floors_recycler: RecyclerView? = null
    private var back_button: ImageView? = null
    var progressDialog: ProgressDialog? = null
    internal var w: Window? = null
    var cluster_id: String? = null
    var mall_name: String? = null
    var mall_address: String? = null
    var floor_list: List<MallMapMain>? = null
    private var floor: Int = 0
    private val Current_floor = ArrayList<Int>()
    val floor_data_list = ArrayList<String>()
    val mall_brand = ArrayList<String>()
    var cluster3DMap: Cluster3DMap? = null
    private var is_Map_Loaded: Boolean? = false
    var ATTRS: IntArray? = null
    var current_flor: Int = 0
    var mall_text: TextView? = null
    var mall_address_txt: TextView? = null
    var mall_stores: String? = "("

    private var wifiManager: WifiManager?=null
    private var results:List<ScanResult>?=null
    private var arrayList = ArrayList<String>()
    private var myMultiMap = HashMap<String, List<Int>>()
    private var mydev_name = HashMap<String, String>()
    private var mydevsignal_mapper = HashMap<String, Int>()
    //  private var progressBar: ProgressBar?=null
    private var scan_mode = false
    private var count = 0
    var gson: Gson?=null
    var root: Root?=null
    var json_val = "{\n" +
            "\"point_list\": [\n" +
            "\n" +
            "{\n" +
            "\"name\": \"chaayos\",\n" +
            "\"signal_val\": [{\n" +
            "\"hw_id\": \"04:95:e6:22:bb:71\",\n" +
            "\"hw_name\": \"Unyde_Win\",\n" +
            "\"hw_value\": \"-81\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"40:9b:cd:0d:11:70\",\n" +
            "\"hw_name\": \"Unyde_Core\",\n" +
            "\"hw_value\": \"-81\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"64:70:02:8a:d8:da\",\n" +
            "\"hw_name\": \"UNYDE_DMZ\",\n" +
            "\"hw_value\": \"-77\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"18:0f:76:35:f8:e8\",\n" +
            "\"hw_name\": \"Unyde-Conference\",\n" +
            "\"hw_value\": \"-53\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"9c:7d:a3:09:b8:98\",\n" +
            "\"hw_name\": \"UnydeNetzi-2.4G\",\n" +
            "\"hw_value\": \"-76\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"30:46:9a:a3:61:72\",\n" +
            "\"hw_name\": \"VISION MISSION\",\n" +
            "\"hw_value\": \"-85\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"32:46:9a:a3:61:73\",\n" +
            "\"hw_name\": \"NETGEAR_Guest1\",\n" +
            "\"hw_value\": \"-85\",\n" +
            "\"sample_size\": \"16\"\n" +
            "}\n" +
            "\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"blackberry\",\n" +
            "\"signal_val\": [{\n" +
            "\"hw_id\": \"04:95:e6:22:bb:71\",\n" +
            "\"hw_name\": \"Unyde_Win\",\n" +
            "\"hw_value\": \"-77\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"40:9b:cd:0d:11:70\",\n" +
            "\"hw_name\": \"Unyde_Core\",\n" +
            "\"hw_value\": \"-66\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"64:70:02:8a:d8:da\",\n" +
            "\"hw_name\": \"UNYDE_DMZ\",\n" +
            "\"hw_value\": \"-84\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"18:0f:76:35:f8:e8\",\n" +
            "\"hw_name\": \"Unyde-Conference\",\n" +
            "\"hw_value\": \"-72\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"9c:7d:a3:09:b8:98\",\n" +
            "\"hw_name\": \"UnydeNetzi-2.4G\",\n" +
            "\"hw_value\": \"-72\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"30:46:9a:a3:61:72\",\n" +
            "\"hw_name\": \"VISION MISSION\",\n" +
            "\"hw_value\": \"-84\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"32:46:9a:a3:61:73\",\n" +
            "\"hw_name\": \"NETGEAR_Guest1\",\n" +
            "\"hw_value\": \"-86\",\n" +
            "\"sample_size\": \"16\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"haldiram\",\n" +
            "\"signal_val\": [{\n" +
            "\"hw_id\": \"04:95:e6:22:bb:71\",\n" +
            "\"hw_name\": \"Unyde_Win\",\n" +
            "\"hw_value\": \"-57\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"40:9b:cd:0d:11:70\",\n" +
            "\"hw_name\": \"Unyde_Core\",\n" +
            "\"hw_value\": \"-83\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"64:70:02:8a:d8:da\",\n" +
            "\"hw_name\": \"UNYDE_DMZ\",\n" +
            "\"hw_value\": \"-85\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"9c:7d:a3:09:b8:98\",\n" +
            "\"hw_name\": \"UnydeNetzi-2.4G\",\n" +
            "\"hw_value\": \"-60\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"18:0f:76:35:f8:e8\",\n" +
            "\"hw_name\": \"Unyde-Conference\",\n" +
            "\"hw_value\": \"-80\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"30:46:9a:a3:61:72\",\n" +
            "\"hw_name\": \"VISION MISSION\",\n" +
            "\"hw_value\": \"-81\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"32:46:9a:a3:61:73\",\n" +
            "\"hw_name\": \"NETGEAR_Guest1\",\n" +
            "\"hw_value\": \"-82\",\n" +
            "\"sample_size\": \"16\"\n" +
            "},\n" +
            "{\n" +
            "\"hw_id\": \"78:32:1b:38:8a:57\",\n" +
            "\"hw_name\": \"D-Link VMF\",\n" +
            "\"hw_value\": \"-77\",\n" +
            "\"sample_size\": \"16\"\n" +
            "}\n" +
            "\n" +
            "]\n" +
            "}\n" +
            "\n" +
            "]\n" +
            "\n" +
            "}";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cluster_full_map)
        wifiManager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        // progressBar = findViewById(R.id.progressBar) as ProgressBar
        if (!wifiManager!!.isWifiEnabled())
        {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show()
            wifiManager!!.setWifiEnabled(true)
        }

        scan_mode = true
        count = 0
        myMultiMap.clear()
        mydev_name.clear()
        runnable.run()
        gson = Gson()
        root = Root()
        root = gson!!.fromJson(json_val, Root::class.java)
        cluster_id = getIntent().getStringExtra("cluster_id")
        mall_name = getIntent().getStringExtra("mall_name")
        mall_address = getIntent().getStringExtra("mall_address")
        mall_brand!!.addAll(getIntent().getStringArrayListExtra("mall_brand"))



        mall_text = findViewById(R.id.mall_name)
        mall_text!!.text = mall_name
        mall_address_txt = findViewById(R.id.mall_address_txt)
        mall_address_txt!!.text = mall_address
        back_button = findViewById(R.id.back_button)
        floors_recycler = findViewById(R.id.floors_recycler)
        ////////////////////////////

        ATTRS = intArrayOf(android.R.attr.listDivider)

        val a = obtainStyledAttributes(ATTRS)
        val divider = a.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen._4sdp)
        val insetDivider =
            InsetDrawable(divider, resources.getDimensionPixelSize(R.dimen._35sdp), 0, inset, 0)
        a.recycle()
        val itemDecorfloor =
            DividerItemDecoration(this@ClusterMapActivity, DividerItemDecoration.VERTICAL)
        itemDecorfloor.setDrawable(insetDivider)
        floor_list =
            DatabaseClient.getInstance(ApplicationContext.get().applicationContext).db.mallMapMain()
                .getFloor(cluster_id!!);

        if (!(floor_list!!.size == 0)) {
            for (i in 0 until floor_list!!.size) {
                floor_data_list.add("" + floor_list!!.get(i).floor_alias)
                if (floor_list!!.get(i).floor_number.equals(floor)) {
                    Current_floor.add(1)
                    current_flor = floor
                } else {
                    Current_floor.add(0)
                }
            }
        }

        floors_recycler?.layoutManager =
            LinearLayoutManager(this@ClusterMapActivity, RecyclerView.VERTICAL, false)
        floors_recycler?.adapter = FlorsRecAdapter(
            this@ClusterMapActivity!!,
            floor_list,
            current_flor,
            Current_floor,
            this
        )
        floors_recycler?.addItemDecoration(itemDecorfloor)
        progressDialog = ProgressDialog(this@ClusterMapActivity);
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Loading Map")
        progressDialog!!.show()
        gLView = findViewById(R.id.glView) as ModelSurfaceView

        cluster3DMap =
            Cluster3DMap(this, gLView!!, this, floors_recycler!!, this, this, cluster_id!!)
        cluster3DMap!!.init()




        back_button!!.setOnClickListener {
            finish()
        }

    }
    private val handler = Handler()
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            scanWifi()
            if (scan_mode) {
                handler.postDelayed(this, 2000)
            }
        }
    }
    internal var wifiReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context:Context, intent:Intent) {
            results = wifiManager!!.getScanResults()
            unregisterReceiver(this)
            for (scanResult in results!!)
            {
                if (!myMultiMap.containsKey(scanResult.BSSID))
                {
                    val abc = ArrayList<Int>()
                    abc.add(scanResult.level)
                    myMultiMap.put(scanResult.BSSID, abc)
                    mydev_name.put(scanResult.BSSID, scanResult.SSID)
                }
                else
                {
                    val abc = myMultiMap.get(scanResult.BSSID) as ArrayList<Int>
                    abc.add(scanResult.level)
                    myMultiMap.put(scanResult.BSSID, abc!!)
                }
                arrayList.add(scanResult.SSID + " - " + scanResult.BSSID + " - " + scanResult.level)
                //adapter.notifyDataSetChanged();
            }
            //value.setText(key_val);
        }
    }

    private fun scanWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), "Give Location permission to this application", Toast.LENGTH_SHORT).show()
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        }
        else
        {
            //scanning();
            count++
            //  progressBar!!.setProgress(count * 16)
            //value.setText("Locating....");
            arrayList.clear()
            registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            wifiManager!!.startScan()
            //Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
            if (count > 5)
            {
                //value.setText("Locating.");
                //  progressBar!!.setProgress(count * 16)
                count = 0
                scan_mode = false
                //handler.removeCallbacks(runnable);
                // wifiManager.();
                // Toast.makeText(this, "Getnearest point", Toast.LENGTH_SHORT).show();
                Log.d("Vivek tag", "Getnearest point")
                get_nearest_point()
            }
        }
    }

    private fun get_nearest_point() {
        val itr = myMultiMap.keys.iterator()
        while (itr.hasNext())
        {
            val key = itr.next()
            val value = myMultiMap.get(key)
            //Log.d("Vivek Key",key+"Size:"+value.size());
            //Toast.makeText(getApplicationContext(),key+myMultiMap.keySet().size(),Toast.LENGTH_SHORT).show();
            var sum = 0
            for (i in value!!.indices)
            {
                sum += value!!.get(i)
            }
            mydevsignal_mapper.put(key, sum / value!!.size)
            // Toast.makeText(getApplicationContext(),"Size"+myMultiMap.keySet().size(),Toast.LENGTH_SHORT).show();
        }
        val s = root!!.getPoint_list()
        var nearest_point_status = false
        var landmark_index = -1
        var point_distance = 10000
        var sorted_store = HashMap<Int, Int>()
        var passed_point = HashMap<String, HashMap<String, Int>>()
        for (i in s.indices)
        {
            Log.d("Vivek Key", "Data Set Size:" + s.size)
            val signal_list = root!!.getPoint_list().get(i).getSignal_val()
            nearest_point_status = true
            val hwid_list = HashMap<String, Int>()
            val hwid_nm_list = HashMap<String, Int>()

            for (j in signal_list.indices)
            {
                /// Log.d("Vivek Key","Signal List Size:"+signal_list.size()+""+ root.getPoint_list().get(i).getSignal_val().get(j).getHw_id());
                if (myMultiMap.containsKey(signal_list.get(j).getHw_id()) )//&& ((mydevsignal_mapper.get(signal_list.get(j).getHw_id()))> -70))
                {
                    var diff = signal_list.get(j).getHw_value() - (mydevsignal_mapper.get(signal_list.get(j).getHw_id())!!)
                    //Toast.makeText(getApplicationContext(),""+diff,Toast.LENGTH_SHORT).show();
                    if (diff < 0)
                    {
                        diff = -1 * diff
                    }
                    if (diff <= 15)
                    {
                        point_distance += diff
                        Log.d("Matched", root!!.getPoint_list().get(i).getName() + "!" + diff + "!!" + signal_list.get(j).getHw_id() + "||" + signal_list.get(j).getHw_value() + " || " + (mydevsignal_mapper.get(signal_list.get(j).getHw_id())))
                        hwid_list.put(signal_list.get(j).getHw_id(), diff)
                    }
                    else
                    {
                        Log.d("NotMatched", root!!.getPoint_list().get(i).getName() + "!" + diff + "!!" + signal_list.get(j).getHw_id() + "||" + signal_list.get(j).getHw_value() + " || " + (mydevsignal_mapper.get(signal_list.get(j).getHw_id())))
                        nearest_point_status = false
                    }
                }
            }
            if (nearest_point_status)
            {
                landmark_index = i
                sorted_store.put(point_distance, i)
                Log.d("All Matched", "Checked in" + root!!.getPoint_list().get(landmark_index).getName())
                passed_point.put(root!!.getPoint_list().get(landmark_index).getName(), hwid_list)
                //buttonScan.setText("Checked in"+root.getPoint_list().get(landmark_index).getName());
            }
        }
        var checkedin_store = ""
        var Al3 = ArrayList<String>()
        var prime_set = HashSet<String>()
        if (sorted_store.size > 0)
        {
            // buttonScan.setText(buttonScan.getText()+""+sorted_store.size());
            val sorted = TreeMap<Int, Int>(sorted_store)
            val mappings = sorted.entries
            for (mapping in mappings)
            {
                checkedin_store += (root!!.getPoint_list().get(mapping.value).getName())
                // Log.d("Matched Passed Poirnt",""+mapping.getValue()+""+(root.getPoint_list().get(mapping.getValue()).getName()));
                if (prime_set.size == 0)
                {
                    val te=passed_point.get((root!!.getPoint_list().get(mapping.value).getName()))!!.keys.toMutableSet()
                    prime_set.retainAll(te)
                }
                else
                {
                    val temp_set = passed_point.get((root!!.getPoint_list().get(mapping.value).getName()))!!.keys
                    prime_set.retainAll(temp_set)
                }
            }
        }
        else
        {
            Log.d("Out Side loop", " Not Checked in any store")
            // buttonScan.setText("Finding....");
        }
        Log.d("Matched", "-----------")
        val itr2 = passed_point.keys.iterator()
        val lm_point = HashMap<Int, String>()
        while (itr2.hasNext())
        {
            var key = itr2.next()
            var lmpoint_value = passed_point.get(key)
            var temp_dist = 0
            var itr3 = lmpoint_value!!.keys.iterator()
            var key_temp = ""
            while (itr3.hasNext())
            {
                key_temp = itr3.next()
                if (prime_set.contains(key_temp))
                {
                    temp_dist += lmpoint_value!!.get(key_temp)!!
                    Log.d("Matched", "" + key_temp + "!" + lmpoint_value.get(key_temp))
                    //passed_point.containsValue()
                    //temp_dist =
                }
            }
            Log.d("Matched", "" + temp_dist)
            Log.d("Matched", "" + "------------------------------------" + key + "----")
            lm_point.put(temp_dist, key)
            //buttonScan.setText(buttonScan.getText()+""+key+"!"+temp_dist);
        }
        var sorted1 = TreeMap<Int, String>(lm_point)
        var mappings = sorted1.entries
        var store_marker = ArrayList<Marker_Internal_Nav>()
        store_marker!!.clear()
        Toast.makeText(getApplicationContext(), "Get nearest point", Toast.LENGTH_SHORT).show()

        Log.e("Cluster3DMap:","------VIVEK-----")
        store_marker!!.add(Marker_Internal_Nav(0f,1f,0f,"A","1","B","D"));
        cluster3DMap!!.setStoreMarkers(floor, store_marker)
        for (mapping1 in mappings)
        {
            //root.getPoint_list().get(mapping1.getValue()).getName();
            // buttonScan.setText(mapping1.getValue());
            if (mapping1.value.toString() == "chaayos")
            {
              /*  val openpop = Intent(this@MainActivity, PopUpActivityWifiSignal::class.java)
                openpop.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                openpop.putExtra("brands_name", "Chaayos")
                openpop.putExtra("numstores", "GF,Logix Center")
                openpop.putExtra("offer_count_store", "5")
                openpop.putExtra("coupon_count_txt", "7")*/
                //startActivity(openpop)

            }
            else if (mapping1.value.toString() == "haldiram")
            {
                /*val openpop = Intent(this@MainActivity, PopUpActivityWifiSignal::class.java)
                openpop.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                openpop.putExtra("brands_name", "Haldiram")
                openpop.putExtra("numstores", "GF,Logix Center")
                openpop.putExtra("offer_count_store", "5")
                openpop.putExtra("coupon_count_txt", "7")*/
                //startActivity(openpop)
            }
            else if (mapping1.value.toString() == "blackberry")
            {
               /* val openpop = Intent(this@MainActivity, PopUpActivityWifiSignal::class.java)
                openpop.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                openpop.putExtra("brands_name", "Blackberry")
                openpop.putExtra("numstores", "GF,Logix Center")
                openpop.putExtra("offer_count_store", "5")
                openpop.putExtra("coupon_count_txt", "7")*/
                //startActivity(openpop)
            }
            break
        }
        //buttonScan.setText(checkedin_store);
        //.setText(checkedin_store);
        mydev_name.clear()
        myMultiMap.clear()
        mydevsignal_mapper.clear()
        count = 0
        //progressBar!!.setProgress(count * 16)
        scan_mode = true
    }

    override fun onFloorItemClick(pos: Int) {
        try {
            floor = floor_list!!.get(pos).floor_number
            cluster3DMap!!.show3DMap(floor)
            var markers = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)
                .db.pathNodeList().getCordinatesForMarkers(mall_brand!!, floor.toString());
            var store_marker = ArrayList<Marker_Internal_Nav>()
            store_marker!!.clear()
            if (markers.size > 0) {
                for (i in 0 until markers!!.size) {
                    store_marker!!.add(
                        Marker_Internal_Nav(
                            java.lang.Float.valueOf(
                                if (markers.get(i).site_map_coord_x != "") markers.get(
                                    i
                                ).site_map_coord_x else "0"
                            )!!,
                            java.lang.Float.valueOf(
                                if (markers.get(i).site_map_coord_y != "") markers.get(i).site_map_coord_y else "0"
                            )!!,
                            java.lang.Float.valueOf(
                                if (markers.get(i).site_map_coord_z != "") markers.get(i).site_map_coord_z else "0"
                            )!!,
                            "" + markers.get(i).store_id,
                            "N",
                            markers.get(i).site_id,
                            ""
                        )
                    )
                    Toast.makeText(
                        this@ClusterMapActivity,
                        "" + markers.get(i).store_name,
                        Toast.LENGTH_LONG
                    ).show()
                }
                cluster3DMap!!.setStoreMarkers(floor, store_marker)
            }

        } catch (e: Exception) {

        }

    }

    override fun onStoreClick(store_id: String?, isParking: Boolean?) {
        try {

            if (store_id.equals("")) {
                if (progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                is_Map_Loaded = true
                cluster3DMap!!.show3DMap(0)
                if (mall_brand!!.size > 0) {

                    for (i in 0 until mall_brand!!.size) {
                        Toast.makeText(
                            this@ClusterMapActivity,
                            "" + mall_brand!!.get(i),
                            Toast.LENGTH_LONG
                        ).show()
                        mall_stores = mall_stores + mall_brand!!.get(i) + ","
                    }
                    mall_stores = mall_stores!!.reversed()
                    mall_stores = mall_stores!!.substring(1).reversed() + ")"
                    Toast.makeText(this@ClusterMapActivity, "" + mall_stores, Toast.LENGTH_LONG)
                        .show()
                }

                //  var markers =DatabaseClient.getInstance(ApplicationContext.get().applicationContext).db.pathNodeList().getCordinatesForMarkers1();
                var markers =
                    DatabaseClient.getInstance(ApplicationContext.get().applicationContext)
                        .db.pathNodeList().getCordinatesForMarkers(mall_brand!!, "0");
                var store_marker = ArrayList<Marker_Internal_Nav>()
                store_marker!!.clear()
               /* if (markers.size > 0) {
                    for (i in 0 until markers!!.size) {
                        store_marker!!.add(
                            Marker_Internal_Nav(
                                java.lang.Float.valueOf(
                                    if (markers.get(
                                            i
                                        ).site_map_coord_x != ""
                                    ) markers.get(i).site_map_coord_x else "0"
                                )!!,
                                java.lang.Float.valueOf(
                                    if (markers.get(i).site_map_coord_y != "") markers.get(i).site_map_coord_y else "0"
                                )!!,
                                java.lang.Float.valueOf(
                                    if (markers.get(i).site_map_coord_z != "") markers.get(i).site_map_coord_z else "0"
                                )!!,
                                "" + markers.get(i).store_id,
                                "N",
                                markers.get(i).site_id,
                                ""
                            )
                        )
                        Toast.makeText(
                            this@ClusterMapActivity,
                            "" + markers.get(i).store_name,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    cluster3DMap!!.setStoreMarkers(0, store_marker)
                }*/
                store_marker!!.add(Marker_Internal_Nav(0f,0f,0f,"A","N","B","D"));
            } else {
                var cn = ComponentName(this@ClusterMapActivity, "woogly.unyde.org.wooglyunyde.activities.BrandPageAct");
                var intent = Intent().setComponent(cn);
                intent.putExtra("brand_id",store_id)
                intent.putExtra("brand_name","AND")
                startActivity(intent)
            }

        } catch (e: Exception) {
            Log.e("3D locate", "" + e.message)
        }
    }

    override fun onStoreChange() {
        Log.e("3D locate", "On Store Chnage")
    }

    override fun onCalorieSteps(
        calorie: String?,
        steps: String?,
        instruction_list: MutableList<String>?,
        instruction_site_list: MutableList<String>?,
        instruction_direction_list: MutableList<Int>?
    ) {
        Log.e("3D locate", "Need to implement")
    }


}
