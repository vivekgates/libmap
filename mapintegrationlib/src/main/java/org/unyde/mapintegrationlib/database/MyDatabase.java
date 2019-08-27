package org.unyde.mapintegrationlib.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import org.unyde.mapintegrationlib.database.dao.*;
import org.unyde.mapintegrationlib.database.entity.*;

@Database(entities = {PathNode.class, PathNode_connet.class, Cluster_Primary_Info.class, CheckInCheckOut.class, MallMapMain.class}, version = 1)


public abstract class MyDatabase extends RoomDatabase {

    public abstract PathNodeDao pathNodeList();

    public abstract PathNodeConnectDao pathNodeConnectList();

    public abstract ClusterPrimaryInfoDao clusterPrimaryInfo();

    public abstract CheckinCheckOutDao checkInCheckOut();

    public abstract MallMapMainDao mallMapMain();

}
