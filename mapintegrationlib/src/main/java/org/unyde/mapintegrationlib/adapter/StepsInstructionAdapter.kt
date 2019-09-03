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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.unyde.mapintegrationlib.R
import org.unyde.mapintegrationlib.util.Constants
import java.util.ArrayList

class StepsInstructionAdapter(internal var instruction_list: MutableList<String>?
                      ,internal var  instruction_site_list :MutableList<String>?
                      ,internal var  instruction_direction_list :MutableList<Int>?
                      ,internal var context: Context
) : RecyclerView.Adapter<StepsInstructionAdapterViewholder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsInstructionAdapterViewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.steps_instruction_list, parent, false)

        return StepsInstructionAdapterViewholder(view)
    }

    override fun getItemCount(): Int {

         if (instruction_list?.size != 0) {
             return  instruction_list!!.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: StepsInstructionAdapterViewholder, position: Int) {



        when (instruction_direction_list!!.get(position)) {
            Constants.DESTINATION -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.location_current_map)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }

            Constants.HEAD_TOWARDS_EAST -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_east_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }

            Constants.HEAD_TOWARDS_NORTH -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_north_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.HEAD_TOWARDS_NORTH_EAST -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_north_east_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.HEAD_TOWARDS_NORTH_WEST -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_north_west_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.HEAD_TOWARDS_SOUTH -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_south_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.HEAD_TOWARDS_SOUTH_EAST -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_south_east_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.HEAD_TOWARDS_SOUTH_WEST -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_south_west_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
//            Constants.HEAD_TOWARDS_SOUTH_WEST -> {
//                holder.instruction_image.background = context.resources.getDrawable(R.drawable.location_current_map)
//                holder.instruction_text.setText(instruction_list!!.get(position))
//            }
            Constants.HEAD_TOWARDS_WEST -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.head_towards_west__black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.TAKE_SLIGHT_LEFT -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.take_slight_left_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.TAKE_SLIGHT_RIGHT -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.take_slight_right_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.TAKE_STRAIGHT -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.take_straight_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.TURN_LEFT -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.turn_left_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }
            Constants.TURN_RIGHT -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.turn_right_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }

            Constants.LIFT -> {
                holder.instruction_image.background = context.resources.getDrawable(R.drawable.lift_icon_black)
                holder.instruction_text.setText(instruction_list!!.get(position))
            }


        }









        /*      holder.itemView.setOnClickListener {
                  floorClickListner.onFloorItemClick(position)
                  row_index=position;
                  notifyDataSetChanged()
              }

              if (flor_list!!.get(position).floor_number<1){
                  holder.flor_num_txt.setText(""+flor_list!!.get(position).floor_alias)
              }
              else
              {
                  holder.flor_num_txt.setText(""+flor_list!!.get(position).floor_number +"F")
              }

              if (selected_floor == 101){

                  if (Current_floor.get(position) == 1){
                      holder.floor_selection_card.setBackgroundTintList(ColorStateList.valueOf( context.resources.getColor(R.color.newcolor)))
                      holder.flor_num_txt.setTextColor(Color.WHITE)
                  }
                  else{
                      holder.floor_selection_card.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT))
                      holder.flor_num_txt.setTextColor(Color.BLACK)
                  }

              }
              else
              {
                  if(row_index==position){

                      holder.floor_selection_card.setBackgroundTintList(ColorStateList.valueOf(context.resources.getColor(R.color.newcolor)))
                      holder.flor_num_txt.setTextColor(Color.WHITE)
      //            // holder.tct_back_image.background = context.resources.getDrawable(R.drawable.offer_trend_selected)
      //            holder.nametct.setTypeface(holder.nametct.getTypeface(), Typeface.BOLD);
      //            holder.nametct.setTextColor(context.resources.getColor(R.color.black))
      //           holder.topcur.visibility = View.VISIBLE
                  }
                  else
                  {

                      holder.floor_selection_card.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT))
                      holder.flor_num_txt.setTextColor(Color.BLACK)
      //            //holder.tct_back_image.background = context.resources.getDrawable(R.drawable.outline_top)
      //            holder.nametct.setTextColor(context.resources.getColor(R.color.greysix))
      //            holder.nametct.setTypeface(holder.nametct.getTypeface(), Typeface.NORMAL);
      //            holder.topcur.visibility = View.INVISIBLE
                  }
              }

      */


    }
}

class StepsInstructionAdapterViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var instruction_steps: TextView
    var instruction_text : TextView
    var instruction_image : ImageView


    init {
        instruction_steps = itemView.findViewById(R.id.instruction_step)
        instruction_text = itemView.findViewById(R.id.instruction_text)
        instruction_image = itemView.findViewById(R.id.instruction_image)


    }
}
