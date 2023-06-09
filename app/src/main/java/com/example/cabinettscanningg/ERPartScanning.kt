package com.example.cabinettscanningg

import android.annotation.SuppressLint
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
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityErpartScanningBinding
import com.example.cabinettscanningg.databinding.ActivityPartScanningBinding
import java.sql.Connection
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*

class ERPartScanning : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityErpartScanningBinding
    private var context: Context =this@ERPartScanning
    private var cabinetsc=" "
    private var pcbsc=" "
    private var transforsc=" "
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private var currentDateSimple=""
    private var cabinetitemname=""
    private var cabinetmodelno=""
    private var pcbitemname=""
    private var pcbmoelno=""
    private var transformitemnme=""
    private var transformmodelno=""
    private var day=""
    private  var month=""
    private var year=""
    private var tts: TextToSpeech? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityErpartScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionClass = ConnectionClass(context)
        currentDateSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
            Date()
        )
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR).toString()
        month = (c.get(Calendar.MONTH)+1).toString()
        day = c.get(Calendar.DAY_OF_MONTH).toString()
        binding.dd.text=day.toString()
        binding.mm.text=month.toString()
        binding.yy.text=year.toString()
        Handler(mainLooper).postDelayed({


            con = connectionClass.CONN()!!
            statement = con.createStatement()
            sessionManager = SessionManager(this)

            Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)
        binding.back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,MainActivity::class.java))
//           // cabinetsc=binding.cabinetscanning.text.toString()
//            //checkcabinet(cabinetsc)
//          //  checkCabinetNumbertest(cabinetsc)
//            speakOut("Invalid Cabinet Number")



        })

        binding.username.setOnClickListener(View.OnClickListener {
            pcbsc=binding.pcbscanning.text.toString().trim()
            partgenerationpcbview(pcbsc,"P")
            speakOut("Invalid Cabinet Number name")


        })

        binding.profile.setOnClickListener(View.OnClickListener {

            transforsc=binding.transforscaning.text.toString().trim()
            partgenerationtransformview(transforsc,"T")
            speakOut("Invalid Cabinet Number name transform")
        })

        binding.cabinetscanning.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                cabinetsc=binding.cabinetscanning.text.toString().trim()
                // checkcabinet(cabinetsc)
                checkCabinetNumbertest(cabinetsc)

                return@setOnKeyListener true
            }
            false
        }


        binding.pcbscanning.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                pcbsc=binding.pcbscanning.text.toString().trim()
                partgenerationpcbview(pcbsc,"P")

                return@setOnKeyListener true
            }
            false
        }
        binding.transforscaning.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                transforsc=binding.transforscaning.text.toString().trim()
                partgenerationtransformview(transforsc,"T")
                return@setOnKeyListener true
            }
            false
        }
        tts = TextToSpeech(this, this)
        // ConvertTextToSpeech()
        sessionManager = SessionManager(this)
        binding.username.setText(sessionManager.userName.toString())
    }

    override fun onBackPressed() {
        //  super.onBackPressed()
        startActivity(Intent(context,MainActivity::class.java))
    }

    private fun checkcabinet(cabinetsc: String) {

        try {
            val query ="select Barcodeno,itemname,modelno from Cabinet1 where  Barcodeno =('${cabinetsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                val isExist = resultSet.getString("Barcodeno").trim()
                if(isExist==cabinetsc)
                {
                    cabinetitemname=resultSet.getString("itemname")
                    cabinetmodelno=resultSet.getString("modelno")
                    checkpairing(cabinetsc)
                }



            }
            else
            {
                binding.cabinetscanning.text.clear()
                myToastRed(this,"Invalid Cabinet Number")
                speakOut("Invalid Cabinet Number")

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkpairing(cabinetsc: String) {
        try {
            val query ="select cabinetname from PAIRING where  cabinetname =('${cabinetsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                var cabinetscdb=resultSet.getString("cabinetname").trim()
                if(cabinetscdb==cabinetsc)
                {
                    binding.cabinetscanning.text.clear()
                    myToastyellow(this," Already Exist")
                    speakOut("Already Exist")
                }

            }
            else
            {
                binding.transforscaning.requestFocus()
                Log.d("cbstringg", "checkpairing: "+binding.cabinetscanning.text.toString())

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }
    }

    private fun checkpairingpcb(pcbsc: String) {
        try {
            val query ="select PCBNAME from PAIRING where  PCBNAME =('${pcbsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                var cabinetscdb=resultSet.getString("PCBNAME").trim()
                if(cabinetscdb==pcbsc)
                {
                    binding.pcbscanning.text.clear()
                    myToastyellow(this," Already Exist")
                    speakOut("Already Exist")

                }

            }
            else
            {
                binding.transforscaning.requestFocus()

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }
    }

    private fun checkpairingtransform(transforsc: String) {
        try {
            val query ="select TRANSFORMNAME from PAIRING where  TRANSFORMNAME =('${transforsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                var cabinettransformdb=resultSet.getString("TRANSFORMNAME").trim()
                if(cabinettransformdb==transforsc)
                {
                    binding.transforscaning.text.clear()
                    myToastyellow(this," Already Exist")
                    speakOut("Already Exist")

                }

            }
            else
            {

                Insertpartpairing()

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }
    }
    private fun Insertpartpairing() {
        try {
            var n = 0
            val sql =
                " INSERT INTO PAIRING (finyear,srno,date,month,year,cabinetname,pcbname,transformname,username,citemname,cmodelno,pitemname,pmodelno,titemname,tmodelno,cabtype) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2, "1")
            // statement.setString(3, "2023-01-02 04:25:33.0980000")
            statement.setInt(3, 322023)
//            statement.setString(4, "bike")
            statement.setString(4, month)
            statement.setString(5, year)
            statement.setString(6, cabinetsc)
            statement.setString(7, "ABCD")
            statement.setString(8, transforsc)
            statement.setString(9, sessionManager.userName)
            statement.setString(10, cabinetitemname)
            statement.setString(11, cabinetmodelno)
            statement.setString(12, "1234")
            statement.setString(13, "A")
            statement.setString(14, transformitemnme)
            statement.setString(15, transformmodelno)
            statement.setString(16, "ER")



            n = statement.executeUpdate()
            if (n > 0)
            {

                Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()
                binding.cabinetscanning.text.clear()
                binding.pcbscanning.text.clear()
                binding.transforscaning.text.clear()
                binding.cabinetscanning.requestFocus()

            }
            else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }
        } catch (e: java.lang.Exception) {
            Log.e(ContentValues.TAG, "error: " + e.message)
            Log.d("msgsss", "Insertpartpairing: "+e.message.toString())
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()

            e.printStackTrace()
        }
    }

    private fun partgenerationpcbview(pcbsc: String,P:String) {
        try {
            val query ="select barcodeno,type,itemname,modelno from M1PARTGENERATIONSUB where  barcodeno =('${pcbsc}') and type=('${P}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                var pcbviewdb=resultSet.getString("barcodeno").trim()
                var type=resultSet.getString("type").trim()

                if(pcbviewdb==pcbsc && type==P) {
                    pcbitemname=resultSet.getString("itemname").trim()
                    pcbmoelno=resultSet.getString("modelno").trim()
                    checkpairingpcb(pcbsc)
                }
            }
            else
            {

                binding.pcbscanning.text.clear()
                myToastRed(this,"Invalid PCB Number")
                speakOut("Invalid PCB Number")

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }
    }
    private fun partgenerationtransformview(transforsc: String,T:String) {
        try {
            val query ="select barcodeno,type,itemname,modelno from M1PARTGENERATIONSUB where  barcodeno =('${transforsc}') and type=('${T}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                var partgenerationdb=resultSet.getString("barcodeno").trim()
                var type=resultSet.getString("type").trim()
                if(partgenerationdb==transforsc && type==T )
                {
                    transformitemnme=resultSet.getString("itemname").trim()
                    transformmodelno=resultSet.getString("modelno").trim()
                    checkpairingtransform(transforsc)

                }

            }
            else
            {

                binding.transforscaning.text.clear()
                myToastRed(this,"Invalid Transformer Number")
                speakOut("Invalid Transformer Number")

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }
    }

    /////////////cabinetscanning
    private fun checkCabinetNumbertest(cabinetsc :String) {

        try {
            val query ="select Barcodeno,itemname,modelno from [BARCODEGENERATIONSUB] where  Barcodeno=('${cabinetsc}')"
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                val cabinetscdb = resultSet.getString("Barcodeno")
                Log.d("cabinettt1", "checkCabinetNumbertest: "+cabinetscdb)
                if(cabinetscdb==cabinetsc) {
                    cabinetitemname = resultSet.getString("itemname")
                    cabinetmodelno = resultSet.getString("modelno")

                    checkpairing(cabinetsc)
                }

            }
            else {
                myToastRed(this,"Invalid serial Number")
                binding.cabinetscanning.text.clear()
                speakOut("Invalid serial Number")


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

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


}

