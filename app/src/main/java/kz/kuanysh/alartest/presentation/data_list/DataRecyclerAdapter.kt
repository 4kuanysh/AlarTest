package kz.kuanysh.alartest.presentation.data_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.kuanysh.alartest.databinding.ItemDataBinding
import kz.kuanysh.alartest.domain.model.Data

class DataRecyclerAdapter(
    private val onItemClicked: (Data.Item) -> Unit
) : RecyclerView.Adapter<DataRecyclerAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Data.Item>()

    var onViewHolderBound: ((Int) -> Unit)? = null

    fun addItems(data: List<Data.Item>) {
        val positionStart = items.size
        items.addAll(data)
        notifyItemRangeInserted(positionStart, data.size)
    }

    fun updateData(data: List<Data.Item>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
        onViewHolderBound?.invoke(position)
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(
        private val binding: ItemDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val glide = Glide.with(binding.root.context)

        fun bind(item: Data.Item) {
            with(binding) {
                glide
                    .load("https://miro.medium.com/max/1838/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg")
                    .into(image)
                title.text = item.name
                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }
}