package com.example.cabinettscanningg

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.crm_copy.other.isOnline
import com.crm_copy.other.myToast
import com.crm_copy.other.myToastRed
import com.example.cabinettscanningg.Connections.ConnectionClass
import com.example.cabinettscanningg.Connections.SessionManager
import com.example.cabinettscanningg.databinding.ActivityLoginBinding
import com.example.cabinettscanningg.databinding.ActivityScanningBinding
import org.json.JSONObject
import java.sql.Connection
import java.sql.Statement


class LoginActivity : AppCompatActivity() {

    private val context: Context = this@LoginActivity
    private lateinit var connectionClass: ConnectionClass
    private lateinit var con: Connection
    private lateinit var statement: Statement
    private lateinit var sessionManager: SessionManager
    private var loginUser = ""
    private var scheduler=""
    private lateinit var binding: ActivityLoginBinding
    private var mobilenumber:String?=null
    private var oTPStates = true
    private var pressedTime: Long = 0
    var progressDialog: ProgressDialog? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_login)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        connectionClass = ConnectionClass(this)
        supportActionBar?.hide()
        Handler(mainLooper).postDelayed({


            con= connectionClass.CONN()!!
            statement = con.createStatement()
            Log.d("inslizedd", "onCreate: "+"inslizedd1")


        }, 200)
        initial()
        var abcd7="ammy1234ammy12345"
        abcd7 = abcd7.substring(0, abcd7.length - 3)
        Log.d("abcd66", "onCreate: "+abcd7)


    }
    @SuppressLint("NewApi")
    private fun initial() {
        binding.btnLogin.setOnClickListener {

            val userName =  binding.edtUser.text.toString()
            val password =  binding.edtPassword.text.toString()
            if (isOnline(this)) {

                when {
                    TextUtils.isEmpty(userName) -> {
                        myToast(this, "Please Enter User Name")
                    }
                    TextUtils.isEmpty(password) -> {
                        myToast(this, "Please Enter Password")
                    }
                    else -> {
                        //  binding.progressBar.visibility = View.VISIBLE
                        // btnLogin.isEnabled = false
                     //  progressDialog!!.show()
                        loginData(userName, password)

                        // startActivity(Intent(context, OTPVerification::class.java))


                    }

                }
            } else {
                myToast(this, "No internet connection")
            }
        }
    }


    private fun loginData(userName :String,password:String) {

        try {
            val query ="select USERNAME,USERPASSWORD from USERPERMISSION where  USERNAME=('${userName}') AND  USERPASSWORD=('${password}')"
            val resultSet = statement.executeQuery(query)
            if (resultSet.next())
            {
                val usernamedb = resultSet.getString("USERNAME")
                val userpassworddb = resultSet.getString("USERPASSWORD")

                Log.d("usernamedb1", "loginData: "+usernamedb)
                Log.d("usernamedb2", "loginData: "+userName)
                Log.d("usernamedb3", "loginData: "+userpassworddb)
                Log.d("usernamedb4", "loginData: "+password)

               if(usernamedb==userName && userpassworddb==password) {
                 startActivity(Intent(this,MainActivity::class.java))
                   finish()
                    sessionManager.userName=userName
                    sessionManager.userId=password.toInt()
                    sessionManager.isLogin=true
                }

            }
            else {
                myToastRed(this,"Invalid UserPassword")


            }
        }
        catch (se: Exception)
        {
            Log.e("ERROR", se.message!!)
            Log.d("errorprint", "checkISerialNumbertest: "+se.message.toString())
        }

    }
}
