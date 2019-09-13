package org.unyde.mapintegrationlib.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import org.unyde.mapintegrationlib.database.entity.PathNode;

import java.util.List;

/**
 * Created by Deepak kumar on 01/09/2018
 */

@Dao
public interface PathNodeDao {

    @Query("SELECT * FROM pathnode WHERE clustor_id = :cluster_id")
    List<PathNode> getAll(String cluster_id);

    @Query("SELECT * FROM pathnode WHERE site_id = :site_id")
    List<PathNode> getCordinates(String site_id);


    @Query("SELECT * FROM pathnode WHERE store_id IN (:store_id) and floor_level = :floor")
    List<PathNode> getCordinatesForMarkers(List<String> store_id,String floor);



    @Query("SELECT * FROM pathnode WHERE clustor_id = :cluster_id and store_type='1' and site_type='Primary' order by floor_level,store_name ")
    List<PathNode> getAllOnlyStores(String cluster_id);

   /* @Query("SELECT * FROM pathnode WHERE clustor_id = :cluster_id and store_type!='10' and site_type='Primary' order by floor_level,store_name ")
    List<PathNode> getAllStores(String cluster_id);*/


    @Query("SELECT * FROM pathnode WHERE clustor_id = :cluster_id and site_id= :site_id")
    List<PathNode> getAllStoresForDeletion(String cluster_id, String site_id);
/*
    @Query("SELECT * FROM pathnode WHERE clustor_id = :cluster_id and site_type='Primary' ")
    List<PathNode> getAllStores(String cluster_id);*/

    @Query("SELECT * FROM pathnode WHERE site_id LIKE :id and clustor_id = :cluster_id LIMIT 1")
    List<PathNode> findById(String id, String cluster_id);

    @Query("SELECT * FROM pathnode WHERE store_id LIKE :id and clustor_id = :cluster_id LIMIT 1")
    List<PathNode> findByStoreId(String id, String cluster_id);


    @Query("SELECT * FROM pathnode WHERE store_id = :id and floor_level=:floor_level and (site_type = :type OR site_type = :type1) and clustor_id = :cluster_id")
    List<PathNode> findByStoreId(String id, String type, String type1, int floor_level, String cluster_id);

    @Query("SELECT * FROM pathnode WHERE store_id = :id and site_type = :type and clustor_id = :cluster_id")
    List<PathNode> findByStoreIdSiteType(String id, String type, String cluster_id);

    @Query("SELECT * FROM pathnode WHERE site_type = :type and clustor_id = :cluster_id")
    List<PathNode> findAllForLocate(String type, String cluster_id);


    @Query("SELECT * FROM pathnode WHERE store_id = :id and clustor_id = :cluster_id and (site_type = :type OR site_type = :type1)")
    List<PathNode> findBySourceStoreId(String id, String type, String type1, String cluster_id);

    @Query("SELECT * FROM pathnode WHERE site_type = :type and clustor_id = :cluster_id ")
    List<PathNode> findBySiteType(String type, String cluster_id);


    @Query("SELECT * FROM pathnode WHERE floor_level LIKE :floor_level and node_type LIKE :node_type and clustor_id = :cluster_id")
    List<PathNode> findLiftnodeByFloor(String floor_level, String node_type, String cluster_id);

    @Query("SELECT * FROM pathnode WHERE floor_level = :level and clustor_id = :cluster_id  ")
    List<PathNode> findByFloor_level(int level, String cluster_id);


        @Query("SELECT * FROM pathnode WHERE store_name LIKE :store_name and clustor_id = :cluster_id and site_type= :site_type  ")
        List<PathNode> findParkingNodes(String store_name, String cluster_id, String site_type);


    @Query("SELECT * FROM pathnode WHERE site_id = :site_id and site_type= :site_type LIMIT 1  ")
    List<PathNode> findParkingNodesInfo(String site_id, String site_type);


    @Query("SELECT cluster_orientation FROM pathnode WHERE clustor_id = :cluster_id LIMIT 1")
    String findClusterOrientation(String cluster_id);


    @Query("DELETE FROM pathnode WHERE floor_level LIKE :floor_level  and clustor_id = :cluster_id")
    void deletebyClusterIdAndFloor(String floor_level, String cluster_id);


    @Query("DELETE FROM pathnode WHERE clustor_id LIKE :cluster_id  and site_id = :site_id")
    void deletebyClusterIdAndSiteId(String cluster_id, String site_id);

    @Insert
    void addPathNode(PathNode pathNode);

    @Update
    void update(PathNode node);

    @Query("DELETE FROM pathnode")
    void delete();
}
