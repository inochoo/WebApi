package com.sibaamap.webapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.sibaamap.webapi.databinding.ActivityMainBinding
import java.io.InputStreamReader
import java.net.HttpCookie
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var biding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)

        fetchCurrencyData().start()


    }

    private fun fetchCurrencyData(): Thread
    {
        return Thread{
            val url = URL("https://open.er-api.com/v6/latest/aud")
            val connection = url.openConnection() as HttpsURLConnection

            if(connection.responseCode == 200){
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem,"UTF-8")
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()


            }else{
                biding.baseCurrency.text = "Failed Connection"
            }
        }
    }

    private fun updateUI(request: Request?) {
        runOnUiThread {
            kotlin.run {
                if (request != null) {
                    biding.lastUpdated.text = request.time_last_update_utc
                    biding.nzd.text = String.format("NZD: %.2f", request.rates.NZD)
                    biding.usd.text = String.format("USD: %.2f", request.rates.USD)
                    biding.gbp.text = String.format("GBP: %.2f", request.rates.GBP)
                }

            }
        }

    }

}