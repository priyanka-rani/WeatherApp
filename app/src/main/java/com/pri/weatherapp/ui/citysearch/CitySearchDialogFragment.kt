package com.pri.weatherapp.ui.citysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.pri.weatherapp.R
import com.pri.weatherapp.databinding.DialogCitySearchBinding

class CitySearchDialogFragment : DialogFragment() {
    private lateinit var binding: DialogCitySearchBinding
    private val viewModel by navGraphViewModels<CitySearchViewModel>(R.id.main_nav)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btCitySearch.setOnClickListener { v: View? ->
            val citySearched = binding.etCitySearch.text.toString()
            viewModel.setSearchedCityQuery(citySearched)
            dismiss()
        }
    }
}