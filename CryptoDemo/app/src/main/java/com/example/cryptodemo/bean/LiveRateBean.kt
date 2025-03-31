package com.example.cryptodemo.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LiveRateBean(
    val ok: Boolean,
    val tiers: List<Tier>,
    val warning: String
):Parcelable


@Parcelize
data class Rate(
    val amount: String,
    val rate: String
):Parcelable


@Parcelize
data class Tier(
    val from_currency: String,
    val rates: List<Rate>,
    val time_stamp: Int,
    val to_currency: String
):Parcelable