// OtherActivity.kt
package cam.candy.myruns2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MapEntryActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_entry_activity)

        supportActionBar?.title= getString(R.string.map_activity_title)
    }
}
