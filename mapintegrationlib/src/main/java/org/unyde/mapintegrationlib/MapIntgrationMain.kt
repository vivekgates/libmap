package org.unyde.mapintegrationlib

import android.app.Activity
import android.content.Context
import org.unyde.mapintegrationlib.viewmodel.ClusterDetailViewModel
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import org.unyde.mapintegrationlib.model.store_info.StoreInfo
import org.unyde.mapintegrationlib.network.ApiClient
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

            ApplicationContext.getInstance().init(c);
            mViewModel_clusterList = ViewModelProviders.of(c).get(MallFloorListViewModel::class.java!!)
            mViewModel_clusterList!!.init(c, "28.554810", cluster_id)
            mViewModel_clusterList!!.mallFloorList.observeForever { clusterDetail ->

                if (clusterDetail.data!!.status!!.equals("1"))
                {
                    if (clusterDetail.data!!.floors!!.size > 0) {
                        for (i in 0 until clusterDetail.data!!.floors!!.size) {

                            // for (j in 0 until clusterDetail.data!!.get(i).clusterFloorDetailsList!!.size) {
                            var cluster_id =
                                clusterDetail.data!!.floors!!.get(i).clusterId!!
                            var url_map =ApiClient.imageUrl+
                                    clusterDetail.data!!.floors!!.get(i).floorMap.toString()
                            var url_json =ApiClient.imageUrl+
                                    clusterDetail.data!!.floors!!.get(i).floorJson.toString()
                            startMapDownloadWorker(
                                c,
                                "Noida",
                                cluster_id.toString(),
                                url_map,
                                url_json,
                                "MAP" + cluster_id + "Download" + i
                            )
                            //  startMapJsonDownloadWorker("Noida", cluster_id, url_json, "JSON"+cluster_id + "Download" + j)
                            // }

                        }

                    } else {


                    }
                }
                else
                {

                }



            }

        }

        fun startMapDownloadWorker(
            c: Activity,
            city: String,
            mall_id: String,
            url: String,
            url_json: String,
            uniqueWork: String
        ) {

            mWorkManager = WorkManager.getInstance()
            val data = Data.Builder()
                .putString("images", url)
                .putString("city", city)
                .putString("Mall_Id", mall_id)
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

            LiveDataHelper.getInstance().observePercentage()
                .observeForever {
                    Toast.makeText(c, it.toString(), Toast.LENGTH_LONG).show()
                }

        }


        fun getStoreDetails(site_id: String, cluster_id: String,context:Context): StoreInfo {
          /*  var storeInfo= StoreInfo()
            var store_info = MyApplication.get()!!.db!!.pathNodeList().findById(site_id, cluster_id)
            if (store_info!!.size > 0) {
                if (store_info!!.get(0).store_type.toInt() == 1) {

                    storeInfo?.address = store_info!!.get(0).address
                    storeInfo?.clustor_id = store_info!!.get(0).clustor_id
                    storeInfo?.floor_level = store_info!!.get(0).floor_level
                    storeInfo?.floor_name = store_info!!.get(0).floor_name
                    storeInfo?.mobile = store_info!!.get(0).mobile
                    storeInfo?.phone = store_info!!.get(0).phone
                    storeInfo?.store_id = store_info!!.get(0).store_id
                    storeInfo?.store_name = store_info!!.get(0).store_name
                    storeInfo?.store_logo = store_info!!.get(0).store_logo
                }

            }*/
            var storeInfo=Store_In_Out.getInstance().check_in(site_id,cluster_id,context)


            return storeInfo!!
        }


    }




}
