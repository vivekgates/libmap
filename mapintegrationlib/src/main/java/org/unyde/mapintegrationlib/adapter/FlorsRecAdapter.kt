package org.unyde.mapintegrationlib.adapter
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.unyde.mapintegrationlib.ApplicationContext
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap
import org.unyde.mapintegrationlib.R
import org.unyde.mapintegrationlib.database.entity.MallMapMain
import org.unyde.mapintegrationlib.interfaces.FloorClickListner
import org.unyde.mapintegrationlib.util.Pref_manager
import java.util.ArrayList

class FlorsRecAdapter(internal var context: Context
                      , internal var flor_list: List<MallMapMain>?, internal var selected_floor: Int
                      , internal var Current_floor: ArrayList<Int>
                      , internal var floorClickListner: FloorClickListner
) : RecyclerView.Adapter<FlorsRecAdapterViewholder>() {

    var row_index = selected_floor
    var floor = Pref_manager.getFloor_Level(ApplicationContext.get().applicationContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlorsRecAdapterViewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.floor_items, parent, false)

        return FlorsRecAdapterViewholder(view)
    }

    override fun getItemCount(): Int {

        return if (flor_list != null) {
            flor_list!!.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: FlorsRecAdapterViewholder, position: Int) {

        try {
            holder.itemView.setOnClickListener {
                if (Cluster3DMap.mActionMode!!.equals(Cluster3DMap.IndoorMode.NAVIGATION)) {

                }
                else
                {
                    row_index = position;
                    notifyDataSetChanged()
                    floorClickListner.onFloorItemClick(position)
                }



            }




            if (flor_list!!.get(position).floor_number < 1) {

                if(flor_list!!.get(position).cluster_id==105 && flor_list!!.get(position).floor_number==-1)
                {
                    holder.flor_num_txt.setText("0"  )
                    holder.flor_name_txt.setText( " "+flor_list!!.get(position).floor_alias)
                }
                else
                {
                    holder.flor_num_txt.setText("" + flor_list!!.get(position).floor_number)
                    holder.flor_name_txt.setText( " "+flor_list!!.get(position).floor_alias)
                }

            } else {

                if(flor_list!!.get(position).cluster_id==105 && flor_list!!.get(position).floor_number==-1)
                {
                    holder.flor_num_txt.setText("0"  )
                    holder.flor_name_txt.setText( " "+flor_list!!.get(position).floor_alias)
                }
                else
                {
                    holder.flor_num_txt.setText("" + flor_list!!.get(position).floor_number)
                    holder.flor_name_txt.setText( " "+flor_list!!.get(position).floor_alias)
                }

/*
                holder.flor_num_txt.setText("" + flor_list!!.get(position).floor_number)
                holder.flor_name_txt.setText( " "+flor_list!!.get(position).floor_alias)*/
            }

            if (row_index == flor_list!!.get(position).floor_number) {

                holder.numcard.setBackgroundResource(R.drawable.acc_blue_border_ki)
                holder.flor_num_txt.setTextColor(Color.WHITE)
                holder.flor_name_txt.setTextColor(context.resources.getColor(R.color.black))
            } else {

                holder.numcard.setBackgroundResource(R.drawable.acc_white_border_ki)
                holder.flor_num_txt.setTextColor(Color.BLACK)
                holder.flor_name_txt.setTextColor(context.resources.getColor(R.color.base))
            }

            
        /*    if (selected_floor == 101) {

                if (Current_floor.get(position) == 1) {
                    holder.numcard.setBackgroundResource(R.drawable.acc_blue_border_ki)
                    holder.flor_num_txt.setTextColor(Color.WHITE)
                    holder.flor_name_txt.setTextColor(context.resources.getColor(R.color.black))
                } else {
                    holder.numcard.setBackgroundResource(R.drawable.acc_white_border_ki)
                    holder.flor_num_txt.setTextColor(Color.BLACK)
                    holder.flor_name_txt.setTextColor(context.resources.getColor(R.color.base))
                }

            } else {
                if (row_index == flor_list!!.get(position).floor_number) {

                    holder.numcard.setBackgroundResource(R.drawable.acc_blue_border_ki)
                    holder.flor_num_txt.setTextColor(Color.WHITE)
                    holder.flor_name_txt.setTextColor(context.resources.getColor(R.color.black))
                } else {

                    holder.numcard.setBackgroundResource(R.drawable.acc_white_border_ki)
                    holder.flor_num_txt.setTextColor(Color.BLACK)
                    holder.flor_name_txt.setTextColor(context.resources.getColor(R.color.base))
                }
            }*/


        } catch (e: Exception) {

        }


    }
}

class FlorsRecAdapterViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var flor_num_txt: TextView
    var flor_name_txt: TextView
    var numcard : RelativeLayout
    //   var cardback : RelativeLayout
    //var floor_selection_card: FloatingActionButton
    //   var user_floor_icon : ImageView


    init {
        flor_num_txt = itemView.findViewById(R.id.flor_num_txt)
        // cardback = itemView.findViewById(R.id.cardback)
        //floor_selection_card = itemView.findViewById(R.id.floor_selection_card)
        flor_name_txt = itemView.findViewById(R.id.flor_name_txt)
        numcard = itemView.findViewById(R.id.numcard)
        //  user_floor_icon = itemView.findViewById(R.id.user_floor_icon)


    }
}
