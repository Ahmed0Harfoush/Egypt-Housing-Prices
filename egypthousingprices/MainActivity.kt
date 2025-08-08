package com.example.egypthousingprices

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.egypthousingprices.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var regionSpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var sizeInput: EditText
    private lateinit var bedroomsInput: EditText
    private lateinit var bathroomsInput: EditText
    private lateinit var floorInput: EditText
    private lateinit var predictButton: Button
    private lateinit var resultText: TextView

    private val regions = listOf(
        "Nasr City", "New Cairo - El Tagamoa", "Sheikh Zayed", "Madinaty", "New Capital City",
        "Rehab City", "Awayed", "Boulaq Dakrour", "Qasr al-Nil", "Ras El Tin", "Kabbary"
    )

    private val cities = listOf("Cairo", "Giza", "Alexandria")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regionSpinner = findViewById(R.id.regionSpinner)
        citySpinner = findViewById(R.id.citySpinner)
        sizeInput = findViewById(R.id.sizeInput)
        bedroomsInput = findViewById(R.id.bedroomsInput)
        bathroomsInput = findViewById(R.id.bathroomsInput)
        floorInput = findViewById(R.id.floorInput)
        predictButton = findViewById(R.id.predictButton)
        resultText = findViewById(R.id.resultText)

        regionSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regions)
        citySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)

        predictButton.setOnClickListener {
            val region = regionSpinner.selectedItem.toString()
            val city = citySpinner.selectedItem.toString()
            val size = sizeInput.text.toString().toDoubleOrNull()
            val bedrooms = bedroomsInput.text.toString().toIntOrNull()
            val bathrooms = bathroomsInput.text.toString().toIntOrNull()
            val floor = floorInput.text.toString().toIntOrNull()

            if (size == null || bedrooms == null || bathrooms == null || floor == null) {
                Toast.makeText(this, "⚠️ Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = PredictionRequest(
                region = region,
                city = city,
                size = size,
                bedrooms = bedrooms,
                bathrooms = bathrooms,
                floor = floor
            )

            RetrofitClient.apiService.predict(request).enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val prediction = response.body()!!.prediction
                        resultText.text = "💰 Estimated Price: %.2f EGP".format(prediction)
                    } else {
                        resultText.text = "❌ Server Error"
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    resultText.text = "❌ Network Failure: ${t.message}"
                }
            })
        }
    }
}
