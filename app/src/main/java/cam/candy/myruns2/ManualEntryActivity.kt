package cam.candy.myruns2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manual_entry_activity)

        supportActionBar?.title = getString(R.string.app_name)

        // Map fields
        val dateField = findViewById<TextView>(R.id.dateField)
        val timeField = findViewById<TextView>(R.id.timeField)
        val durationField = findViewById<TextView>(R.id.durationField)
        val distanceField = findViewById<TextView>(R.id.distanceField)
        val caloriesField = findViewById<TextView>(R.id.caloriesField)
        val heartRateField = findViewById<TextView>(R.id.heartRateField)
        val commentField = findViewById<TextView>(R.id.commentField)

        // Set up fragment result listener for all dialogs
        supportFragmentManager.setFragmentResultListener("input_dialog_result", this) { requestKey, bundle ->
            if (requestKey == "input_dialog_result") {
                val value = bundle.getString("value")
                val tag = bundle.getString("dialog_tag")

                value?.let {
                    tag?.let { dialogTag ->
                        handleDialogResult(dialogTag, value)
                    }
                }
            }
        }

        // Hook click listeners - FIXED: Pass both type and dialog_tag in arguments
        dateField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("date", "dateDialog")
            dialog.show(supportFragmentManager, "dateDialog")
        }

        timeField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("time", "timeDialog")
            dialog.show(supportFragmentManager, "timeDialog")
        }

        durationField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("float", "durationDialog")
            dialog.show(supportFragmentManager, "durationDialog")
        }

        distanceField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("float", "distanceDialog")
            dialog.show(supportFragmentManager, "distanceDialog")
        }

        caloriesField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("number", "caloriesDialog")
            dialog.show(supportFragmentManager, "caloriesDialog")
        }

        heartRateField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("number", "heartRateDialog")
            dialog.show(supportFragmentManager, "heartRateDialog")
        }

        commentField.setOnClickListener {
            val dialog = InputDialogFragment.newInstance("text", "commentDialog")
            dialog.show(supportFragmentManager, "commentDialog")
        }

        // Save + Clear
        val saveBtn: Button = findViewById(R.id.saveButton)
        val clearBtn: Button = findViewById(R.id.clearButton)

        saveBtn.setOnClickListener {
            Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
        }

        clearBtn.setOnClickListener {
            listOf(
                dateField, timeField, durationField, distanceField,
                caloriesField, heartRateField, commentField
            ).forEach {
                it.text = it.resources.getResourceEntryName(it.id)
                    .replace("Field", "")
                    .replaceFirstChar { c -> c.uppercase() }
            }
            Toast.makeText(this, "Data Cleared!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDialogResult(dialogTag: String, value: String) {
        when (dialogTag) {
            "dateDialog" -> {
                val dateField = findViewById<TextView>(R.id.dateField)
                dateField.text = "Date"
            }
            "timeDialog" -> {
                val timeField = findViewById<TextView>(R.id.timeField)
                timeField.text = "Time"
            }
            "durationDialog" -> {
                val durationField = findViewById<TextView>(R.id.durationField)
                durationField.text = "Duration"
            }
            "distanceDialog" -> {
                val distanceField = findViewById<TextView>(R.id.distanceField)
                distanceField.text = "Distance"
            }
            "caloriesDialog" -> {
                val caloriesField = findViewById<TextView>(R.id.caloriesField)
                caloriesField.text = "Calories"
            }
            "heartRateDialog" -> {
                val heartRateField = findViewById<TextView>(R.id.heartRateField)
                heartRateField.text = "Heart Rate"
            }
            "commentDialog" -> {
                val commentField = findViewById<TextView>(R.id.commentField)
                commentField.text = "Comment"
            }
        }
    }
}