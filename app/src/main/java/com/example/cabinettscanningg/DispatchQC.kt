package com.example.cabinettscanningg

import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog
import com.crm_copy.other.getFinYear
import com.crm_copy.other.hideKeyboard
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityDispatchQcBinding
import com.jindal22.adapter.AdapterData
import com.jindal22.model.DispatchModel
import java.lang.Exception
import java.math.BigDecimal

import java.sql.Connection
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class DispatchQC : AppCompatActivity(),AdapterData.OnRemove,TextToSpeech.OnInitListener{
    private  lateinit var binding:ActivityDispatchQcBinding
    private var context: Context =this@DispatchQC
    private var SrNo = ""
    private val PERMISSION_REQUEST_CODE = 112
    private lateinit var connectionClass: ConnectionClass
    private lateinit var conn: Connection
    private lateinit var sessionManager: SessionManager
    private var num = 1
    private var orderNo = 1
    private var deviceName = ""
    private var finyear = 0
    private var currentDate = ""
    private var currentTime = ""
    private var Finyear = ""
    private var srno = ""
    private var date = ""

    private var MOnth = ""
    private var usernamee = ""
    private var yearr = ""
    private var cabinetname = ""
    private var cabunetitemname = ""
    private var cabinetmodelno = ""



    private var CATEGORY = ""
    private var BRANDCODE = ""
    private var line = ""
//    private lateinit var SQTY: BigDecimal
//    private lateinit var WEIGHT: BigDecimal
    private val list = ArrayList<DispatchModel>()
    private var vehicleNo = ""
    private var partyName = ""
    private var mobile = ""
    private var email = ""
    private var dispatchThrough = ""
    private var shipTo = ""
    private var BrType = "di"
    private var add1 = ""
    private var add2 = ""
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDispatchQcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        sessionManager = SessionManager(this)
        Handler(mainLooper).postDelayed({
            connectionClass = ConnectionClass(this)
            conn = connectionClass.CONN()!!
            if (intent.getStringExtra("srNo") != null) {
                val oldSrNo = intent.getStringExtra("srNo").toString()
               // getOrderNoData(oldSrNo)
            }
        }, 200)
        deviceName = Build.MODEL
        binding.btnDetail.setOnClickListener {
            showDialog()
        }

        binding.toolBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }


        tts = TextToSpeech(this, this)

        list.clear()
        initial()

    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)
        val alertDialog: AlertDialog = dialogBuilder.create()

        val edtVehicleNo = dialogView.findViewById<View>(R.id.edtVehicleNo) as EditText
        val edtPartyName = dialogView.findViewById<View>(R.id.edtPartyName) as EditText
        val edtMobile = dialogView.findViewById<View>(R.id.edtMobile) as EditText
        val edtEmail = dialogView.findViewById<View>(R.id.edtEmail) as EditText
        val edtDispatchThrough = dialogView.findViewById<View>(R.id.edtDispatchThrough) as EditText
        val edtShipTo = dialogView.findViewById<View>(R.id.edtShipTo) as EditText
        val btnSave = dialogView.findViewById<View>(R.id.btnSave) as Button

        if (SrNo!=""){
            btnSave.text="UPDATE"
        }else{
            btnSave.text="CLOSE"
        }
        edtPartyName.setText(partyName)
        edtVehicleNo.setText(vehicleNo)
        edtMobile.setText(mobile)
        edtEmail.setText(email)
        edtDispatchThrough.setText(dispatchThrough)
        edtShipTo.setText(shipTo)

        btnSave.setOnClickListener {
            vehicleNo = edtVehicleNo.text.toString()
            partyName = edtPartyName.text.toString()
            mobile = edtMobile.text.toString()
            email = edtEmail.text.toString()
            dispatchThrough = edtDispatchThrough.text.toString()
            shipTo = edtShipTo.text.toString()
            if (SrNo!=""){
                updateDispatchData()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()

    }
    private fun initial() {
        currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        finyear = getFinYear()


        Log.e(ContentValues.TAG, "initial: $currentDate\n$currentTime")

        //event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
        binding.edtBarcode.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard(this)
                checkDuplicateData()
                return@OnKeyListener true
            }
            false
        })

        binding.btnClear.setOnClickListener {
            binding.edtBarcode.setText("")
            SrNo = ""
            vehicleNo=""
            partyName = ""
            mobile = ""
            email = ""
            dispatchThrough = ""
            shipTo = ""
            list.clear()
            binding.recyclerView.adapter = AdapterData(this, list, this)
            binding.tvCount.text = "0"
            binding.edtBarcode.requestFocus()
        }


        binding.btnDetail.setOnClickListener {
            showDialog()
        }
    }
    private fun checkDuplicateData() {
        val query = "Select BARCODENO from M1DispatchSub where BARCODENO = ('${binding.edtBarcode.text}')"
        try {
            val statement = conn.createStatement()
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.edtBarcode.setText("")
                binding.edtBarcode.requestFocus()
                myToastyellow(this, "Barcode already dispatched")
            }
            else
            {
                if (binding.edtBarcode.text.toString() != "") {
                    //yes
                    checkBarcode()
                }
            }
        } catch (se: Exception) {
            Log.e("ERROR", se.message!!)
        }
    }
///yess
    private fun checkBarcode() {
//        val query =
//            "Select BARCODENO,ITEMNAME,SQTY,WEIGHT,VENDORCODE,COLOR,MODELNO,CATEGORY,BRANDCODE,SRNO from BARCODEGENERATIONSUB where BARCODENO = ('${binding.edtBarcode.text}')"
        val query =
            "Select FINYEAR,SRNO,DATE,MONTH,YEAR,USERNAME,CABINETNAME,CABINETITEMNAME,CABINETMOELNO,LINE from Qccabinet where CABINETNAME = ('${binding.edtBarcode.text}')"

        try {
            val statement = conn.createStatement()
            val resultSet = statement.executeQuery(query)
            if (resultSet.next()) {
                do {
                    Finyear = resultSet.getString("FINYEAR")
                    srno = resultSet.getString("SRNO")
                    date = resultSet.getString("DATE")
                    MOnth = resultSet.getString("MONTH")
                    yearr = resultSet.getString("YEAR")
                    usernamee = resultSet.getString("USERNAME")
                    cabinetname = resultSet.getString("CABINETNAME")
                    cabunetitemname = resultSet.getString("CABINETITEMNAME")
                    cabinetmodelno = resultSet.getString("CABINETMOELNO")
                    line = resultSet.getString("LINE")
                    list.add(0, DispatchModel(Finyear, srno,date,MOnth, yearr, usernamee,cabinetname,cabunetitemname,cabinetmodelno,line))



                } while (resultSet.next())
                binding.recyclerView.adapter = AdapterData(this, list, this)
                binding.tvCount.text = list.size.toString()
                binding.edtBarcode.setText("")
                binding.edtBarcode.requestFocus()
                if (SrNo=="") {
                    generateSrNo()
                    getOrderNo()
                    saveDispatchData()
                }else{

                    ///yess
                    saveDispatchSubData()
                }
            } else {
                binding.edtBarcode.setText("")
                binding.edtBarcode.requestFocus()
                myToastRed(this, "Barcode not found")
                speakOut("Barcode not found")
            }
        } catch (se: Exception) {
            Log.e("ERROR", se.message!!)
        }
    }

    private fun generateSrNo() {
        try {
            Log.e(ContentValues.TAG, "BrType: >>>>>>>>>>$BrType")
            val qry =
                "select SrNo from M1Dispatch where SrNo = (select max(SrNo) from M1Dispatch where BrType=('$BrType'))"
            val statement = conn.createStatement()
            val rsgr: ResultSet = statement.executeQuery(qry)
            if (rsgr.next()) {
                do {
                    val sr = rsgr.getString("SrNo")
                    Log.e(ContentValues.TAG, "OLD_SrNo: $sr")
                    val p = Pattern.compile("\\d+")
                    val m = p.matcher(sr)
                    while (m.find()) {
                        num = m.group().toInt()
                        num++
                        Log.e(ContentValues.TAG, "NEW_SrNo: " + generateCode(num.toString()))
                        SrNo = generateCode(num.toString())
                    }
                } while (rsgr.next())

            } else {
                SrNo = generateCode(1.toString())
                Log.e(ContentValues.TAG, "SrNo: $SrNo")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "error: " + e.message)
            e.printStackTrace()
        }

    }

    private fun getOrderNo() {
        try {
            val qry =
                "select OrderNo from M1Dispatch where OrderNo = (select max(OrderNo) from M1Dispatch)"
            val statement = conn.createStatement()
            val rsgr: ResultSet = statement.executeQuery(qry)
            if (rsgr.next()) {
                do {
                    orderNo = rsgr.getInt("OrderNo")
                    Log.e(ContentValues.TAG, "OLD_odrNo: $orderNo")
                    orderNo++
                    Log.e(ContentValues.TAG, "NEW_odrNo: $orderNo")
                } while (rsgr.next())
            } else {
                Log.e(ContentValues.TAG, "odrNo: 1")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "error: " + e.message)
            e.printStackTrace()
        }

    }

    private fun generateCode(num: String): String {
        val number = when (num.length) {
            1 -> "00000$num"
            2 -> "0000$num"
            3 -> "000$num"
            4 -> "00$num"
            5 -> "0$num"
            6 ->  num
            else -> num
        }
        return "D$number"
    }
    private fun saveDispatchData() {

        val sql =
            "INSERT INTO M1Dispatch (SrNo, Date, PartyName, OrderNo,VehicleNo,DispatchThrough,ShipTo,MobileNo,Email,UserName,CompanyName,Time,finyear,BrType,Add1,Add2,deviceName)" +
                    " VALUES (?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?,?,?,?,?)"
        val statement = conn.prepareStatement(sql)
        statement.setString(1, SrNo)
        statement.setInt(2, currentDate.toInt())
        statement.setString(3, partyName)
        statement.setInt(4,orderNo)
        statement.setString(5, vehicleNo)
        statement.setString(6, dispatchThrough)
        statement.setString(7, shipTo)
        statement.setString(8, mobile)
        statement.setString(9, email)
        statement.setString(10, sessionManager.userName)
        statement.setString(11, sessionManager.userName)///companyname
        statement.setString(12, currentTime)
        statement.setInt(13, finyear)
        statement.setString(14, BrType)
        statement.setString(15, add1)
        statement.setString(16, add2)
        statement.setString(17, deviceName)

        val rowsInserted = statement.executeUpdate()
        if (rowsInserted > 0) {

            saveDispatchSubData()
//                  myToast(this,"Data saved")
        }
    }

    private fun saveDispatchSubData() {
        val sql = "INSERT INTO M1DispatchSub (SrNo,BarcodeNo,ITEMNAME,SQTY,WEIGHT, Date,finyear,BrType,VENDORCODE,COLOR,BELNO,deviceName,CATEGORY,MODELNO,BRANDCODE) VALUES (?,?,?,?,?, ?, ?,?,?,?,?,?,?,?,?)"
        val statement = conn.prepareStatement(sql)
        statement.setString(1, SrNo)
        statement.setString(2, cabinetname)
        statement.setString(3, cabunetitemname)
        statement.setBigDecimal(4, BigDecimal("72.80"))
        statement.setBigDecimal(5,  BigDecimal("72.80"))
        statement.setInt(6, currentDate.toInt())
        statement.setInt(7, finyear)
        statement.setString(8, BrType)
        statement.setString(9, "GRADE")
        statement.setString(10, "COLOR")
        statement.setString(11, "belno")
        statement.setString(12, deviceName)
        statement.setString(13, CATEGORY)
        statement.setString(14, "MODELNO")
        statement.setString(15, cabinetmodelno)
        val rowsInserted = statement.executeUpdate()
        binding.tvCount.text = list.size.toString()

    }

    override fun onRemoveItem(position: Int, barcode: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to delete $barcode?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                deleteDispatchSubData(barcode)
                list.removeAt(position)
                binding.recyclerView.adapter = AdapterData(this, list, this)
                binding.tvCount.text = list.size.toString()
                sDialog.dismissWithAnimation()
            }
            .setCancelClickListener { sDialog -> sDialog.cancel() }
            .show()
    }
    private fun updateDispatchData() {
        val sql =
            "UPDATE M1Dispatch SET PartyName=?, VehicleNo=?,DispatchThrough=?,ShipTo=?,MobileNo=?,Email=? WHERE SrNo=?"
        val statement = conn.prepareStatement(sql)
        statement.setString(1, partyName)
        statement.setString(2, vehicleNo)
        statement.setString(3, dispatchThrough)
        statement.setString(4, shipTo)
        statement.setString(5, mobile)
        statement.setString(6, email)
        statement.setString(7, SrNo)
        val rowsInserted: Int = statement.executeUpdate()
        if (rowsInserted > 0) {
            Log.e(ContentValues.TAG, "updateDispatchData:")

        }

    }
    private fun deleteDispatchSubData(barcode: String) {
        val sql = "DELETE FROM M1DispatchSub WHERE BarcodeNo=?"
        val statement = conn.prepareStatement(sql)
        statement.setString(1, barcode)
        val rowsDeleted = statement.executeUpdate()
        if (rowsDeleted > 0) {
            Log.e(ContentValues.TAG, "A row was deleted successfully!")
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