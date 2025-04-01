package com.example.cryptodemo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodemo.bean.CoinCurrentBean
import com.example.cryptodemo.bean.Currency
import com.example.cryptodemo.bean.LiveRateBean
import com.example.cryptodemo.bean.Tier
import com.example.cryptodemo.bean.Wallet
import com.example.cryptodemo.bean.WalletBalanceBean
import com.example.cryptodemo.utils.Ext
import com.example.cryptodemo.utils.GsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 *
 * @author: Pack
 * @date: 2025/4/2
 * @Desc：
 */
class UserViewModel : ViewModel() {

    // 币种列表
    var currenciesFlow: MutableStateFlow<List<Currency>>? = null

    // 币种列表
    var walletFlow: MutableStateFlow<List<Wallet>>? = null

    //汇率
    var tiersFlow: MutableStateFlow<List<Tier>>? = null


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListDate() {

        viewModelScope.launch {

            val flow1 = flow {
                val coinCurrentBean =
                    GsonUtils.fromJson(Ext.getCurrenciesJson(), CoinCurrentBean::class.java)
                emit(coinCurrentBean)
            }.flowOn(Dispatchers.IO)
                .onStart { Log.d("start", "onStart") }
                .filterNotNull()
                .catch { Log.d("expecting", "expecting") }

            val flow2 = flow {
                val walletBean =
                    GsonUtils.fromJson(Ext.getWalletBalanceJson(), WalletBalanceBean::class.java)
                emit(walletBean)
            }.flowOn(Dispatchers.IO)
                .onStart { Log.d("start", "onStart") }
                .filterNotNull()
                .catch { Log.d("expecting", "expecting") }


            val flow3 = flow {
                val liveRateBean =
                    GsonUtils.fromJson(Ext.getLiveRateJson(), LiveRateBean::class.java)
                emit(liveRateBean)
            }.flowOn(Dispatchers.IO)
                .onStart { Log.d("start", "onStart") }
                .filterNotNull()
                .catch { Log.d("expecting", "expecting") }


            val listFlow = flowOf(flow1, flow2, flow3)

            listFlow.flattenConcat()
                .collect { value ->
                    when (value) {
                        is Currency -> {
                            currenciesFlow?.value?.toMutableList()?.add(value)
                        }

                        is Wallet -> {
                            walletFlow?.value?.toMutableList()?.add(value)
                        }

                        is Tier -> {
                            tiersFlow?.value?.toMutableList()?.add(value)
                        }
                    }
                }


        }


    }


}