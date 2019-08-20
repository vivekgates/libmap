package org.unyde.mapintegrationlib.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import org.unyde.mapintegrationlib.database.dao.ClusterPrimaryInfoDao;
import org.unyde.mapintegrationlib.database.dao.PathNodeConnectDao;
import org.unyde.mapintegrationlib.database.dao.PathNodeDao;
import org.unyde.mapintegrationlib.database.entity.Cluster_Primary_Info;
import org.unyde.mapintegrationlib.database.entity.PathNode;
import org.unyde.mapintegrationlib.database.entity.PathNode_connet;

@Database(entities = {PathNode.class, PathNode_connet.class, Cluster_Primary_Info.class}, version = 1)


public abstract class MyDatabase extends RoomDatabase {

    public abstract PathNodeDao pathNodeList();

    public abstract PathNodeConnectDao pathNodeConnectList();

    public abstract ClusterPrimaryInfoDao clusterPrimaryInfo();

}
