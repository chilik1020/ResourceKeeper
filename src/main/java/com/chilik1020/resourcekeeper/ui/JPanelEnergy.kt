package com.chilik1020.resourcekeeper.ui

import com.chilik1020.resourcekeeper.controller.energy.EnergyCommonLogController
import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import com.chilik1020.resourcekeeper.controller.energy.listener.*
import com.chilik1020.resourcekeeper.utils.ColorUtil
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.utils.chartPanelToPng
import org.jfree.chart.*
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.ui.tabbedui.VerticalLayout
import java.awt.*
import javax.swing.*
import javax.swing.border.MatteBorder
import javax.swing.JLabel
import javax.swing.JPanel



class JPanelEnergy: JPanel() {

    private val fontSize20 = Font("Serif", Font.BOLD, 20)
    private val fontSize16 = Font("Serif", Font.BOLD, 16)
    private val fontSize14 = Font("Serif", Font.BOLD, 14)

    private val labelName = JLabel("Энергия")

    private val labelCommon = JLabel("Файл ресурса:")
    private val labelLabMax = JLabel("Файл LabMax:")

    var commonFilePath: JTextField = JTextField(2)
    var commonFileChooseButton: JButton = JButton()

    var energyFilePath: JTextField = JTextField(2)
    var energyFileChooseButton: JButton = JButton()

    private var lblCurrent: JLabel = JLabel()
    private val lblEnergyMin: JLabel = JLabel()
    private val lblEnergyMax: JLabel = JLabel()
    private val lblEnergyMean: JLabel = JLabel()

    private val lblNumberOfShot: JLabel = JLabel()

    var energyCurrent: JLabel = JLabel()
    var energyMin: JLabel = JLabel()
    var energyMax: JLabel = JLabel()
    var energyMean: JLabel = JLabel()
    var shotCurrent: JLabel = JLabel()


    private val lblFileSize: JLabel = JLabel()
    val logSize: JLabel = JLabel()
    var logSizeTrackEn: JCheckBox = JCheckBox()

    private lateinit var panelChartLabMax: JPanel
    private val lblLabMaxChart: JLabel = JLabel("График LabMax")
    private val lblRangeLabMax: JLabel = JLabel("Ось Х: ")
    internal var rangeXLabMaxChoose: JComboBox<String> = JComboBox(arrayOf("1000","5000","10000","50000","100000","Все"))

    private lateinit var jChartPanelCommon: ChartPanel
    private var lblCommonChart: JLabel = JLabel("График ресурса")
    private var lblRangeDomainCommon: JLabel = JLabel("Ось X:")
    private var lblRangeAxesCommon: JLabel = JLabel("Ось Y(мин,макс):")
    var rangeXCommonChoose: JComboBox<String> = JComboBox(arrayOf("10000","50000","100000","500000","1000000","Все"))
    var minYCommonChart: JFormattedTextField = JFormattedTextField()
    var maxYCommonChart: JFormattedTextField = JFormattedTextField()
    private val rangeAxesCommonUpdateBtn: JButton = JButton("Обновить")

    private val lblResource: JLabel = JLabel("Ресурс:  ")
    val resourceValue: JLabel = JLabel("0")

    private lateinit var chartCommon: JFreeChart
    private lateinit var chartLabMax: JFreeChart

    lateinit var seriesLabMax: XYSeries
    lateinit var plotLabMax: XYPlot

    lateinit var seriesCommonStartEnergy: XYSeries
    lateinit var seriesCommonStopEnergy: XYSeries
    lateinit var seriesCommonMeanEnergy: XYSeries
    lateinit var plotCommon: XYPlot

    private val chartCommonWidth = 430
    private val chartCommonHeight = 310
    private val chartLabMaxWidth = 430
    private val chartLabMaxHeight = 200


    init {
        isVisible = true

        initComponents()
    }

    private fun initComponents() {
        layout = VerticalLayout()
        border = MatteBorder(3, 1, 1, 1, Color.GRAY)

        val panelNorth = JPanel()
        val panelCenter = JPanel()
        val panelSouth = JPanel()

        /*
           PANEL NORTH
         *************************************************************************************************************/

        panelNorth.layout = GridBagLayout()

        panelNorth.alignmentX = Component.LEFT_ALIGNMENT

        (panelNorth.layout as GridBagLayout).columnWidths = intArrayOf(5,50, 100, 50, 50, 100, 50, 5)

        //---- Имя панели "Энергия" ----
        labelName.font = fontSize16
        labelName.preferredSize = Dimension(550,20)
        panelNorth.add(labelName, GridBagConstraints(1, 0, 8, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        //---- "Выберите файл полного ресурса" ----
        panelNorth.add(labelCommon, GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        //---- energyFilePath ----
        commonFilePath.preferredSize = Dimension(100,20)
        commonFilePath.scrollOffset = 100
        panelNorth.add(commonFilePath, GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        //---- commonFileChooseButton ----
        commonFileChooseButton.text = "Выбрать"
        panelNorth.add(commonFileChooseButton, GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))


        //---- "Выберите файл лога LabMax" ----
        panelNorth.add(labelLabMax, GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        //---- energyFilePath ----
        energyFilePath.preferredSize = Dimension(100,20)
        energyFilePath.scrollOffset = 100
        panelNorth.add(energyFilePath, GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        //---- energyFileChooseButton ----
        energyFileChooseButton.text = "Выбрать"
        panelNorth.add(energyFileChooseButton, GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        //---- "Размер лога LabMax" ----
        lblFileSize.text = "Размер лога"
        panelNorth.add(lblFileSize, GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        //---- energyFilePath ----
        logSize.isOpaque = true
        panelNorth.add(logSize, GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        panelNorth.add(logSizeTrackEn, GridBagConstraints(6, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))


        /*
            PANEL CENTER
         *************************************************************************************************************/
        panelCenter.layout = VerticalLayout()

        val panelLabMaxChartSet = JPanel()
        panelLabMaxChartSet.layout = GridBagLayout()
        panelLabMaxChartSet.alignmentX = Component.LEFT_ALIGNMENT
        (panelLabMaxChartSet.layout as GridBagLayout).columnWidths = intArrayOf(5,80, 80, 80, 80,200, 5)

        lblLabMaxChart.font = fontSize14
        panelLabMaxChartSet.add(lblLabMaxChart,GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        panelLabMaxChartSet.add(lblRangeLabMax,GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))

        panelLabMaxChartSet.add(rangeXLabMaxChoose,GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                Insets(5, 5, 5, 5), 0, 0))


        panelChartLabMax = JPanel()
        panelChartLabMax.layout = VerticalLayout()
        panelChartLabMax.border = BorderFactory.createEmptyBorder(0, 5, 0, 5)

        val dataset: XYDataset = createDataset()
        chartLabMax = createChart(dataset)
        val jChartPanel = ChartPanel(chartLabMax)
        jChartPanel.preferredSize = Dimension(chartLabMaxWidth, chartLabMaxHeight)
        jChartPanel.minimumSize = Dimension(chartLabMaxWidth, chartLabMaxHeight)
        jChartPanel.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        jChartPanel.background = Color.GRAY

        val panelLabMaxDetails = JPanel()
        panelLabMaxDetails.layout = GridBagLayout()
        panelLabMaxDetails.alignmentX = Component.LEFT_ALIGNMENT
        (panelLabMaxDetails.layout as GridBagLayout).columnWidths = intArrayOf(5,90, 90, 90, 90, 90,90, 5)

        //---- Счётчик выстрелорв ----
        lblNumberOfShot.text = "Выстрел"
        lblNumberOfShot.font = fontSize14
        panelLabMaxDetails.add(lblNumberOfShot,GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        shotCurrent.font = fontSize20
        shotCurrent.text = "0"
        shotCurrent.isOpaque = true
        shotCurrent.background = ColorUtil.labMaxDetailsShotColor
        panelLabMaxDetails.add(shotCurrent,GridBagConstraints(1, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        //---- Ресурс ----
        lblResource.text = "Ресурс"
        lblResource.font = fontSize14
        panelLabMaxDetails.add(lblResource,GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        resourceValue.font = fontSize20
        resourceValue.text = "0"
        resourceValue.isOpaque = true
        resourceValue.background = ColorUtil.labMaxDetailsShotColor
        panelLabMaxDetails.add(resourceValue,GridBagConstraints(2, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        lblCurrent.text = "Eтек"
        lblCurrent.font = fontSize14
        panelLabMaxDetails.add(lblCurrent,GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        energyCurrent.font = fontSize20
        energyCurrent.text = "0"
        energyCurrent.isOpaque = true
        energyCurrent.background = ColorUtil.labMaxDetailsEnergyColor
        panelLabMaxDetails.add(energyCurrent,GridBagConstraints(3, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))


        //---- Энергия мин ----
        lblEnergyMin.text = "Eмин"
        lblEnergyMin.font = fontSize14
        panelLabMaxDetails.add(lblEnergyMin,GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        energyMin.font = fontSize20
        energyMin.text = "0"
        energyMin.isOpaque = true
        energyMin.background = ColorUtil.labMaxDetailsEnergyColor2
        panelLabMaxDetails.add(energyMin,GridBagConstraints(4, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        //---- Энергия макс ----
        lblEnergyMax.text = "Eмакс"
        lblEnergyMax.font = fontSize14
        panelLabMaxDetails.add(lblEnergyMax,GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        energyMax.font = fontSize20
        energyMax.text = "0"
        energyMax.isOpaque = true
        energyMax.background = ColorUtil.labMaxDetailsEnergyColor2
        panelLabMaxDetails.add(energyMax,GridBagConstraints(5, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))


        //---- Энергия средняя ----
        lblEnergyMean.text = "Eср"
        lblEnergyMean.font = fontSize14
        panelLabMaxDetails.add(lblEnergyMean,GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        energyMean.font = fontSize20
        energyMean.text = "0"
        energyMean.isOpaque = true
        energyMean.background = ColorUtil.labMaxDetailsEnergyColor2
        panelLabMaxDetails.add(energyMean,GridBagConstraints(6, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))


        panelChartLabMax.add(jChartPanel)
        panelChartLabMax.add(panelLabMaxDetails)

        panelCenter.add(panelLabMaxChartSet)
        panelCenter.add(panelChartLabMax)
        panelCenter.add(JSeparator(SwingConstants.HORIZONTAL))


        /*
            PANEL SOUTH
         *************************************************************************************************************/
        panelSouth.layout = VerticalLayout()
        panelSouth.border = BorderFactory.createEmptyBorder(0, 5, 0, 5)

        val panelCommonChartSet = JPanel()
        panelCommonChartSet.layout = GridBagLayout()
        panelCommonChartSet.alignmentX = Component.LEFT_ALIGNMENT

        (panelCommonChartSet.layout as GridBagLayout).columnWidths = intArrayOf(5,50, 50, 50, 50, 50, 50, 70,5)

        lblCommonChart.font = fontSize14
        panelCommonChartSet.add(lblCommonChart,GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        panelCommonChartSet.add(lblRangeDomainCommon,GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        panelCommonChartSet.add(lblRangeAxesCommon,GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        panelCommonChartSet.add(rangeXCommonChoose,GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        minYCommonChart.preferredSize = Dimension(50, 20)
        panelCommonChartSet.add(minYCommonChart,GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        maxYCommonChart.preferredSize = Dimension(50, 20)
        panelCommonChartSet.add(maxYCommonChart,GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        rangeAxesCommonUpdateBtn.preferredSize = Dimension(70, 20)
        panelCommonChartSet.add(rangeAxesCommonUpdateBtn,GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))


        lblResource.font = fontSize14

        resourceValue.font = fontSize20
        resourceValue.isOpaque = true
        resourceValue.background = ColorUtil.commonDetailsShotColor


        val datasetCommon: XYDataset = createDatasetCommon()

        chartCommon = createChartCommon(datasetCommon)


        jChartPanelCommon = ChartPanel(chartCommon)
        jChartPanelCommon.preferredSize = Dimension(chartCommonWidth, chartCommonHeight)
        jChartPanelCommon.minimumSize = Dimension(chartCommonWidth, chartCommonHeight)
        jChartPanelCommon.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        jChartPanelCommon.background = Color.GRAY
        jChartPanelCommon.size = Dimension(chartCommonWidth, chartCommonHeight)

        panelSouth.add(panelCommonChartSet)
        panelSouth.add(jChartPanelCommon)

        /*
            ADD ELEMENTS ON ENERGY PANEL
         *************************************************************************************************************/

        minYCommonChart.value = JsonConfig.energyCommonMin
        maxYCommonChart.value = JsonConfig.energyCommonMax

        commonElementsState(false)
        labMaxElementsState(false)

        add(panelNorth)
        add(JSeparator(SwingConstants.HORIZONTAL))
        add(panelCenter)
        add(panelSouth)
    }

    fun commonElementsState(state: Boolean) {
        rangeXCommonChoose.isEnabled = state
        minYCommonChart.isEnabled = state
        maxYCommonChart.isEnabled = state
        rangeAxesCommonUpdateBtn.isEnabled = state
    }

    fun labMaxElementsState(state: Boolean) {
        rangeXLabMaxChoose.isEnabled = state
        energyFilePath.isEnabled = state
        energyFileChooseButton.isEnabled = state
        logSizeTrackEn.isEnabled = state
    }

    fun addListeners(controller: EnergyCommonLogController) {
        commonFileChooseButton.addActionListener(EnergyCommonLogFileChooseListener(this, controller))
        rangeXCommonChoose.addActionListener(RangeCommonChooseListener(this, controller))
        rangeAxesCommonUpdateBtn.addActionListener{
            controller.setAxisRange()
        }
    }


    fun addListeners(controller: EnergyLabMaxController) {
        energyFileChooseButton.addActionListener(EnergyLabMaxFileChooseListener(this, controller))
        logSizeTrackEn.actionCommand = "log file track"
        logSizeTrackEn.addActionListener(LabMaxLogSizeTracker(this, controller))
        rangeXLabMaxChoose.addActionListener(RangeLabMaxChooseListener(this,controller))

    }

    fun saveLabMaxChartAsPng() {
        chartPanelToPng("labmax.png", panelChartLabMax)
    }

    fun saveCommonChartAsPng() {
        chartPanelToPng("picall.png", jChartPanelCommon)
    }

    fun createAlert(title: String,message: String, type: Int) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                type)
    }

    private fun createChart(dataset: XYDataset): JFreeChart {

        val chart = ChartFactory.createXYLineChart(
                "",
                "Выстрел",
                "Энергия, мДж",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        )


        plotLabMax = chart.xyPlot

        val renderer = XYLineAndShapeRenderer(true, false)
        renderer.setSeriesPaint(0, ColorUtil.labMaxChartColor)
        renderer.setSeriesStroke(0, BasicStroke(1.0f))

        plotLabMax.renderer = renderer
        plotLabMax.backgroundPaint = ColorUtil.chartsBackgroundColor

        plotLabMax.isRangeGridlinesVisible = true
        plotLabMax.rangeGridlinePaint = Color.gray

        plotLabMax.isDomainGridlinesVisible = true
        plotLabMax.domainGridlinePaint = Color.gray

        val axis = plotLabMax.domainAxis
        val axis2 = plotLabMax.rangeAxis

        val font = Font("Arial", Font.BOLD, 14)
        axis.tickLabelFont = font
        axis.labelFont = font
        axis2.tickLabelFont = font
        axis2.labelFont = font
        return chart

    }

    private fun createDataset(): XYDataset {
        seriesLabMax = XYSeries("")

        val dataset = XYSeriesCollection()
        dataset.addSeries(seriesLabMax)

        return dataset
    }

    private fun createChartCommon(dataset: XYDataset): JFreeChart {

        val chart = ChartFactory.createXYLineChart(
                "",
                "Выстрел, 10e3",
                "Энергия, мДж",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        )

        plotCommon = chart.xyPlot

        val renderer = XYLineAndShapeRenderer(true, false)

        renderer.setSeriesStroke(0, BasicStroke(3.0f))
        renderer.setSeriesPaint(0, ColorUtil.commonChartMeanColor)

        renderer.setSeriesStroke(1, BasicStroke(3.0f))
        renderer.setSeriesPaint(1, ColorUtil.commonChartStopColor)

        renderer.setSeriesStroke(2, BasicStroke(3.0f))
        renderer.setSeriesPaint(2, ColorUtil.commonChartStartColor)

        plotCommon.renderer = renderer
        plotCommon.backgroundPaint = ColorUtil.chartsBackgroundColor

        plotCommon.isRangeGridlinesVisible = true
        plotCommon.rangeGridlinePaint = Color.gray

        plotCommon.isDomainGridlinesVisible = true
        plotCommon.domainGridlinePaint = Color.gray

        val axis = plotCommon.domainAxis
        val axis2 = plotCommon.rangeAxis

        val font = Font("Arial", Font.BOLD, 14)
        axis.tickLabelFont = font
        axis.labelFont = font
        axis2.tickLabelFont = font
        axis2.labelFont = font
        return chart

    }

    private fun createDatasetCommon(): XYDataset {
        seriesCommonStartEnergy = XYSeries("Start")
        seriesCommonStopEnergy = XYSeries("Stop")
        seriesCommonMeanEnergy = XYSeries("Mean")

        val dataset = XYSeriesCollection()
        dataset.addSeries(seriesCommonMeanEnergy)
        dataset.addSeries(seriesCommonStopEnergy)
        dataset.addSeries(seriesCommonStartEnergy)

        return dataset
    }
}