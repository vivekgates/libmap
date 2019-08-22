package org.unyde.mapintegrationlib.worker.map_json_download_worker

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import org.unyde.mapintegrationlib.ApplicationContext
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.navigation.navigation_cluster_data
import org.unyde.mapintegrationlib.MyApplication
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.database.entity.Cluster_Primary_Info
import org.unyde.mapintegrationlib.database.entity.PathNode
import org.unyde.mapintegrationlib.database.entity.PathNode_connet
import org.unyde.mapintegrationlib.util.Pref_manager
import java.io.*
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class MapJsonParseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {


    override fun doWork(): Result {
        val image_name = inputData.getString("Image_Name")
        val file_name = inputData.getString("file_name")
        val city = inputData.getString("city")
        val mall_id = inputData.getString("Mall_Id")

        var json: String? = null
        var gson: Gson? = null
        gson = Gson()


        try {

            var `is` = FileInputStream(File(file_name))
            Log.e("NavigationJson", "" + file_name)
            var size = `is`.available()
            var buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            var charset: Charset = Charsets.UTF_8
            json = String(buffer, charset)

            var n_c_d = gson!!.fromJson(json, navigation_cluster_data::class.java)
            //   var cluster_name = n_c_d.cluster_name
            var floor_list = n_c_d.floor_list

            for (i in floor_list.indices) {
                //   constructor(cluster_id: Int, cluster_name: String,
                //   cluster_orientation: String, floor_level: String,
                //   floor_name: String, floor_map: String,
                //   floor_map_height: String, floor_map_width: String, default_location_site_id: String, default_location_site_name: String) {

                var clusterPrimaryInfo = DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.clusterPrimaryInfo().getAll(n_c_d.cluster_id, floor_list.get(i).floor_level)

                if (clusterPrimaryInfo.size > 0) {
                    DatabaseClient.getInstance( ApplicationContext.get())!!.db!!.clusterPrimaryInfo().delete(floor_list.get(i).floor_level, n_c_d.cluster_id)
                    var cluster_primary_info = Cluster_Primary_Info(n_c_d.cluster_id.toInt(), n_c_d.cluster_name, n_c_d.cluster_orientation,
                        floor_list.get(i).floor_level, floor_list.get(i).floor_name, floor_list.get(i).floor_map, floor_list.get(i).map_height, floor_list.get(i).map_width, floor_list.get(i).default_siteid, floor_list.get(i).default_sitename)


                    /* var cluster_primary_info=Cluster_Primary_Info(n_c_d.cluster_id.toInt(),n_c_d.cluster_name,n_c_d.cluster_orientation,
                             floor_list.get(i).floor_level, floor_list.get(i).floor_name, floor_list.get(i).floor_map, floor_list.get(i).map_height, floor_list.get(i).map_width,"","")
     */
                    DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.clusterPrimaryInfo().addClusterPrimaryInfo(cluster_primary_info)
                } else {
                    var cluster_primary_info = Cluster_Primary_Info(n_c_d.cluster_id.toInt(), n_c_d.cluster_name, n_c_d.cluster_orientation,
                        floor_list.get(i).floor_level, floor_list.get(i).floor_name, floor_list.get(i).floor_map, floor_list.get(i).map_height, floor_list.get(i).map_width, floor_list.get(i).default_siteid, floor_list.get(i).default_sitename)


                    /* var cluster_primary_info=Cluster_Primary_Info(n_c_d.cluster_id.toInt(),n_c_d.cluster_name,n_c_d.cluster_orientation,
                             floor_list.get(i).floor_level, floor_list.get(i).floor_name, floor_list.get(i).floor_map, floor_list.get(i).map_height, floor_list.get(i).map_width,"","")
     */
                    DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.clusterPrimaryInfo().addClusterPrimaryInfo(cluster_primary_info)
                }


                val site_list = floor_list.get(i).site_list
                for (j in site_list.indices) {
                    val pathNode = PathNode()
                    val pathNode_connet = PathNode_connet()
                    pathNode.clustor_id = "" + n_c_d.cluster_id
                    pathNode.floor_level = "" + floor_list.get(i).floor_level
                    pathNode.floor_name = "" + floor_list.get(i).floor_name
                    pathNode.node_name = site_list.get(j).node_name
                    pathNode.node_type = site_list.get(j).node_type
                    pathNode.site_id = site_list.get(j).siteid
                    pathNode.store_id = site_list.get(j).store_id
                    pathNode.site_map_coord_x = site_list.get(j).site_map_3dcoord_x
                    pathNode.site_map_coord_y = site_list.get(j).site_map_3dcoord_y
                    pathNode.site_map_coord_z = site_list.get(j).site_map_3dcoord_z
                    pathNode.site_type = site_list.get(j).site_type
                    pathNode.store_logo = site_list.get(j).store_logo
                    // pathNode.store_name = site_list.get(j).store_name
                    var store_info = site_list.get(j).store_name

                    if (store_info.contains("#")) {
                        val spilted = store_info.split("#".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                        var mobile = ""
                        var phoneNumber = ""

                        try {
                            if (spilted[3].toString().isNotEmpty()) {
                                mobile = spilted[3].toString()
                            } else (spilted[4].toString().isNotEmpty())
                            {
                                phoneNumber = spilted[4].toString()
                            }

                        } catch (e: Exception) {
                            Log.e("NavigationJson", e.message)
                        }

                        pathNode.store_name = spilted[0].toString()
                        pathNode.address = spilted[1].toString()
                        pathNode.store_type = spilted[2].toString()
                        pathNode.mobile = mobile
                        pathNode.phone = phoneNumber
                    }

                    pathNode.cluster_orientation = n_c_d.cluster_orientation


                    //  pathNode.cluster_orientation = "" + n_c_d.cluster_orientation
                    Pref_manager.setClusterOrientation(ApplicationContext.get(), "" + n_c_d.cluster_orientation)

                    var store_for_deletion = DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.pathNodeList().getAllStoresForDeletion(pathNode.clustor_id, pathNode.site_id)
                    if (store_for_deletion.size > 0) {
                        for (x in store_for_deletion.indices) {
                            DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.pathNodeList().deletebyClusterIdAndSiteId(store_for_deletion.get(x).clustor_id, store_for_deletion.get(x).site_id)
                            DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.pathNodeConnectList().deletebyClusterIdAndSiteId(store_for_deletion.get(x).clustor_id, store_for_deletion.get(x).site_id)
                        }
                        DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.pathNodeList().addPathNode(pathNode)
                    } else {
                        DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.pathNodeList().addPathNode(pathNode)
                    }

                    for (k in site_list.get(j).connection_list.indices) {
                        val key = site_list.get(j).siteid.toString()
                        val key_val = site_list.get(j).connection_list.get(k).siteid.toString()
                        pathNode_connet.clustor_id = n_c_d.cluster_id
                        pathNode_connet.floor_level = "" + floor_list.get(i).floor_level
                        pathNode_connet.site_id = site_list.get(j).siteid.toString()
                        pathNode_connet.site_id_connect = site_list.get(j).connection_list.get(k).siteid.toString()
                        DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.pathNodeConnectList().addPathNode_connect(pathNode_connet)
                    }
                }

            }


        } catch (ex: IOException) {
            Log.e("dataJson", "" + ex.message)
        }



        return Result.success()
    }





}