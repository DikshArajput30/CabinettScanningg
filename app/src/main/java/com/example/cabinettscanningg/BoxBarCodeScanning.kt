package com.example.cabinettscanningg

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.databinding.ActivityBoxBarCodeScanningBinding
import java.sql.Connection
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*

class BoxBarCodeScanning : AppCompatActivity() {
    private lateinit var binding:ActivityBoxBarCodeScanningBinding
    private var context:Context=this@BoxBarCodeScanning
    private  var boxbarsc=""
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private  var boxcabinetname=" "
    private  var boxcabinetitemname=" "
    private  var boxcabinetmodelno=" "

    private var day=""
    private  var month=""
    private var year=""
    private var currentDateSimple=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBoxBarCodeScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentDateSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
            Date()
        )
        connectionClass = ConnectionClass(context)
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
            Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)
        binding.back.setOnClickListener(View.OnClickListener {
           // startActivity(Intent(context,MainActivity::class.java))
            boxbarsc=binding.boxbarcabinetscanning.text.toString().trim()
            checkcabinet(boxbarsc)
        })

        binding.boxbarcabinetscanning.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                boxbarsc=binding.boxbarcabinetscanning.text.toString().trim()
                checkcabinet(boxbarsc)

                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(context,MainActivity::class.java))

    }

    private fun checkcabinet(boxbarsc: String) {

        try {
            val query ="select CABINETNAME,CABINETITEMNAME,CABINETMOELNO from Qccabinet where  CABINETNAME =('${boxbarsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                boxcabinetname  = resultSet.getString("cabinetname").trim()

                if(boxcabinetname==boxbarsc)
                {
                    boxcabinetitemname  = resultSet.getString("cabinetitemname").trim()
                    boxcabinetmodelno  = resultSet.getString("CABINETMOELNO")
                    // InsertQCcabinet(cabinetname)
                    checkduplicate(boxbarsc)
                }



            }
            else
            {
                binding.boxbarcabinetscanning.text.clear()
                myToastRed(this,"Invalid Cabinet Number")

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }

    private fun checkduplicate(boxbarsc: String) {

        try {
            val query ="select cabinetname from BOXBARSC where  cabinetname =('${boxbarsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                boxcabinetname  = resultSet.getString("cabinetname").trim()

                if(boxcabinetname==boxbarsc)
                {

                    binding.boxbarcabinetscanning.text.clear()
                    myToastyellow(this," Already Exist")
                }



            }
            else
            {
                binding.boxbarcabinetscanning.text.clear()
                InsertBoxcabinet(boxbarsc)

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }

    private fun InsertBoxcabinet(boxbarsc: String) {

        try {
            var n = 0
            val sql =
                " INSERT INTO BOXBARSC (finyear,srno,date,month,year,username,cabinetname,cabinetitemname,CABINETMOELNO) VALUES (?,?,?,?,?,?,?,?,?)"

            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2, "1")
            statement.setString(3,currentDateSimple)
            statement.setString(4,month)
            statement.setString(5,year)
            statement.setString(6,"diksha")
            statement.setString(7,boxbarsc)
            statement.setString(8,boxcabinetitemname)
            statement.setString(9,boxcabinetmodelno)


            n = statement.executeUpdate()
            if (n > 0)
            {

                Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()
                binding.boxbarcabinetscanning.text.clear()


            }
            else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }
        } catch (e: java.lang.Exception) {
            Log.e(ContentValues.TAG, "error: " + e.message)
            Log.d("msgsss", "Insertpartpairing: "+e.message.toString())
            e.printStackTrace()
        }

    }
}