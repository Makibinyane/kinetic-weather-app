package com.example.global_kinetic_weather_app.ui

import android.Manifest
import android.R.attr.name
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.global_kinetic_weather_app.R
import com.example.global_kinetic_weather_app.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), LocationListener {

    private var locationManager: LocationManager? = null
    private var provider: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria()
        provider = locationManager?.getBestProvider(criteria, false)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE
            )
        }


        val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            Log.d("LOCATION", "Provider $provider has been selected.")
            onLocationChanged(location)
        }
    }

    /* Request updates at startup */
    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE
            )
        }

        provider?.let { locationManager?.requestLocationUpdates(it, 400, 1f, this) }
    }

    /* Remove the locationlistener updates when Activity is paused */
    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(this)
    }


    companion object {
        private const val REQUEST_FINE = 100

    }

    override fun onLocationChanged(location: Location) {
        setUpViewModel(location)
        Log.d("LOCATION", "Latitude: " + location.latitude)
        Log.d("LOCATION", "Longitude: " + location.longitude)
    }

    private fun setUpViewModel(location: Location) {
        val viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.fetchWeatherReport(location.longitude, location.latitude)
        viewModel.showProgress.observe(this, {
            if (it) {
                progress_bar.visibility = View.VISIBLE
            } else {
                progress_bar.visibility = View.GONE
                btnRefresh.visibility = View.VISIBLE
            }
        })
        viewModel.weatherLiveData.observe(this, { weatherData ->
            Log.d("LOCATION", "Data: " + weatherData.toString())
            val currentTemperature = viewModel.convertKelvinToCelsius(weatherData?.main?.temp)
            txtCurrentTemperature.text = currentTemperature.toString() + " \u2103"
            txtDescription.text = getString(R.string.weather_desc, weatherData?.weather?.get(0)?.description)
            txtWindSpeed.text = getString(R.string.wind_speed, weatherData?.wind?.speed.toString())
            txtHumidity.text = getString(R.string.humidity, weatherData?.main?.humidity.toString())
            txtTown.text = getString(R.string.town, weatherData?.name)
            val locale = Locale("", weatherData?.sys?.country)
            txtCountry.text = getString(R.string.country, locale.displayCountry)
            txtPressure.text = getString(R.string.pressure, weatherData?.main?.pressure.toString())

        })

        btnRefresh.setOnClickListener {
            onLocationChanged(location)
        }

    }
}