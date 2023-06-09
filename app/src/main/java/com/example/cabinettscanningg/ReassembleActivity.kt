package com.example.cabinettscanningg

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.crm_copy.other.getIPAddress
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityQcScanningBinding
import com.example.cabinettscanningg.databinding.ActivityReassembleBinding
import com.example.cabinettscanningg.databinding.ActivityScanningBinding
import java.sql.Connection
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*

class ReassembleActivity : AppCompatActivity(){
    private val context: Context = this@ReassembleActivity
    private lateinit var binding: ActivityReassembleBinding
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement

    private var currentDateSimple = ""
    private var cabinetname = ""
    private var cabinetitemname = ""
    private var cabinetmodelno = ""
    private var day = ""
    private var month = ""
    private var year = ""
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReassembleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionClass = ConnectionClass(context)
        currentDateSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
            Date()
        )
        val c = Calendar.getInstance()

        year = c.get(Calendar.YEAR).toString()
        month = (c.get(Calendar.MONTH) + 1).toString()
        day = c.get(Calendar.DAY_OF_MONTH).toString()
        binding.dd.text = day.toString()
        binding.mm.text = month.toString()
        binding.yy.text = year.toString()
        sessionManager = SessionManager(this)

        Log.d("mongthh1", "onCreate: " + year)
        Log.d("mongthh2", "onCreate: " + month)
        Log.d("mongthh3", "onCreate: " + day)
        Handler(mainLooper).postDelayed({


            con = connectionClass.CONN()!!
            statement = con.createStatement()
            Log.d("inslizedd", "onCreate: " + "inslizedd1")


        }, 200)
        binding.back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, MainActivity::class.java))

        })

        binding.cabinetscanqcre.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: " + "clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                cabinetname = binding.cabinetscanqcre.text.toString().trim()
                insertFollowUpData(cabinetname)

                return@setOnKeyListener true
            }
            false
        }
        binding.username.setText(sessionManager.userName.toString())
    }

    private fun insertFollowUpData(cabinetname:String) {
        //mam run kraye debug ? kruyesok
        try {
            var n = 0
            val cstmt = con.prepareCall("{call CABREASSEMBLE1  (?,?,?,?,?,?,?)}")
            cstmt.setInt(1, 2022)
            cstmt.setString(2, "1000")
            cstmt.setString(3, "20230201")
            cstmt.setString(4, "67")
            cstmt.setString(5, "4656")
            cstmt.setString(6,sessionManager.userName.toString() )
            cstmt.setString(7, cabinetname)
///is war ho gya abhi nhi ho rha tha its ok maam iska data dlt kaise kru proc ka jo insert ho gyamam jis table m kia uspe delte

            n = cstmt.executeUpdate()
            if (n > 0)
            {

                Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()




            }
            else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Exception: ${e.toString()}")
            Log.d("procedureecall", "insertFollowUpData: "+e.toString())
        }
    }


}