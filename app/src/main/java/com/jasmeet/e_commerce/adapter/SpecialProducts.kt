package com.jasmeet.e_commerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jasmeet.e_commerce.databinding.SpecialRvItemBinding
import com.jasmeet.e_commerce.model.Product

class SpecialProducts:RecyclerView.Adapter<SpecialProducts.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(private var binding: SpecialRvItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(product:Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageSpecialRvItem)
                tvAdName.text = product.name
                tvAdPrice.text = product.price.toString()

            }
        }
    }

    private  val diffCallback = object : DiffUtil.ItemCallback<Product>(){

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProducts.SpecialProductsViewHolder {

       return SpecialProductsViewHolder(
           SpecialRvItemBinding.inflate(
               LayoutInflater.from(parent.context),parent,false

           )
       )
    }

    override fun onBindViewHolder(holder: SpecialProducts.SpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
