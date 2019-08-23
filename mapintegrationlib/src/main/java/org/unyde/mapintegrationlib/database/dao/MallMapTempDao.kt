package org.unyde.mapintegrationlib.database.dao

import androidx.room.*
import org.unyde.mapintegrationlib.database.entity.MallMapTemp


/**
 * Created by Deepak kumar on 01/09/2018
 */

@Dao
interface MallMapTempDao {


    @get:Query("SELECT * FROM mall_map_temp ")
    val all: List<MallMapTemp>

    @get:Query("SELECT * FROM mall_map_temp WHERE isJsonDownloaded=0 LIMIT 1")
    val file_json: List<MallMapTemp>


    @get:Query("SELECT * FROM mall_map_temp WHERE isMapDownloaded=0 LIMIT 1")
    val file_obj: List<MallMapTemp>

    @Query("SELECT * FROM mall_map_temp WHERE floor_number LIKE :id and cluster_id LIKE :cluster LIMIT 1")
    fun findById(id: Int,cluster:String): List<MallMapTemp>


    @Query("SELECT * FROM mall_map_temp WHERE cluster_id LIKE :cluster")
    fun findByClusterId(cluster: String): List<MallMapTemp>


    @Query("SELECT * FROM mall_map_temp WHERE floor_json_file LIKE :id LIMIT 1")
    fun getDataPathJSon(id: String): List<MallMapTemp>

    @Query("SELECT * FROM mall_map_temp WHERE floor_number LIKE :id LIMIT 1")
    fun findByFloorId(id: Int): List<MallMapTemp>

    @Query("SELECT floor_alias FROM mall_map_temp WHERE floor_number LIKE :id")
    fun findAliasByFloorId(id: Int): String

    @Query("SELECT * FROM mall_map_temp WHERE  floor_map LIKE :id LIMIT 1")
    fun getDataPathImage(id: String): List<MallMapTemp>


//    @Query("SELECT * FROM file_path  WHERE ally_exist = 'Pair'")
//    fun getAllData() : List<MallMapTemp>


    @Query("UPDATE mall_map_temp SET isJsonDownloaded = 1 WHERE floor_json_file = :floor_json_file")
    fun update_isJsonDownloaded(floor_json_file: String)


    @Query("UPDATE mall_map_temp SET isMapDownloaded = 1 WHERE floor_map = :floor_json_file")
    fun update_isMapDownloaded(floor_json_file: String)


    @Query("UPDATE mall_map_temp SET local_pathImage = :local_path WHERE floor_number = :flor_no")
    fun update_Image(flor_no: Int,local_path: String)

    @Query("UPDATE mall_map_temp SET local_pathJson = :local_path WHERE floor_number = :flor_no")
    fun update_Gsoon(flor_no: Int,local_path: String)

    @Query("SELECT * FROM mall_map_temp where cluster_id=:cluster_id  ORDER BY floor_number DESC")
    fun getFloor(cluster_id:String): List<MallMapTemp>

    @Query("DELETE FROM mall_map_temp where cluster_id=:cluster_id ")
    fun deleteByClusterId(cluster_id:String)



    @Insert
    fun addfilePath(connectedDevice: MallMapTemp)

    @Update
    fun update(product: MallMapTemp)


    @Query("DELETE FROM mall_map_temp")
    fun subnukeTable()

    @Delete
    fun delete(connectedDevice: MallMapTemp)
}
