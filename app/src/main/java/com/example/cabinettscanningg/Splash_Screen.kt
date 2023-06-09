package com.example.cabinettscanningg

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.crm_copy.other.myToast
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import java.sql.Connection

class Splash_Screen : AppCompatActivity() {
    private val context: Context = this@Splash_Screen
    private lateinit var connectionClass: ConnectionClass
    private var conn: Connection? = null
    var username=" "
    var userpassword=" "
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sessionManager = SessionManager(this)
        Handler(mainLooper).postDelayed({
//            conn = connectionClass.CONN()
//            if (conn == null) {
//                myToast(this@Splash_Screen, "Server issue")
//            }
            //   progressDialog!!.dismiss()

            Log.d("username1", "onCreate: "+username)
            Log.d("username2", "onCreate: "+ sessionManager.isLogin)

            if( sessionManager.isLogin==true)
            {

                startActivity(Intent(context, MainActivity::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(context, LoginActivity::class.java))
                finish()
            }

        }, 0)
    }
}