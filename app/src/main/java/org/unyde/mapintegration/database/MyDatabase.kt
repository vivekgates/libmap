package org.unyde.mapintegration.database


import androidx.room.Database
import androidx.room.RoomDatabase
import org.unyde.mapintegration.database.dao.PathNodeConnectDao
import org.unyde.mapintegration.database.dao.PathNodeDao
import org.unyde.mapintegration.database.entity.PathNode
import org.unyde.mapintegration.database.entity.PathNode_connet

@Database(entities = [PathNode::class, PathNode_connet::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun pathNodeList(): PathNodeDao

    abstract fun pathNodeConnectList(): PathNodeConnectDao

}
