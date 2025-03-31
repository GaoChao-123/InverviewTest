package com.example.cryptodemo.bean

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

/**
 *
 * @author: Pack
 * @date: 2025/3/31
 * @Desc：
 */

@Parcelize
class ListDateAll(
    var currencies: List<Currency>? = null,
    var wallet: List<Wallet>? = null,
    var tiers: List<Tier>? = null
) : Parcelable