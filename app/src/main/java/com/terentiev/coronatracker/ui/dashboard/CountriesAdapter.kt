package com.terentiev.coronatracker.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.terentiev.coronatracker.R
import com.terentiev.coronatracker.data.Country
import kotlinx.android.synthetic.main.country_cardview.view.*

class CountriesAdapter :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>(), Filterable {
    private lateinit var countries: List<Country>
    private var filteredCountries = listOf<Country>()

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

    fun setCountries(countries: List<Country>) {
        this.countries = countries
        if (filteredCountries.isEmpty()) {
            this.filteredCountries = countries
        } else {
        }

        notifyDataSetChanged()
    }

    override fun getItemCount() = filteredCountries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.place.text = "#" + (position + 1).toString()
        holder.country.text = filteredCountries[position].country
        holder.cases.text = filteredCountries[position].cases.toString()
        holder.deaths.text = filteredCountries[position].deaths.toString()
        holder.todayCases.text = filteredCountries[position].todayCases.toString()
        holder.todayDeaths.text = filteredCountries[position].todayDeaths.toString()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredCountries = if (charString.isEmpty()) {
                    countries
                } else {
                    val filteredList = arrayListOf<Country>()
                    for (row in countries) {
                        if (row.country!!.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredCountries
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredCountries = p1?.values as List<Country>
                notifyDataSetChanged()
            }

        }
    }

}
