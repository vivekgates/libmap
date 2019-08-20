package org.unyde.mapintegrationlib.util

import android.content.Context



class Pref_manager(internal var context: Context) {



    companion object {
        val CLUSTER_ORIENTATION = "CLUSTER_ORIENTATION"


        fun setClusterOrientation(context: Context, editable: String) {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_ORIENTATION, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(CLUSTER_ORIENTATION, editable).commit()
        }

        fun getClusterOrientation(context: Context): String? {
            var sharedPreferences = context.getSharedPreferences(CLUSTER_ORIENTATION, Context.MODE_PRIVATE)
            return sharedPreferences.getString(CLUSTER_ORIENTATION, "")
        }


    }


}
