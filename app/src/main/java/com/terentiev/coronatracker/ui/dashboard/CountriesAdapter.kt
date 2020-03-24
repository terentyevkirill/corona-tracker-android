package com.terentiev.coronatracker.ui.dashboard

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.terentiev.coronatracker.R
import com.terentiev.coronatracker.data.Country
import kotlinx.android.synthetic.main.country_card.view.*

class CountriesAdapter(countriesEvents: ItemEvents) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>(), Filterable {
    private lateinit var countries: List<Country>
    private var filteredCountries = listOf<Country>()
    private val listener: ItemEvents = countriesEvents
    private var filterString: String = ""

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, country: Country, listener: ItemEvents) {
            itemView.tv_place.text = "#${position + 1}"
            itemView.tv_country.text = country.country
            itemView.tv_cases_num.text = country.cases.toString()
            itemView.tv_deaths_num.text = country.deaths.toString()
            itemView.tv_today_cases_num.text = country.todayCases.toString()
            itemView.tv_today_deaths_num.text = country.todayDeaths.toString()
            itemView.tv_recovered_num.text = country.recovered.toString()
            Glide
                .with(itemView.context)
                .load("https://www.countryflags.io/${country.countryInfo.iso2}/shiny/64.png")
                .into(itemView.iv_flag)

            itemView.setOnLongClickListener {
                listener.onItemLongClicked(country)
                true
            }
            itemView.setOnClickListener {
                listener.onItemClicked(position, country)
            }

        }
    }

    interface ItemEvents {
        fun onItemLongClicked(country: Country)
        fun onItemClicked(position: Int, country: Country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_card, parent, false)
        return ViewHolder(view)
    }

    fun setCountries(countries: List<Country>) {
        this.countries = countries
        filter.filter(filterString)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredCountries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actualPosition =
            countries.indexOf(countries.single { country -> country.country == filteredCountries[position].country })
        holder.bind(actualPosition, filteredCountries[position], listener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filterString = charString
                filteredCountries = if (charString.isEmpty()) {
                    countries
                } else {
                    val filteredList = arrayListOf<Country>()
                    for (row in countries) {
                        if (row.country!!.toLowerCase().contains(charString.toLowerCase())
                            || (row.countryInfo.iso2!!.toLowerCase()
                                .contains(charString.toLowerCase()) && !row.countryInfo.iso2.equals(
                                "NO DATA"
                            ))
                            || (row.countryInfo.iso3!!.toLowerCase()
                                .contains(charString.toLowerCase()) && !row.countryInfo.iso3.equals(
                                "NO DATA"
                            ))
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
