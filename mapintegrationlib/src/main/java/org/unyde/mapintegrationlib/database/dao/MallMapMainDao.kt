package org.unyde.mapintegrationlib.database.dao

import androidx.room.*
import org.unyde.mapintegrationlib.database.entity.MallMapMain


@Dao
interface MallMapMainDao {

    @Query("SELECT * FROM mall_map_main WHERE cluster_id LIKE :cluster")
    fun all(cluster: String): List<MallMapMain>

    @Query("SELECT * FROM mall_map_main WHERE(isMapDownloaded=0 or isJsonDownloaded=0) and cluster_id=:cluster")
    fun downlaodedAll(cluster: String): List<MallMapMain>

    @Query("SELECT cluster_id FROM mall_map_main WHERE isMapDownloaded=1 or isJsonDownloaded=1")
    fun downlaoded_Cluster(): List<String>


    @Query("UPDATE mall_map_main SET isJsonDownloaded = 1 WHERE cluster_id = :cluster and floor_number= :floor_number")
    fun update_isJsonDownloaded(cluster: String,floor_number: String)


    @Query("UPDATE mall_map_main SET isMapDownloaded = 1 WHERE cluster_id = :cluster and floor_number= :floor_number")
    fun update_isMapDownloaded(cluster: String,floor_number: String)

    @Query("UPDATE mall_map_main SET local_pathImage = :local_pathImage WHERE cluster_id = :cluster and floor_number= :floor_number")
    fun update_local_map_path(cluster: String,floor_number: String,local_pathImage: String)


    @Query("UPDATE mall_map_main SET floor_json_date = :floor_json_date WHERE cluster_id = :cluster and floor_number= :floor_number")
    fun update_jsonDate(cluster: String,floor_number: String,floor_json_date: String)

    @Query("UPDATE mall_map_main SET floor_map_date = :floor_map_date WHERE cluster_id = :cluster and floor_number= :floor_number")
    fun update_mapDate(cluster: String,floor_number: String,floor_map_date: String)




    @Query("SELECT * FROM mall_map_main WHERE cluster_id LIKE :cluster and floor_number= :floor_number")
    fun floorData(cluster: String,floor_number: String): MallMapMain



    @Query("SELECT * FROM mall_map_main WHERE cluster_id = :cluster and isJsonDownloaded=0 LIMIT 1")
    fun file_json(cluster: String): List<MallMapMain>


    @Query("SELECT * FROM mall_map_main WHERE cluster_id = :cluster and  isMapDownloaded=0 LIMIT 1")
    fun file_obj(cluster: String): List<MallMapMain>

    @Query("SELECT * FROM mall_map_main WHERE floor_number LIKE :id and cluster_id LIKE :cluster LIMIT 1")
    fun findById(id: Int, cluster: String): List<MallMapMain>


    @Query("SELECT * FROM mall_map_main WHERE floor_json_file LIKE :id LIMIT 1")
    fun getDataPathJSon(id: String): List<MallMapMain>

    @Query("SELECT * FROM mall_map_main WHERE floor_number LIKE :id LIMIT 1")
    fun findByFloorId(id: Int): List<MallMapMain>

    @Query("SELECT floor_alias FROM mall_map_main WHERE floor_number LIKE :id")
    fun findAliasByFloorId(id: Int): String

    @Query("SELECT * FROM mall_map_main WHERE  floor_map LIKE :id LIMIT 1")
    fun getDataPathImage(id: String): List<MallMapMain>





//    @Query("SELECT * FROM mall_map_main  WHERE ally_exist = 'Pair'")
//    fun getAllData() : List<MallMapMain>


    @Query("UPDATE mall_map_main SET isJsonDownloaded = 0,floor_json_file= :temp_floor_json_file WHERE floor_json_file = :floor_json_file")
    fun update_isJsonNotDownloaded(floor_json_file: String,temp_floor_json_file: String)


    @Query("UPDATE mall_map_main SET isMapDownloaded = 0,floor_map= :temp_floor_map_file WHERE floor_map = :floor_json_file")
    fun update_isMapNotDownloaded(floor_json_file: String,temp_floor_map_file: String)






    @Query("UPDATE mall_map_main SET isJsonDownloaded = 2 WHERE floor_json_file = :floor_json_file")
    fun update_isJsonDownloadedError(floor_json_file: String)


    @Query("UPDATE mall_map_main SET isMapDownloaded = 2 WHERE floor_map = :floor_json_file")
    fun update_isMapDownloadedError(floor_json_file: String)


/*
    @Query("UPDATE mall_map_main SET isMtlDownloaded = 0 WHERE floor_number = :floor_number")
    fun update_isNotMtlDownloaded(floor_number: Int)

    @Query("UPDATE mall_map_main SET isMapDownloaded = 0 WHERE floor_number = :floor_number")
    fun update_isNotMapDownloaded(floor_number: Int)*/


    @Query("UPDATE mall_map_main SET local_pathImage = :local_path WHERE floor_number = :flor_no")
    fun update_Image(flor_no: Int, local_path: String)

    @Query("UPDATE mall_map_main SET local_pathJson = :local_path WHERE floor_number = :flor_no")
    fun update_Gsoon(flor_no: Int, local_path: String)

    @Query("SELECT * FROM mall_map_main where cluster_id=:cluster_id  ORDER BY floor_number DESC")
    fun getFloor(cluster_id: String): List<MallMapMain>


    @Insert
    fun addfilePath(connectedDevice: MallMapMain)

    @Update
    fun update(product: MallMapMain)


    @Query("DELETE FROM mall_map_main")
    fun subnukeTable()

    @Delete
    fun delete(connectedDevice: MallMapMain)
}
