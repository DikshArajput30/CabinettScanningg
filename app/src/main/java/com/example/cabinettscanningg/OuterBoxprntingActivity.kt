package com.example.cabinettscanningg

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.crm_copy.other.myToastRed
import com.crm_copy.other.myToastyellow
import com.example.cabinettscanningg.Connections.ConnectionClass
import java.sql.Connection
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*

class OuterBoxprntingActivity : AppCompatActivity() {
//    private lateinit var binding:ActivityOuterBoxprntingBinding
//private lateinit var binding:ActivityOuterBoxprntingActivity
//
//    private var context:Context=this@OuterBoxprntingActivity
//    private val remainList = ArrayList<OuterBoxModel>()
//    private lateinit var connectionClass: ConnectionClass
//    private lateinit var con: Connection
//    private lateinit var statement: Statement
//    private var day=""
//    private  var month=""
//    private var year=""
//    private var currentDateSimple=""
//    private var outercabinetsc=" "
//    private var outercabinetitemname=" "
//    private var outercabinetmodelno=" "
//    private val modellist = ArrayList<OuterBoxModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding=ActivityOuterBoxprntingActivity.inflate(layoutInflater)
        setContentView(R.layout.activity_outer_boxprnting)
//
//        currentDateSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
//            Date()
//        )
//        connectionClass = ConnectionClass(context)
//        val c = Calendar.getInstance()
//        year = c.get(Calendar.YEAR).toString()
//        month = (c.get(Calendar.MONTH)+1).toString()
//        day = c.get(Calendar.DAY_OF_MONTH).toString()
//        binding.dd.text=day.toString()
//        binding.mm.text=month.toString()
//        binding.yy.text=year.toString()
//
//        Handler(mainLooper).postDelayed({
//
//
//            con = connectionClass.CONN()!!
//            statement = con.createStatement()
//            Log.d("inslizedd", "onCreate: "+"inslizedd1")
//
//
//        }, 200)
//
//        binding.back.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(context,MainActivity::class.java))
//        })
//
//        binding.cabinetscanning.setOnKeyListener { view, keyCode, keyEvent ->
//            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
//                return@setOnKeyListener false
//            }
//            Log.d("clcikmee", "onCreate: "+"clcikmee")
//            if (keyCode == KeyEvent.KEYCODE_ENTER) {
//
//                outercabinetsc=binding.cabinetscanning.text.toString().trim()
//                checkcabinet(outercabinetsc)
//
//                return@setOnKeyListener true
//            }
//            false
//        }
//
//    }
//
//    override fun onBackPressed() {
//        //super.onBackPressed()
//        startActivity(Intent(context,MainActivity::class.java))
//
//    }
//
//
//    private fun checkcabinet(outercabinet: String) {
//
//        try {
//            val query ="select CABINETNAME,CABINETITEMNAME,CABINETMOELNO from BOXBARSC where  CABINETNAME =('${outercabinet}') "
//            val resultSet = statement.executeQuery(query)
//            if (resultSet.next())
//            {
//
//                var outercabinetscdb  = resultSet.getString("cabinetname").trim()
//
//                if(outercabinetsc==outercabinet)
//                {
//                    outercabinetsc  = resultSet.getString("cabinetitemname").trim()
//                    outercabinetmodelno  = resultSet.getString("CABINETMOELNO")
//                    // InsertQCcabinet(cabinetname)
//                    checkduplicate(outercabinet)
//                }
//
//
//
//            }
//            else
//            {
//                binding.boxbarcabinetscanning.text.clear()
//                myToastRed(this,"Invalid Cabinet Number")
//
//            }
//        }
//        catch (se: Exception)
//        {
//            Log.e("ERROR", se.message!!)
//        }
//
//    }
//
//    private fun checkduplicate(boxbarsc: String) {
//
//        try {
//            val query ="select cabinetname from Outerbox where  cabinetname =('${boxbarsc}') "
//            val resultSet = statement.executeQuery(query)
//            if (resultSet.next())
//            {
//
//                var outercabinetdb  = resultSet.getString("cabinetname").trim()
//
//                if(outercabinetdb==boxbarsc)
//                {
//
//                    binding.boxbarcabinetscanning.text.clear()
//                    myToastyellow(this," Already Exist")
//                }
//
//
//
//            }
//            else
//            {
//                binding.boxbarcabinetscanning.text.clear()
//                modellist.add(OuterBoxModel(boxbarsc,outercabinetitemname,outercabinetmodelno))
//                binding.list.adapter = ModelListAdapter(this, modellist)
//
//                //InsertBoxcabinet(boxbarsc)
//
//            }
//        }
//        catch (se: Exception)
//        {
//            Log.e("ERROR", se.message!!)
//        }
//
//    }
    }
}