package android.example.jsonrecyclerview2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class RvAdapter(ctx: Context, private val doctorsArrayList: ArrayList<Data>,val listeer:(Data)->Unit):
    RecyclerView.Adapter<RvAdapter.MyViewHolder>(), Filterable {
    private var flag: Boolean = true


    private val mainList = doctorsArrayList
    private val searchList = ArrayList<Data>(doctorsArrayList)
    private val genderList = ArrayList<Data>()



    private val inflater: LayoutInflater

    init {

        inflater = LayoutInflater.from(ctx)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_piece, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvAdapter.MyViewHolder, position: Int) {


        doctorsArrayList[position].imgurl.let { Picasso.get().load(it).into(holder.iv) }
        holder.name.setText(doctorsArrayList[position].name)
        val currentItem = mainList[position]
      //  holder.bind(currentItem)
        holder.bindView(currentItem,listeer)


    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
/*
        var name: TextView
               var iv: ImageView


                init { name = itemView.findViewById<View>(R.id.songTitle) as TextView
                    iv = itemView.findViewById<View>(R.id.coverImage) as ImageView

                }

               fun bind(data: Data) {
                    name.text = data.name

                }

           */




        val iv = itemView.findViewById<ImageView>(R.id.doctorImage)
        val name = itemView.findViewById<TextView>(R.id.doctorName)


        fun bindView(data: Data, listener: (Data) -> Unit) {
           // iv.setImageResource(data.imgurl)
            Glide.with(itemView.context).load(data.imgurl).into(iv)
            name.text = data.name
            itemView.setOnClickListener { listener(data) }
        }




    }




    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.maleCheck -> {
                    if (checked) {
                        flag = false
                        mainList.forEach {
                            if (it.gender == "male") {
                                genderList.add(it)
                                notifyDataSetChanged()

                            }
                        }


                    } else {
                        genderList.addAll(doctorsArrayList)
                        notifyDataSetChanged()
                        genderList.clear()
                        flag = true

                    }
                }
                R.id.femaleCheck -> {
                    if (checked) {
                        flag = false

                        mainList.forEach {
                            if (it.gender == "female") {
                                genderList.add(it)
                                notifyDataSetChanged()
                            }
                        }
                    } else {
                        genderList.addAll(doctorsArrayList)
                        notifyDataSetChanged()
                        genderList.clear()
                        flag = true


                    }
                }
            }
        }

    }


    override fun getFilter(): Filter {
        val filteredList = ArrayList<Data>()
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {

                if (constraint.isBlank() or constraint.isEmpty()) {
                    filteredList.addAll(searchList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    if (flag) {
                        searchList.forEach {

                            if (it.name?.lowercase(Locale.ROOT)?.contains(filterPattern) == true) {

                                filteredList.add(it)
                            }
                        }
                    }
                    if (!flag) {
                        genderList.forEach {

                            if (it.name?.lowercase(Locale.ROOT)?.contains(filterPattern) == true) {

                                filteredList.add(it)
                            }
                        }

                    }


                }

                val result = FilterResults()
                result.values = filteredList

                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mainList.clear()
                mainList.addAll(results!!.values as List<Data>)
                notifyDataSetChanged()
            }
        }
    }


}

