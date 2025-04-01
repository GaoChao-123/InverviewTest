package com.example.cryptodemo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptodemo.bean.ListDateAll
import com.example.cryptodemo.databinding.ActivityMainBinding
import com.example.cryptodemo.utils.CalculatorUtils
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {


    private lateinit var listDateAll: StateFlow<ListDateAll>
    private lateinit var coinListAdapter: CoinListAdapter

    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initDate()
        initView()
        setSelectChain()
    }


    private fun initDate() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this


        viewModel.getListDate()  //请求解析数据

        if (viewModel.currenciesFlow?.value?.isNotEmpty() == true &&
            viewModel.walletFlow?.value?.isNotEmpty() == true &&
            viewModel.tiersFlow?.value?.isNotEmpty() == true
        ) {
            listDateAll.value.apply {
                currencies = viewModel.currenciesFlow?.value
                wallet = viewModel.walletFlow?.value
                tiers = viewModel.tiersFlow?.value
            }
        }
    }


    private fun initView() {
        coinListAdapter = CoinListAdapter()
        coinListAdapter.items = listDateAll.value.wallet!!
        coinListAdapter.setListDate(listDateAll)
        binding.listRvCoin.layoutManager = CustomLinearLayoutManager(this)
        binding.listRvCoin.adapter = coinListAdapter
    }

    val selectCoinType: Int = 1   //btc

    @SuppressLint("SetTextI18n")
    private fun setSelectChain() {    //计算默认选择主链price 假数据
        val totalPrice = CalculatorUtils.getTotalPriceByUnitAmount("", "", 8) ?: "36.8"

        if (listDateAll.value.currencies?.isNotEmpty() == true)
            binding.tvTotal.text = "$" + totalPrice + "USD"
    }


    //设置empty

//    @SuppressLint("NotifyDataSetChanged")
//    private fun initEmptyView(): View {
////        emptyView = layoutInflater.inflate(
////            R.layout.empty_view_bg1, binding.web3MainChainRv, false
////        )
//        return View()
//    }

    inner class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        override fun canScrollHorizontally(): Boolean {
            return false
        }

        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}