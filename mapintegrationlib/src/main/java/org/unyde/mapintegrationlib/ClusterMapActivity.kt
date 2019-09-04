package org.unyde.mapintegrationlib

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.util.ArrayList

class ClusterMapActivity : AppCompatActivity(), FloorClickListner, SceneLoader.Callback,Cluster3DMap.CalorieStepsCallback {


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
    var cluster3DMap: Cluster3DMap? = null
    private var is_Map_Loaded: Boolean? = false
    var ATTRS: IntArray? = null
    var current_flor: Int = 0
    var mall_text : TextView ?=null
    var mall_address_txt : TextView ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cluster_full_map)
        cluster_id = getIntent().getStringExtra("cluster_id")
        mall_name = getIntent().getStringExtra("mall_name")
        mall_address = getIntent().getStringExtra("mall_address")
        mall_text = findViewById(R.id.mall_name)
        mall_text!!.text=mall_name
        mall_address_txt = findViewById(R.id.mall_address_txt)
        mall_address_txt!!.text=mall_address
        back_button = findViewById(R.id.back_button)
        floors_recycler = findViewById(R.id.floors_recycler)
        ////////////////////////////

        ATTRS = intArrayOf(android.R.attr.listDivider)

        val a = obtainStyledAttributes(ATTRS)
        val divider = a.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen._4sdp)
        val insetDivider = InsetDrawable(divider, resources.getDimensionPixelSize(R.dimen._35sdp), 0, inset, 0)
        a.recycle()
        val itemDecorfloor = DividerItemDecoration(this@ClusterMapActivity, DividerItemDecoration.VERTICAL)
        itemDecorfloor.setDrawable(insetDivider)
        floor_list =DatabaseClient.getInstance(ApplicationContext.get().applicationContext).db.mallMapMain().getFloor(cluster_id!!);

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

        floors_recycler?.layoutManager = LinearLayoutManager(this@ClusterMapActivity, RecyclerView.VERTICAL, false)
        floors_recycler?.adapter = FlorsRecAdapter(this@ClusterMapActivity!!, floor_list, current_flor, Current_floor, this)
        floors_recycler?.addItemDecoration(itemDecorfloor)
        progressDialog = ProgressDialog(ApplicationContext.get().applicationContext);
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Loading Map")
        progressDialog!!.show()
        gLView = findViewById(R.id.glView) as ModelSurfaceView

        cluster3DMap = Cluster3DMap(this, gLView!!, this, floors_recycler!!,  this, this, cluster_id!!)
        cluster3DMap!!.init()


        back_button!!.setOnClickListener {
            finish()
        }

    }


    override fun onFloorItemClick(pos: Int) {
        try {
            floor = floor_list!!.get(pos).floor_number
            cluster3DMap!!.show3DMap(floor)

        } catch (e: Exception) {

        }

    }

    override fun onStoreClick(store_id: String?, isParking: Boolean?) {
        try {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            is_Map_Loaded = true
            cluster3DMap!!.show3DMap(0)
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
