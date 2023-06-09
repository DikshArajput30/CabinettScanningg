package com.example.cabinettscanningg

import android.R
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityQcScanningBinding
import com.google.zxing.*
import com.zebra.sdk.comm.ConnectionException
import com.zebra.sdk.comm.TcpConnection
import java.io.File
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*


class QcScanningActivity : AppCompatActivity(), TextToSpeech.OnInitListener,AdapterView.OnItemSelectedListener {
    private lateinit var binding:  ActivityQcScanningBinding
    private var context:Context=this@QcScanningActivity
    private var cabinetsc=""
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private var currentDateSimple=""
    private  var cabinetname=""
    private  var cabinetitemname=""
    private  var cabinetmodelno=""
    private var day=""
    private  var month=""
    private  var newmonth=""
    private  var newday=""

    private var year=""
    private var newdate=""
    private var tts: TextToSpeech? = null
    var macAddress=" "
    var rowCount = 0
    var srnum=" "
    var maxcount = "0"
    var line=" "
    private lateinit var sessionManager: SessionManager
    var list_of_items = arrayOf("Please Select","1", "2", "3","4","5","6","7","8","9")
    var Finyear1=" "
    var CabinetName1=" "
    var Macaddress1=" "
    var Date1=" "
    var month1=" "
    var Username1=" "
    var ITEMCODE1=" "
    var NETWEIGHT1=" "
    var GROSSWEIGHT1=" "
    var MRP1=" "
    var DIMENSION1=" "
    var pr1=" "
    var ITEMNAME1=""
    var largemrpip=" "
    var smallmrpip=" "
    var ip="192.168.1.75"
    var small=""
    var large=" "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityQcScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentDateSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
            Date()
        )
        connectionClass = ConnectionClass(context)
        sessionManager = SessionManager(this)
        binding.spinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.spinner!!.setAdapter(aa)

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

        Log.d("newdate", "onCreate: "+newdate)

        Handler(mainLooper).postDelayed({


            con = connectionClass.CONN()!!
            statement = con.createStatement()
            Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)
        binding.back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,MainActivity::class.java))
            //sendZplSmallTcpQC(smallmrpip)
//            sendZplOverTcpQC(smallmrpip)
//            sendZplOverTcpQC(smallmrpip)

           // sendZplOverTcpQC("192.168.1.75")
            //sendZplOverTcpQC("192.168.1.75")

            Log.d("smamanmnma", "onCreate: "+smallmrpip)

        })

        binding.cabinetscanqc.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {


                return@setOnKeyListener false
            }
            Log.d("clcikmee", "onCreate: "+"clcikmee")
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                cabinetsc=binding.cabinetscanqc.text.toString().trim()
                rowcount()
                maxvalue()
                MacAddress()
                Log.d("rowcount2", "insertFollowQC: "+rowCount)

                insertFollowQC(cabinetsc,rowCount)

                getfolderpath()

                    //sendZplOverTcpQC(large.trim().toString())
                   // sendZplOverTcpQC(large.trim().toString())
                smallmrpip=binding.small.text.toString()
                largemrpip=binding.large.text.toString()
                Log.d("smamaktrippsdsd", "onCreate: "+CabinetName1)
                if(CabinetName1==" ")
                {
                    Log.d("emoii1", "onCreate: "+"emoii1")

                }
                else
                {
                    sendZplOverTcpQC(largemrpip.trim().toString())
                    sendZplSmallTcpQC(smallmrpip.trim().toString())
                    CabinetName1=" "
                    Log.d("emoii12", "onCreate: "+"emoii2")
                }


                return@setOnKeyListener true
            }
            false
        }

      //  generenatebarcode()

        val gfgPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(gfgPolicy)


        tts = TextToSpeech(this, this)

        binding.username.setText(sessionManager.userName.toString())
            //   sendZplSmallTcpQC(smallmrpip)
        getfolderpath()

    }
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
        line=position.toString()
        (binding.spinner.getSelectedView() as TextView).setTextColor(Color.BLACK)

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onBackPressed() {
        startActivity(Intent(context,MainActivity::class.java))
    }

    private fun checkcabinet(cabinetsc: String) {

        try {
            val query ="select cabinetname,citemname,cmodelno from PAIRING where  cabinetname =('${cabinetsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                cabinetname  = resultSet.getString("cabinetname").trim()

                if(cabinetname==cabinetsc)
                {
                    cabinetitemname  = resultSet.getString("citemname").trim()
                    cabinetmodelno  = resultSet.getString("cmodelno").trim()
                   // InsertQCcabinet(cabinetname)
                    checkduplicate(cabinetsc)
                }



            }
            else
            {
                binding.cabinetscanqc.text.clear()
                myToastRed(this,"Invalid Cabinet Number")
                speakOut(" Invalid Cabinet Number")


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }
    private fun InsertQCcabinet(cabinetname: String) {
        try {
             var n=0
//            val sql =
//                " INSERT INTO Qccabinet (finyear,srno,date,month,year,username,cabinetname,cabinetitemname,CABINETMOELNO) VALUES (?,?,?,?,?,?,?,?,?)"
            val sql =
                " INSERT INTO Qccabinet (finyear,srno,date,month,year,username,cabinetname,cabinetitemname,CABINETMOELNO) VALUES (?,?,?,?,?,?,?,?,?)"

            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2, "1")
            statement.setString(3,currentDateSimple)
            statement.setString(4,month)
            statement.setString(5,year)
            statement.setString(6,sessionManager.userName)
            statement.setString(7,cabinetname)
            statement.setString(8,cabinetitemname)
            statement.setString(9,cabinetmodelno)


            n = statement.executeUpdate()
            if (n > 0)
            {
                binding.cabinetscanqc.text.clear()
                Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()

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

    private fun checkduplicate(cabinetsc: String) {

        try {
            val query ="select cabinetname from Qccabinet where  cabinetname =('${cabinetsc}') "
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {

                cabinetname  = resultSet.getString("cabinetname").trim()

                if(cabinetname==cabinetsc)
                {

                  binding.cabinetscanqc.text.clear()
                    myToastyellow(this," Already Exist")
                    speakOut(" Already Exist")
                }



            }
            else
            {

                MacAddress()
                InsertTemptable(cabinetsc)
                binding.cabinetscanqc.text.clear()
                truncateTable()
                 MacAddress()
                  Log.d("mackaddree", "onCreate: "+macAddress)
                  InsertTemptable(cabinetsc)

                InsertQCcabinet(cabinetsc)

            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }


    @SuppressLint("SuspiciousIndentation")
    @Throws(ConnectionException::class)
     fun sendZplOverTcpQC(theIpAddress: String?) {
        // Instantiate connection for ZPL TCP port at given address
        val thePrinterConn: com.zebra.sdk.comm.Connection =
            TcpConnection(theIpAddress, TcpConnection.DEFAULT_ZPL_TCP_PORT)
        try {
            thePrinterConn.open()
//            val abcd="301SA3VE1A0H00001".trim()
            val abcd1="282828282"
            val abcd="ammy1234ammy12345"
            var ammyitemname=ITEMNAME1
            ammyitemname = ammyitemname.substring(0, ammyitemname.length - 3)

////////////////////first ip 1292.168.1.35
            val zplData="\u0010CT~~CD,~CC^~CT~\n" +
                    "^XA\n" +
                    "~TA000\n" +
                    "~JSN\n" +
                    "^LT0\n" +
                    "^MNW\n" +
                    "^MTT\n" +
                    "^PON\n" +
                    "^PMN\n" +
                    "^LH0,0\n" +
                    "^JMA\n" +
                    "^PR5,5\n" +
                    "~SD24\n" +
                    "^JUS\n" +
                    "^LRN\n" +
                    "^CI27\n" +
                    "^PA0,1,1,0\n" +
                    "^XZ\n" +
                    "^XA\n" +
                    "^MMT\n" +
                    "^PW1181\n" +
                    "^LL1181\n" +
                    "^LS0\n" +
                    "^FT77,91^A0N,50,46^FH\\^CI28^FDMFG. MONTH & YEAR^FS^CI27\n" +
                    "^FT77,150^A0N,46,48^FH\\^CI28^FDPart Number^FS^CI27\n" +
                    "^FT77,259^A0N,50,48^FH\\^CI28^FDSerial Number^FS^CI27\n" +
                    "^FT77,371^A0N,50,48^FH\\^CI28^FDPart Name or Item^FS^CI27\n" +
                    "^FT77,425^A0N,50,41^FH\\^CI28^FDNet Quantity^FS^CI27\n" +
                    "^FT69,498^A0N,50,48^FH\\^CI28^FDNet Weight^FS^CI27\n" +
                    "^FT69,561^A0N,50,48^FH\\^CI28^FDGross Weight^FS^CI27\n" +
                    "^FT69,628^A0N,50,48^FH\\^CI28^FDDimension^FS^CI27\n" +
                    "^FT69,707^A0N,50,48^FH\\^CI28^FDMaximum Retail Price^FS^CI27\n" +
                    "^FT645,93^A0N,46,46^FH\\^CI28^FD$month1,$Finyear1^FS^CI27\n" +
                    "^FT645,150^A0N,46,46^FH\\^CI28^FD$ammyitemname^FS^CI27\n" +
                    "^FT645,367^A0N,46,46^FH\\^CI28^FDHOME UPS^FS^CI27\n" +
                    "^FT661,421^A0N,46,46^FH\\^CI28^FD1 N^FS^CI27\n" +
                    "^FT661,557^A0N,46,46^FH\\^CI28^FD$GROSSWEIGHT1 KG ^FS^CI27\n" +
                    "^FT603,604^A0N,29,43^FH\\^CI28^FD$DIMENSION1^FS^CI27\n" +
                    "^FT606,637^A0N,22,30^FH\\^CI28^FD(LENGTH X WIDTH X HEIGHT)^FS^CI27\n" +
                    "^FO577,326^GB0,855,5^FS\n" +
                    "^FO577,41^GB0,120,5^FS\n" +
                    "^FO45,100^GB1136,0,5^FS\n" +
                    "^FO42,170^GFA,69,1008,144,:Z64:eJz7/x8r+MMwQAC7c0gGDwaZe0gFg809OMCDQeceHIB5kLmHVCBPLfcAALoIr/A=:508C\n" +
                    "^FO45,321^GB1136,0,5^FS\n" +
                    "^FO42,375^GB1139,0,5^FS\n" +
                    "^FO33,434^GB1148,0,5^FS\n" +
                    "^FO33,505^GB1148,0,5^FS\n" +
                    "^FO45,573^GB1136,0,5^FS\n" +
                    "^FO33,652^GB1148,0,5^FS\n" +
                    "^FO45,722^GB1136,0,5^FS\n" +
                    "^FT69,778^A0N,48,48^FH\\^CI28^FDMarketed By :^FS^CI27\n" +
                    "^FT55,837^A0N,38,38^FH\\^CI28^FDEastman Auto & Power Limited. ^FS^CI27\n" +
                    "^FT55,883^A0N,38,38^FH\\^CI28^FDPlot No.- 572, Udyog Vihar ^FS^CI27\n" +
                    "^FT55,928^A0N,46,30^FH\\^CI28^FDPhase-5, Gurgaon (Haryana) - 122016^FS^CI27\n" +
                    "^FT594,775^A0N,48,48^FH\\^CI28^FDCorporate Office:^FS^CI27\n" +
                    "^FO39,945^GFA,69,1008,144,:Z64:eJz7/x8r+McwQAC7c0gGfwaZe0gFPwaZe3CAP4POPTgA4yBzD6mAmVruAQC0LbFY:530B\n" +
                    "^FT69,996^A0N,48,41^FH\\^CI28^FDManufactured By:^FS^CI27\n" +
                    "^FT55,1049^A0N,42,30^FH\\^CI28^FDVoltsman Power Technologies Pvt. Ltd.^FS^CI27\n" +
                    "^FT55,1095^A0N,42,30^FH\\^CI28^FDKhasra No. 93/25, 2-09 and 94/21, 2-06,^FS^CI27\n" +
                    "^FT55,1138^A0N,42,30^FH\\^CI28^FDFirni Road, Mundka, New Delhi , West^FS^CI27\n" +
                    "^FT55,1172^A0N,33,33^FH\\^CI28^FDDelhi,Delhi 110041^FS^CI27\n" +
                    "^FT594,837^A0N,38,38^FH\\^CI28^FDEastman Auto & Power Limited.^FS^CI27\n" +
                    "^FT594,883^A0N,38,38^FH\\^CI28^FDPlot No.-572 Udyog Vihar, Phase-5^FS^CI27\n" +
                    "^FT594,941^A0N,46,46^FH\\^CI28^FDGurgaon (Haryana)- 122016^FS^CI27\n" +
                    "^FT594,996^A0N,48,46^FH\\^CI28^FDFor Consumer Complaints:^FS^CI27\n" +
                    "^FT594,1049^A0N,42,41^FH\\^CI28^FDIndia Toll Free No.: 18004198610^FS^CI27\n" +
                    "^FT594,1105^A0N,52,38^FH\\^CI28^FDEmail : Support@EastmanGlobal.com^FS^CI27\n" +
                    "^FT594,1159^A0N,42,41^FH\\^CI28^FDWebsite: www.eaplworld.com^FS^CI27\n" +
                    "^FT615,711^A0N,46,46^FH\\^CI28^FDRs.^FS^CI27\n" +
                    "^FT678,711^A0N,46,46^FH\\^CI28^FD$MRP1^FS^CI27\n" +
                    "^FT860,700^A0N,39,34^FH\\^CI28^FD(inclusive of all taxes)^FS^CI27\n" +
                    "^FT661,494^A0N,46,46^FH\\^CI28^FD$NETWEIGHT1 KG ^FS^CI27\n" +
                    "^FT603,311^A0N,38,56^FH\\^CI28^FD$CabinetName1^FS^CI27\n" +
                    "^BY3,3,92^FT494,273^BCN,,N,N\n" +
                    "^FH\\^FD>:$CabinetName1^FS\n" +
                    "^FO33,785^GB309,0,5^FS\n" +
                    "^FO72,1004^GB309,0,5^FS\n" +
                    "^FO603,783^GB380,0,5^FS\n" +
                    "^FO603,1004^GB477,0,5^FS\n" +
                    "^FO34,41^GB1147,1140,4^FS\n" +
                    "^PQ1,0,1,Y\n" +
                    "^XZ\n"

            ////////////second ip 192.168.1.54
//            val zplData="\u0010CT~~CD,~CC^~CT~\n" +
//                    "^XA\n" +
//                    "~TA000\n" +
//                    "~JSN\n" +
//                    "^LT35\n" +
//                    "^MNW\n" +
//                    "^MTD\n" +
//                    "^PON\n" +
//                    "^PMN\n" +
//                    "^LH0,0\n" +
//                    "^JMA\n" +
//                    "^PR6,6\n" +
//                    "~SD23\n" +
//                    "^JUS\n" +
//                    "^LRN\n" +
//                    "^CI27\n" +
//                    "^PA0,1,1,0\n" +
//                    "^XZ\n" +
//                    "^XA\n" +
//                    "^MMT\n" +
//                    "^PW1181\n" +
//                    "^LL1181\n" +
//                    "^LS0\n" +
//                    "^FT61,73^A0N,50,46^FH\\^CI28^FDMFG. MONTH & YEAR^FS^CI27\n" +
//                    "^FT61,132^A0N,46,48^FH\\^CI28^FDPart Number^FS^CI27\n" +
//                    "^FT61,241^A0N,50,48^FH\\^CI28^FDSerial Number^FS^CI27\n" +
//                    "^FT61,353^A0N,50,48^FH\\^CI28^FDPart Name Or Item^FS^CI27\n" +
//                    "^FT61,407^A0N,50,41^FH\\^CI28^FDNet Quantity^FS^CI27\n" +
//                    "^FT53,480^A0N,50,48^FH\\^CI28^FDNet Weight^FS^CI27\n" +
//                    "^FT53,543^A0N,50,48^FH\\^CI28^FDGross Weight^FS^CI27\n" +
//                    "^FT53,609^A0N,50,48^FH\\^CI28^FDDimension^FS^CI27\n" +
//                    "^FT53,689^A0N,50,48^FH\\^CI28^FDMaximum Retail Price^FS^CI27\n" +
//                    "^FT636,75^A0N,46,46^FH\\^CI28^FD$month1,$year^FS^CI27\n" +
//                    "^FT636,141^A0N,46,46^FH\\^CI28^FD$ammyitemname^FS^CI27\n" +
//                    "^FT636,349^A0N,46,46^FH\\^CI28^FDHOME UPS^FS^CI27\n" +
//                    "^FT645,403^A0N,46,46^FH\\^CI28^FD1 N^FS^CI27\n" +
//                    "^FT645,539^A0N,46,46^FH\\^CI28^FD$GROSSWEIGHT1 KG ^FS^CI27\n" +
//                    "^FT588,586^A0N,29,43^FH\\^CI28^FD$DIMENSION1^FS^CI27\n" +
//                    "^FT590,619^A0N,22,30^FH\\^CI28^FD(LENGTH X WIDHT X HEIGHT)^FS^CI27\n" +
//                    "^FO561,308^GB0,856,5^FS\n" +
//                    "^FO561,23^GB0,120,5^FS\n" +
//                    "^FO29,82^GB1143,0,5^FS\n" +
//                    "^FO26,152^GFA,69,1008,144,:Z64:eJz7/x8r+MMwQAC7c0gGDwaZe0gFg809OMC/QeceHIB5kLmHVCBPLfcAACBHsGg=:34FD\n" +
//                    "^FO29,303^GB1140,0,5^FS\n" +
//                    "^FO26,357^GB1146,0,5^FS\n" +
//                    "^FO17,416^GB1157,0,5^FS\n" +
//                    "^FO17,487^GB1155,0,5^FS\n" +
//                    "^FO29,555^GB1143,0,5^FS\n" +
//                    "^FO17,634^GB1155,0,5^FS\n" +
//                    "^FO29,704^GB1143,0,5^FS\n" +
//                    "^FT53,759^A0N,48,48^FH\\^CI28^FDMarketed By :^FS^CI27\n" +
//                    "^FT39,819^A0N,38,38^FH\\^CI28^FDEastman Auto & Power Limited. ^FS^CI27\n" +
//                    "^FT39,864^A0N,38,38^FH\\^CI28^FDPlot No.- 572, Udyog Vihar ^FS^CI27\n" +
//                    "^FT39,910^A0N,46,30^FH\\^CI28^FDPhase-5, Gurgaon (Haryana) - 122016^FS^CI27\n" +
//                    "^FT578,757^A0N,48,48^FH\\^CI28^FDCorporate Office:^FS^CI27\n" +
//                    "^FO23,927^GFA,73,1008,144,:Z64:eJz7/x8r+McwQAC7c0gGfwaZe0gFPwaZe3CBDwPtADTwAUewMQ4y95AKmKnlHgDsUrUk:9FD0\n" +
//                    "^FT53,978^A0N,48,41^FH\\^CI28^FDManufactured By:^FS^CI27\n" +
//                    "^FT39,1022^A0N,33,25^FH\\^CI28^FDVoltsman Power Technologies Pvt. Ltd.^FS^CI27\n" +
//                    "^FT39,1053^A0N,33,25^FH\\^CI28^FDKhasra No. 93/25, 2-09 and 94/21, 2-06,^FS^CI27\n" +
//                    "^FT39,1084^A0N,33,25^FH\\^CI28^FDFirni Road, Mundka, New Delhi , West^FS^CI27\n" +
//                    "^FT39,1125^A0N,33,33^FH\\^CI28^FDDelhi,Delhi 110041^FS^CI27\n" +
//                    "^FT578,819^A0N,38,38^FH\\^CI28^FDEastman Auto & Power Limited.^FS^CI27\n" +
//                    "^FT578,864^A0N,38,35^FH\\^CI28^FDPlot No.-572 Udyog Vihar, Phase-5^FS^CI27\n" +
//                    "^FT578,923^A0N,46,46^FH\\^CI28^FDGurgaon (Haryana)- 122016^FS^CI27\n" +
//                    "^FT578,978^A0N,48,46^FH\\^CI28^FDFor Consumer Complaints:^FS^CI27\n" +
//                    "^FT578,1031^A0N,42,41^FH\\^CI28^FDIndia Toll Free No.: 18004198610^FS^CI27\n" +
//                    "^FT578,1077^A0N,42,30^FH\\^CI28^FDEmail : Support@EastmanGlobal.com^FS^CI27\n" +
//                    "^FT578,1120^A0N,42,41^FH\\^CI28^FDWebsite: www.eaplworld.com^FS^CI27\n" +
//                    "^FT578,693^A0N,46,46^FH\\^CI28^FDRs.^FS^CI27\n" +
//                    "^FT641,693^A0N,46,46^FH\\^CI28^FD$MRP1^FS^CI27\n" +
//                    "^FT787,692^A0N,38,33^FH\\^CI28^FD(inclusive of all taxes)^FS^CI27\n" +
//                    "^FT645,476^A0N,46,46^FH\\^CI28^FD$NETWEIGHT1 KG ^FS^CI27\n" +
//                    "^FT588,293^A0N,38,56^FH\\^CI28^FD$CabinetName1^FS^CI27\n" +
//                    "^BY3,3,92^FT480,255^BCN,,N,N\n" +
//                    "^FH\\^FD>:$CabinetName1^FS\n" +
//                    "^FO17,767^GB309,0,5^FS\n" +
//                    "^FO56,986^GB309,0,5^FS\n" +
//                    "^FO588,765^GB380,0,5^FS\n" +
//                    "^FO588,986^GB477,0,5^FS\n" +
//                    "^FO18,23^GB1154,1141,4^FS\n" +
//                    "^PQ1,0,1,Y\n" +
//                    "^XZ\n"

            thePrinterConn.write(zplData.toByteArray())
            //ConvertTextToSpeech();
            Log.d("sssdds1", "sendZplOverTcp: " + "toastt1")
        } catch (e: ConnectionException) {
            // Handle communications error here.
            e.printStackTrace()
            Log.d("sssdds2", "sendZplOverTcp: $e")
            Toast.makeText(this, "Please connect to network", Toast.LENGTH_LONG).show()
        } finally {
            // Close the connection to release resources.
            thePrinterConn.close()
            Log.d("sssdds3", "sendZplOverTcp: " + "toastt")
        }
    }
    fun sendZplSmallTcpQC(theIpAddress: String?) {
        // Instantiate connection for ZPL TCP port at given address
        val thePrinterConn: com.zebra.sdk.comm.Connection =
            TcpConnection(theIpAddress, TcpConnection.DEFAULT_ZPL_TCP_PORT)
        try {
            thePrinterConn.open()
//            val abcd="301SA3VE1A0H00001".trim()
            val abcd1="282828282"
            val abcd="ammy1234ammy12345"
            Log.d("CabinetName1", "sendZplSmallTcpQC: "+CabinetName1)

            ///////////new
//            val zplData="\u0010CT~~CD,~CC^~CT~\n" +
//                    "^XA\n" +
//                    "~TA000\n" +
//                    "~JSN\n" +
//                    "^LT0\n" +
//                    "^MNW\n" +
//                    "^MTT\n" +
//                    "^PON\n" +
//                    "^PMN\n" +
//                    "^LH0,0\n" +
//                    "^JMA\n" +
//                    "^PR6,6\n" +
//                    "~SD24\n" +
//                    "^JUS\n" +
//                    "^LRN\n" +
//                    "^CI27\n" +
//                    "^PA0,1,1,0\n" +
//                    "^XZ\n" +
//                    "^XA\n" +
//                    "^MMT\n" +
//                    "^PW472\n" +
//                    "^LL177\n" +
//                    "^LS0\n" +
//                    "^BY2,3,95^FT11,125^BCN,,N,N\n" +
//                    "^FH\\^FD>:$CabinetName1^FS\n" +
//                    "^FT39,167^A0N,36,46^FH\\^CI28^FD$CabinetName1^FS^CI27\n" +
//                    "^PQ1,0,1,Y\n" +
//                    "^XZ"
//            val zplData="\u0010CT~CD,~CC^~CT\n" +
//                    "^XA\n" +
//                    "~TA000\n" +
//                    "~JSN\n" +
//                    "^LT0\n" +
//                    "^MNW\n" +
//                    "^MTT\n" +
//                    "^PON\n" +
//                    "^PMN\n" +
//                    "^LH0,0\n" +
//                    "^JMA\n" +
//                    "^PR3,3\n" +
//                    "~SD18\n" +
//                    "^JUS\n" +
//                    "^LRN\n" +
//                    "^CI27\n" +
//                    "^PA0,1,1,0\n" +
//                    "^XZ\n" +
//                    "^XA\n" +
//                    "^MMT\n" +
//                    "^PW472\n" +
//                    "^LL177\n" +
//                    "^LS0\n" +
//                    "^BY2,3,95^FT11,125^BCN,,N,N\n" +
//                    "^FH\\^FD>:$CabinetName1^FS\n" +
//                    "^FT39,167^A0N,36,46^FH\\^CI28^FD$CabinetName1^FS^CI27\n" +
//                    "^PQ1,0,1,Y\n" +
//                    "^XZ"

            //////////////////////////on;yy 54
//            val zplData="\u0010CT~CD,~CC^~CT\n" +
//                    "^XA\n" +
//                    "~TA000\n" +
//                    "~JSN\n" +
//                    "^LT0\n" +
//                    "^MNW\n" +
//                    "^MTT\n" +
//                    "^PON\n" +
//                    "^PMN\n" +
//                    "^LH0,0\n" +
//                    "^JMA\n" +
//                    "^PR3,3\n" +
//                    "~SD18\n" +
//                    "^JUS\n" +
//                    "^LRN\n" +
//                    "^CI27\n" +
//                    "^PA0,1,1,0\n" +
//                    "^XZ\n" +
//                    "^XA\n" +
//                    "^MMT\n" +
//                    "^PW472\n" +
//                    "^LL177\n" +
//                    "^LS0\n" +
//                    "^BY2,3,95^FT11,125^BCN,,N,N\n" +
//                    "^FH\\^FD>:$CabinetName1^FS\n" +
//                    "^FT39,167^A0N,36,46^FH\\^CI28^FD$CabinetName1^FS^CI27\n" +
//                    "^PQ1,0,1,Y\n" +
//                    "^XZ"
            var zplData="^XA\n" +
                    "~TA000\n" +
                    "~JSN\n" +
                    "^LT0\n" +
                    "^MNW\n" +
                    "^MTT\n" +
                    "^PON\n" +
                    "^PMN\n" +
                    "^LH0,0\n" +
                    "^JMA\n" +
                    "^PR4,4\n" +
                    "~SD18\n" +
                    "^JUS\n" +
                    "^LRN\n" +
                    "^CI27\n" +
                    "^PA0,1,1,0\n" +
                    "^XZ\n" +
                    "^XA\n" +
                    "^MMT\n" +
                    "^PW472\n" +
                    "^LL177\n" +
                    "^LS0\n" +
                    "^BY3,3,77^FT24,117^BCN,,N,N\n" +
                    "^FH\\^FD>;$CabinetName1>67^FS\n" +
                    "^FT90,160^A0N,33,33^FH\\^CI28^FD$CabinetName1^FS^CI27\n" +
                    "^PQ1,0,1,Y\n" +
                    "^XZ"

            thePrinterConn.write(zplData.toByteArray())
            //ConvertTextToSpeech();
            Log.d("sssdds1", "sendZplOverTcp: " + "toastt1")

        } catch (e: ConnectionException) {
            // Handle communications error here.
            e.printStackTrace()
            Log.d("sssdds2", "sendZplOverTcp: $e")
            Toast.makeText(this, "Please connect to network", Toast.LENGTH_LONG).show()
        } finally {
            // Close the connection to release resources.
            thePrinterConn.close()
            Log.d("sssdds3", "sendZplOverTcp: " + "toastt")
        }
    }
    @SuppressLint("SuspiciousIndentation")


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

        macAddress =
            Settings.Secure.getString(this.applicationContext.contentResolver, "android_id")
        Log.d("macaddress", "MacAddress: "+macAddress)
    }
    private fun InsertTemptable(cabinetname: String) {
        try {
            var n = 0
            val sql =
                " INSERT INTO TEMPUPS (finyear,CabinetName,Macaddress,Date,month,Username) VALUES (?,?,?,?,?,?)"

            val statement = con.prepareStatement(sql)

            statement.setString(1, year)//CompanyId
            statement.setString(2,cabinetname)
            statement.setString(3, macAddress)
            statement.setString(4,currentDateSimple)
            statement.setString(5,month)
            statement.setString(6,sessionManager.userName)



            n = statement.executeUpdate()
            if (n > 0)
            {

               // Toast.makeText(this, "successfully Inserted Temp Table", Toast.LENGTH_SHORT).show()
                binding.cabinetscanqc.text.clear()
                // generenatebarcode()


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

    private fun truncateTable() {
        try {
            var n = 0
            val sql = "TRUNCATE TABLE TEMPUPS"

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
////////////////////////////////////////////
private fun insertFollowQC(cabinetname:String,counting:Int) {
    //mam run kraye debug ? kruyesok
    try {
        var n = 0

        if (counting == 0) {
            srnum = "1"
          //  rowCount=Integer.valueOf(srnum)
            Log.d("rowcount1", "insertFollowQC: "+rowCount)
        } else {
            val abc = Integer.valueOf(maxcount) + 1
           // rowCount=abc
            Log.d("maxxx", "insertDatatwo: $abc")
            srnum = abc.toString()
        }
        val cstmt = con.prepareCall("{call UPSCAB  (?,?,?,?,?,?,?,?,?)}")
        cstmt.setString(1, cabinetname)
        cstmt.setString(2, macAddress)
//        cstmt.setInt(3, 20230503)
        cstmt.setInt(3, newdate.toInt())
        cstmt.setString(4, month)
        cstmt.setString(5,sessionManager.userName )
        cstmt.setString(6,year )
        cstmt.setString(7, srnum)
        cstmt.setString(8, year)
        cstmt.setString(9, line)
///is war ho gya abhi nhi ho rha tha its ok maam iska data dlt kaise kru proc ka jo insert ho gyamam jis table m kia uspe delte

        n = cstmt.executeUpdate()
        if (n > 0)
        {

            Toast.makeText(this, "successfully Inserted", Toast.LENGTH_SHORT).show()

            checktempcheck();

            binding.cabinetscanqc.text.clear()

//            sendZplOverTcpQC("192.168.1.75")



        }
        else {
//            var qc=binding.cabinetscanqc.text.toString()
//            Log.d("qcc", "insertFollowQC: "+qc)
//            if(qc==" ")
//            {
//                Toast.makeText(this, "Please Scan Barcode", Toast.LENGTH_SHORT).show()
//                binding.cabinetscanqc.text.clear()
//            }
//            else
//            {
//                Toast.makeText(this, "Already Exists", Toast.LENGTH_SHORT).show()
//                binding.cabinetscanqc.text.clear()
//            }
            Toast.makeText(this, "Already Exists Or Invalid Code", Toast.LENGTH_SHORT).show()
            speakOut("Already Exists Or Invalid Code")
              binding.cabinetscanqc.text.clear()
        }

    } catch (e: Exception) {
        Log.e(ContentValues.TAG, "Exception: ${e.toString()}")
        Log.d("procedureecall", "insertFollowUpData: "+e.toString())
    }
}


    private fun rowcount() {
        try {
            val query = "select count(*)  from Qccabinet"
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
            val query = "select MAX(SRNO) As SRNO  from Qccabinet"
            val resultSet: ResultSet = statement.executeQuery(query)
            if (resultSet.next()) {
                maxcount = resultSet.getString("SRNO")
                Log.d("maxcountttt", "rowcount: $maxcount")
            }
        } catch (e: java.lang.Exception) {
            Log.d("clclclclcl2", "rowcount: $e")
            // Handle the exception
        }
        //  String tota="Total Scanned-";
        // binding.ocp.setText(tota+String.valueOf(rowCount));
    }


    private fun checktempcheck() {

        try {
            val query ="select * from TEMPUPS  where line= $line"
            val resultSet = statement.executeQuery(query)
            if (resultSet.next()) {

                 Finyear1=resultSet.getString("Finyear")
                 CabinetName1=resultSet.getString("CabinetName").toString()
                 Macaddress1=resultSet.getString("Macaddress")
                  //Date1=resultSet.getInt("Date").toString()
                var month=resultSet.getString("month")
                 Username1=resultSet.getString("username")
                ITEMNAME1=resultSet.getString("ITEMNAME")
                 ITEMCODE1=resultSet.getString("itemcode")
                 NETWEIGHT1=resultSet.getString("netweight")

                 MRP1=resultSet.getString("mrp")
                 DIMENSION1=resultSet.getString("dimension")
                 pr1=resultSet.getString("pr")
                GROSSWEIGHT1=resultSet.getString("grossweight")
                if(month.equals("1"))
                {
                    month1="JAN"
                }
                if(month.equals("2"))
                {
                    month1="FEB"
                }
                if(month.equals("3"))
                {
                    month1="MARCH"
                }
                if(month.equals("4"))
                {
                    month1="APRIL"
                }
                if(month.equals("5"))
                {
                    month1="MAY"
                }
                if(month.equals("6"))
                {
                    month1="JUNE"
                }
                if(month.equals("7"))
                {
                    month1="JULY"
                }
                if(month.equals("8"))
                {
                    month1="AUG"
                }
                if(month.equals("9"))
                {
                    month1="SEP"
                }
                if(month.equals("10"))
                {
                    month1="OCT"
                }
                if(month.equals("11"))
                {
                    month1="NOV"
                }
                if(month.equals("12"))
                {
                    month1="DEC"
                }


                Log.d("cabinettime1", "checktempcheck: "+CabinetName1)
                Log.d("cabinettime2", "checktempcheck: "+Macaddress1)
                Log.d("cabinettime3", "checktempcheck: "+month1)
                Log.d("cabinettime4", "checktempcheck: "+Username1)
                Log.d("cabinettime5", "checktempcheck: "+ITEMCODE1)
                Log.d("cabinettime6", "checktempcheck: "+NETWEIGHT1)
                Log.d("cabinettime7", "checktempcheck: "+MRP1)
                Log.d("cabinettime8", "checktempcheck: "+DIMENSION1)
                Log.d("cabinettime9", "checktempcheck: "+pr1)
                Log.d("cabinettime10", "checktempcheck: "+GROSSWEIGHT1)


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
        }

    }


    private fun getfolderpath()
    {
        val folderName = "CabinetS"
        val folderPath = File(Environment.getExternalStorageDirectory(), folderName)

        if (folderPath.exists() && folderPath.isDirectory) {
            val files = folderPath.listFiles { file ->
                file.isFile && file.extension == "txt" // Filter for text files only, modify if needed
            }

            if (files != null) {
                for (file in files) {
                    val content = file.readText()
                    var abbb=content.split("&")

//                    largemrpip=abbb.get(0)
//                   smallmrpip=abbb.get(1)
//
                    large=abbb.get(0)
                    small=abbb.get(1)
                    binding.large.text=(large.toString())
                    binding.small.text=(small.toString())


                    Log.d("getabbbb1", "getfolderpath: "+abbb)
                    Log.d("getabbbb2", "getfolderpath: "+large)
                    Log.d("getabbbb3", "getfolderpath: "+small)


                    // Do something with the file content
                }
            } else {
                // No text files found in the folder
                Log.d("contentt2", "getfolderpath: "+"doesnott")

            }
        } else {
            // Folder does not exist
            folderPath.mkdirs()

        }
    }

    //////////////////////



}

