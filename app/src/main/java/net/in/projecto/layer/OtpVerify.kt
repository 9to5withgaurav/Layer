package net.`in`.projecto.layer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import com.chaos.view.PinView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class OtpVerify : AppCompatActivity() {
    private var verificationId: String? = null
    private var auth: FirebaseAuth? = null
    private lateinit var resendBtn: TextView
    private var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)
        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val subTitle = findViewById<TextView>(R.id.subTitle)
        val bundle = intent.extras
        val getMobileIntent = bundle?.getString("MOBILE")
        val getData = resources.getString(R.string.otpSubtitle, getMobileIntent)
        val getMobile = "+91$getMobileIntent"
        subTitle.text = getData

        val pinInput = findViewById<PinView>(R.id.pin)
        auth = FirebaseAuth.getInstance()
        val snackBarViw = findViewById<CoordinatorLayout>(R.id.coordinateView)

        resendBtn = findViewById(R.id.resendCode)

        sendOtp(getMobile,snackBarViw)

        resendBtn.setOnClickListener {
            sendOtp(getMobile,snackBarViw)
        }

        pinInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s != null && s.length == 6 && verificationId != null){
                    val credential = PhoneAuthProvider.getCredential(verificationId!!,s.toString())
                    signInWithPhoneAuthCredential(credential, snackBarViw)
                }
            }

        })

    }


    private fun sendOtp(mobile:String,view: View){
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(mobile) // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    showSnackBar(view,"login successful")
                    auth?.firebaseAuthSettings?.setAppVerificationDisabledForTesting(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        showSnackBar(view,"Invalid request")
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        showSnackBar(view,"Sms quota exhausted")
                    } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                        // reCAPTCHA verification attempted with null Activity
                        showSnackBar(view,"Recaptcha issue")
                    }

                }

                override fun onCodeSent(verficationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = verficationId
                    showSnackBar(view,"code sent!")
                }

            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        timer?.cancel()
        resendBtn.isEnabled = false
        //resend code hide upTo 30seconds
        val timeoutMillis = 30000L
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    resendBtn.isEnabled = true
                }
            }
        },timeoutMillis)
    }

    fun showSnackBar(view: View,msg:String){
        val snackBar = Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        val param = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
        param.gravity = Gravity.TOP
        snackBarView.layoutParams = param
        snackBar.show()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,view: View) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    val sharedPref = getSharedPreferences("CID",Context.MODE_PRIVATE)
                    with(sharedPref.edit()){
                        putString("customer","${user?.uid}")
                        apply()
                    }
                    val intent = Intent(this,ProfileActivity::class.java)
                    try {
                        startActivity(intent)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        showSnackBar(view,"Invalid Code")
                    }
                    // Update UI
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
