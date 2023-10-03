package com.example.playstorecollection.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playstorecollection.MainActivity
import com.example.playstorecollection.MainActivity2
import com.example.playstorecollection.R
import com.example.playstorecollection.WebviewActivity2



class AppsAdapter(
    private var data: ArrayList<String>,
    private val context: Context,
    private val isFromHomepage: Boolean
) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item2, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        var fileName = ""
        if (item.contains(".zip")){
            fileName = item.replace(".zip","")
            Log.d("ccc",fileName)
        }
        holder.textView.text = fileName
//        if (fileName.equals("Google")) {
//            holder.itemImage.setImageResource(R.drawable.google_icon_logo_svgrepo_com)
//        } else if (fileName.equals("Uber")) {
//            holder.itemImage.setImageResource(R.drawable.uber)
//        } else if (fileName.equals("Facebook")) {
//            holder.itemImage.setImageResource(R.drawable.facebook_2_logo_svgrepo_com)
//        }else if (fileName.equals("Instagram")) {
//            holder.itemImage.setImageResource(R.drawable.instagram_svgrepo_com)
//        }else if (fileName.equals("BigBasket")) {
//            holder.itemImage.setImageResource(R.drawable.ic_bigbasketplaystore)
//        }else if (fileName.equals("Flip")) {
//            holder.itemImage.setImageResource(R.drawable.flipkart_icon)
//        }
//        else if (fileName.equals("Shopsy")) {
//            holder.itemImage.setImageResource(R.drawable.ic_shopsyplaystore)
//        } else if (fileName.equals("DailyHunt")) {
//            holder.itemImage.setImageResource(R.drawable.ic_newsplaystore)
//        }else if (fileName.equals("Watchlist")) {
//            holder.itemImage.setImageResource(R.drawable.watchlist)
//        }
//        else if (fileName.equals("NativeFeature")) {
//            holder.itemImage.setImageResource(R.drawable.maps)
//        }
//        else if (fileName.equals("Rapido")) {
//            holder.itemImage.setImageResource(R.drawable.rapido)
//        }

         if (fileName.equals("Activity")) {
            holder.itemImage.setImageResource(R.drawable.activity)
        } else if (fileName.equals("Earnings")) {
            holder.itemImage.setImageResource(R.drawable.earnings)
        } else if (fileName.equals("Greetings")) {
            holder.itemImage.setImageResource(R.drawable.greetings)
        }else if (fileName.equals("LMS")) {
            holder.itemImage.setImageResource(R.drawable.lms)
        }else if (fileName.equals("Reviews")) {
            holder.itemImage.setImageResource(R.drawable.reviews)
        }else if (fileName.equals("Team")) {
            holder.itemImage.setImageResource(R.drawable.team)
        }else if (fileName.equals("Performance")) {
            holder.itemImage.setImageResource(R.drawable.performance)
        }

//        if (isFromHomepage) {
//            holder.itemView.setOnLongClickListener(OnLongClickListener { v ->
//                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
//                alertDialog.setTitle(holder.textView.text)
//                alertDialog.setMessage("Do you want to uninstall the app ")
//                alertDialog.setPositiveButton("YES",
//                    DialogInterface.OnClickListener { dialog, which ->
//                        if (context is MainActivity2) {
//                            (context as MainActivity2).handleDeleteItem(position)
//                        }
//                    })
//                alertDialog.setNegativeButton("CANCEL",
//                    DialogInterface.OnClickListener { dialog, which ->
//                        dialog.cancel()
//                    })
//
//                val dialog: AlertDialog = alertDialog.create()
//                dialog.show()
//                true
//            })
//        }



        holder.itemView.setOnClickListener {

            if (isFromHomepage) {

                if (context is MainActivity2) {
                    (context as MainActivity2).downloadFromFirebase(item,context)
//                    val myIntent = Intent(context, WebviewActivity2::class.java)
//                    myIntent.putExtra("appName",item)
//                   context.startActivity(myIntent)
                }
            } else {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
                alertDialog.setTitle(holder.textView.text)
                alertDialog.setMessage("Do you want to install the app ")
                alertDialog.setPositiveButton("YES",
                    DialogInterface.OnClickListener { dialog, which ->

                        if (context is MainActivity) {
                            (context as MainActivity).downloadFromFirebase(item)
                        }
                        val myIntent = Intent(context, MainActivity2::class.java)
                        myIntent.putExtra("installapp", item)
                        context.startActivity(myIntent)
                    })
                alertDialog.setNegativeButton("CANCEL",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })

                val dialog: AlertDialog = alertDialog.create()
                dialog.show()

            }
        }
    }
    fun filterList(filteredList: ArrayList<String>) {
        data = filteredList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val textView: TextView = itemView.findViewById(R.id.tvTitle)
    }
}