package com.example.cryptodemo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptodemo.bean.CoinCurrentBean
import com.example.cryptodemo.bean.Currency
import com.example.cryptodemo.bean.ListDateAll
import com.example.cryptodemo.bean.LiveRateBean
import com.example.cryptodemo.bean.Tier
import com.example.cryptodemo.bean.Wallet
import com.example.cryptodemo.bean.WalletBalanceBean
import com.example.cryptodemo.databinding.ActivityMainBinding
import com.example.cryptodemo.utils.CalculatorUtils
import com.example.cryptodemo.utils.Ext
import com.example.cryptodemo.utils.GsonUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    // 币种列表
    private var currenciesFlow: MutableStateFlow<List<Currency>>? = null

    // 币种列表
    private var walletFlow: MutableStateFlow<List<Wallet>>? = null

    //汇率
    private var tiersFlow: MutableStateFlow<List<Tier>>? = null
    lateinit var listDateAll: StateFlow<ListDateAll>
    private lateinit var coinListAdapter: CoinListAdapter

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        initDate()
        initView()
        setSelectChain()
    }


    private fun initDate() {
        lifecycleScope.launch {
            val coinCurrentBean =
                GsonUtils.fromJson(Ext.getCurrenciesJson(), CoinCurrentBean::class.java)
            if (coinCurrentBean.currencies.isNotEmpty()) {
                currenciesFlow?.value = coinCurrentBean.currencies.toMutableList()
            }

            val walletBean =
                GsonUtils.fromJson(Ext.getWalletBalanceJson(), WalletBalanceBean::class.java)
            if (walletBean.wallet.isNotEmpty()) {
                walletBean.wallet.toMutableList()
                walletFlow?.value = walletBean.wallet.toMutableList()
            }
        }

        val liveRateBean = GsonUtils.fromJson(Ext.getLiveRateJson(), LiveRateBean::class.java)
        if (liveRateBean.tiers.isNotEmpty()) {
            liveRateBean.tiers.toMutableList().asFlow().onEach {
                tiersFlow?.value = liveRateBean.tiers.toMutableList()
            }
        }

        if (currenciesFlow?.value?.isNotEmpty() == true &&
            walletFlow?.value?.isNotEmpty() == true &&
            tiersFlow?.value?.isNotEmpty() == true
        ) {

            listDateAll.value.apply {
                currencies = currenciesFlow?.value
                wallet = walletFlow?.value
                tiers = tiersFlow?.value
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