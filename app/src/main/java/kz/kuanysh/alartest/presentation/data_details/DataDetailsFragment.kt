package kz.kuanysh.alartest.presentation.data_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kz.kuanysh.alartest.R
import kz.kuanysh.alartest.databinding.FragmentDataDetailsBinding

class DataDetailsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDataDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs by navArgs<DataDetailsFragmentArgs>()
    private val item by lazy { safeArgs.item }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(item.latitude, item.longitude)
        googleMap.addMarker(
            MarkerOptions().position(latLng).title(item.id)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).apply {
                getMapAsync(this@DataDetailsFragment)
            }
            id.text = item.id
            name.text = item.name
            country.text = item.country
        }
    }

}