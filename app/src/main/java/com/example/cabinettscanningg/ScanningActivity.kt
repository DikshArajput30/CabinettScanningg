package com.example.cabinettscanningg
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.crm_copy.other.myToastGreen
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityScanningBinding
import java.sql.Connection
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*

class ScanningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanningBinding
    private var context: Context =this@ScanningActivity
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private var cabinetsc=" "
    private var currentDateSimple=""
    private  var cabinetitemname=""
    private var cabinetmoelno=""
    private var day=""
    private  var month=""
    private var year=""
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionClass = ConnectionClass(context)
        sessionManager = SessionManager(this)
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
        Log.d("mongthh1", "onCreate: "+year)
        Log.d("mongthh2", "onCreate: "+month)
        Log.d("mongthh3", "onCreate: "+day)
        Handler(mainLooper).postDelayed({


                con = connectionClass.CONN()!!
                statement = con.createStatement()
                Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)
        binding.back.setOnClickListener(View.OnClickListener {
          // startActivity(Intent(context,MainActivity::class.java))
           cabinetsc=binding.cabinetscanning.text.toString()
          checkCabinetNumbertest(cabinetsc)
        })

        binding.cabinetscanning.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                cabinetsc=binding.cabinetscanning.text.toString().trim()
                checkCabinetNumbertest(cabinetsc)

                return@setOnKeyListener true
            }
            false
        }

        binding.username.setText(sessionManager.userName.toString())
    }

    override fun onBackPressed() {
        startActivity(Intent(context,MainActivity::class.java))

    }
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
                    cabinetmoelno = resultSet.getString("modelno")
                    checkdupli(cabinetsc)
                }

            }
            else {
               myToastRed(this,"Invalid serial Number")
               binding.cabinetscanning.text.clear()


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }

    private fun checkdupli(cabinetsc: String) {

            try {
               val query ="select Barcodeno from Cabinet1 where  Barcodeno =('${cabinetsc}') "

               // val query ="select Barcodeno from BARCODEGENERATIONSUB] where  Barcodeno=('${cabinetsc}')"


                val resultSet = statement.executeQuery(query)
                if (resultSet.next())
                {

                    val cabinetscdupicate = resultSet.getString("Barcodeno").trim()
                    Log.d("dupblcatee1", "checkdupli: "+cabinetscdupicate.toString())

                   if (cabinetscdupicate==cabinetsc)

                    {
                       myToastyellow(this," Already Exist")
                        binding.cabinetscanning.text.clear()

                   }


                }
                else
                {
                    insertcabinetScanning()
                }
            }
            catch (se: Exception)
            {
                Log.e("ERROR", se.message!!)
            }

    }

    private fun insertcabinetScanning() {
        try {var n = 0
            val sql =
                " INSERT INTO Cabinet1 (finyear,srno,date,month,year,username,barcodeno,itemname,modelno) VALUES (?,?,?,?,?,?,?,?,?)"
            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2, "1")
            // statement.setString(3, "2023-01-02 04:25:33.0980000")
            statement.setString(3, currentDateSimple)
            statement.setString(4, month)
            statement.setString(5, year)
            statement.setString(6, sessionManager.userName.toString())
            statement.setString(7, cabinetsc)
            statement.setString(8, cabinetitemname)
            statement.setString(9, cabinetmoelno)



            n = statement.executeUpdate()
            if (n > 0)
            {

                Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()
                binding.cabinetscanning.text.clear()

            }
            else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }
        } catch (e: java.lang.Exception) {
            Log.e(ContentValues.TAG, "error: " + e.message)
            e.printStackTrace()
        }

    }

}