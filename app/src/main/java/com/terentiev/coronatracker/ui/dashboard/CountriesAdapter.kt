package com.terentiev.coronatracker.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.terentiev.coronatracker.R
import com.terentiev.coronatracker.data.AverageInfo
import com.terentiev.coronatracker.data.Country
import kotlinx.android.synthetic.main.country_card.view.*
import kotlinx.android.synthetic.main.worldwide_card.view.*
import java.text.SimpleDateFormat
import java.util.*

const val TYPE_HEADER: Int = 0
const val TYPE_ITEM: Int = 1

class CountriesAdapter(countriesEvents: ItemEvents) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private lateinit var countries: List<Country>
    private var filteredCountries = listOf<Country>()
    private var averageInfo: AverageInfo? = null
    private val listener: ItemEvents = countriesEvents
    private var filterString: String = ""


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, country: Country, listener: ItemEvents) {
            itemView.tv_place.text = "#${position + 1}"
            itemView.tv_country.text = country.country
            itemView.tv_cases_num.text = country.cases.toString()
            itemView.tv_deaths_num.text = country.deaths.toString()
            itemView.tv_today_cases_num.text = country.todayCases.toString()
            itemView.tv_today_deaths_num.text = country.todayDeaths.toString()
            itemView.tv_recovered_num.text = country.recovered.toString()
            if (country.countryInfo.flag.contains("unknow")) {
                itemView.iv_flag.visibility = View.GONE
            } else {
                itemView.iv_flag.visibility = View.VISIBLE
                Glide
                    .with(itemView.context)
                    .load(country.countryInfo.flag)
                    .transform(CenterCrop(), RoundedCorners(11))
                    .into(itemView.iv_flag)
            }


            itemView.setOnLongClickListener {
                listener.onItemLongClicked(country)
                true
            }
            itemView.setOnClickListener {
                listener.onItemClicked(position, country)
            }

        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(averageInfo: AverageInfo?) {
            if (averageInfo == null) {
                itemView.visibility = View.GONE
                itemView.layoutParams = RecyclerView.LayoutParams(0, 1)
            } else {
                itemView.visibility = View.VISIBLE
                itemView.layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                itemView.tv_ww_cases_num.text = averageInfo.cases.toString()
                itemView.tv_ww_deaths_num.text = averageInfo.deaths.toString()
                itemView.tv_ww_recovered_num.text = averageInfo.recovered.toString()
                itemView.tv_updated_at.text = SimpleDateFormat(
                    "dd MMM yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date(averageInfo.updated))
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    interface ItemEvents {
        fun onItemLongClicked(country: Country)
        fun onItemClicked(position: Int, country: Country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.worldwide_card, parent, false)
            )
        } else {
            ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.country_card, parent, false)
            )
        }
    }

    fun setCountries(countries: List<Country>) {
        this.countries = countries
        filter.filter(filterString)
        notifyDataSetChanged()
    }

    fun setAverageInfo(averageInfo: AverageInfo) {
        this.averageInfo = averageInfo
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (averageInfo != null) {
            filteredCountries.size + 1
        } else {
            filteredCountries.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_HEADER) {
            (holder as HeaderViewHolder).bind(averageInfo)
        } else {
            val actualPosition =
                countries.indexOf(countries.single { country -> country.country == filteredCountries[position - 1].country })
            (holder as ItemViewHolder).bind(
                actualPosition,
                filteredCountries[position - 1],
                listener
            )
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private var bufferList = listOf<Country>()  // to avoid Inconsistency error
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filterString = charString
                bufferList = if (charString.isEmpty()) {
                    countries
                } else {
                    val filteredList = arrayListOf<Country>()
                    for (row in countries) {
                        if ((row.country.toLowerCase().contains(charString.toLowerCase())
                                    )
                            || (row.countryInfo.iso2 != null && row.countryInfo.iso2!!.toLowerCase()
                                .contains(charString.toLowerCase())
                                    )
                            || (row.countryInfo.iso3 != null && row.countryInfo.iso3!!.toLowerCase()
                                .contains(charString.toLowerCase())
                                    )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = bufferList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (p1?.values != null) {
                    bufferList = p1?.values as List<Country>
                    filteredCountries = bufferList
//                    filteredCountries = p1?.values as List<Country>
                    notifyDataSetChanged()
                }
            }

        }
    }

}
