package org.unyde.mapintegrationlib

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap
import org.unyde.mapintegrationlib.InternalNavigation.demo.SceneLoader
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.Marker_Internal_Nav
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView
import org.unyde.mapintegrationlib.database.DatabaseClient
import org.unyde.mapintegrationlib.database.entity.MallMapMain
import org.unyde.mapintegrationlib.database.entity.PathNode
import org.unyde.mapintegrationlib.interfaces.FloorClickListner
import org.unyde.mapintegrationlib.util.Constants
import org.unyde.mapintegrationlib.util.Pref_manager
import java.lang.Float
import java.util.ArrayList

class ClusterMapActivity1 : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


}
