package com.example.cabinettscanningg

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.crm_copy.other.currentDate
import com.crm_copy.other.myToast
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityDispatchListBinding
import com.example.cabinettscanningg.databinding.ActivityDispatchQcBinding
import com.jindal22.adapter.AdapterData
import com.jindal22.model.DispatchListModel
import com.jindal22.model.DispatchModel
import java.lang.Exception
import java.sql.Connection
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DispatchList : AppCompatActivity(),TextToSpeech.OnInitListener {
    private  lateinit var binding: ActivityDispatchListBinding
    private var context: Context =this@DispatchList
    private val list = ArrayList<DispatchListModel>()
    private var BARCODENO=" "
    private var ITEMNAME=" "
    private var DATE=" "
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private lateinit var sessionManager: SessionManager
    private var fromDate = ""
    private  var toDate=" "
    private var BrType = "di"
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDispatchListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionClass = ConnectionClass(context)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        Handler(mainLooper).postDelayed({


            con = connectionClass.CONN()!!
            statement = con.createStatement()
            sessionManager = SessionManager(this)
            getExistingOrderData()
            Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)
        currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        binding.ivFromCalender.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                context, { _, y, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    val mo= monthOfYear+1
                    var m = mo.toString()
                    var d = dayOfMonth.toString()

                    if (d.length==1){
                        d = "0$dayOfMonth"
                    }
                    if (m.length==1){
                        m = "0$mo"
                    }
                    val date = "$d-${m}-$y"
                    fromDate = "$y${m}$d"
                    binding.edtFromDate.setText(date)
                }, year, month, day)
            dpd.show()
        }
        binding.ivToDateCalender.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                context, { _, y, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    val mo= monthOfYear+1
                    var m = mo.toString()
                    var d = dayOfMonth.toString()

                    if (d.length==1){
                        d = "0$dayOfMonth"
                    }
                    if (m.length==1){
                        m = "0$mo"
                    }
                    val date = "$d-${m}-$y"
                    toDate = "$y${m}$d"
                    binding.edtToDate.setText(date)
                    if (fromDate!="")
                        getListDataFromToDate()
                }, year, month, day)
            dpd.show()
        }

        binding.tvClear.setOnClickListener {
            getListData()
           binding.edtFromDate.setText("")
           binding.edtToDate.setText("")
        }

        binding.back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,MainActivity::class.java))

        })
        tts = TextToSpeech(this, this)


    }

    private fun getExistingOrderData() {
        list.clear()
        val query = "Select BARCODENO,ITEMNAME,DATE from M1DispatchSub"
        try {
            val statement = con.createStatement()
            val resultSet = statement.executeQuery(query)
            if (resultSet.next()) {
                do {
                    BARCODENO = resultSet.getString("BARCODENO")
                    ITEMNAME = resultSet.getString("ITEMNAME")

                    DATE = resultSet.getString("DATE")

                    list.add(0, DispatchListModel(BARCODENO, ITEMNAME,DATE))

                } while (resultSet.next())
                val obj_adapter = DispatchListAdapter(list)

                binding.recyclerview.layoutManager = LinearLayoutManager(this)
                binding.recyclerview.adapter = obj_adapter
            }

        } catch (se: Exception) {
            Log.e("ERROR", se.message!!)
        }
    }

    private fun getListDataFromToDate() {
        list.clear()
        Log.e(ContentValues.TAG, "getListDataFromToDate: '$fromDate' AND '$toDate'")
        val query =
            "Select BARCODENO,ITEMNAME,DATE from M1DispatchSub where BrType = ('$BrType') and Date BETWEEN '$fromDate' AND '$toDate' ORDER BY Date"
        val statement = con.createStatement()
        val resultSet = statement.executeQuery(query)
        if (resultSet.next()) {
            do {
                BARCODENO = resultSet.getString("BARCODENO")
                ITEMNAME = resultSet.getString("ITEMNAME")

                DATE = resultSet.getString("DATE")

                list.add(0, DispatchListModel(BARCODENO, ITEMNAME,DATE))


            } while (resultSet.next())
            val obj_adapter = DispatchListAdapter(list)

            binding.recyclerview.layoutManager = LinearLayoutManager(this)
            binding.recyclerview.adapter = obj_adapter

        }else{
            runOnUiThread {
                myToast(this, "List is Empty")
                speakOut("List is Empty")

                list.clear()
                val obj_adapter = DispatchListAdapter(list)

                binding.recyclerview.layoutManager = LinearLayoutManager(this)
                binding.recyclerview.adapter = obj_adapter
            }
        }
    }
    private fun getListData() {
        list.clear()
        val query =
            "Select BARCODENO,ITEMNAME,DATE from M1DispatchSub  where Date = ('$currentDate') and  BrType=('$BrType')"
        val statement = con.createStatement()
        val resultSet = statement.executeQuery(query)
        if (resultSet.next()) {
            do {
                BARCODENO = resultSet.getString("BARCODENO")
                ITEMNAME = resultSet.getString("ITEMNAME")

                DATE = resultSet.getString("DATE")

                list.add(0, DispatchListModel(BARCODENO, ITEMNAME,DATE))

            } while (resultSet.next())
            val obj_adapter = DispatchListAdapter(list)

            binding.recyclerview.layoutManager = LinearLayoutManager(this)
            binding.recyclerview.adapter = obj_adapter

        }else{
            runOnUiThread {
                myToast(this, "No order for today")
                speakOut("No order for today")
            }
        }
    }

    private fun speakOut(textvalue:String) {
        val text = textvalue
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")

    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                // btnSpeak!!.isEnabled = true
            }
        }
    }
}