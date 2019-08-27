package org.unyde.mapintegrationlib.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import org.unyde.mapintegrationlib.database.entity.MallDataDownloadStatus;
import org.unyde.mapintegrationlib.database.entity.PathNode;

import java.util.List;

/**
 * Created by Deepak kumar on 01/09/2018
 */

@Dao
public interface MallDataDownloadStatusDao {

    @Query("SELECT * FROM mall_data_status WHERE clustor_id = :cluster_id")
    List<MallDataDownloadStatus> getAll(String cluster_id);

    @Query("UPDATE mall_data_status SET downloaded_map = 1 WHERE  clustor_id = :cluster_id")
    void update_isJMapDownloaded(String cluster_id);


    @Insert
    void addMallDataStatus(MallDataDownloadStatus mallDataDownloadStatus);
}
