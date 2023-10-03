package com.example.playstorecollection

import DataResponse
import LoadingActivity
import android.Manifest
import android.Manifest.permission.*
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playstorecollection.adapter.AppsAdapter
import com.example.playstorecollection.callbacks.WatchlistApiServices
import com.example.playstorecollection.model.*
import com.example.playstorecollection.utills.DecompressFast
import com.example.playstorecollection.utills.ThemeSwitchState
import com.example.playstorecollection.utills.UseNameState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.progressBar
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
    private var appsList = java.util.ArrayList<String>()
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var isUnzippingFinished = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()

        progressBar2 = findViewById(R.id.progressBar)
        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)
        val rvInstalledAppsList = findViewById<View>(R.id.rv_apps2) as RecyclerView


         sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        getAPIData()
        getWatchlistSymbolData("new")
      //  val themeSwitch = findViewById<Switch>(R.id.themeSwitch)
    //    themeSwitch.isChecked = ThemeSwitchState.isDarkThemeEnabled
        themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("ThemeSwitch", "Switch state changed: isChecked = $isChecked")
            // Handle switch state changes here
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ThemeSwitchState.isDarkThemeEnabled=true
                Log.d("ThemeSwitch", "Dark theme enabled:${ThemeSwitchState.isDarkThemeEnabled}")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ThemeSwitchState.isDarkThemeEnabled=false
            }
        }

        handler = Handler(Looper.getMainLooper())
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
   //     val pieChartView: PieChartView = findViewById(R.id.pieChart)
        getFirebaseStorageList()
        runnable = Runnable {
            // Code to be executed after the delay
            // Toast.makeText(this, "Delayed task executed!", Toast.LENGTH_SHORT).show()
            adapter = appsList.let { AppsAdapter(it, this, true) }
            Log.d("newww", appsList.toString())
            rvInstalledAppsList.adapter = adapter
            val numberOfColumns = 4
            rvInstalledAppsList.layoutManager = GridLayoutManager(this,numberOfColumns)
            progressBar.visibility = View.GONE
       }


        handler.postDelayed(runnable, 1000)




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


//         installedAppsList = retrieveSavedData()
//         adapter = installedAppsList.let { AppsAdapter(it, this, true) }
//
//
//
//        rvInstalledAppsList.adapter = adapter
//        val numberOfColumns = 4
//        rvInstalledAppsList.layoutManager = GridLayoutManager(this,numberOfColumns)


        fab2.setOnClickListener {
            val myIntent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(myIntent)
        }
        if (retrieveSavedData().isEmpty()) {
            hometext.visibility = View.GONE
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
                                UseNameState.UserName=profile.username
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

    override fun onResume() {
        super.onResume()
    }


    public fun downloadFromFirebase(appName: String,context: Context) {
        if (ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT <= 30
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity2,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            getUrlFromFirebase(appName,context)
        }

    }
    private fun getUrlFromFirebase(appname: String,context: Context) {
        // Points to the root reference
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference()
        val firebaseFile: StorageReference =
            storageRef.child(appname)

        // val rootPath = File(Environment.getExternalStorageDirectory(), "file_name")

        /*  var path =
              Environment.getDataDirectory().absolutePath.toString()
          val mFolder = File(path)*/

        /*var pathNew = ""
        //if (pathNew.isEmpty()){
            val file = File(this.filesDir, appname)
            pathNew = file.absolutePath
            Log.d("PATH", pathNew)
            if (!file.exists()) {
                file.mkdirs()
            }
       // }*/

        /* if (!rootPath.exists()) {
            // rootPath.mkdirs()
         }
         val localFile = File(file, appname)
         val  final = File(filesDir,appname)

         val fileName = "example.zip" // Specify the desired file name
         val fileupdate = File(filesDir, appname) // Internal storage file object
         val filePath = fileupdate.absolutePath*/
        /* firebaseFile.getFile(final).addOnSuccessListener {
             Log.e("firebase2 ",";local success " +file.toString() + localFile);
             handler.postDelayed(Runnable {
                 unzipfile(appname)
             },2000)
         }.addOnFailureListener{
             Log.e("firebase2 failure",";local failure " +it);
         }*/


        firebaseFile.downloadUrl.addOnSuccessListener {
            downloadFile2(this, it.toString(), appname,context)
            Log.d("collectt_success", it.toString())
        }.addOnFailureListener {
            Log.d("collectt", it.toString())
        }


    }
    private fun downloadFile2(context: Context, url: String?, fileName: String?,myContext:Context) {
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        Log.e("Decompress33", "download succdess")
        downloadManager.enqueue(request)

    //   handler.postDelayed(Runnable {
            unzipfile2(fileName,myContext)
     //   }, 3000)
    }
    fun unzipfile2(fileNameZip: String?,myContext: Context) {
        val zipFileName2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" +fileNameZip
        var filename = fileNameZip?.replace(".zip","")
        val destinationPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/unzippedAppNew/"+ filename + "/"
        val destinationPath2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()


        val df = DecompressFast(
            zipFileName2,
            destinationPath
        )
        df.unzip()
        val myIntent = Intent(myContext, WebviewActivity2::class.java)
            myIntent.putExtra("appName",filename)
            myContext.startActivity(myIntent)




        //isUnzippingFinished=true

        // df.unzipNew(zipFileName2,destinationPath)

        // df.unpackZip(destinationPath2,zipFileName2)
        // unzipFilePath( zipFileName2,destinationPath )

        Toast.makeText(this@MainActivity2, "Unziping file", Toast.LENGTH_SHORT).show()
    }
    fun getFirebaseStorageList() {
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference()


        progressBar.visibility = View.VISIBLE
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                for (item in listResult.items) {
                    // Process each item (file) here
                    val fileName = item.name
                    Log.d("newwwfile url2", fileName)
                    appsList.add(fileName)
                }

            }
            .addOnFailureListener { err ->
                Log.d("newwwfile err", err.toString())
                progressBar.visibility = View.GONE
                // Handle any errors that occur while listing the files
            }
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
