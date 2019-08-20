package org.unyde.mapintegration.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import org.unyde.mapintegration.database.entity.PathNode_connet;

import java.util.List;

/**
 * Created by Deepak kumar on 01/09/2018
 */

@Dao
public interface PathNodeConnectDao {

    @Query("SELECT * FROM pathnode_connect WHERE clustor_id = :cluster_id")
    List<PathNode_connet> getAll(String cluster_id);

    @Query("SELECT * FROM pathnode_connect WHERE floor_level LIKE :id and clustor_id = :cluster_id")
    List<PathNode_connet> findByFloorLevel(int id, String cluster_id);

    @Insert
    void addPathNode_connect(PathNode_connet pathNodeconnet);

    @Update
    void update(PathNode_connet nodeConnect);

    @Query("DELETE FROM pathnode_connect WHERE floor_level LIKE :floor_level  and clustor_id = :cluster_id")
    void deletebyClusterIdAndFloor(String floor_level, String cluster_id);

    @Query("DELETE FROM pathnode_connect WHERE clustor_id = :cluster_id and site_id = :site_id")
    void deletebyClusterIdAndSiteId(String cluster_id, String site_id);


    @Query("DELETE FROM pathnode_connect")
    void delete();
}
