package `in`.codeandroid.readingdatabase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        val dbHandler = DBHandler(this)
        dbHandler.getWritableDb()
        btn_launch_list.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
