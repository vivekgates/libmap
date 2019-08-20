package org.unyde.mapintegrationlib.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import org.unyde.mapintegrationlib.database.entity.Cluster_Primary_Info;
import java.util.List;

@Dao
public interface ClusterPrimaryInfoDao {

    @Query("SELECT * FROM cluster_primary_info WHERE cluster_id = :cluster_id and floor_level = :floor_level")
    List<Cluster_Primary_Info> getAll(String cluster_id, String floor_level);

    @Query("SELECT cluster_orientation FROM cluster_primary_info WHERE floor_level LIKE :floor_level and cluster_id = :cluster_id LIMIT 1")
    String findClusterOrientation(String floor_level, String cluster_id);

    @Query("SELECT floor_map_width FROM cluster_primary_info WHERE floor_level LIKE :floor_level and cluster_id = :cluster_id LIMIT 1")
    String findMapWidth(String floor_level, String cluster_id);

    @Query("SELECT floor_map_height FROM cluster_primary_info WHERE floor_level LIKE :floor_level and cluster_id = :cluster_id LIMIT 1")
    String findMapHeight(String floor_level, String cluster_id);

    @Query("SELECT default_location_site_id FROM cluster_primary_info WHERE floor_level LIKE :floor_level and cluster_id = :cluster_id LIMIT 1")
    String findDefaultSiteId(String floor_level, String cluster_id);

    @Query("SELECT default_location_site_name FROM cluster_primary_info WHERE floor_level LIKE :floor_level and cluster_id = :cluster_id LIMIT 1")
    String findDefaultSiteName(String floor_level, String cluster_id);

    @Insert
    void addClusterPrimaryInfo(Cluster_Primary_Info clusterPrimaryInfo);


    @Query("DELETE FROM cluster_primary_info WHERE floor_level LIKE :floor_level and cluster_id = :cluster_id")
    void delete(String floor_level, String cluster_id);
}
