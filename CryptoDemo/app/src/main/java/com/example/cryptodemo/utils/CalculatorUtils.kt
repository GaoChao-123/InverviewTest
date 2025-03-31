package com.example.cryptodemo.utils

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.lazy.layout.IntervalList
import java.math.BigDecimal

/**
 *
 * @author: Pack
 * @date: 2025/3/31
 * @Desc 计算价格工具类
 */
 object CalculatorUtils {

    /**
     * 余额=数量 * 汇率
     */
    fun getTotalPriceByUnitAmount(unitAmount: String,rate:String,disPlayDecimal:Int=8): String {
        try {
            if (TextUtils.isEmpty(unitAmount)) return "0.00"

            val mul = mulScaleAuto(
                BigDecimal(unitAmount), BigDecimal(rate), disPlayDecimal
            )
            return mul.toPlainString()
        } catch (e: Exception) {
           Log.d("error",e.message+"")
            return "0.00"
        }
    }

    /**
     * 乘法(根据传入的参数进行精度控制)
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    fun mulScaleAuto(a: BigDecimal, b: BigDecimal?, scale: Int): BigDecimal {
        return a.multiply(b).setScale(scale, BigDecimal.ROUND_DOWN)
    }

}