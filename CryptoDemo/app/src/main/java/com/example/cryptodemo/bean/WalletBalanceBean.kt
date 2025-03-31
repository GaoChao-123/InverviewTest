package com.example.cryptodemo.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class WalletBalanceBean(
    val ok: Boolean,
    val wallet: List<Wallet>,
    val warning: String
):Parcelable

@Parcelize
data class Wallet(
    val amount: Double,
    val currency: String
):Parcelable