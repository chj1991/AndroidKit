package com.sys.androidkit.feature.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sys.androidkit.feature.image.databinding.ItemGalleryImageBinding

class GalleryImageAdapter(
    private val onClick: (GalleryImage) -> Unit,
) : ListAdapter<GalleryRow, GalleryImageAdapter.VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemGalleryImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val binding: ItemGalleryImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(row: GalleryRow) {
            binding.ivThumb.load(row.image.uri) {
                crossfade(true)
                placeholder(R.drawable.ic_image_placeholder)
                error(R.drawable.ic_image_error)
            }
            binding.viewDim.visibility = if (row.selected) View.VISIBLE else View.GONE
            if (row.selected) {
                binding.tvBadge.visibility = View.VISIBLE
                binding.tvBadge.text = row.order?.toString() ?: "✓"
            } else {
                binding.tvBadge.visibility = View.GONE
            }
            binding.root.setOnClickListener { onClick(row.image) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<GalleryRow>() {
        override fun areItemsTheSame(oldItem: GalleryRow, newItem: GalleryRow): Boolean =
            oldItem.image.id == newItem.image.id

        override fun areContentsTheSame(oldItem: GalleryRow, newItem: GalleryRow): Boolean =
            oldItem == newItem
    }
}
