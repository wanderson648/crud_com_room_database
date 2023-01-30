package com.wo.orgs.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wo.orgs.R
import com.wo.orgs.databinding.ProductItemBinding
import com.wo.orgs.extensions.formatterToCoinBrazil
import com.wo.orgs.extensions.tryRefreshImage
import com.wo.orgs.model.Product
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ListProductAdapter(
    private val context: Context,
    products: List<Product> = emptyList(),
    var whenItemClickListener: (product: Product) -> Unit = {}
) : RecyclerView.Adapter<ListProductAdapter.ProductViewHolder>() {

    private val products = products.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    fun updateList(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }


    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val title = binding.txtProductTitle
            val desc = binding.txtProductDescription
            val amount = binding.txtProductAmount
            val rvItemCard = binding.card

            title.text = product.name
            desc.text = product.description
            amount.text = product.amount.formatterToCoinBrazil()

            val visibility = if (product.image == null) View.GONE else View.VISIBLE
            binding.imgProductItem.visibility = visibility

            binding.imgProductItem.tryRefreshImage(product.image)

            rvItemCard.setOnClickListener {
                whenItemClickListener.invoke(product)
            }

        }

    }

}