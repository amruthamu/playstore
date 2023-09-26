package com.example.playstorecollection

import DataResponse
import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playstorecollection.adapter.AppsAdapter
import com.example.playstorecollection.callbacks.WatchlistApiServices
import com.example.playstorecollection.model.*
import kotlinx.android.synthetic.main.activity_main2.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity2 : AppCompatActivity()  {

    private var installedAppsList = ArrayList<String>()
    private var newAppsList = ArrayList<String>()
    lateinit var progressBar2: ProgressBar
    var insApps: String = ""
    lateinit var adapter : AppsAdapter;
    lateinit var sharedPreferences :SharedPreferences
    lateinit var  responseData : WatchlistData
    private lateinit var dataTextView: TextView
    private lateinit var name: TextView
    private lateinit var gwpAmount: TextView
    private lateinit var gwpTarget: TextView
    private lateinit var gwpPercent: TextView
    private lateinit var nopValue: TextView
    private lateinit var currency: TextView
    private lateinit var nopTarget: TextView
    private lateinit var nopPercent: TextView
    private lateinit var agentCount: TextView
    private lateinit var dealersCount: TextView
    private lateinit var today: TextView
    private lateinit var week: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()

        progressBar2 = findViewById(R.id.progressBar)
        val rvInstalledAppsList = findViewById<View>(R.id.rv_apps2) as RecyclerView
         sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        getAPIData()
        getWatchlistSymbolData("new")
   //     val pieChartView: PieChartView = findViewById(R.id.pieChart)



        if (!intent.extras?.getString("installapp").isNullOrEmpty()) {
            intent.extras?.getString("installapp")?.let {
                 insApps = intent.extras?.getString("installapp")!!
            }


            if (retrieveSavedData().contains(insApps)){
                Toast.makeText(this@MainActivity2, "App Already installed", Toast.LENGTH_SHORT).show()
            } else {
                progressBar2.visibility = View.VISIBLE
                val handler1 = Handler(Looper.getMainLooper())
                handler1.postDelayed(Runnable {
                    progressBar2.visibility = View.GONE
                },3000)
            }


        }

        permissions()?.let {
            ActivityCompat.requestPermissions(this@MainActivity2,
                it,
                1)
        };

        // val sharedPref = getPreferences(Context.MODE_PRIVATE)

        /* if (installedAppsList.isNotEmpty()){
             val editor: SharedPreferences.Editor =  sharedPref.edit()
             val set: MutableSet<String> = HashSet()
             set.addAll(installedAppsL   ist)
             editor.putStringSet("savedApps",set)
             editor.apply()
             editor.commit()
         }*/

        installedAppsList = retrieveSavedData()


        if (insApps.isNotEmpty() && !installedAppsList.contains(insApps)) {
            installedAppsList.add(insApps)
            val jsonArray = JSONArray(installedAppsList)
            val jsonString = jsonArray.toString()

            val editor = sharedPreferences.edit()
            editor.putString("my_list", jsonString)
            editor.apply()
        }


         installedAppsList = retrieveSavedData()
         adapter = installedAppsList.let { AppsAdapter(it, this, true) }



        rvInstalledAppsList.adapter = adapter
        val numberOfColumns = 3
        rvInstalledAppsList.layoutManager = GridLayoutManager(this,numberOfColumns)


        fab2.setOnClickListener {
            val myIntent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(myIntent)
        }
        if (retrieveSavedData().isEmpty()) {
            hometext.visibility = View.VISIBLE
        } else {
            hometext.visibility = View.GONE
        }


         dataTextView = findViewById(R.id.userName)
        name = findViewById(R.id.text_inside_circle)
         gwpAmount = findViewById(R.id.amount)

         gwpTarget = findViewById(R.id.target)
       // gwpPercent = findViewById(R.id.userName)
         nopValue = findViewById(R.id.amount1)

       //  currency = findViewById(R.id.userName)
         nopTarget = findViewById(R.id.target1)
        // nopPercent = findViewById(R.id.userName)
         agentCount = findViewById(R.id.sixty)
         dealersCount = findViewById(R.id.fifty)
         today = findViewById(R.id.sixty1)
         week = findViewById(R.id.fifty1)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the ApiService
        val apiService = retrofit.create(ApiService::class.java)

        // Make the API call
        val call = apiService.fetchData()
        call.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.isSuccessful) {
                    val dataResponse = response.body()
                    if (dataResponse != null && dataResponse.data.profiles.isNotEmpty()) {
                        val profile = dataResponse.data.profiles[0]
                        // Inside onResponse callback
                            val dataResponse = response.body()
                            if (dataResponse != null && dataResponse.data.profiles.isNotEmpty()) {
                                val profile = dataResponse.data.profiles[0]
                                dataTextView.text = profile.username
                                name.text=profile.name
                                gwpAmount.text=profile.currency+profile.gwpAmount
                                nopValue.text=profile.nopValue
                                nopTarget.text="Target "+profile.nopTarget
                              //  nopPercent.text=profile.nopPercent
                                agentCount.text=profile.agentCount
                                dealersCount.text=profile.dealersCount
                                today.text=profile.today
                                week.text=profile.week



                            }


                    }
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun retrieveSavedData(): ArrayList<String> {

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        // val mySet = sharedPreferences.getStringSet("my_list", emptySet())

        val savedJsonString = sharedPreferences.getString("my_list", null)
        if (savedJsonString != null) {
            val savedArrayList = ArrayList<String>()
            val jsonArray2 = JSONArray(savedJsonString)
            for (i in 0 until jsonArray2.length()) {
                val item = jsonArray2.getString(i)
                if (!newAppsList.contains(item)) {
                    newAppsList.add(item)
                }

            }
            Log.d("ne22", savedArrayList.toString())

            // Use the retrieved ArrayList...
        } else {
            // Handle the case when no ArrayList is saved in SharedPreferences...
            Log.d("ne22", "savedJsonString null" + newAppsList.toString())

        }
        return newAppsList
    }

    fun handleDeleteItem(position: Int) {
        installedAppsList.removeAt(position)

        val jsonArray = JSONArray(installedAppsList)
        val jsonString = jsonArray.toString()

        val editor = sharedPreferences.edit()
        editor.putString("my_list", jsonString)
        editor.apply()
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }

    private fun getAPIData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiservice.myflow.ai/uSBankPOC/") // Base URL of your API
            .client(OkHttpClient()) // You can customize the OkHttpClient if needed
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter
            .build()

        val apiService = retrofit.create(WatchlistApiServices::class.java)
        val requestbody = WatchlistRequestBody("")
        val call = apiService.getData(requestbody)
        call!!.enqueue(object : Callback<WatchlistData?> {
            override fun onResponse(
                call: Call<WatchlistData?>,
                response: Response<WatchlistData?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("retrofitt data", data.toString())
                    if (data != null) {
                        responseData = data
                    }
                    // Handle the successful response
                    // Access the data from the response object
                } else {
                    // Handle error
                    Log.d("retrofitt data error", response.toString())
                }
            }

            override fun onFailure(call: Call<WatchlistData?>, t: Throwable) {
                // Handle the error
                t.printStackTrace()
                Log.d("retrofitt data failure", t.toString())
            }
        })
    }

    fun getWatchlistSymbolData(message: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiservice.myflow.ai/uSBankPOC/") // Base URL
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val requestbody = SymbolRequest(message)
        val apiService = retrofit.create(WatchlistApiServices::class.java)

        val call = apiService.getWatchlistSymbols(requestbody)
        Log.d("requestbodyy1",requestbody.toString())
        Log.d("requestbodyy1",message.toString())

        call.enqueue(object : Callback<SymbolData> {
            override fun onResponse(
                call: Call<SymbolData>,
                response: Response<SymbolData>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("retrofitt data symbol2", data.toString())
                    if (data != null) {
                        //responseData = data
                    }

                    // Handle the successful response
                    // Access the data from the response object
                } else {
                    // Handle error
                    Log.d("retrofitt datasym error", response.toString())
                }
                Log.d("retrofitt data body", response.errorBody().toString())
            }

            override fun onFailure(call: Call<SymbolData?>, t: Throwable) {
                // Handle the error
                t.printStackTrace()
                Log.d("retrofitt data failure", t.toString())
            }
        })

    }
    var storge_permissions = arrayOf<String>(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33 = arrayOf<String>(
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.READ_MEDIA_AUDIO,
        android.Manifest.permission.READ_MEDIA_VIDEO
    )

    fun permissions(): Array<String>? {
        val p: Array<String>
        p = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storge_permissions_33
        } else {
            storge_permissions
        }
        return p
    }
}
