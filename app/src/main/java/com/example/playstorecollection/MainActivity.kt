package com.example.playstorecollection

import android.Manifest
import android.app.DownloadManager
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playstorecollection.adapter.AppsAdapter
import com.example.playstorecollection.utills.DecompressFast
import com.example.playstorecollection.utills.ThemeManager
import com.example.playstorecollection.utills.ThemeSwitchState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    var msg: String = ""
    private var appsList = ArrayList<String>()
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    var adapter: AppsAdapter? = null
    val filename2 = "demoFile.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
       val themeSwitch = findViewById<Switch>(R.id.themeSwitch)
        setSupportActionBar(toolbar)
        toolbar.setTitle("Mini App Store ")
       // val themeManager = ThemeManager(this)
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
        //getUrlFromFirebase("gs://playstorecollection-f5eba.appspot.com")
        getFirebaseStorageList()

        /*fab.setOnClickListener {
            val myIntent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(myIntent)
        }*/

        val rvAppsList = findViewById<View>(R.id.rv_apps) as RecyclerView

        // stringList  = listOf<String>("Google","OLA","UBER","Facebook")

        runnable = Runnable {
            // Code to be executed after the delay
            // Toast.makeText(this, "Delayed task executed!", Toast.LENGTH_SHORT).show()

            adapter = appsList.let { AppsAdapter(it, this, false) }
            Log.d("newww", appsList.toString())
            rvAppsList.adapter = adapter
            val numberOfColumns = 3
            rvAppsList.layoutManager = GridLayoutManager(this,numberOfColumns)
            progressBar.visibility = View.GONE
        }


        handler.postDelayed(runnable, 3000)


    }

    private fun setupTheme() {
        if (ThemeSwitchState.isDarkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                // Handle the search action here
                return true
            }
            // Handle other menu item clicks if needed
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu)

        // below line is to get our menu item.
        val searchItem = menu?.findItem(R.id.action_search)

        // getting search view of our item.
        //val searchView = searchItem?.actionView

        //writeData()
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print

                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                // myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                val query = s.toString().toLowerCase(Locale.getDefault())
                filterWithQuery(query)
                return false
            }
        })
        return true
    }

    private fun filterWithQuery(text: String) {
        val filteredList: ArrayList<String> = ArrayList<String>()
        for (item in appsList) {
            if (item.toLowerCase().startsWith(text.lowercase(Locale.getDefault()))) {
                filteredList.add(item)
            }
        }
        adapter?.filterList(filteredList)
    }

    open fun moveToNext() {
        val myIntent = Intent(this@MainActivity, WebviewActivity::class.java)
        startActivity(myIntent)
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



    // for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    private fun getUrlFromFirebase(appname: String) {
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
            downloadFile2(this, it.toString(), appname)
            Log.d("collectt_success", it.toString())
        }.addOnFailureListener {
            Log.d("collectt", it.toString())
        }


    }


    /*dateRef.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri?> {
        override fun onSuccess(downloadUrl: Uri?) {
            //do something with downloadurl
            Log.d("collectt",downloadUrl.toString())
        }
    }).addOnFailureListener( {
        // Handle any errors that occurred during the download
        Log.d("collectt",it.toString())
    })
}*/

    private fun downloadFile2(context: Context, url: String?, fileName: String?) {
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

        handler.postDelayed(Runnable {
            unzipfile2(fileName)
        }, 3000)
    }

    fun downloadFile(context: Context, url: String?, appfileName: String?) {
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        val directory = getDir("downloads", Context.MODE_PRIVATE)
        val destinationPath = File(directory, appfileName).path
        val finalfile = File(filesDir.absolutePath, "appfileName2")
        val file = File(context.filesDir, appfileName)
        file.createNewFile()

        val directory2 = getExternalFilesDir(null)
        val dirType = Environment.DIRECTORY_DOWNLOADS // Choose the appropriate directory type
        val directoryType = Environment.DIRECTORY_DOWNLOADS
        val appSpecificDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(appfileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            //.setDestinationInExternalPublicDir(file.toString(), appfileName+"2")
          // .setDestinationInExternalPublicDir(appSpecificDir.toString(), appfileName)
         //.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, appfileName)
        // .setDestinationInExternalPublicDir(finalfile.absolutePath, appfileName)
        //  .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, appfileName)

        Log.e("Decompress33", "download succdess")
        downloadManager.enqueue(request)

        handler.postDelayed(Runnable {
            unzipfile2(appfileName)
        }, 3000)
    }

    fun unzipfile2(fileNameZip: String?) {
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
       // df.unzipNew(zipFileName2,destinationPath)

        // df.unpackZip(destinationPath2,zipFileName2)
       // unzipFilePath( zipFileName2,destinationPath )

        Toast.makeText(this@MainActivity, "Unziping file", Toast.LENGTH_SHORT).show()
    }

    fun unzipfile(context: Context, fileNameZip: String?) {
        val zipFileName2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + fileNameZip
        val destinationPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/unzipped" + fileNameZip + "/"
        val destinationPath2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()

        val finalfile = File(filesDir, fileNameZip)

        /* val file = File(filesDir, "appfileName")

         val filePath = file.absolutePath*/
        //Log.d("TAG", "File path: $filePath")
        val finalfile2 = File(filesDir.absolutePath, "appfileName2")
        val unzipLocation: String = getFilesDir().toString()     + "/unzipped"
        val file = File(context.filesDir, fileNameZip)
       // file.createNewFile()
        //  val df = DecompressFast(file.toString()+"/"+fileNameZip, destinationPath)
       // val df = DecompressFast(finalfile2.toString() + "/" + fileNameZip, unzipLocation)
       /* val df = DecompressFast(
            file.toString() + "/" + fileNameZip,
            file.toString()
        )*/
        val df = DecompressFast(
            file.toString(),
            unzipLocation
        )

        df.unzip()

        // unzipFilePath( finalfile2.absolutePath,filePath+"/unzipped" )
        // df.unpackZip(destinationPath2,zipFileName2)

        Toast.makeText(this@MainActivity, "Unziping file", Toast.LENGTH_SHORT).show()

    }

    fun unzipFilePath(zipFilePath: String, destinationFolderPath: String) {
        try {
            val buffer = ByteArray(1024)
            val zipInputStream = ZipInputStream(BufferedInputStream(FileInputStream(zipFilePath)))

            var zipEntry: ZipEntry? = zipInputStream.nextEntry

            while (zipEntry != null) {
                val entryName = zipEntry.name

                // Exclude _MACOSX directory and ._ files
                if (entryName.contains("_MACOSX")) {
                    entryName.replace("_MACOSX", "")
                }
                if (!entryName.contains("_MACOSX") && !entryName.startsWith("._")) {
                    val filePath = destinationFolderPath + File.separator + entryName

                    if (zipEntry.isDirectory) {
                        File(filePath).mkdirs()
                    } else {
                        val outputStream = openFileOutput(filePath, Context.MODE_PRIVATE)
                        val bufferedOutputStream = BufferedOutputStream(outputStream)

                        var bytesRead = zipInputStream.read(buffer)
                        while (bytesRead != -1) {
                            bufferedOutputStream.write(buffer, 0, bytesRead)
                            bytesRead = zipInputStream.read(buffer)
                        }

                        bufferedOutputStream.close()
                    }
                }

                zipEntry = zipInputStream.nextEntry
            }

            zipInputStream.closeEntry()
            zipInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    public fun downloadFromFirebase(appName: String) {
        if (ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT <= 30
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            getUrlFromFirebase(appName)
        }

    }
}