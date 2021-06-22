package com.chilik1020.resourcekeeper.ui

import com.chilik1020.resourcekeeper.controller.temperature.BaseTemperatureController
import com.chilik1020.resourcekeeper.controller.temperature.listener.BaseTemperatureLimitTracker
import com.chilik1020.resourcekeeper.controller.temperature.listener.ButtonStartListener
import com.chilik1020.resourcekeeper.controller.temperature.listener.RangeTempChooseListener
import com.chilik1020.resourcekeeper.utils.ColorUtil
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.xy.XYDataset
import java.awt.*
import javax.swing.*

abstract class JPanelBaseTemperatureChartsPanel: JPanel() {

    private val fontSize16 = Font("Serif", Font.BOLD, 16)

    var sourceChoose: JComboBox<String> = JComboBox()
    var btnStart: JButton = JButton()
    var lblRangeX: JLabel = JLabel("Ось Х:")
    var rangeTempChoose: JComboBox<String> = JComboBox(arrayOf("10 мин","30 мин","1 час","2 часа","5 часов","Всё время"))
    var btnResetChart: JButton = JButton("Стереть график")


    var listChartPanels = mutableListOf<ChartPanel>()
    var listSeries = mutableListOf<TimeSeries>()
    var listPlots = mutableListOf<XYPlot>()
    val tempChCurrentsOnChart = mutableListOf<JLabel>()
    val tempChMins = mutableListOf<JFormattedTextField>()
    val tempChMaxs = mutableListOf<JFormattedTextField>()
    val tempChTrackEns = mutableListOf<JCheckBox>()
    val tempChanelNames = mutableListOf<String>()

    val chartWidth = 510
    var chartHeight = 115


    open fun initComponents() {
        initFields()
        createMainPanel()
        createCharts()
    }

    abstract fun initFields()

    open fun createMainPanel() {
        val mainPanel = JPanel()
        mainPanel.layout = GridBagLayout()
   //     mainPanel.background = Color.LIGHT_GRAY
   //     border = MatteBorder(3, 1, 1, 1, Color.GRAY)

        (mainPanel.layout as GridBagLayout).columnWidths = intArrayOf(5, 110, 50,50, 100, 100,80,5)

        (mainPanel.layout as GridBagLayout).rowHeights = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        (mainPanel.layout as GridBagLayout).columnWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4)
        (mainPanel.layout as GridBagLayout).rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4)

        //---- comChoose ----
        sourceChoose.name = "sourceChoose"
        sourceChoose.preferredSize = Dimension(150,20)
        mainPanel.add(sourceChoose, GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        //---- btnComOn ----
        btnStart.text = "Старт"
        btnStart.background = ColorUtil.btnRedColor
        btnStart.preferredSize = Dimension(100,20)
        mainPanel.add(btnStart, GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        lblRangeX.preferredSize = Dimension(50,20)
        mainPanel.add(lblRangeX,GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        rangeTempChoose.preferredSize = Dimension(150,20)
        mainPanel.add(rangeTempChoose,GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        btnResetChart.preferredSize = Dimension(100,20)
        mainPanel.add(btnResetChart,GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        rangeTempChoose.isEnabled = false
        btnResetChart.isEnabled = false

        add(mainPanel)
    }

    open fun createCharts() {
        for (i in 0 until tempChTrackEns.size) {
            val dataset: XYDataset = createDataset(i)
            val chart = createChart(dataset, i)
            val jChartPanel = ChartPanel(chart)
            jChartPanel.preferredSize = Dimension(chartWidth, chartHeight)
            jChartPanel.minimumSize = Dimension(chartWidth, chartHeight)
            jChartPanel.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
          //  jChartPanel.background = Color.GRAY
            listChartPanels.add(jChartPanel)
        }
        cycleCreateAndInitializeChartItems()
    }

    open fun createChartItemPanel(i: Int) {
        val container = JPanel()
        container.layout = FlowLayout()
     //   container.background = Color.LIGHT_GRAY

        val leftPanel = JPanel()
        leftPanel.apply {
            layout = BoxLayout(leftPanel, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
         //   background = Color.LIGHT_GRAY
        }

        leftPanel.add(JLabel(tempChanelNames[i]))
        leftPanel.add(listChartPanels[i])

        val rightPanel = JPanel()
        rightPanel.apply {
            layout = BoxLayout(rightPanel, BoxLayout.X_AXIS)
            alignmentX = Component.CENTER_ALIGNMENT
         //   background = Color.LIGHT_GRAY
        }

        tempChCurrentsOnChart[i].isOpaque = true
   //     tempChCurrentsOnChart[i].background = ColorUtil.temperatureChartsColor[i]
        tempChCurrentsOnChart[i].font = fontSize16
        tempChCurrentsOnChart[i].text = "      "
        tempChCurrentsOnChart[i].foreground = ColorUtil.temperatureChartsColor[i]
        tempChCurrentsOnChart[i].alignmentX = Component.CENTER_ALIGNMENT
      //  tempChCurrentsOnChart[i].background = Color.LIGHT_GRAY

        val tracksPanel = JPanel()
        tracksPanel.layout = GridBagLayout()
     //   tracksPanel.background = Color.LIGHT_GRAY
        (tracksPanel.layout as GridBagLayout).columnWidths = intArrayOf(40, 40)
        (tracksPanel.layout as GridBagLayout).rowHeights = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        (tracksPanel.layout as GridBagLayout).columnWeights = doubleArrayOf(0.0, 0.0, 0.0, 1.0E-4)
        (tracksPanel.layout as GridBagLayout).rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4)


        tracksPanel.add(JLabel("Макс"), GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        tracksPanel.add(JLabel("Мин"), GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        tracksPanel.add(JLabel("✓"), GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        tempChMaxs[i].preferredSize = Dimension(40, 20)
        tracksPanel.add(tempChMaxs[i], GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        tempChMins[i].preferredSize = Dimension(40, 20)
        tempChMins[i].alignmentX = Component.RIGHT_ALIGNMENT
        tracksPanel.add(tempChMins[i], GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        tempChTrackEns[i].preferredSize = Dimension(20, 20)
        tracksPanel.add(tempChTrackEns[i], GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        rightPanel.add(tempChCurrentsOnChart[i])
        rightPanel.add(tracksPanel)


        container.add(leftPanel)
        container.add(rightPanel)

        add(container)
    }

    abstract fun cycleCreateAndInitializeChartItems()

    open fun createDataset(chanel: Int): XYDataset {
        val dataset = TimeSeriesCollection()
        dataset.addSeries(listSeries[chanel])
        return dataset
    }

    open fun createChart(dataset: XYDataset, chanel: Int): JFreeChart {

        val chart = ChartFactory.createTimeSeriesChart(
                "",
                "",
                "",
                dataset
        )
        chart.isBorderVisible = false
        chart.backgroundPaint = ColorUtil.chartsBackgroundColor

        val plot = chart.xyPlot

        val renderer = XYLineAndShapeRenderer(true, false)
        renderer.setSeriesStroke(0, BasicStroke(2.0f))
        renderer.setSeriesPaint(0, ColorUtil.temperatureChartsColor[chanel])

        renderer.setSeriesVisibleInLegend(0,false)

        plot.renderer = renderer
        plot.backgroundPaint = ColorUtil.chartsPlotBackgroundColor
        plot.isRangeGridlinesVisible = true
        plot.rangeGridlinePaint = Color.gray

        plot.isDomainGridlinesVisible = true
        plot.domainGridlinePaint = Color.gray


        listPlots[chanel] = plot

        val axis = plot.domainAxis
        val axis2 = plot.rangeAxis
        axis.tickLabelPaint = Color.LIGHT_GRAY
        axis2.tickLabelPaint = Color.LIGHT_GRAY
//        val font = Font("Arial", Font.BOLD, 16)
//        axis.tickLabelFont = font
//        axis.labelFont = font
//        axis2.tickLabelFont = font
//        axis2.labelFont = font
        return chart

    }

    open fun addListeners(controller: BaseTemperatureController, tracker: BaseTemperatureLimitTracker) {
        btnStart.addActionListener(ButtonStartListener(this, controller))
        rangeTempChoose.addActionListener(RangeTempChooseListener(this, controller))

        btnResetChart.addActionListener{
            controller.resetView()
        }

        for(tr in  tempChTrackEns)
            tr.addActionListener(tracker)

    }

    fun createAlert(title: String,message: String) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.WARNING_MESSAGE)
    }

    abstract fun changeStateElements(state: Boolean)

    open fun disableTab(){
        btnStart.isEnabled = false
        sourceChoose.isEnabled = false
    }
}