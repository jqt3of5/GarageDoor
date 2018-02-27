package org.jqt3of5.android.garagedoor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.ArrayAdapter
import android.widget.ListView
import io.particle.android.sdk.accountsetup.LoginActivity
import io.particle.android.sdk.cloud.ParticleCloud
import io.particle.android.sdk.cloud.ParticleCloudException
import io.particle.android.sdk.cloud.ParticleCloudSDK
import io.particle.android.sdk.cloud.ParticleDevice
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary
import io.particle.android.sdk.utils.Async

class MainActivity : AppCompatActivity() {

    private var mDeviceListView : ListView? = null
    private var mAdapter : DeviceListAdapter = DeviceListAdapter(this.applicationContext)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fab = findViewById<FloatingActionButton>(R.id.add_fab);
        fab.setOnClickListener {
            ParticleDeviceSetupLibrary.startDeviceSetup(this.applicationContext,javaClass)
        }
        mDeviceListView = findViewById(R.id.device_list)
        mDeviceListView?.adapter = mAdapter

        ParticleDeviceSetupLibrary.init(this.applicationContext)
    }

    override fun onStart() {
        super.onStart()

        if (ParticleCloudSDK.getCloud().accessToken == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            return;
        }
    }

    override fun onResume() {
        super.onResume()
        if (ParticleCloudSDK.getCloud().accessToken != null) {
            mAdapter.RefreshDevices()
        }
    }
}
