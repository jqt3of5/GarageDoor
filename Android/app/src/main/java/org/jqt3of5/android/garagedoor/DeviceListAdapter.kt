package org.jqt3of5.android.garagedoor

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.particle.android.sdk.cloud.ParticleCloud
import io.particle.android.sdk.cloud.ParticleCloudException
import io.particle.android.sdk.cloud.ParticleCloudSDK
import io.particle.android.sdk.cloud.ParticleDevice
import io.particle.android.sdk.utils.Async
import java.util.zip.Inflater

/**
 * Created by Brittany on 2/25/2018.
 */
class DeviceListAdapter : BaseAdapter {

    private var mDevices : List<ParticleDevice> = emptyList()
    private var mContext : Context
    private var mInflater : LayoutInflater

    constructor(context : Context) : super()
    {
        mContext = context
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    public fun RefreshDevices(){

        var adapter = this
        if (ParticleCloudSDK.getCloud().accessToken != null) {
            Async.executeAsync(ParticleCloudSDK.getCloud(),object : Async.ApiWork<ParticleCloud, List<ParticleDevice>>()
            {
                override fun callApi(apiCaller: ParticleCloud?): List<ParticleDevice>{
                    return ParticleCloudSDK.getCloud().devices
                }

                override fun onSuccess(result: List<ParticleDevice>?) {
                    if (result == null) return
                    mDevices = result
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(exception: ParticleCloudException?) {
                    Toast.makeText(adapter.mContext, exception?.message,Toast.LENGTH_SHORT)
                }
            })
        }
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    override fun getItem(index: Int): Any {
        return mDevices.get(index)
    }

    override fun getCount(): Int {
     return mDevices.count()
    }

    override fun getView(index: Int, convertView: View?, parentView: ViewGroup?): View {
        var view = mInflater.inflate(R.layout.device_item, parentView,false)
        var online = view.findViewById<ImageView>(R.id.online_image_view)
        var name = view.findViewById<TextView>(R.id.device_name_textview)

        var device = getItem(index) as ParticleDevice
        if (device.isConnected)
        {
            (online.drawable as GradientDrawable).setColor(Color.GREEN)
        }
        else
        {
            (online.drawable as GradientDrawable).setColor(Color.RED)
        }

        name.setText(device.name)

        return view
    }
}