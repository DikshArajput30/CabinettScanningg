package com.example.cabinettscanningg

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crm_copy.other.myToastRed
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityMainBinding
import java.io.File
import java.nio.file.Files.createDirectory
import java.sql.Connection
import java.sql.Statement
import android.Manifest
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private var context: Context=this@MainActivity
    private lateinit var sessionManager: SessionManager
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private var username=""
    private var userpassword=""
    private val PERMISSION_REQUEST_CODE = 112

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
       setContentView(binding.root)

        sessionManager = SessionManager(this)
        binding.cabinetscanninglayout.setOnClickListener(View.OnClickListener {
              startActivity(Intent(context,ScanningActivity::class.java))
        })
        binding.partscanninglayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,PartScanning::class.java))
        })
        binding.outerboxscanninglayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,OuterBoxActivity::class.java))
        })
        binding.reassemblelayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,ReassembleActivity::class.java))
        })
        binding.qcscanninglayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,QcScanningActivity::class.java))
        })
        binding.boxbarcodescanning.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,BoxBarCodeScanning::class.java))
        })
        binding.outerboxscanninglayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,OuterBoxActivity::class.java))
        })
        binding.erpartscanninglayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,ERPartScanning::class.java))
        })
        binding.dispatchlistlayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,DispatchQC::class.java))
        })
        binding.dispatchlistlayoutlist.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,DispatchList::class.java))
        })
        binding.reqcupslayout.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,REQCUPSActivity::class.java))
        })
        connectionClass = ConnectionClass(context)

        Handler(mainLooper).postDelayed({
            con = connectionClass.CONN()!!
            statement = con.createStatement()
            checkpartscanning(sessionManager.userName.toString(),sessionManager.userId)
            checkererpairscanning(sessionManager.userName.toString(),sessionManager.userId)
            checkreassemblescanning(sessionManager.userName.toString(),sessionManager.userId)
            checkqcscanning(sessionManager.userName.toString(),sessionManager.userId)
            checkerbatteryscanning(sessionManager.userName.toString(),sessionManager.userId)
            checkdispatchscanning(sessionManager.userName.toString(),sessionManager.userId)
            checkdispatchlistscanning(sessionManager.userName.toString(),sessionManager.userId)
           // checkreqcscanning(sessionManager.userName.toString(),sessionManager.userId)
        }, 200)
        if (!checkPermission()) {
            requestPermission()
        }
        else
        {
            createDirectory()

        }
        val myDirectory = File(Environment.getExternalStorageDirectory(), "CabinetS")
        Log.d("mydyyy", "onCreate: "+myDirectory)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }


    @SuppressLint("SuspiciousIndentation")
    private fun checkpartscanning(username :String, userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='CABINETPAIRING' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
             binding.partscanninglayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.partscanninglayout.visibility=View.GONE


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkreassemblescanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='REASSEMBLE' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.reassemblelayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.reassemblelayout.visibility=View.GONE



            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkqcscanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='UPSQC' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.qcscanninglayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.qcscanninglayout.visibility=View.GONE



            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkerbatteryscanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='ERCQC' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.outerboxscanninglayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.outerboxscanninglayout.visibility=View.GONE


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkererpairscanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='ECABINETPAIRING' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.erpartscanninglayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.erpartscanninglayout.visibility=View.GONE


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkdispatchscanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='DISPATCH' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.dispatchlistlayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.dispatchlistlayout.visibility=View.GONE


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkdispatchlistscanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='DISPATCHLIST' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.dispatchlistlayoutlist.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.dispatchlistlayoutlist.visibility=View.GONE


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
    private fun checkreqcscanning(username :String,userpassword:Int) {

        try {
            val query="select * from [USERPERMISSION] where username=('${username}') and USERPASSWORD=('${userpassword}') and FORMNAME ='UPSREQC' AND PVIEW='1'"

            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                binding.reqcupslayout.visibility=View.VISIBLE

                Log.d("cominhsoon1", "checkpartscanning: "+"cominhsoon")
            }
            else {
                binding.reqcupslayout.visibility=View.GONE



            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this,
                "Write External Storage permission allows us to save files. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .")
                createDirectory()
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .")
            }
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }



    private fun createDirectory() {
        val myDirectory = File(Environment.getExternalStorageDirectory(), "CabinetS")
        if (!myDirectory.exists()) {
            myDirectory.mkdirs()
            Toast.makeText(context,"created",Toast.LENGTH_SHORT).show()
            val folderPath="/storage/emulated/0/CabinetS"
            val fileName = "ip.txt" // Example file name
            val content = "192.168.1.75 & 192.168.1.10 & First ip Large MRP Print and Second ip Small print" // Example content

            saveTextToFile(folderPath, fileName, content)

        }

        else
        {
            //Toast.makeText(context,"already created",Toast.LENGTH_SHORT).show()
        }

    }



    fun saveTextToFile(folderPath: String, fileName: String, content: String) {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val file = File(folder, fileName)
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



}