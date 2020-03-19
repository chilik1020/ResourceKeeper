package com.chilik1020.resourcekeeper.ui

import java.awt.*
import javax.swing.*
import javax.swing.border.MatteBorder

class JPanelGeneral : JPanel() {

    private val fontSize16 = Font("Serif", Font.BOLD, 16)
    private val labelName = JLabel("Смена")
    var name1TextField: JFormattedTextField = JFormattedTextField()
    var name2TextField: JFormattedTextField = JFormattedTextField()

    init {
        isVisible = true
        initComponents()
    }

    private fun initComponents() {
        layout = GridBagLayout()
        border = MatteBorder(3, 1, 1, 1, Color.GRAY)

        (layout as GridBagLayout).columnWidths = intArrayOf(5, 90, 150, 150, 140, 5)

        labelName.font = fontSize16
        add(labelName, GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))
        name1TextField.preferredSize = Dimension(150,20)
        add(name1TextField, GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        name2TextField.preferredSize = Dimension(150,20)
        add(name2TextField, GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

    }
}