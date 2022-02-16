package com.josholadele.huaweitest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.josholadele.huaweitest.cache.AppSharedPreference
import com.josholadele.huaweitest.models.ObjectTypeInfoHelper
import com.josholadele.huaweitest.models.ParcelableAuth
import com.josholadele.huaweitest.models.TaskItem
import java.util.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "HomeActivity"
    lateinit var addButtonFAB: FloatingActionButton
    lateinit var itemET: TextInputEditText
    lateinit var addItemButton: Button
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var appSharedPreference: AppSharedPreference
    lateinit var parcelableAuth: ParcelableAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        addButtonFAB = findViewById(R.id.add)
        itemET = findViewById(R.id.item_et)
        addItemButton = findViewById(R.id.addItemButton)
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))
        addButtonFAB.setOnClickListener(this)
        appSharedPreference = AppSharedPreference()

        parcelableAuth = Gson().fromJson(
            appSharedPreference.getUser(), ParcelableAuth::class.java
        )
        addItemButton.setOnClickListener(this)
        initAGConnectCloudDB(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.options_logout) {
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun logout() {
        appSharedPreference.logoutUser()

        val intent = Intent(this, SetupActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(intent)
    }

    override fun onClick(p0: View?) {

        if (p0!!.id == R.id.add) {
            Toast.makeText(this, "Add new item!", Toast.LENGTH_LONG).show()
//            AGConnectCrash.getInstance().testIt(this)

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        if (p0.id == R.id.addItemButton) {
            upsertBookInfo(TaskItem(itemET.text.toString(), parcelableAuth.userId, Calendar.getInstance().time))

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun initAGConnectCloudDB(context: Context?) {
        AGConnectCloudDB.initialize(context!!)
        configure()
    }

    fun configure() {
        var instance = AGConnectInstance.buildInstance(
            AGConnectOptionsBuilder().setRoutePolicy(
                AGCRoutePolicy.CHINA
            ).build(this)
        );
        mCloudDB = AGConnectCloudDB.getInstance(instance, AGConnectAuth.getInstance(instance))

        mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())

        mConfig = CloudDBZoneConfig(
            "testZone",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
        )
        mConfig!!.persistenceEnabled = true
        val task = mCloudDB.openCloudDBZone2(mConfig!!, true)
        task.addOnSuccessListener {
            Log.i(TAG, "Open cloudDBZone success")
            mCloudDBZone = it
            // Add subscription after opening cloudDBZone success
//            addSubscription()
        }.addOnFailureListener {
            Log.w(TAG, "Open cloudDBZone failed for " + it.message)
        }
    }

    lateinit var mConfig: CloudDBZoneConfig
    lateinit var mCloudDBZone: CloudDBZone
    lateinit var mCloudDB: AGConnectCloudDB

    fun upsertBookInfo(bookInfo: TaskItem?) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val upsertTask = mCloudDBZone!!.executeUpsert(bookInfo!!)
        upsertTask.addOnSuccessListener { cloudDBZoneResult ->
            Log.i(TAG, "Upsert $cloudDBZoneResult records")
        }.addOnFailureListener {
            Toast.makeText(this, "Insert book info failed!", Toast.LENGTH_LONG).show()
//            mUiCallBack.updateUiOnError("Insert book info failed")
        }
    }
}