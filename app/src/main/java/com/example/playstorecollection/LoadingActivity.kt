import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.playstorecollection.R

class LoadingActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Delay for 3 seconds and then finish this activity
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3000)
    }
}
