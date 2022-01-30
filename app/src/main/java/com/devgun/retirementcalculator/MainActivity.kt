package com.devgun.retirementcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlin.Exception
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    lateinit var calculateButton: Button
    lateinit var monthlySavingsEditText: EditText
    lateinit var interestEditText: EditText
    lateinit var ageEditText: EditText
    lateinit var retirementEditText: EditText
    lateinit var currentEditText: EditText
    lateinit var resultTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(application, "c8f5e3ff-4e0e-491a-93bd-e7197f255107", Analytics::class.java, Crashes::class.java)

        calculateButton = findViewById(R.id.calculateButton)
        monthlySavingsEditText = findViewById(R.id.monthlySavingsEditText)
        interestEditText = findViewById(R.id.interestEditText)
        ageEditText = findViewById(R.id.ageEditText)
        retirementEditText = findViewById(R.id.retirementEditText)
        currentEditText = findViewById(R.id.currentEditText)
        resultTextView = findViewById(R.id.resultTextView)



        calculateButton.setOnClickListener {
            //throw Exception("Something went wrong mf!")
            //Crashes.generateTestCrash()
            try {
                val interestRate: Float = interestEditText.text.toString().toFloat()
                val currentAge: Float = ageEditText.text.toString().toFloat()
                val retirementAge: Float = retirementEditText.text.toString().toFloat()
                val monthly: Float = monthlySavingsEditText.text.toString().toFloat()
                val current: Float = currentEditText.text.toString().toFloat()

                val properties: HashMap<String, String> = hashMapOf()
                properties["interest_rate"] = interestRate.toString()
                properties["current_age"] = currentAge.toString()
                properties["retirement_age"] = retirementAge.toString()
                properties["monthly"] = monthly.toString()
                properties["current"] = current.toString()




                if (interestRate!! <= 0) {
                    Analytics.trackEvent("wrong_interest_rate", properties)
                }
                if (currentAge!! >= retirementAge!!) {
                    Analytics.trackEvent("wrong_age", properties)
                }

                val futureSavings = calculateRetirement(interestRate, current, monthly, ((retirementAge - currentAge)* 12).toInt())
                resultTextView.text = "At the current rate of $interestRate%, saving $$monthly a month you will have $${String.format("%f", futureSavings)} by $retirementAge."

                Toast.makeText(this, "Clicked!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Analytics.trackEvent("Exception_at_click: ${e.message}")
            }


        }

    }

    fun calculateRetirement(interestRate: Float, currentSavings: Float, monthly: Float, numMonths: Int): Float{
        var futureSavings = currentSavings * (1 + (interestRate/100/12)).pow(numMonths)
        for (i in 1..numMonths){
            futureSavings += monthly * (1 + (interestRate/100/12)).pow(i)
        }
        return futureSavings

    }


}
