package org.unyde.mapintegrationlib.InternalNavigation

import android.app.Activity
import android.net.Uri
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.lang.Float
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.unyde.mapintegrationlib.ApplicationContext
import org.unyde.mapintegrationlib.InternalNavigation.demo.MainSceneLoader
import org.unyde.mapintegrationlib.InternalNavigation.demo.SceneLoader
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.Marker_Internal_Nav
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.mapview.RouteLayer
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelRenderer
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.database.entity.PathNode
import org.unyde.mapintegrationlib.interfaces.FloorClickListner
import org.unyde.mapintegrationlib.util.Constants


class Cluster3DMap(internal var activity: AppCompatActivity, internal var glView: ModelSurfaceView, var callback: SceneLoader.Callback, var floor_pop_list: RecyclerView, var floorClickListner: FloorClickListner, var calorieCallback: CalorieStepsCallback?, var clusterId: String) {


    var renderer: ModelRenderer? = null
    var routeLayer: RouteLayer? = null
    var path_node_array: Array<String>? = null
    val paramUri: Uri? = null
    val paramType: Int = -1
    var init_scene_animation: Boolean = true
    internal var dbNode: List<PathNode>? = null
    private var destination_beacon_site_id: String? = null
    private var destination_store_name: String? = null
    private var destination_floor_level: String? = null
    private var destination_cordinate_x: String? = null
    private var destination_cordinate_y: String? = null
    private var destination_cordinate_z: String? = null
    //internal var calorieCallback: CalorieStepsCallback? = null

    var temp_connection_list_source: java.util.HashMap<String, List<String>>? = null
    var temp_connection_list_destination: java.util.HashMap<String, List<String>>? = null


    companion object {
        val TAG = "Cluster3DMap"
        var scene: SceneLoader? = null
        var mActionMode: IndoorMode? = null
        var pan_value: kotlin.Float? =2f
    }

    fun init() {

        mActionMode = IndoorMode.NORMAL
        routeLayer = RouteLayer()

        scene = MainSceneLoader(activity, paramUri, paramType, glView, callback, clusterId)
        scene!!.init()
        renderer = ModelRenderer(glView, activity.applicationContext)

    }


    fun show3DMap(floor: Int?) {
        try {
            if (Constants.floor_model.size > 0) {
                scene!!.camera_im_animation_played_status = false
                //shownFloorMap = floor.toString()
                for (i in 0 until Constants.floor_model!!.size) {
                    if (floor == Constants.floor_model.get(i).index) {
                        scene!!.delete_object_by_class("pin_i_m_here");
                        scene!!.objects.clear()
                        scene!!.isdeleted_marker = true;
                        scene!!.addObject(Constants.floor_bg);
                        //scene!!.addObject(Constants.floor_model.get(i))
                        scene!!.selected_floor_object = Constants.floor_model.get(i)
                        scene!!.loadFloorImage(Constants.floor_model.get(i).path,""+floor,""+clusterId)
                        // scene!!.addObject(scene!!.scene_bg_plane)
                        // if (init_scene_animation) {
                        //  init_scene_animation = false
                        if (mActionMode == IndoorMode.NORMAL) {
                           // scene!!.set_camera_animation1()
                        }
                        break
                        // }
                    }
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun instruction_path( x:kotlin.Float, y:kotlin.Float, z:kotlin.Float,isInstructionPathmarker:Boolean) {

        scene!!.create_instruction_path(x,y,z,isInstructionPathmarker)
    }


    fun set_I_m_here_Markers(marker_i_m_here: ArrayList<Marker_Internal_Nav>) {

        val handler2 = Handler(Looper.getMainLooper())
        handler2.postDelayed({
             scene!!.setImHereMarker(marker_i_m_here)
        }, 1)
    }


    fun getDirection(source_beacon_id: String, destination_beacon_id: String, source_beacon_floor: String) {
        // remove_obj_by_class("pin");
        // mActionMode = IndoorMode.DIRECTION
        try {
            show3DMap(source_beacon_floor!!.toInt())
            // setStoreMarkers(source_beacon_floor!!.toInt())
            temp_connection_list_source = HashMap()
            temp_connection_list_destination = HashMap()
            destination_data(destination_beacon_id)
            getConnectionList(source_beacon_floor!!.toInt())
            routeLayer!!.node_connection_list = temp_connection_list_source
            routeLayer!!.setGraph();

            if (source_beacon_floor!!.toInt() == destination_floor_level!!.toInt()) {

                routeLayer!!.getpathforsame_floor(source_beacon_floor, source_beacon_id, destination_beacon_id)

            } else {
                setConnectionList_dest(destination_floor_level!!.toInt())
                routeLayer!!.node_connection_list_dest = temp_connection_list_destination
                routeLayer!!.setGraph1()
                val liftnodelist_source = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList().findLiftnodeByFloor(source_beacon_floor, "Lift",clusterId)
                val liftnodelist_dest = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList().findLiftnodeByFloor(destination_floor_level, "Lift",clusterId)
                routeLayer!!.getpathfordifferent_floor(source_beacon_floor, source_beacon_id, liftnodelist_source, liftnodelist_dest, destination_beacon_id, destination_floor_level)

            }
            val distict_floor_array = ArrayList<String>()
            distict_floor_array!!.add(source_beacon_floor)
            distict_floor_array!!.add(destination_floor_level!!)
            scene!!.distict_floor_array=distict_floor_array
            show3DMapNavigation(source_beacon_floor!!.toInt())
        } catch (e: Exception) {
            if(!source_beacon_id.equals(destination_beacon_id))
            {
                Log.e("Cluster3DMapDirection", e.message)
                calorieCallback!!.onCalorieSteps("","", null, null, null)
            }

        }


    }

    private fun destination_data(destination_beacon_id: String) {

        try {
            dbNode = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList().findById(destination_beacon_id,clusterId)
            if (dbNode != null && dbNode!!.size != 0) {
                destination_beacon_site_id = dbNode!!.get(0).getSite_id()
                destination_store_name = dbNode!!.get(0).getStore_name()
                destination_cordinate_x = dbNode!!.get(0).getSite_map_coord_x()
                destination_cordinate_y = dbNode!!.get(0).getSite_map_coord_y()
                destination_cordinate_z = dbNode!!.get(0).getSite_map_coord_z()
                destination_floor_level = dbNode!!.get(0).getFloor_level()
            }
        } catch (e: Exception) {

        }


    }

    private fun getConnectionList(floor_level: Int) {
        try {
            val node = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList().findByFloor_level(floor_level,clusterId)

            val nodeConnection = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeConnectList().findByFloorLevel(floor_level,clusterId)
            temp_connection_list_source!!.clear()
            for (i in nodeConnection.indices) {
                val key = nodeConnection[i].site_id
                if (temp_connection_list_source!!.containsKey(key)) {
                    val temp_list = temp_connection_list_source!![key]
                    (temp_list!! as java.util.ArrayList).add(nodeConnection[i].site_id_connect)
                    temp_connection_list_source!![key] = temp_list
                } else {
                    val temp_list = java.util.ArrayList<String>()
                    temp_list.add(nodeConnection[i].site_id_connect)
                    temp_connection_list_source!![key] = temp_list
                }
            }
        } catch (e: Exception) {

        }


    }


    private fun setConnectionList_dest(floor_level: Int) {
        try {
            val nodeConnection =DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeConnectList().findByFloorLevel(floor_level,clusterId)
            temp_connection_list_destination!!.clear()
            for (i in nodeConnection.indices) {
                val key = nodeConnection[i].site_id
                if (temp_connection_list_destination!!.containsKey(key)) {
                    val temp_list = temp_connection_list_destination!![key]
                    (temp_list!! as java.util.ArrayList).add(nodeConnection[i].site_id_connect)
                    temp_connection_list_destination!![key] = temp_list

                } else {
                    val temp_list = java.util.ArrayList<String>()
                    temp_list.add(nodeConnection[i].site_id_connect)
                    temp_connection_list_destination!![key] = temp_list
                }
            }
        } catch (e: Exception) {

        }

    }

    fun show3DMapNavigation(floor: Int?) {
        try {
            var allNode: List<PathNode>

            val allMarker = ArrayList<Marker_Internal_Nav>()

            routeLayer!!.path_node_array = routeLayer!!.selected_floor_path_mapping[Integer.parseInt(floor.toString())]!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


            for (i in 0 until routeLayer!!.path_node_array!!.size) {
                allNode = DatabaseClient.getInstance(ApplicationContext.get().applicationContext)!!.db!!.pathNodeList().findById(routeLayer!!.path_node_array[i],clusterId)
                if (allNode!!.size > 0) {
                    // allMarker!!.add(Marker_Internal_Nav(java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_x != "") allNode!!.get(0).site_map_coord_x else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_y != "") allNode!!.get(0).site_map_coord_y else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_z != "") allNode!!.get(0).site_map_coord_z else "0")!!))
                    allMarker!!.add(Marker_Internal_Nav(java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_x != "") allNode!!.get(0).site_map_coord_x else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_y != "") allNode!!.get(0).site_map_coord_y else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_z != "") allNode!!.get(0).site_map_coord_z else "0")!!, allNode!!.get(0).store_id!!, allNode!!.get(0).floor_name!!, allNode!!.get(0).site_id!!, allNode!!.get(0).store_name))
                }
            }
            scene!!.draw_path(allMarker)

            var path_node_array_1: Array<String>? = null
            if (routeLayer!!.selected_floor_path_mapping.size > 0) {
                for ((k, v) in routeLayer!!.selected_floor_path_mapping) {
                    println("$k = $v")
                    path_node_array_1 = "$v"!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                }
                path_node_array=path_node_array_1
            }


            var totCalorie = routeLayer!!.tot_calorie.toInt()
            var totSteps = routeLayer!!.tot_steps
            var instruction_list = routeLayer!!.instruction_list
            var instruction_site_list = routeLayer!!.instruction_site_list
            var instruction_direction_list = routeLayer!!.instruction_direction_list
            calorieCallback!!.onCalorieSteps(totCalorie.toString(), totSteps.toString(), instruction_list, instruction_site_list, instruction_direction_list)
            if (mActionMode == IndoorMode.DIRECTION) {
                scene!!.set_directional_camera_trigger()
            } else if (mActionMode == IndoorMode.NAVIGATION) {
                scene!!.setthirdpersoncamera()
            }

        } catch (e: java.lang.Exception) {
            Log.e("Clauste3D", ""+e.message)
            calorieCallback!!.onCalorieSteps("","", null, null, null)

        }
    }


    fun setStoreMarkers(floor: Int,store_marker:ArrayList<Marker_Internal_Nav>) {
        try {
            var marker = store_marker

            val handler = Handler(Looper.getMainLooper())
           // scene!!.setMarker(marker, floor)
            handler.postDelayed({
                scene!!.setMarker(marker, floor)
            }, 100)

            Handler().postDelayed({

            }, 100)

          /*  if (mActionMode == IndoorMode.NORMAL) {

                val floor_data = MyApplication.get()!!.db!!.floor_data().findById(floor)
                if (!(floor_data.size == 0)) {
                    for (i in 0 until floor_data.size) {
                        marker!!.add(Marker_Internal_Nav(java.lang.Float.valueOf(if (floor_data.get(i).cordinate_x != "") floor_data.get(i).cordinate_x else "0")!!, java.lang.Float.valueOf(if (floor_data.get(i).cordinate_y != "") floor_data.get(i).cordinate_y else "0")!!, java.lang.Float.valueOf(if (floor_data.get(i).cordinate_z != "") floor_data.get(i).cordinate_z else "0")!!, "" + floor_data.get(i).store_id, "N", floor_data.get(i).site_id, ""))
                    }
                }


                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    scene!!.setMarker(marker, floor)
                }, 100)

                      Handler().postDelayed({

                      }, 100)

            } else if (mActionMode == IndoorMode.DIRECTION || mActionMode == IndoorMode.NAVIGATION) {

                val handler1 = Handler(Looper.getMainLooper())

                handler1.postDelayed({
                    scene!!.setMarker(marker, floor)
                }, 100)
                 Handler().postDelayed({
                     scene!!.setMarker(marker, floor)
                 }, 100)
            }*/

        } catch (e: Exception) {
            Log.e("Cluster3DMap:", "" + e.message)
        }
    }




/*
    fun show3DMapNavigation(floor: Int?) {
        try {
            var allNode: List<PathNode>

            val allMarker = ArrayList<Marker_Internal_Nav>()

            routeLayer!!.path_node_array = routeLayer!!.selected_floor_path_mapping[Integer.parseInt(floor.toString())]!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


            for (i in 0 until routeLayer!!.path_node_array!!.size) {
                allNode = MyApplication.get()!!.db!!.pathNodeList().findById(routeLayer!!.path_node_array[i],clusterId)
                if (allNode!!.size > 0) {
                    // allMarker!!.add(Marker_Internal_Nav(java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_x != "") allNode!!.get(0).site_map_coord_x else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_y != "") allNode!!.get(0).site_map_coord_y else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_z != "") allNode!!.get(0).site_map_coord_z else "0")!!))
                    allMarker!!.add(Marker_Internal_Nav(java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_x != "") allNode!!.get(0).site_map_coord_x else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_y != "") allNode!!.get(0).site_map_coord_y else "0")!!, java.lang.Float.valueOf(if (allNode!!.get(0).site_map_coord_z != "") allNode!!.get(0).site_map_coord_z else "0")!!, allNode!!.get(0).store_id!!, allNode!!.get(0).floor_name!!, allNode!!.get(0).site_id!!, allNode!!.get(0).store_name))
                }
            }
            scene!!.draw_path(allMarker)



            var path_node_array_1: Array<String>? = null
            if (routeLayer!!.selected_floor_path_mapping.size > 0) {
                for ((k, v) in routeLayer!!.selected_floor_path_mapping) {
                    println("$k = $v")
                    path_node_array_1 = "$v"!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                }
                path_node_array=path_node_array_1
            }






            var totCalorie = routeLayer!!.tot_calorie.toInt()
            var totSteps = routeLayer!!.tot_steps
            var instruction_list = routeLayer!!.instruction_list
            var instruction_site_list = routeLayer!!.instruction_site_list
            var instruction_direction_list = routeLayer!!.instruction_direction_list
            calorieCallback!!.onCalorieSteps(totCalorie.toString(), totSteps.toString(), instruction_list, instruction_site_list, instruction_direction_list)
            if (mActionMode == IndoorMode.DIRECTION) {
               // scene!!.set_directional_camera_trigger()
            } else if (mActionMode == IndoorMode.NAVIGATION) {
                scene!!.setthirdpersoncamera()
            }
            //

        } catch (e: java.lang.Exception) {
            Log.e("Clauste3D", ""+e.message)
            calorieCallback!!.onCalorieSteps("","", null, null, null)

        }
    }





    fun setStoreMarkers(floor: Int) {
        try {
            var marker = ArrayList<Marker_Internal_Nav>()
            marker!!.clear()
            if (mActionMode == IndoorMode.NORMAL) {

                val floor_data = MyApplication.get()!!.db!!.floor_data().findById(floor)
                if (!(floor_data.size == 0)) {
                    for (i in 0 until floor_data.size) {
                        marker!!.add(Marker_Internal_Nav(java.lang.Float.valueOf(if (floor_data.get(i).cordinate_x != "") floor_data.get(i).cordinate_x else "0")!!, java.lang.Float.valueOf(if (floor_data.get(i).cordinate_y != "") floor_data.get(i).cordinate_y else "0")!!, java.lang.Float.valueOf(if (floor_data.get(i).cordinate_z != "") floor_data.get(i).cordinate_z else "0")!!, "" + floor_data.get(i).store_id, "N", floor_data.get(i).site_id, ""))
                    }
                }


                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    scene!!.setMarker(marker, floor)
                }, 100)

          *//*      Handler().postDelayed({

                }, 100)
*//*
            } else if (mActionMode == IndoorMode.DIRECTION || mActionMode == IndoorMode.NAVIGATION) {


                val handler1 = Handler(Looper.getMainLooper())

                handler1.postDelayed({
                    scene!!.setMarker(marker, floor)
                }, 100)
               *//* Handler().postDelayed({
                    scene!!.setMarker(marker, floor)
                }, 100)*//*
            }


        } catch (e: Exception) {
            Log.e("Cluster3DMap:", "" + e.message)
        }
    }

    fun setFloorHighlighted(floor: Int) {
        try {

            //val floor_list = MyApplication.get()!!.db!!.filePathMain().getFloor(Helper.hex2decimal(Pref_manager.getBECONID(MyApplication.instance.applicationContext).substring(8, 13)).toString())
            val floor_list = MyApplication.get()!!.db!!.filePathMain().getFloor(clusterId)
            // val floor_list = MyApplication.get()!!.db!!.filePth().getFloor(Helper.hex2decimal(Pref_manager.getBECONID(MyApplication.instance.applicationContext).substring(8, 13)).toString())
            val distict_floor_array = ArrayList<String>()
            val Current_floor = ArrayList<Int>()
            val distict_floor_data = MyApplication.get()!!.db!!.floor_data().findDistictFloor()
            Current_floor?.clear()
            distict_floor_array?.clear()

            if (!(distict_floor_data?.size == 0)) {
                for (j in 0 until distict_floor_data!!.size) {
                    distict_floor_array!!.add(distict_floor_data.get(j).floor_level.toString())
                }
            }
            if (!(distict_floor_data?.size == 0)) {
                for (j in 0 until floor_list!!.size) {

                    if (distict_floor_array!!.contains(floor_list!!.get(j).floor_number.toString())) {
                        Current_floor.add(1)
                    } else {
                        Current_floor.add(0)
                    }
                }
            }
            floor_pop_list?.adapter = FlorsRecAdapter(fragment!!, floor_list, floor, Current_floor, floorClickListner)

            if (mActionMode != IndoorMode.NAVIGATION) {
                var floyt = FlorsRecAdapter2(fragment!!, floor_list, 101, Current_floor, Constants.distict_floor_store_count_array, floorClickListner)
                floor_pop_list2?.adapter = floyt
                floyt.notifyDataSetChanged()
            }


        } catch (e: Exception) {

        }

    }


    fun setFloorHighlighted1(floor: Int, floor_distinct_list: ArrayList<String>, floor_distinct_store_count_list: java.util.ArrayList<FloorCountPojo>, dest_floor: Int) {
        try {
            val floor_list = MyApplication.get()!!.db!!.filePathMain().getFloor(clusterId)
            //val floor_list = MyApplication.get()!!.db!!.filePathMain().getFloor(Helper.hex2decimal(Pref_manager.getBECONID(MyApplication.instance.applicationContext).substring(8, 13)).toString())
            //val floor_list = MyApplication.get()!!.db!!.filePth().getFloor(Helper.hex2decimal(Pref_manager.getBECONID(MyApplication.instance.applicationContext).substring(8, 13)).toString())
            val Current_floor = ArrayList<Int>()
            Current_floor?.clear()

            for (j in 0 until floor_list!!.size) {

                if (floor_distinct_list!!.contains(floor_list!!.get(j).floor_number.toString())) {
                    Current_floor.add(1)
                } else {
                    Current_floor.add(0)
                }
            }

            if (mActionMode == IndoorMode.NAVIGATION || mActionMode == IndoorMode.DIRECTION) {
                floor_pop_list?.adapter = FlorsRecAdapter3(fragment!!, floor_list, floor, Current_floor, floorClickListner, dest_floor)
            } else {
                floor_pop_list?.adapter = FlorsRecAdapter(fragment!!, floor_list, floor, Current_floor, floorClickListner)
            }



            floor_pop_list2?.adapter = FlorsRecAdapter2(fragment!!, floor_list, 101, Current_floor, Constants.distict_floor_store_count_array, floorClickListner)
        } catch (e: Exception) {

        }


    }


    fun getDirection(source_beacon_id: String, destination_beacon_id: String, source_beacon_floor: String) {
        // remove_obj_by_class("pin");
        // mActionMode = IndoorMode.DIRECTION
        try {
            show3DMap(source_beacon_floor!!.toInt())
           // setStoreMarkers(source_beacon_floor!!.toInt())
            temp_connection_list_source = HashMap()
            temp_connection_list_destination = HashMap()
            destination_data(destination_beacon_id)
            getConnectionList(source_beacon_floor!!.toInt())
            routeLayer!!.node_connection_list = temp_connection_list_source
            routeLayer!!.setGraph();

            if (source_beacon_floor!!.toInt() == destination_floor_level!!.toInt()) {

                routeLayer!!.getpathforsame_floor(source_beacon_floor, source_beacon_id, destination_beacon_id)

            } else {
                setConnectionList_dest(destination_floor_level!!.toInt())
                routeLayer!!.node_connection_list_dest = temp_connection_list_destination
                routeLayer!!.setGraph1()
                val liftnodelist_source = MyApplication.get()!!.db!!.pathNodeList().findLiftnodeByFloor(source_beacon_floor, "Lift",clusterId)
                val liftnodelist_dest = MyApplication.get()!!.db!!.pathNodeList().findLiftnodeByFloor(destination_floor_level, "Lift",clusterId)
                routeLayer!!.getpathfordifferent_floor(source_beacon_floor, source_beacon_id, liftnodelist_source, liftnodelist_dest, destination_beacon_id, destination_floor_level)

            }
            val distict_floor_array = ArrayList<String>()
            distict_floor_array!!.add(source_beacon_floor)
            distict_floor_array!!.add(destination_floor_level!!)
            scene!!.distict_floor_array=distict_floor_array
            show3DMapNavigation(source_beacon_floor!!.toInt())
            setFloorHighlighted1(source_beacon_floor!!.toInt(), distict_floor_array, Constants.distict_floor_store_count_array, destination_floor_level!!.toInt()!!)
        } catch (e: Exception) {
            if(!source_beacon_id.equals(destination_beacon_id))
            {
                Log.e("Cluster3DMapDirection", e.message)
                calorieCallback!!.onCalorieSteps("","", null, null, null)
            }

        }


    }

    private fun destination_data(destination_beacon_id: String) {

        try {
            dbNode = MyApplication.get()!!.db!!.pathNodeList().findById(destination_beacon_id,clusterId)
            if (dbNode != null && dbNode!!.size != 0) {
                destination_beacon_site_id = dbNode!!.get(0).getSite_id()
                destination_store_name = dbNode!!.get(0).getStore_name()
                destination_cordinate_x = dbNode!!.get(0).getSite_map_coord_x()
                destination_cordinate_y = dbNode!!.get(0).getSite_map_coord_y()
                destination_cordinate_z = dbNode!!.get(0).getSite_map_coord_z()
                destination_floor_level = dbNode!!.get(0).getFloor_level()
            }
        } catch (e: Exception) {

        }


    }


    private fun getConnectionList(floor_level: Int) {
        try {
            val node = MyApplication.get()!!.db!!.pathNodeList().findByFloor_level(floor_level,clusterId)

            val nodeConnection = MyApplication.get()!!.db!!.pathNodeConnectList().findByFloorLevel(floor_level,clusterId)
            temp_connection_list_source!!.clear()
            for (i in nodeConnection.indices) {
                val key = nodeConnection[i].site_id
                if (temp_connection_list_source!!.containsKey(key)) {
                    val temp_list = temp_connection_list_source!![key]
                    (temp_list!! as java.util.ArrayList).add(nodeConnection[i].site_id_connect)
                    temp_connection_list_source!![key] = temp_list
                } else {
                    val temp_list = java.util.ArrayList<String>()
                    temp_list.add(nodeConnection[i].site_id_connect)
                    temp_connection_list_source!![key] = temp_list
                }
            }
        } catch (e: Exception) {

        }


    }


    private fun setConnectionList_dest(floor_level: Int) {
        try {
            val nodeConnection = MyApplication.get()!!.db!!.pathNodeConnectList().findByFloorLevel(floor_level,clusterId)
            temp_connection_list_destination!!.clear()
            for (i in nodeConnection.indices) {
                val key = nodeConnection[i].site_id
                if (temp_connection_list_destination!!.containsKey(key)) {
                    val temp_list = temp_connection_list_destination!![key]
                    (temp_list!! as java.util.ArrayList).add(nodeConnection[i].site_id_connect)
                    temp_connection_list_destination!![key] = temp_list

                } else {
                    val temp_list = java.util.ArrayList<String>()
                    temp_list.add(nodeConnection[i].site_id_connect)
                    temp_connection_list_destination!![key] = temp_list
                }
            }
        } catch (e: Exception) {

        }

    }*/


    enum class IndoorMode {
        NORMAL,
        DIRECTION,
        NAVIGATION
    }

    interface CalorieStepsCallback {
        fun onCalorieSteps(calorie: String?, steps: String?, instruction_list: MutableList<String>?, instruction_site_list: MutableList<String>?, instruction_direction_list: MutableList<Int>?)
    }

}
