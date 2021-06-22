package com.chilik1020.resourcekeeper

import com.chilik1020.resourcekeeper.di.DeviceModule

import com.chilik1020.resourcekeeper.ui.MainView
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatIntelliJLaf
import com.formdev.flatlaf.FlatLightLaf
import com.jtattoo.plaf.acryl.AcrylLookAndFeel
import di.AppComponent
import di.BufferModule
import di.DaggerAppComponent
import di.ViewModule
import javax.swing.UIManager

class MainApp {
    companion object {
        lateinit var mainView: MainView
        val injectorComponent: AppComponent = DaggerAppComponent
                .builder()
                .bufferModule(BufferModule)
                .deviceModule(DeviceModule)
                .viewModule(ViewModule)
                .build()

        @JvmStatic
        fun main(args: Array<String>) {
            JsonConfig.readConfig()

        //    UIManager.setLookAndFeel(AcrylLookAndFeel())
      //      UIManager.setLookAndFeel(FlatDarkLaf())
            UIManager.setLookAndFeel(FlatDarculaLaf())
//            UIManager.setLookAndFeel(MintLookAndFeel())
            mainView = MainView()
        }
    }

}
