package consultdocs.com.sub.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.TextInputEditText
import android.view.View
import com.google.firebase.auth.PhoneAuthProvider
import consultdocs.com.sub.R
import consultdocs.com.sub.bean.UserData
import consultdocs.com.sub.services.FirebaseServices
import consultdocs.com.sub.utility.ApplicationConstants
import consultdocs.com.sub.utility.ApplicationUtility
import consultdocs.com.sub.utility.TaskOnComplete
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import android.app.DatePickerDialog
import android.support.design.widget.BottomSheetDialog
import android.util.TypedValue
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class Registration : AppCompatActivity(), TaskOnComplete {

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var mobile: String
    private lateinit var dob: String
    private lateinit var gender: String
    private lateinit var otp: String
    private lateinit var res: String
    private val firebaseServices = FirebaseServices()
    private var myCountDownTimer = MyCountDownTimer((ApplicationConstants.OTP_TIME * 1000), 1000)
    private var myCalendar: Calendar = Calendar.getInstance()
    private lateinit var mobileNumber: String
    private lateinit var resendText: TextView
    private lateinit var loginButton: Button
    private lateinit var mProgressBar: ProgressBar
    private var isOTPAuthenticationInProgress = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mProgressBar = findViewById(R.id.registerProgressBar)
        setupToolBar()
        setupUI()
        input_dob.setOnClickListener {
            setDate()
        }
        login.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun setDate() {
        val date : DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }


    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        input_dob.setText(sdf.format(myCalendar.time))
        ApplicationUtility.hideSoftKeyboard(this@Registration)
    }

    fun register(view: View){
        val result = getInputsAndValidate()
        when (result) {
            ApplicationConstants.NO_GENDER -> {
                ApplicationUtility.showSnack(this.resources.getString(R.string.enter_all_fields), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.EMPTY_FIELDS -> {
                ApplicationUtility.showSnack(this.resources.getString(R.string.enter_all_fields), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.FUTURE_DATE -> {
                ApplicationUtility.showSnack(this.resources.getString(R.string.date_err), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.EMAIL_NOMATCH -> {
                ApplicationUtility.showSnack(this.resources.getString(R.string.email_err), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.MSISDN_NOMATCH -> {
                ApplicationUtility.showSnack(this.resources.getString(R.string.msisdn_err), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            else -> {
                mProgressBar.visibility = View.VISIBLE
                createUserAuth()
            }
        }
    }

    private fun createUserAuth() {
        if(mProgressBar.visibility == View.GONE) {
            mProgressBar.visibility = View.VISIBLE
        }
        mobileNumber = ApplicationConstants.COUNTRY_CODE + mobile
        firebaseServices.setListener(this as TaskOnComplete)
        firebaseServices.authenticateUser(this, mobileNumber, ApplicationConstants.REGISTRATION)
    }

    private fun registerUser() {
        val userData = UserData(email= email, msisdn = mobile, name = name, gender = gender, dob = dob)
        firebaseServices.saveUser(
            ApplicationConstants.USERS,
            userData,
            ApplicationConstants.NEW_PROFILE,
            ApplicationConstants.EMPTY)
    }

    override fun onResponseReceived(response: String) {
        when (response) {
            ApplicationConstants.AUTH_SUCCESS -> {
                //otp success
            }
            ApplicationConstants.FAIL_RESP -> {
                mProgressBar.visibility = View.GONE
                ApplicationUtility.showSnack(this.resources.getString(R.string.generic_error), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.CREATE_AUTH_FAIL -> {
                mProgressBar.visibility = View.GONE
                ApplicationUtility.showSnack(this.resources.getString(R.string.err_creating_auth), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.CREATE_AUTH_SUCCESS -> {
                registerUser()
            }
            ApplicationConstants.CODE_SENT -> {
                mProgressBar.visibility = View.GONE
            }
            ApplicationConstants.CODE_TIME_OUT -> {
                mProgressBar.visibility = View.GONE
                ApplicationUtility.showSnack(this.resources.getString(R.string.no_otp), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            ApplicationConstants.SUCCESS_RESP -> {
                mProgressBar.visibility = View.GONE
                ApplicationUtility.showToast(this, this.resources.getString(R.string.reg_success))
                startActivity(Intent(this, Login::class.java))
            }
            ApplicationConstants.TASK_CANCELLED -> {
                mProgressBar.visibility = View.GONE
                ApplicationUtility.showSnack(this.resources.getString(R.string.generic_error), root_layout_registration, ApplicationConstants.ACTION_OK)
                return
            }
            else -> ApplicationUtility.showSnack(this.resources.getString(R.string.generic_error), root_layout_registration, ApplicationConstants.ACTION_OK)
        }
    }

    fun showOTPDialog(verificationId: String, context: Activity) {
        startTimerForOTP()
        val mBottomSheetDialog = BottomSheetDialog(context)
        val sheetView = context.layoutInflater.inflate(R.layout.otp_layout, null)
        mBottomSheetDialog.setCancelable(false)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
        loginButton = sheetView.findViewById<Button>(R.id.verify_button)
        loginButton.typeface = ApplicationUtility.fontBold(context)
        loginButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        val otpInput = sheetView.findViewById<TextInputEditText>(R.id.otp_input)
        val cancelButton = sheetView.findViewById<Button>(R.id.cancel_button)
        cancelButton.typeface = ApplicationUtility.fontBold(context)
        cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        val resendImg = sheetView.findViewById<ImageView>(R.id.resend_image)
        resendText = sheetView.findViewById(R.id.resend_text)
        otp = otpInput.text.toString().trim()
        if(otp.isEmpty()){
            loginButton.isEnabled = false
        }
        resendText.setOnClickListener {
            if (!isOTPAuthenticationInProgress) {
                createUserAuth()
                startTimerForOTP()
            }
        }
        resendImg.setOnClickListener {
            if (!isOTPAuthenticationInProgress) {
                createUserAuth()
                startTimerForOTP()
            }
        }
        cancelButton.setOnClickListener {
            myCountDownTimer.cancelTimer()
        }
        loginButton.setOnClickListener {
            mProgressBar.visibility = View.VISIBLE
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            firebaseServices.signInWithPhoneAuthCredential(credential, context)
        }
    }

    private fun getInputsAndValidate() : String{
        if(gender_group.checkedRadioButtonId == -1){
            return ApplicationConstants.NO_GENDER
        }else{
            val genderButton : RadioButton = findViewById(gender_group.checkedRadioButtonId)
            gender = genderButton.text.toString()
        }
        name = input_name.text.toString().trim()
        mobile = input_mobile.text.toString().trim()
        dob = input_dob.text.toString().trim()
        if(name.isEmpty() || mobile.isEmpty() || dob.isEmpty() || gender.isEmpty()){
            return ApplicationConstants.EMPTY_FIELDS
        }
        if(input_email.text.toString().trim().isEmpty()){
            email = ApplicationConstants.EMPTY
            res = ApplicationConstants.SUCCESS_RESP
        }else{
            email = input_email.text.toString().trim()
            res = ApplicationUtility.regexValidator(Pattern.compile(ApplicationConstants.EMAIL_REGEX, Pattern.CASE_INSENSITIVE), email)
        }
        if(res == ApplicationConstants.FAIL_RESP){
            return ApplicationConstants.EMAIL_NOMATCH
        }
        if(ApplicationUtility.regexValidator(Pattern.compile(ApplicationConstants.MSISDN_REGEX), mobile) == ApplicationConstants.FAIL_RESP){
            return ApplicationConstants.MSISDN_NOMATCH
        }
        val date = Date()
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val currentDate = sdf.format(date)
        val inputDate = sdf.parse(dob)
        if(inputDate.after(sdf.parse(currentDate))){
            return ApplicationConstants.FUTURE_DATE
        }

        return ApplicationConstants.SUCCESS_RESP
    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            isOTPAuthenticationInProgress = false
            loginButton.isEnabled = false
        }

        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            resendText.text = "Click to resend in ${(millisUntilFinished / 1000)} seconds"
        }

        fun cancelTimer() {
            isOTPAuthenticationInProgress = false
            super.cancel()
        }
    }

    private fun startTimerForOTP() {
        myCountDownTimer.start()
    }

    private fun setupUI() {
        input_name.typeface = ApplicationUtility.fontRegular(this)
        input_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        input_email.typeface = ApplicationUtility.fontRegular(this)
        input_email.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        input_mobile.typeface = ApplicationUtility.fontRegular(this)
        input_mobile.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        input_dob.typeface = ApplicationUtility.fontRegular(this)
        input_dob.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        male.typeface = ApplicationUtility.fontRegular(this)
        male.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        female.typeface = ApplicationUtility.fontRegular(this)
        female.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        btn_signup.typeface = ApplicationUtility.fontBold(this)
        btn_signup.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_LARGE)

        loginLabel.typeface = ApplicationUtility.fontRegular(this)
        loginLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        login.typeface = ApplicationUtility.fontBold(this)
        login.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        toolbarTitle.text = getString(R.string.registration)
        toolbarTitle.typeface = ApplicationUtility.fontBold(this)
        toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }
}
