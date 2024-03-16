package com.pri.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.pri.weatherapp.R
import com.pri.weatherapp.data.model.Coord
import com.pri.weatherapp.data.model.Status
import com.pri.weatherapp.databinding.FragmentHomeBinding
import com.pri.weatherapp.ui.citysearch.CitySearchViewModel
import com.pri.weatherapp.utils.setDataImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val citySearchViewModel by navGraphViewModels<CitySearchViewModel>(R.id.main_nav)
    private val client by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
        client.lastLocation
            .addOnSuccessListener(
                requireActivity()
            ) { location ->
                if (location != null) {
                    setLocationData(location)
                } else {
                    binding.progressBar.isVisible = true
                    startLocationUpdates()
                }
            }
        binding.initView()
        observeLiveData()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun setLocationData(location: Location) {
        val latitude = Math.round(location.latitude * 100.0) / 100.0
        val longitude = Math.round(location.longitude * 100.0) / 100.0
        viewModel.callWeatherApiByCoord(Coord(latitude, longitude))

    }

    private fun FragmentHomeBinding.initView() {
        btOptions.setOnClickListener { showPopup(it) }
        ivEdit.setOnClickListener { goToCitySearch() }
        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun observeLiveData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data
                        val list = data?.list.orEmpty()
                        binding.rvForecast.adapter = DailyForecastAdapter(list)
                        val todayWeather = list.firstOrNull()
                        val weather = todayWeather?.weather?.firstOrNull()
                        val description = weather?.description
                        val icons = weather?.icon
                        val currentDate = Date()
                        val dateString = currentDate.toString()
                        val dateSplit = dateString.trim().split(" ".toRegex())
                        val date = dateSplit[0] + ", " + dateSplit[1] + " " + dateSplit[2]
                        val main = todayWeather?.main
                        val temparature = main?.temp ?: 0.0
                        val temp = Math.round(temparature).toString() + "°C"
                        val humidity = main?.humidity ?: 0.0
                        val hum = Math.round(humidity).toString() + "%"
                        val feelsLike = main?.feelsLike ?: 0.0
                        val feelsValue = Math.round(feelsLike).toString() + "°"
                        val wind = todayWeather?.wind
                        val windValue = "${wind?.speed ?: 0.0} km/h"
                        val city = data?.city
                        binding.run {
                            tvCity.text = city?.name
                            tvDegree.text = temp
                            tvMain.text = description.orEmpty()
                            ivWeatherImage.setDataImage(icons)
                            tvTime.text = date
                            tvHumidity.text = hum
                            tvRealfeel.text = feelsValue
                            tvWind.text = windValue
                            swipeRefreshLayout.isRefreshing = false
                            progressBar.isVisible = false
                        }
                    }

                    Status.ERROR -> {
                        resource.message?.let { message ->
                            showError(message)
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                    }

                    Status.LOADING -> {
                        /* no-op */
                    }
                }

            }
        }
        citySearchViewModel.searchedCityQuery.observe(viewLifecycleOwner) {
            it?.let { citySearched ->
                weatherByCityName(citySearched)
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                        client.lastLocation
                            .addOnSuccessListener(
                                requireActivity()
                            ) { location ->
                                if (location != null) {
                                    val latitude = Math.round(location.latitude * 100.0) / 100.0
                                    val longitude = Math.round(location.longitude * 100.0) / 100.0
                                    viewModel.callWeatherApiByCoord(Coord(latitude, longitude))
                                }else{
                                    startLocationUpdates()
                                }
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun weatherByCityName(city: String) {
        binding.progressBar.isVisible = true
        viewModel.callCurrentWeatherByCity(city)
    }


    private fun showPopup(v: View?) {
        val popup = PopupMenu(requireContext(), v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.id_otherCity -> {
                goToCitySearch()
                false
            }

            else -> false
        }
    }

    private fun goToCitySearch() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCitySearchDialogFragment())
    }

}