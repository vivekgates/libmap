package org.unyde.mapintegrationlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClusterFullMap : AppCompatActivity() {

    var mall_name : TextView ?=null
    var mall_address_txt : TextView ?=null
    var floors_recycler : RecyclerView ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cluster_full_map)
        mall_name = findViewById(R.id.mall_name)
        mall_address_txt = findViewById(R.id.mall_address_txt)
        floors_recycler = findViewById(R.id.floors_recycler)

    }
}
