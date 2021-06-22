package com.chilik1020.resourcekeeper.ui

import com.chilik1020.resourcekeeper.utils.ColorUtil
import com.chilik1020.resourcekeeper.utils.JsonConfig
import org.jfree.ui.tabbedui.VerticalLayout
import java.awt.*
import javax.swing.*
import javax.swing.border.MatteBorder

class JPanelTemperature: JPanel() {

    private val labelName = JLabel("Температура")

    private val fontSize16 = Font("Serif", Font.BOLD, 16)
    private val fontSize14 = Font("Serif", Font.BOLD, 14)


//    private val lblLogOn: JLabel = JLabel("LogOn")
//    val logOn: JCheckBox = JCheckBox()

    val panelOwenCharts = JPanelOwenTemperatureChartsPanel()
    val panelLtuInnerCharts = JPanelLtuInnerTemperatureChartsPanel()

    init {
        isVisible = true
        initComponents()
    }

    private fun initComponents() {

        layout = VerticalLayout()
        //background = Color.LIGHT_GRAY
        val panelNorth = JPanel()

        /*
         * Panel temperature data
         **************************************************************************************************************/

        panelNorth.apply {
            layout = GridBagLayout()
        //    background = Color.LIGHT_GRAY
        }

      //  border = MatteBorder(3, 1, 1, 1, Color.GRAY)
        border = BorderFactory.createMatteBorder(1,1,1,1, ColorUtil.panelBorderColor)

        (panelNorth.layout as GridBagLayout).columnWidths = intArrayOf(5, 110, 50,50, 100, 100,80,5)

        (panelNorth.layout as GridBagLayout).rowHeights = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        (panelNorth.layout as GridBagLayout).columnWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4)
        (panelNorth.layout as GridBagLayout).rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4)

     //   labelName.font = fontSize16
        labelName.preferredSize = Dimension(110,20)
        panelNorth.add(labelName,GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5,5, 5, 5), 0, 0))


//        //---- LogOn ----
//        lblLogOn.preferredSize = Dimension(70,20)
//        panelNorth.add(lblLogOn, GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
//                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
//                Insets(5, 5, 5, 5), 0, 0))
//
//        logOn.isSelected = true
//        logOn.preferredSize = Dimension(30,20)
//        panelNorth.add(logOn, GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
//                GridBagConstraints.EAST, GridBagConstraints.BOTH,
//                Insets(5, 5, 5, 5), 0, 0))

        val tabbedChartsPanel = JTabbedPane()

        tabbedChartsPanel.apply {
        //    background = Color.LIGHT_GRAY
        }

        tabbedChartsPanel.add("Овен УКТ38", panelOwenCharts)
        tabbedChartsPanel.add("Датчики LTU", panelLtuInnerCharts)

        add(panelNorth)
        add(tabbedChartsPanel)
    }
}