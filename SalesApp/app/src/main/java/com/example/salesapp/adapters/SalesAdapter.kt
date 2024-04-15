package com.example.salesapp.adapters

import android.content.Context
import android.graphics.Movie
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesapp.R
import com.example.salesapp.adapters.SalesAdapter.SalesViewHolder
import com.example.salesapp.model.Product
import com.squareup.picasso.Picasso


class SalesAdapter(private var sales: ArrayList<Product>, private val contexto: Context) : RecyclerView.Adapter<SalesViewHolder>() {

    var onItemClick : ((Product)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
        return SalesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val p : Product = sales[position]
        val nome = p.nome
        val imagem = p.imagem
        val preco = p.preco
        val desconto = p.desconto
        val precosb = StringBuilder()
        precosb.append("Desde ").append(preco)
        val discountsb = StringBuilder()
        discountsb.append("- ").append(desconto)
        var beforeprice = -1.0

        if(preco!=null && desconto!=null){
            val pricelen = preco.length
            val desclen = desconto.length
            val amount = preco.substring(0,pricelen-1).replace(",",".").toDouble()
            val desc = desconto.substring(0,desclen-1)
            beforeprice = (amount * (desc.toDouble()/100)) + amount
        }

        val newbeforeprice = StringBuilder().append(String.format("%.2f", beforeprice)).append(" €")

        holder.antes.text = newbeforeprice
        holder.antes.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.nome.text = nome
        holder.price.text = precosb
        holder.discount.text = discountsb
        Picasso.get().load(imagem).into(holder.imagem)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(p)
        }
    }

    override fun getItemCount(): Int {
        return sales.size
    }

    fun filteredProducts(filteredList: List<Product>) {
        sales = ArrayList(filteredList)
        notifyDataSetChanged()
    }

    class SalesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var nome: TextView
        var price: TextView
        var discount: TextView
        var imagem: ImageView
        var antes: TextView

        init {
            nome = itemView.findViewById<View>(R.id.product_name) as TextView
            imagem = itemView.findViewById<View>(R.id.product_img) as ImageView
            price = itemView.findViewById<View>(R.id.product_price) as TextView
            discount = itemView.findViewById<View>(R.id.descontotxt) as TextView
            antes = itemView.findViewById<View>(R.id.beforeprice) as TextView

        }

    }
}