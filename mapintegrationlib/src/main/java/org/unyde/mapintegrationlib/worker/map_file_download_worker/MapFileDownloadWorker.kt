package org.unyde.mapintegrationlib.worker.map_file_download_worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request
import org.unyde.mapintegrationlib.ApplicationContext
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.worker.helper.LiveDataHelper
import java.io.*
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


/////https://s3.ap-south-1.amazonaws.com/ally-production-images/cluster/105/map/expo_groundfloor.zip
class MapFileDownloadWorker(private val mContext: Context, workerParameters: WorkerParameters) :
    Worker(mContext, workerParameters) {

    private val liveDataHelper: LiveDataHelper

    init {
        liveDataHelper = LiveDataHelper.getInstance()
    }
    @SuppressLint("RestrictedApi", "CheckResult")
    override fun doWork(): Result {
        var outputData: Data? = null
        val url = inputData.getString("images")
        val city = inputData.getString("city")
        val mall_id = inputData.getString("Mall_Id")
        val floor_number = inputData.getString("floor_number")
        var spilted = url!!.split("/".toRegex()).reversed().dropLastWhile({ it.isEmpty() }).toTypedArray()
        try {
            val myDir = applicationContext.getExternalFilesDir(null)
            val path = "$myDir/MAP/" + city + "/" + mall_id

            val buf = ByteArray(2048)
            var byteRead: Int
            val f = File(path + "/", spilted[0])
            if (!f.parentFile.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            } else {
                f.delete()
                f.createNewFile()
            }

            var outStream = BufferedOutputStream(FileOutputStream(File(path + "/" + spilted[0])))


            val builder = OkHttpClient.Builder()
            builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
            val client = builder.build()

            val request = Request.Builder()
                .url(inputData.getString("images"))
                .build()

            val response = client.newCall(request).execute()
            var `in` = response.body()?.byteStream()
            byteRead = `in`!!.read(buf)
            while (byteRead != -1) {
                outStream.write(buf, 0, byteRead)
                byteRead = `in`.read(buf)
            }
            `in`.close()
            outStream.close()

            outputData = Data.Builder().putString("Image_Name", path+ "/")
                .putString("file_name", path + "/" + spilted[0])
                .putString("city", city)
                .putString("floor_number", floor_number)
                .putString("Mall_Id", mall_id).build()

            DatabaseClient.getInstance(ApplicationContext.get())!!.db!!.mallMapMain()!!.update_isMapDownloaded(mall_id!!,floor_number!!)

            liveDataHelper!!.updatePercentage(50)

        } catch (e: Exception) {
            return Result.failure()
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            return Result.failure()
            e.printStackTrace()
        }


        return Result.Success(outputData!!)
    }


}