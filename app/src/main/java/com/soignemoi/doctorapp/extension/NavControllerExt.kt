package com.soignemoi.doctorapp.extension

import android.os.Bundle
import androidx.navigation.NavController

fun NavController.navigateSafe(resId: Int, args: Bundle? = null): Boolean {
    return try {
        navigate(resId, args)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}