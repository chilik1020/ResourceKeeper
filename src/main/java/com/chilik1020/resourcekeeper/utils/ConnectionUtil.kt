package com.chilik1020.resourcekeeper.utils

import org.apache.http.HttpHost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.DefaultProxyRoutePlanner
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet


fun isConnectionGood(): Boolean {
    try {
        val httpclient: CloseableHttpClient
        if (JsonConfig.proxyEnable) run {
            val proxy = HttpHost(JsonConfig.proxyAddress,
                    JsonConfig.proxyPort,
                    HttpHost.DEFAULT_SCHEME_NAME)
            val planner = DefaultProxyRoutePlanner(proxy)
            httpclient = HttpClients.custom().setRoutePlanner(planner).build()
        } else
            httpclient = HttpClients.createDefault()

        val response = httpclient.execute(HttpGet("http://www.google.com"))
        val statusCode = response.statusLine.statusCode
        return statusCode == HttpStatus.SC_OK
    } catch (ex: Exception) {
        return false
    }
}