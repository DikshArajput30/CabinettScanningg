package com.example.cabinettscanningg.Connections

import android.annotation.SuppressLint
import android.content.Context
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class ConnectionClass(private val context: Context)
{
    companion object {
       // private const val ip = "192.168.1.33" //server-> 43.230.201.20 // 192.168.1.60
      //private const val ip = "43.230.201.20"///testing
    private const val ip = "192.168.1.10"//second
        private const val port = "1433" //server-> 1232 // 1433
        private const val classes = "net.sourceforge.jtds.jdbc.Driver"
        private const val database = "TRACKNTRACE" //KrishnaPaper //WayinfotechInventory
       // private const val database = "Trackbtrace"
        private const val username = "sa"
       // private const val password = "Yu6SBA5s4u#zcT6%e" //server -> Yu6SBA5s4u#zcT6%e //root
        private const val password = "sql@2018" //server -> Yu6SBA5s4u#zcT6%e //root

        private const val url = "jdbc:jtds:sqlserver://$ip:$port/$database"
    }

    @SuppressLint("NewApi")
    fun CONN(): Connection? {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        try {
            Class.forName(classes)
            conn = DriverManager.getConnection(url, username, password)
            Log.e("Success", "Success")
        } catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errormsgg", "CONN: "+se.message.toString())
//          Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show()
        }
        return conn
    }
}