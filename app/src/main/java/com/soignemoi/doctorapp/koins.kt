package com.soignemoi.doctorapp

import com.soignemoi.doctorapp.dashboard.DashboardViewModel
import com.soignemoi.doctorapp.login.LoginViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

fun allModules() = listOf<Module>(
    loginActivityModule(),
    dashboardActivityModule()
)

fun loginActivityModule() = module {
    viewModel { LoginViewModel() }
}

fun dashboardActivityModule() = module{
    viewModel { DashboardViewModel() }
}