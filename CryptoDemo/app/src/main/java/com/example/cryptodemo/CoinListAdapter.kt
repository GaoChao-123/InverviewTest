package com.example.cryptodemo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.example.cryptodemo.bean.ListDateAll
import com.example.cryptodemo.bean.Wallet
import com.example.cryptodemo.databinding.ItemCoinBinding
import com.example.cryptodemo.utils.CalculatorUtils
import kotlinx.coroutines.flow.StateFlow

/**
 *
 * @author: Pack
 * @date: 2025/3/31
 * @Desc：
 */
class CoinListAdapter() : BaseQuickAdapter<Wallet, CoinListAdapter.VH>() {


    private lateinit var list: StateFlow<ListDateAll>


    fun setListDate(listDateAll: StateFlow<ListDateAll>){
        this.list = listDateAll
    }

    class VH(
        parent: ViewGroup,
        val binding: ItemCoinBinding = ItemCoinBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int, item: Wallet?) {
        val listdate = list.value
        if (item?.currency.equals("BTC") || item?.currency.equals("ETH") || item?.currency.equals("CRO")) {
            holder.binding.apply {
                val imgUrl = listdate.currencies?.get(position)?.colorful_image_url
                Glide.with(context).load(imgUrl).into(ivCoin)

                tvCoinName.text =
                    listdate.currencies?.get(position)?.symbol
                        ?: ("这个是标题" + (listdate.currencies?.get(
                            position
                        )?.symbol ?: "USD"))
                tvCoinAmount.text = listdate.wallet?.get(position)?.amount?.toDouble().toString();
                //设置总费用
                tvTotalUsd.text = "$" + listdate.tiers?.get(position)?.rates?.get(position)?.let {
                    CalculatorUtils.getTotalPriceByUnitAmount(
                        listdate.wallet?.get(position)?.amount.toString(), it.rate,
                        listdate.currencies?.get(position)?.display_decimal ?: 8
                    )
                }
            }
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}