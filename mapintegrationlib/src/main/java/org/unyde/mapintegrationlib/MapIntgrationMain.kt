package org.unyde.mapintegrationlib

import android.app.Activity
import android.content.Context
import android.content.Intent
import org.unyde.mapintegrationlib.viewmodel.ClusterDetailViewModel
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.database.entity.MallMapMain
import org.unyde.mapintegrationlib.model.store_info.StoreInfo
import org.unyde.mapintegrationlib.network.ApiClient
import org.unyde.mapintegrationlib.services.CheckInCheckOutService
import org.unyde.mapintegrationlib.util.Helper
import org.unyde.mapintegrationlib.util.Pref_manager
import org.unyde.mapintegrationlib.viewmodel.MallFloorListViewModel
import org.unyde.mapintegrationlib.worker.helper.LiveDataHelper
import org.unyde.mapintegrationlib.worker.map_file_download_worker.FileUnzipWorker
import org.unyde.mapintegrationlib.worker.map_file_download_worker.MapFileDownloadWorker
import org.unyde.mapintegrationlib.worker.map_json_download_worker.JsonFileDownloadWorker
import org.unyde.mapintegrationlib.worker.map_json_download_worker.MapJsonParseWorker
import woogly.unyde.org.wooglyunyde.cluster.Store_In_Out


private var mViewModel_cluster: ClusterDetailViewModel? = null
private var mViewModel_clusterList: MallFloorListViewModel? = null
private var mWorkManager: WorkManager? = null

class MapIntgrationMain {

    companion object {
        /* fun downloadmap(c: FragmentActivity, cluster_id: String) {

             ApplicationContext.getInstance().init(c);
             mViewModel_cluster = ViewModelProviders.of(c).get(ClusterDetailViewModel::class.java!!)
             mViewModel_cluster!!.init(c, "28.554810", cluster_id)
             mViewModel_cluster!!.clusterDetails.observeForever { clusterDetail ->

                 if (clusterDetail.data!!.size > 0) {
                     for (i in 0 until clusterDetail.data!!.size) {

                         for (j in 0 until clusterDetail.data!!.get(i).clusterFloorDetailsList!!.size) {
                             var cluster_id =
                                 clusterDetail.data!!.get(i).clusterFloorDetailsList!!.get(j).cluster_id.toString()
                             var url_map =
                                 clusterDetail.data!!.get(i).clusterFloorDetailsList!!.get(j).floor_map.toString()
                             var url_json =
                                 clusterDetail.data!!.get(i).clusterFloorDetailsList!!.get(j).floor_json_file.toString()
                             startMapDownloadWorker(
                                 c,
                                 "Noida",
                                 cluster_id,
                                 url_map,
                                 url_json,
                                 "MAP" + cluster_id + "Download" + j
                             )
                             //  startMapJsonDownloadWorker("Noida", cluster_id, url_json, "JSON"+cluster_id + "Download" + j)
                         }

                     }

                 } else {


                 }

             }

         }*/

        fun downloadmap(c: FragmentActivity, cluster_id: String) {


            mViewModel_clusterList =
                ViewModelProviders.of(c).get(MallFloorListViewModel::class.java!!)
            mViewModel_clusterList!!.init(c, "28.554810", cluster_id)
            mViewModel_clusterList!!.mallFloorList.observeForever { clusterDetail ->

                if (clusterDetail.data!!.status == 1) {
                    if (clusterDetail.data!!.floors!!.size > 0) {
                        for (i in 0 until clusterDetail.data!!.floors!!.size) {
                            try {


                                // for (j in 0 until clusterDetail.data!!.get(i).clusterFloorDetailsList!!.size) {
                                var cluster_id =
                                    clusterDetail.data!!.floors!!.get(i).clusterId!!
                                var url_map = ApiClient.imageUrl +
                                        clusterDetail.data!!.floors!!.get(i).floorMap.toString()
                                var url_json = ApiClient.imageUrl +
                                        clusterDetail.data!!.floors!!.get(i).floorJson.toString()
                                var floor_number =
                                    clusterDetail.data!!.floors!!.get(i).floorNumber.toString()
                                var floor_map_date =
                                    clusterDetail.data!!.floors!!.get(i).floorMapDate
                                var floor_json_date =
                                    clusterDetail.data!!.floors!!.get(i).floorJsonDate
                                var floor_date = clusterDetail.data!!.floors!!.get(i).floorDate

                                if (floor_map_date != null) {

                                } else {
                                    floor_map_date = ""
                                }

                                if (floor_json_date != null) {

                                } else {
                                    floor_json_date = ""
                                }

                                if (floor_date != null) {

                                } else {
                                    floor_date = ""
                                }

                                var floor_data =
                                    DatabaseClient.getInstance(c)!!.db!!.mallMapMain()!!.floorData(
                                        cluster_id.toString(),
                                        floor_number
                                    )

                                if (floor_data != null) {

                                    val isMapUpdateAvailable = Helper.dateComarison(
                                        floor_data.floor_map_date,
                                        floor_map_date
                                    )

                                    if (floor_data.isMapDownloaded == 1) {
                                        if (isMapUpdateAvailable) {
                                            DatabaseClient.getInstance(c)!!.db!!.mallMapMain()!!.update_mapDate(
                                                cluster_id.toString(),
                                                floor_number, floor_map_date
                                            )
                                            startMapFileDownloadWorker(
                                                "Noida",
                                                cluster_id.toString(),
                                                floor_number,
                                                url_map,
                                                "MAP" + cluster_id + "Download" + i
                                            )
                                        }
                                    } else {
                                        DatabaseClient.getInstance(c)!!.db!!.mallMapMain()!!.update_mapDate(
                                            cluster_id.toString(),
                                            floor_number, floor_map_date
                                        )
                                        startMapFileDownloadWorker(
                                            "Noida",
                                            cluster_id.toString(),
                                            floor_number,
                                            url_map,
                                            "MAP" + cluster_id + "Download" + i
                                        )
                                    }


                                    val isJsonUpdateAvailable = Helper.dateComarison(
                                        floor_data.floor_json_date,
                                        floor_json_date
                                    )


                                    if (floor_data.isJsonDownloaded == 1) {
                                        if (isJsonUpdateAvailable) {
                                            DatabaseClient.getInstance(c)!!.db!!.mallMapMain()!!.update_jsonDate(
                                                cluster_id.toString(),
                                                floor_number, floor_json_date
                                            )

                                            startJSONDownloadWorker(
                                                "Noida",
                                                cluster_id.toString(),
                                                floor_number,
                                                url_json,
                                                "MAP" + cluster_id + "Download" + i
                                            )
                                        }
                                    } else {
                                        DatabaseClient.getInstance(c)!!.db!!.mallMapMain()!!.update_jsonDate(
                                            cluster_id.toString(),
                                            floor_number, floor_json_date
                                        )
                                        startJSONDownloadWorker(
                                            "Noida",
                                            cluster_id.toString(),
                                            floor_number,
                                            url_json,
                                            "MAP" + cluster_id + "Download" + i
                                        )
                                    }


                                    val isDataUpdateAvailable = Helper.dateComarison(
                                        floor_data.floor_date,
                                        floor_date
                                    )

                                    if (isDataUpdateAvailable) {

                                    }
                                } else {

                                    val mallMap = MallMapMain(
                                        clusterDetail.data!!.floors!!.get(i).id!!,
                                        clusterDetail.data!!.floors!!.get(i).clusterId!!,
                                        clusterDetail.data!!.floors!!.get(i).floorNumber!!.toInt(),
                                        clusterDetail.data!!.floors!!.get(i).status!!,
                                        clusterDetail.data!!.floors!!.get(i).floorAlias!!,
                                        clusterDetail.data!!.floors!!.get(i).floorMap!!,
                                        clusterDetail.data!!.floors!!.get(i).floorJson!!,
                                        "",
                                        "",
                                        0,
                                        0,
                                        floor_map_date,
                                        floor_json_date,
                                        floor_date

                                    )

                                    DatabaseClient.getInstance(c)!!.db!!.mallMapMain()!!.addfilePath(
                                        mallMap
                                    )

                                    startMapDownloadWorker(
                                        "Noida",
                                        cluster_id.toString(),
                                        floor_number,
                                        url_map,
                                        url_json,
                                        "MAP" + cluster_id + "Download" + i
                                    )
                                }


                                //  startMapJsonDownloadWorker("Noida", cluster_id, url_json, "JSON"+cluster_id + "Download" + j)
                                // }
                            } catch (e: Exception) {

                            }


                        }

                    } else {


                    }
                } else {

                }


            }

        }

        fun startMapDownloadWorker(
            city: String,
            mall_id: String,
            floor_number: String,
            url: String,
            url_json: String,
            uniqueWork: String
        ) {

            mWorkManager = WorkManager.getInstance()
            val data = Data.Builder()
                .putString("images", url)
                .putString("city", city)
                .putString("Mall_Id", mall_id)
                .putString("floor_number", floor_number)
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)

            val oneTimeRequest = OneTimeWorkRequest.Builder(MapFileDownloadWorker::class.java)
                .setInputData(data)
                .setConstraints(constraints.build())
                .build()


            val data1 = Data.Builder()
                .putString("json", url_json)
                .putString("city", city)
                .putString("Mall_Id", mall_id)
                .putString("floor_number", floor_number)
                .build()


            val oneTimeRequest1 = OneTimeWorkRequest.Builder(JsonFileDownloadWorker::class.java)
                .setInputData(data1)
                .setConstraints(constraints.build())
                .build()


            var continuation = mWorkManager!!
                .beginUniqueWork(
                    uniqueWork,
                    ExistingWorkPolicy.KEEP,
                    oneTimeRequest
                )

            //Toast.makeText(c, "Starting worker", Toast.LENGTH_SHORT).show()


            val save = OneTimeWorkRequest.Builder(FileUnzipWorker::class.java!!)
                .addTag(uniqueWork)
                .build()

            val saveDB = OneTimeWorkRequest.Builder(MapJsonParseWorker::class.java!!)
                .addTag(uniqueWork)
                .build()

            continuation = continuation.then(save)
            continuation = continuation.then(oneTimeRequest1)
            continuation = continuation.then(saveDB)

            // Actually start the work
            continuation.enqueue()


        }


        fun startMapFileDownloadWorker(
            city: String,
            mall_id: String,
            floor_number: String,
            url: String,
            uniqueWork: String
        ) {

            mWorkManager = WorkManager.getInstance()
            val data = Data.Builder()
                .putString("images", url)
                .putString("city", city)
                .putString("Mall_Id", mall_id)
                .putString("floor_number", floor_number)
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)

            val oneTimeRequest = OneTimeWorkRequest.Builder(MapFileDownloadWorker::class.java)
                .setInputData(data)
                .setConstraints(constraints.build())
                .build()

            var continuation = mWorkManager!!
                .beginUniqueWork(
                    uniqueWork,
                    ExistingWorkPolicy.KEEP,
                    oneTimeRequest
                )

            val save = OneTimeWorkRequest.Builder(FileUnzipWorker::class.java!!)
                .addTag(uniqueWork)
                .build()

            continuation = continuation.then(save)

            // Actually start the work
            continuation.enqueue()


        }


        fun startJSONDownloadWorker(
            city: String,
            mall_id: String,
            floor_number: String,
            url_json: String,
            uniqueWork: String
        ) {

            mWorkManager = WorkManager.getInstance()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)

            val data1 = Data.Builder()
                .putString("json", url_json)
                .putString("city", city)
                .putString("Mall_Id", mall_id)
                .putString("floor_number", floor_number)
                .build()


            val oneTimeRequest1 = OneTimeWorkRequest.Builder(JsonFileDownloadWorker::class.java)
                .setInputData(data1)
                .setConstraints(constraints.build())
                .build()


            var continuation = mWorkManager!!
                .beginUniqueWork(
                    uniqueWork,
                    ExistingWorkPolicy.KEEP,
                    oneTimeRequest1
                )

            //Toast.makeText(c, "Starting worker", Toast.LENGTH_SHORT).show()

            val saveDB = OneTimeWorkRequest.Builder(MapJsonParseWorker::class.java!!)
                .addTag(uniqueWork)
                .build()

            continuation = continuation.then(saveDB)

            // Actually start the work
            continuation.enqueue()

        }


        fun getStoreDetails(
            site_id: String,
            cluster_id: String,
            context: Context,
            instance_id: String,
            user_id: String
        ): StoreInfo {
            ApplicationContext.getInstance().init(context);
            Pref_manager.setUserId(context, user_id)
            Pref_manager.setInstanceId(context, instance_id)
            var storeInfo = Store_In_Out.getInstance().check_in(site_id, cluster_id, context)
            return storeInfo!!
        }


        fun startCheckinCheckOutService(context: Context, instance_id: String, user_id: String) {
            ApplicationContext.getInstance().init(context)
            Pref_manager.setUserId(context, user_id)
            Pref_manager.setInstanceId(context, instance_id)
            if (!Helper.isMyServiceRunning(CheckInCheckOutService::class.java, context)) {
                context.startService(Intent(context, CheckInCheckOutService::class.java))
            }
        }


        fun nearestBeaconForNavigation(site_id: String, cluster_id: String, context: Context) {
            try {

                var nearClusterId = Helper.hex2decimal(site_id!!.substring(8, 13).toString())

                if (cluster_id!!.equals(nearClusterId.toString())) {
                    //source_data_i_m_here(nearMeUserId!!)

                }
            } catch (e: Exception) {

            }


        }


    }


}
