package cam.candy.myruns2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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


        // Hook click listeners
        dateField.setOnClickListener {
            showInputDialog("Select Date", "date") { value -> dateField.text = "Date: $value" }
        }
        timeField.setOnClickListener {
            showInputDialog("Select Time", "time") { value -> timeField.text = "Time: $value" }
        }
        durationField.setOnClickListener {
            showInputDialog("Enter Duration", "float") { value -> durationField.text = "Duration: $value" }
        }
        distanceField.setOnClickListener {
            showInputDialog("Enter Distance", "float") { value -> distanceField.text = "Distance: $value" }
        }
        caloriesField.setOnClickListener {
            showInputDialog("Enter Calories", "number") { value -> caloriesField.text = "Calories: $value" }
        }
        heartRateField.setOnClickListener {
            showInputDialog("Enter Heart Rate", "number") { value -> heartRateField.text = "Heart Rate: $value" }
        }
        commentField.setOnClickListener {
            showInputDialog("Enter Comment", "text") { value -> commentField.text = "Comment: $value" }
        }

        // Save + Clear
        val saveBtn: Button = findViewById(R.id.saveButton)
        val clearBtn: Button = findViewById(R.id.clearButton)

        saveBtn.setOnClickListener {
            Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
        }

        clearBtn.setOnClickListener {
            listOf(dateField, timeField, durationField, distanceField, caloriesField, heartRateField, commentField)
                .forEach { it.text = it.resources.getResourceEntryName(it.id).replace("Field", "").replaceFirstChar { c -> c.uppercase() } }
            Toast.makeText(this, "Data Cleared!", Toast.LENGTH_SHORT).show()
        }
    }

    // Reusable dialog
    private fun showInputDialog(title: String, type: String, onValueSet: (String) -> Unit) {
        when (type) {
            "date" -> {
                val picker = DatePickerDialog(
                    this,
                    { _, _, _, _ -> /* do nothing */ },
                    2024, 0, 1 // year, month, day (pick any defaults you want)
                )
                picker.show()
            }
            "time" -> {
                val picker = TimePickerDialog(this, null, 12, 0, true)
                picker.show()
            }
            "float" -> {
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setView(input)
                    .setPositiveButton("OK",null)
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            "number" -> {
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_NUMBER
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setView(input)
                    .setPositiveButton("OK",null)
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            "text" -> {
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setView(input)
                    .setPositiveButton("OK",null)
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }
}
