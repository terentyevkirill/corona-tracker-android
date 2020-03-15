package com.terentiev.coronatracker.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.terentiev.coronatracker.R
import com.terentiev.coronatracker.data.Country
import kotlinx.android.synthetic.main.country_cardview.view.*
import org.w3c.dom.Text

class CountriesAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.tv_place
        val country: TextView = itemView.tv_country
        val cases: TextView = itemView.tv_cases_num
        val todayCases: TextView = itemView.tv_today_cases_num
        val deaths: TextView = itemView.tv_deaths_num
        val todayDeaths: TextView = itemView.tv_today_deaths_num
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.place.text = (position + 1).toString()
        holder.country.text = countries[position].country
        holder.cases.text = countries[position].cases.toString()
        holder.deaths.text = countries[position].deaths.toString()
        holder.todayCases.text = countries[position].todayCases.toString()
        holder.todayDeaths.text = countries[position].todayDeaths.toString()
    }

}
