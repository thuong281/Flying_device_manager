package com.example.flyingdevicemanager.app_ui.map

import android.content.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.ActivityMapsBinding
import com.example.flyingdevicemanager.util.*
import com.example.flyingdevicemanager.util.base.BaseResponse
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import io.socket.client.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.net.URISyntaxException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    
    private val viewModel: MapsViewModel by viewModels()
    
    private val args: MapsActivityArgs by navArgs()
    
    lateinit var sharedPreferences: SharedPreferences
    
    var deviceLocationMarker: Marker? = null
    lateinit var previousLatLng: LatLng
    
    private val URL = Constants.BASE_URL
    private lateinit var socket: Socket
    
    init {
        try {
            socket = IO.socket(URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sharedPreferences = applicationContext.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        initSocket()
        observeData()
    }
    
    private fun initSocket() {
        val userId = args.deviceId
        
        viewModel.getDeviceLocation(getToken().toString(), userId.toString())
        
        // init socket listener
        socket.connect()
        socket.on("update_device_${userId}") { array ->
            lifecycleScope.launch {
                val data: JSONObject = JSONObject(array[0].toString())
                val lat = data.getDouble("lat")
                val long = data.getDouble("long")
                showLocationOnMap(lat, long)
            }
        }
    }
    
    private fun observeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.locationResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        showLocationOnMap(
                            it.body()?.data?.lat!!,
                            it.body()?.data?.long!!
                        )
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
    }
    
    private fun showLocationOnMap(lat: Double, long: Double) {
        val latLng = LatLng(lat, long)
        
        // draw current device current location
        if (deviceLocationMarker == null) {
            val height = 64 * 1.5
            val width = 128 * 1.5
            val bitMapDraw = resources.getDrawable(R.drawable.drone_small) as BitmapDrawable
            val b = bitMapDraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width.toInt(), height.toInt(), false)
            val markerOptions = MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory.fromBitmap(smallMarker)
            ).anchor(0.5F, 0.5F)
            deviceLocationMarker = mMap.addMarker(markerOptions)
            previousLatLng = latLng
        } else {
            deviceLocationMarker!!.position = latLng
            // store previous location
            val markerOptions = MarkerOptions().position(previousLatLng).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.blue_dot)
            ).anchor(0.5F, 0.5F)
            mMap.addMarker(markerOptions)
            previousLatLng = latLng
        }
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0F);
        mMap.animateCamera(cameraUpdate)
    }
    
    private fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }
    
    private fun errorMessage(response: Response<BaseResponse<Any>>) {
        Toast.makeText(
            applicationContext,
            ErrorUtils.parseMessage(response as Response<Any>).errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
    
}