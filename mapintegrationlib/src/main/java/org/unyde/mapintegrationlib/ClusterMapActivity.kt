package org.unyde.mapintegrationlib

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap
import org.unyde.mapintegrationlib.InternalNavigation.demo.SceneLoader
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.Marker_Internal_Nav
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.database.entity.MallMapMain
import org.unyde.mapintegrationlib.database.entity.PathNode
import org.unyde.mapintegrationlib.interfaces.FloorClickListner
import org.unyde.mapintegrationlib.util.Constants
import org.unyde.mapintegrationlib.util.Pref_manager
import java.lang.Float
import java.util.ArrayList

class ClusterMapActivity : AppCompatActivity(), FloorClickListner, SceneLoader.Callback,
    Cluster3DMap.CalorieStepsCallback {


    ///////////////Source Beacon
    private var source_beacon_id_i_m_here: String? = null
    private var storetypeid: String = ""
    private var source_beacon_siteid_i_m_here: String? = null
    private var source_store_name_i_m_here: String? = null
    private var source_floor_level_i_m_here: String? = null
    private var source_site_type_i_m_here: String? = null
    private var source_site_address_i_m_here: String? = null
    private var source_cordinate_x_i_m_here: String? = null
    private var source_cordinate_y_i_m_here: String? = null
    private var source_cordinate_z_i_m_here: String? = null
    private var source_floor: String? = null

    ///////////////Destination

    private var destination_store: Int = 0
    private var destination_site: String = ""
    private var dest_floor_level: String = ""
    private var destination_store_name: String = ""
    private var destination_store_address: String = ""
    private var destination_floor: String? = null

    ///////////////////////////////Views

    private var gLView: ModelSurfaceView? = null
    private var leftsegment: RecyclerView? = null
    private var leftsegment2: RecyclerView? = null
    var progressDialog: ProgressDialog? = null

    ////////////////////////Map
    var store_info: List<PathNode>? = null


    /////////////////////////////////Common
    var isViaBeacon: Boolean? = false
    internal var sourcedbNode_i_m_here: List<PathNode>? = null
    var cluster_id: String? = null
    var floor_list: List<MallMapMain>? = null
    private var floor: Int = 0
    private val Current_floor = ArrayList<Int>()
    val floor_data_list = ArrayList<String>()
    var cluster3DMap: Cluster3DMap? = null
    var opendialog: Boolean? = true
    private var is_Map_Loaded: Boolean? = false
    private var shownFloorMap: String? = null
    private var marker_i_m_here = ArrayList<Marker_Internal_Nav>()
    internal var locateNode: List<PathNode>? = null
    ////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cluster_map)

        ///////////from Other Activity
        source_beacon_siteid_i_m_here = intent.getStringExtra("source_site_id")
        source_floor_level_i_m_here = intent.getStringExtra("source_floor")
        source_store_name_i_m_here = intent.getStringExtra("source_store_name")
        source_floor = intent.getStringExtra("source_floor")
        destination_site = intent.getStringExtra("destination_site_id")
        dest_floor_level = intent.getStringExtra("destination_floor_level")
        destination_floor = intent.getStringExtra("destination_floor_level")
        destination_store_name = intent.getStringExtra("destination_store_name")
        destination_store_address = intent.getStringExtra("destination_store_address")
        destination_store = intent.getIntExtra("destination_store_id", 0)
        cluster_id = getIntent().getStringExtra("cluster_id")
        isViaBeacon = getIntent().getBooleanExtra("isViaBeacon", false)
        ////////////////////////////

        Constants.i_m_here_marker = null
        floor = source_floor_level_i_m_here!!.toInt()
        shownFloorMap = floor.toString()


        progressDialog = ProgressDialog(ApplicationContext.get().applicationContext);
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Loading Map")
        progressDialog!!.show()

        gLView = findViewById(R.id.glView) as ModelSurfaceView

        cluster3DMap = Cluster3DMap(this, gLView!!, this, leftsegment!!, leftsegment2!!, this, this, cluster_id!!)
        cluster3DMap!!.init()


    }

    private fun source_data_i_m_here(store_id: String) {
        sourcedbNode_i_m_here =
            DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList()
                .findById(store_id, cluster_id)
        if (sourcedbNode_i_m_here!!.size != 0) {
            source_beacon_id_i_m_here = sourcedbNode_i_m_here!!.get(0).getStore_id()
            source_beacon_siteid_i_m_here = sourcedbNode_i_m_here!!.get(0).getSite_id()
            source_store_name_i_m_here = sourcedbNode_i_m_here!!.get(0).getStore_name()
            source_site_address_i_m_here = sourcedbNode_i_m_here!!.get(0).address
            storetypeid = sourcedbNode_i_m_here!!.get(0).store_type

            source_cordinate_x_i_m_here = sourcedbNode_i_m_here!!.get(0).getSite_map_coord_x()
            source_cordinate_y_i_m_here = sourcedbNode_i_m_here!!.get(0).getSite_map_coord_y()
            source_cordinate_z_i_m_here = sourcedbNode_i_m_here!!.get(0).getSite_map_coord_z()
            source_floor_level_i_m_here = sourcedbNode_i_m_here!!.get(0).getFloor_level()
            source_site_type_i_m_here = sourcedbNode_i_m_here!!.get(0).getSite_type()


            if (store_id == destination_site) {
                ///Pref_manager.customToastNew(this@Cluster3DLocateMapActivity!!, "You have reached your destination.", "")
                if (opendialog == true) {
                    opendialog = false
                    if (storetypeid.toInt() != 1) {
                        /*  var prodInNostore = Intent(this@Cluster3DLocateMapActivity!!, NoStoreActivityTwo::class.java)
                          val gson = GsonBuilder().disableHtmlEscaping().create()
                          prodInNostore.putExtra("Store_name", source_store_name_i_m_here);
                          prodInNostore.putExtra("Store_address", source_site_address_i_m_here);
                          prodInNostore.putExtra("Store_id", source_beacon_id_i_m_here);
                          prodInNostore.putExtra("show_btn", "" + 0);
                          prodInNostore.putExtra("no_navigation", "" + 1);
                          prodInNostore.putExtra("allData", "")
                          prodInNostore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          prodInNostore.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                          startActivity(prodInNostore)*/
                    } else {
                        /* var prodIn = Intent(this@Cluster3DLocateMapActivity, StorePageForReachedDestination::class.java)
                         prodIn.putExtra("Store_name", source_store_name_i_m_here);
                         prodIn.putExtra("Store_address", source_site_address_i_m_here);
                         prodIn.putExtra("Store_id", source_beacon_id_i_m_here);
                         prodIn.putExtra("show_btn", "" + 0);
                         prodIn.putExtra("no_navigation", "" + 1);
                         prodIn.putExtra("allData", "")
                         startActivity(prodIn)*/

                    }
                    Cluster3DMap.mActionMode == Cluster3DMap.IndoorMode.NORMAL
                    finish()

                }


            }
            if (this!!.is_Map_Loaded == true) {
                if (source_floor_level_i_m_here!!.toInt() == shownFloorMap!!.toInt()) {
                    //   Log.e(TAG, source_floor_level_i_m_here + " " + floor)
                    marker_i_m_here.clear()
                    marker_i_m_here!!.add(
                        Marker_Internal_Nav(
                            Float.valueOf((if (source_cordinate_x_i_m_here != "") source_cordinate_x_i_m_here else "0")!!)!!,
                            Float.valueOf((if (source_cordinate_y_i_m_here != "") source_cordinate_y_i_m_here else "0")!!)!!,
                            Float.valueOf((if (source_cordinate_z_i_m_here != "") source_cordinate_z_i_m_here else "0")!!)!!,
                            source_beacon_id_i_m_here,
                            "N",
                            source_beacon_siteid_i_m_here,
                            source_site_type_i_m_here
                        )
                    )
                    cluster3DMap!!.set_I_m_here_Markers(marker_i_m_here)

                } else {
                    Cluster3DMap.scene!!.delete_object_by_class("pin_i_m_here");
                    Constants.i_m_here_marker.setId(source_beacon_siteid_i_m_here)
                    marker_i_m_here.clear()
                    cluster3DMap!!.set_I_m_here_Markers(marker_i_m_here)
                }
            }

        }


    }

    override fun onFloorItemClick(pos: Int) {

    }

    override fun onStoreClick(store_id: String?, isParking: Boolean?) {
        try {
            is_Map_Loaded = true
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            if (isParking!!) {
            } else {
                if (store_id.equals("", true)) {

                    if (isViaBeacon!!) {
                        cluster3DMap!!.show3DMap(Pref_manager.getFloor_Level(ApplicationContext.get().applicationContext))
                    } else {
                        cluster3DMap!!.show3DMap(source_floor_level_i_m_here!!.toInt())
                    }
                    All_Locate()


                } else {


                }
            }
        } catch (e: Exception) {
            Log.e("3D locate", "" + e.message)
        }
    }

    override fun onStoreChange() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalorieSteps(
        calorie: String?,
        steps: String?,
        instruction_list: MutableList<String>?,
        instruction_site_list: MutableList<String>?,
        instruction_direction_list: MutableList<Int>?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun All_Locate() {
        locateNode =DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList().findAllForLocate("Primary", cluster_id)

        if (locateNode!!.size > 0) {
            shownFloorMap = floor.toString()
            Cluster3DMap.mActionMode = Cluster3DMap.IndoorMode.DIRECTION
            Cluster3DMap.scene!!.destination_floor_number=source_floor
            getDirection()
            source_data_i_m_here(source_beacon_siteid_i_m_here!!)

        }


    }

    fun getDirection() {

        try {

            if (!Cluster3DMap.mActionMode!!.equals(Cluster3DMap.IndoorMode.NAVIGATION)) {
                /*if (nav_bottomsheet?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    nav_bottomsheet?.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    nav_bottomsheet!!.isHideable = true
                    nav_bottomsheet?.setState(BottomSheetBehavior.STATE_HIDDEN);

                }*/
            }
            cluster3DMap!!.getDirection(source_beacon_siteid_i_m_here!!, destination_site, source_floor_level_i_m_here!!)

        } catch (e: Exception) {
            Log.e("3Dlocategetdirection", "" + e.message)
        }

    }



}
