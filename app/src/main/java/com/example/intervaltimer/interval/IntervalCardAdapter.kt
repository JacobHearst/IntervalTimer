package com.example.intervaltimer.interval

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.R
import com.example.room.Interval
import com.example.intervaltimer.Util

/**
 * A [RecyclerView.Adapter] responsible for processing previous orders
 *
 * @property intervals Copy of intervals list
 */
class IntervalCardAdapter : RecyclerView.Adapter<IntervalCardAdapter.ViewHolder>() {
    private var intervals: MutableList<Interval>? = null

    var wasSwiped = false

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     * Each data item is just a string in this case that is shown in a TextView.
     *
     * @param view The view being used to display items
     */
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    /**
     * Set the intervals being displayed
     *
     * @param items Updated orders list
     */
    fun setItems(items: List<Interval>) {
        this.intervals = items.sortedWith(compareBy{ it.index }).toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Create new views (invoked by the layout manager)
     *
     * @param parent Parent view
     * @param viewType Type of view holder to create
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.interval_list_item, parent, false)

        return ViewHolder(view)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder View holder that has been bound
     * @param position Position of the item in the array
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val intervalName = holder.view.findViewById<TextView>(R.id.intervalName)
        val intervalTime = holder.view.findViewById<TextView>(R.id.intervalTime)
        val editButton = holder.view.findViewById<ImageButton>(R.id.editButton)

        if (intervals != null) {
            val interval = this.intervals!![position]
            intervalName.text = interval.name
            intervalTime.text = interval.reps?.toString() ?: Util.getDurationLabel(
                interval.time!!
            )


            editButton.setOnClickListener {
                // TODO: Link to Rylee's modal
            }
        }
    }

    /**
     * Return size of the data set
     */
    override fun getItemCount() = intervals?.size ?: 0

    /**
     * Move an interval from one location to another
     *
     * @param from Index to move it from
     * @param to Index to move it to
     */
    fun moveItem(from: Int, to: Int) {
        val intervalCopy = intervals!![from]
        intervals?.removeAt(from)
        intervals?.add(to, intervalCopy)
    }

    fun insertItem(at: Int, interval: Interval) {
        intervals?.add(at, interval)
        notifyDataSetChanged()
    }

    fun deleteItem(interval: Interval) {
        intervals?.remove(interval)
        notifyDataSetChanged()
    }

    /**
     * Return the contents of the data set
     */
    fun getItems(): List<Interval> = this.intervals!!
}
