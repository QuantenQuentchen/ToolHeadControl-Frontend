package com.example.toolheadadjust

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color.green
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.toolheadadjust.databinding.ActivityMainBinding
import com.example.toolheadadjust.ui.gallery.ToolCfgAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var mBluetoothAdapter: BluetoothAdapter? = null
    lateinit var mBluetoothManager: BluetoothManager
    lateinit var mBluetoothDeviceAddress: String
    lateinit var mBluetoothConnectionService: BluetoothService
    lateinit var targetDevice: BluetoothDevice
    lateinit var mLiveDataCords: LiveDataCords
    lateinit var mDataToolCfgList: ArrayList<ToolCfg>
    lateinit var mDBHelper: SQLiteManager
    lateinit var mToolCfgAdapter: ToolCfgAdapter
    companion object {
        const val REQUEST_ENABLE_BT = 1
        const val REQUEST_ACCESS_COARSE_LOCATION = 2
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mBluetoothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
        mLiveDataCords = LiveDataCords()
        mBluetoothConnectionService = BluetoothService(mBluetoothAdapter!!, this, mainActivity = this)

        mDataToolCfgList = ArrayList()
        mDBHelper = SQLiteManager(this)
        mToolCfgAdapter = ToolCfgAdapter(mDataToolCfgList, mDBHelper)
        super.onCreate(savedInstanceState)
        //mBluetoothConnectionService.loadDevice()
        //mBluetoothConnectionService.connect()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //bluetoothAdapter.enable()
        setSupportActionBar(binding.appBarMain.toolbar)

        val fab: FloatingActionButton = binding.appBarMain.fab
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val filter: IntentFilter = IntentFilter()


        //Intent Filter
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        this.registerReceiver(mBroadcastReceiver, filter)

        //Fab button
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            navController.navigate(R.id.nav_tool_config_edit, Bundle().apply {
                putInt("Xcord", mLiveDataCords._xCord.value!!)
                putInt("Ycord", mLiveDataCords._yCord.value!!)
            })
        }
        fab.hide()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> {
                    fab.hide()
                }
                R.id.nav_gallery -> {
                    fab.show()
                }
                R.id.nav_slideshow -> {
                    fab.show()
                }
                R.id.nav_tool_connection -> {
                    fab.hide()
                }
                R.id.nav_tool_config_edit -> {
                    fab.hide()
                }
                R.id.nav_tool_config_detail -> {
                    fab.hide()
                }
            }
        }
        mToolCfgAdapter.onItemClick = { toolCfg ->
            navController.navigate(R.id.nav_tool_config_detail, Bundle().apply {
                putString("Name", toolCfg.name)
                putInt("XCord", toolCfg.xCord)
                putInt("YCord", toolCfg.yCord)
                putInt("index", mToolCfgAdapter.toolCfgList.indexOf(toolCfg))
                putInt("id", toolCfg.id)
            })
        }
        binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.purple_200))
        val connectionObserver = androidx.lifecycle.Observer<Boolean>{ isConnected ->
            if (isConnected) {
                binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.light_blue_600))
            } else {
                binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.gray_400))
            }

        }
        mLiveDataCords._isConnected.observe(this, connectionObserver)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_tool_connection,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver(){
        var device: BluetoothDevice? = null
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val action = intent.action
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            when (action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    mLiveDataCords._isConnected.postValue(true)
                    Log.d("Bluetooth", "Connected")
                    binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.ic_connection_indicator_connected_background))
                    Toast.makeText(this@MainActivity, "Bluetooth Connected!", Toast.LENGTH_SHORT).show()
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    mLiveDataCords._isConnected.postValue(false)
                    Log.d("Bluetooth", "Disconnected")
                    binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.gray_400))
                    Toast.makeText(this@MainActivity, "Bluetooth Disconnected!", Toast.LENGTH_SHORT).show()
                }
                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> {
                    Log.d("Bluetooth", "Disconnect Requested")
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    Log.d("Bluetooth", "Bond State Changed")
                }
                BluetoothDevice.ACTION_CLASS_CHANGED -> {
                    Log.d("Bluetooth", "Class Changed")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("Bluetooth", "Found")
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    Log.d("Bluetooth", "Name Changed")
                }
                BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                    Log.d("Bluetooth", "Pairing Request")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}