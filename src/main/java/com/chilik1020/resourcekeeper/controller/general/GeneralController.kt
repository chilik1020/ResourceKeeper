package com.chilik1020.resourcekeeper.controller.general

import com.chilik1020.resourcekeeper.MainApp
import com.chilik1020.resourcekeeper.base.BaseController
import com.chilik1020.resourcekeeper.ui.JPanelGeneral
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.swing.JOptionPane

class GeneralController : BaseController() {
    @Inject
    lateinit var view: JPanelGeneral

    init {
        GlobalScope.launch {
            while (true) {
                delay(50000)
                val name1 = view.name1TextField.text.trim()
                val name2 = view.name1TextField.text.trim()

                if (name1 == "" && name2 == "") {
                    val result = JOptionPane.showInputDialog(
                            MainApp.mainView,
                            "Кто за комбайном?")
                    view.name1TextField.text = result
                }
            }
        }
    }
}