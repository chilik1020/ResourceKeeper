package com.chilik1020.resourcekeeper.utils

import java.io.IOException
import java.awt.AWTException
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import javax.imageio.ImageIO
import java.awt.Toolkit.getDefaultToolkit
import java.awt.image.BufferedImage
import java.lang.Exception
import javax.swing.JComponent

fun makeScreenShot(path: String) {
    try {
        val robot = Robot()
        val screenShot = robot.createScreenCapture(Rectangle(getDefaultToolkit().screenSize))
        ImageIO.write(screenShot!!, "PNG", File(path))
    } catch (e: AWTException) {
        println(e.message)
    } catch (e: IOException) {
        println(e.message)
    }
}

fun chartPanelToPng(name: String, chartPanel: JComponent) {
    val dirPath = JsonConfig.pictureDirectory

    val directory = File(dirPath)
    if (!directory.exists())
        directory.mkdirs()

    val file = File("$dirPath\\$name")

    val bi = BufferedImage(chartPanel.size.width, chartPanel.size.height, BufferedImage.TYPE_INT_ARGB)
    val graphics = bi.createGraphics()
    chartPanel.paint(graphics)
    graphics.dispose()
    try {
        ImageIO.write(bi, "png", file)
    } catch (e: Exception) {
    }
}


