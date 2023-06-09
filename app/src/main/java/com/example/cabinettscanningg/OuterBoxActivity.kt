package com.example.cabinettscanningg

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityOuterBoxBinding
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*

class OuterBoxActivity : AppCompatActivity() ,TextToSpeech.OnInitListener  {
    private lateinit var binding:ActivityOuterBoxBinding

    private var context: Context =this@OuterBoxActivity
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private var day=""
    private  var month=""
    private var year=""
    private var currentDateSimple=""
    private var outercabinetsc=" "
    private var outercabinetitemname=" "
    private var outercabinetmodelno=" "
    private val modellist = ArrayList<OuterBoxModel>()
    var found = false
    private var tts: TextToSpeech? = null
    var macAddress=" "
    var macAddress1=" "
    var macAddress2=" "
    var macAddress3=" "
    var macAddress4=" "
    var macAddress5=" "
    var macAddress6=" "
    var macAddress7=" "
    var macAddress8=" "
    var cabinet1=" "
    var cabinet2=" "
    var cabinet3=" "
    var cabinet4=" "
    var cabinet5=" "
    var cabinet6=" "
    var cabinet7=" "
    var cabinet8=" "
    var itemname=" "
    var itemcode=" "
    private lateinit var sessionManager: SessionManager
    private var newdate=""
    private  var newmonth=""
    private  var newday=""
    var maxcount = "0"
    var rowCount = 0
    var srnum=" "



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOuterBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentDateSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
            Date()
        )
        connectionClass = ConnectionClass(context)
        sessionManager = SessionManager(this)

        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR).toString()
        month = (c.get(Calendar.MONTH)+1).toString()
        day = c.get(Calendar.DAY_OF_MONTH).toString()
        binding.dd.text=day.toString()
        binding.mm.text=month.toString()
        binding.yy.text=year.toString()
        if(month.length==1)
        {
            newmonth='0'+month
        }
        else
        {
            newmonth=month
        }
        if(day.length==1)
        {
            newday='0'+day
        }
        else
        {
            newday=day
        }
        newdate=year+newmonth+newday


        Handler(mainLooper).postDelayed({


            con = connectionClass.CONN()!!
            statement = con.createStatement()
            Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)

        binding.back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,MainActivity::class.java))
            //outercabinetsc=binding.boxbarcabinetscanning.text.toString().trim()
            //checkcabinet(outercabinetsc)
          //  binding.qrcodeScanning.visibility=View.VISIBLE
        })

        binding.qrcodeScanning.setOnClickListener(View.OnClickListener {

            if(modellist.size==8) {
                MacAddress()
                InsertTemptable();

               // InsertOutercabinet()
            }

        })

        binding.boxbarcabinetscanning.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                outercabinetsc=binding.boxbarcabinetscanning.text.toString().trim()
                checkcabinet(outercabinetsc)

                return@setOnKeyListener true
            }
            false
        }
        tts = TextToSpeech(this, this)

       // MacAddress();
//        val macAddress1 =
//            Settings.Secure.getString(this.applicationContext.contentResolver, "android_id")
        Log.d("macaddress1", "onCreate: "+macAddress1)
        binding.username.setText(sessionManager.userName.toString())


    }

    override fun onBackPressed() {
        //super.onBackPressed()
        startActivity(Intent(context,MainActivity::class.java))

    }


    private fun checkcabinet(outercabinet: String) {

        try {
//            val query ="select CABINETNAME,CABINETITEMNAME,CABINETMOELNO from BOXBARSC where  CABINETNAME =('${outercabinet}') "
            val query ="select CABINETNAME,CITEMNAME,CMODELNO from PAIRING where  CABINETNAME =('${outercabinet}') "

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                var outercabinetscdb  = resultSet.getString("cabinetname").trim()

                if(outercabinetsc==outercabinet)
                {
                    outercabinetitemname  = resultSet.getString("citemname").trim()
                    outercabinetmodelno  = resultSet.getString("cmodelno")
                    // InsertQCcabinet(cabinetname)
                    checkduplicate(outercabinet)
                }



            }
            else
            {
                binding.boxbarcabinetscanning.text.clear()
                myToastRed(this,"Invalid Cabinet Number")
                speakOut("Invalid Cabinet Number")

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }

    private fun checkduplicate(boxbarsc: String) {

        try {
            val query ="select cabinetname from Outerbox where  cabinetname =('${boxbarsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                var outercabinetdb  = resultSet.getString("cabinetname").trim()

                if(outercabinetdb==boxbarsc)
                {

                    binding.boxbarcabinetscanning.text.clear()
                    myToastyellow(this," Already Exist")
                }



            }
            else
            {


                for (data in modellist) {
                    Log.d("modellistitem1", "checkduplicate: "+data.cabitscan.toString())
                    Log.d("modellistitem2", "checkduplicate: "+binding.boxbarcabinetscanning.text.toString().trim())

                    if (data.cabitscan.toString() == binding.boxbarcabinetscanning.text.toString().trim()) {
                        found = true
                        break
                    }
                }

                if(found)
                {
                    myToastyellow(this," Already Exist in grid")
                    binding.boxbarcabinetscanning.text.clear()
                    found = false
                    speakOut(" Already Exist in grid")
                }
                else {
                    Log.d("listszie", "checkduplicate: "+modellist.size)
                    if(modellist.size!=8) {
                        MacAddress()
                        rowcount();
                        maxvalue()

                        if (rowCount == 0) {

                            if(modellist.size==0) {
                                srnum = "1"
                            }
                            else
                            {
                                srnum = "1"
                            }
                        } else {
                            if(modellist.size==0) {
                                val abc = Integer.valueOf(maxcount) + 1
                                srnum = abc.toString()
                            }
                            else
                            {
                                srnum
                            }


                        }
                        insertOuterboxQC(boxbarsc.trim().toString(),srnum)

                            modellist.add(
                            OuterBoxModel(
                                boxbarsc,
                                outercabinetitemname,
                                outercabinetmodelno
                            )
                        )

                        binding.pacingquantity.text =
                            "Packing Quantity-" + modellist.size.toString()
                        Log.e(ContentValues.TAG, "ListData$modellist")
                        binding.c.visibility = View.VISIBLE
                        binding.list.visibility = View.VISIBLE
                        val obj_adapter = AdapterVechicleItemList(modellist)

                        binding.list.layoutManager = LinearLayoutManager(this)
                        binding.list.adapter = obj_adapter
                        if(modellist.size==1)
                        {
                            MacAddress()
                            cabinet1=boxbarsc;
                            macAddress1=macAddress;

                            Log.d("ssjjshd", "checkduplicate: "+boxbarsc)

                        }
                        if(modellist.size==2)
                        {
                            MacAddress()
                            cabinet2=boxbarsc;
                            macAddress2=macAddress;

                        }
                        if(modellist.size==3)
                        {
                            MacAddress()
                            cabinet3=boxbarsc;
                            macAddress3=macAddress;

                        }
                        if(modellist.size==4)
                        {
                            MacAddress()
                            cabinet4=boxbarsc;
                            macAddress4=macAddress;

                        }
                        if(modellist.size==5)
                        {
                            MacAddress()
                            cabinet5=boxbarsc;
                            macAddress5=macAddress;

                        }
                        if(modellist.size==6)
                        {
                            MacAddress()
                            cabinet6=boxbarsc;
                            macAddress6=macAddress;

                        }
                        if(modellist.size==7)
                        {
                            MacAddress()
                            cabinet7=boxbarsc;
                            macAddress7=macAddress;

                        }
                        if(modellist.size==8)
                        {
                            MacAddress()
                            cabinet8=boxbarsc;
                            macAddress8=macAddress;
                            binding.qrcodeScanning.visibility=View.VISIBLE

                        }
                        binding.boxbarcabinetscanning.text.clear()
                        binding.boxbarcabinetscanning.requestFocus()
                    }
                }
            }

//            val obj_adapter = CustomAdapter(users)
//
//            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//            recyclerView.adapter = obj_adapter
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }

    private fun InsertOutercabinet() {
        for (data in modellist)
        {
        try {
            var n = 0
            val sql =
                " INSERT INTO Outerbox (finyear,srno,date,month,year,username,cabinetname,cabinetitemname,CABINETMODELNO) VALUES (?,?,?,?,?,?,?,?,?)"

            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2, "1")
            statement.setString(3,currentDateSimple)
            statement.setString(4,month)
            statement.setString(5,year)
            statement.setString(6,sessionManager.userName)
            statement.setString(7,data.cabitscan)
            statement.setString(8,data.cbititem)
            statement.setString(9,data.cabitmodelno)


            n = statement.executeUpdate()
            if (n > 0)
            {

                Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()
                binding.boxbarcabinetscanning.text.clear()
                binding.c.visibility = View.GONE
                binding.list.visibility=View.GONE
                binding.pacingquantity.text = "Packing Quantity-0" .toString()
                binding.qrcodeScanning.visibility=View.GONE


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


    private fun MacAddress()
    {
       // val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
       // val wInfo = wifiManager.connectionInfo
       // macAddress = wInfo.macAddress

        macAddress =
            Settings.Secure.getString(this.applicationContext.contentResolver, "android_id")

    }


    private fun InsertTemptable( ) {

        try {
            var n = 0
//            val sql =
//                " INSERT INTO TEMPBATTERY(finyear,Date,month,Username,Cabinetname1,MacAds1,Cabinetname2,MacAds2,Cabinetname3,MacAds3,Cabinetname4,MacAds4,Cabinetname5,MacAds5,Cabinetname6,MacAds6,Cabinetname7,MacAds7,Cabinetname8,MacAds8) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

            val sql =
                " INSERT INTO TEMPBATTERY(finyear,Date,month,Username,MacAds1,ITEMNAME,ITEMCODE,Cabinetname1,Cabinetname2,Cabinetname3,Cabinetname4,Cabinetname5,Cabinetname6,Cabinetname7,Cabinetname8) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2,currentDateSimple)
            statement.setString(3,month)
            statement.setString(4,sessionManager.userName)
            statement.setString(5,macAddress1)
            statement.setString(6, outercabinetitemname)
            statement.setString(7,outercabinetmodelno)
            statement.setString(8, cabinet1)
            statement.setString(9,cabinet2)
            statement.setString(10, cabinet3)
            statement.setString(11,cabinet4)
            statement.setString(12, cabinet5)
            statement.setString(13,cabinet6)
            statement.setString(14, cabinet7)
            statement.setString(15,cabinet8)







            n = statement.executeUpdate()
            if (n > 0)
            {

                // Toast.makeText(this, "successfully Inserted Temp Table", Toast.LENGTH_SHORT).show()
                binding.boxbarcabinetscanning.text.clear()
                binding.c.visibility = View.GONE
                binding.list.visibility=View.GONE
                binding.pacingquantity.text = "Packing Quantity-0" .toString()
                binding.qrcodeScanning.visibility=View.GONE
                // generenatebarcode()


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

    private fun truncateTable() {
        try {
            var n = 0
            val sql = "TRUNCATE TABLE TEMPBATTERY"

            val statement = con.prepareStatement(sql)
            n = statement.executeUpdate()

            if (n > 0) {
                // Truncation successful
            } else {
                // No rows affected
            }
        } catch (e: SQLException) {
            Log.e(ContentValues.TAG, "Error: " + e.message)
            e.printStackTrace()
        }
    }
///
private fun insertOuterboxQC(cabinetname:String,srnum:String) {
    //mam run kraye debug ? kruyesok
    try {
        var n = 0

//        if (counting == 0) {
//            var srnum = "1"
//            //  rowCount=Integer.valueOf(srnum)
//            //Log.d("rowcount1", "insertFollowQC: "+rowCount)
//        } else {
//            //val abc = Integer.valueOf(maxcount) + 1
//            // rowCount=abc
//           // Log.d("maxxx", "insertDatatwo: $abc")
//           //var srnum = abc.toString()
//        }
        val cstmt = con.prepareCall("{call ERCCAB  (?,?,?,?,?,?,?,?)}")
        cstmt.setString(1, cabinetname)
        cstmt.setString(2, macAddress)
//        cstmt.setInt(3, 20230503)
        cstmt.setInt(3, newdate.toInt())
        cstmt.setString(4, month)
        cstmt.setString(5,sessionManager.userName )
        cstmt.setString(6,year )
        cstmt.setString(7, srnum)
        cstmt.setString(8, year)
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
    private fun rowcount() {
        try {
            val query = "select count(*)  from Outerbox"
            val resultSet: ResultSet = statement.executeQuery(query)
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1)
                Log.d("clclclclcl1", "rowcount: $rowCount")
            }
        } catch (e: java.lang.Exception) {
            Log.d("clclclclcl2", "rowcount: $e")
            // Handle the exception
        }
        //  String tota="Total Scanned-";
        //binding.ocp.setText(tota+String.valueOf(rowCount));
    }

    private fun maxvalue() {

        //  int  maxcount=0;
        try {
            val query = "select MAX(SRNO) As SRNO  from Outerbox"
            val resultSet: ResultSet = statement.executeQuery(query)
            if (resultSet.next()) {
                maxcount = resultSet.getString("SRNO")
                Log.d("clclclclcl2", "rowcount: $maxcount")
            }
        } catch (e: java.lang.Exception) {
            Log.d("clclclclcl2", "rowcount: $e")
            // Handle the exception
        }
        //  String tota="Total Scanned-";
        // binding.ocp.setText(tota+String.valueOf(rowCount));
    }

}