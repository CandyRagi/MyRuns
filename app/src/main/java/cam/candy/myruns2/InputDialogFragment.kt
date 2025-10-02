package cam.candy.myruns2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

class InputDialogFragment : DialogFragment() {

    companion object {
        private const val REQUEST_KEY = "input_dialog_result"
        private const val ARG_TYPE = "type"
        private const val ARG_DIALOG_TAG = "dialog_tag"

        // FIXED: Accept both type and dialogTag as parameters
        fun newInstance(type: String, dialogTag: String): InputDialogFragment {
            val frag = InputDialogFragment()
            val args = Bundle()
            args.putString(ARG_TYPE, type)
            args.putString(ARG_DIALOG_TAG, dialogTag)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputType = arguments?.getString(ARG_TYPE)
        val dialogTag = arguments?.getString(ARG_DIALOG_TAG) ?: ""

        return when (inputType) {
            // 📅 DATE PICKER
            "date" -> {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(),
                    { _: DatePicker, year: Int, month: Int, day: Int ->
                        val result = Bundle().apply {
                            putString("value", "$day/${month + 1}/$year")
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                )
            }

            // ⏰ TIME PICKER
            "time" -> {
                val c = Calendar.getInstance()
                TimePickerDialog(
                    requireContext(),
                    { _: TimePicker, hour: Int, minute: Int ->
                        val result = Bundle().apply {
                            putString("value", String.format("%02d:%02d", hour, minute))
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                    },
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    true
                )
            }

            // 🔢 FLOAT INPUT
            "float" -> {
                val input = EditText(requireContext())
                input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                input.hint = ""
                AlertDialog.Builder(requireContext())
                    .setTitle("Enter Value")
                    .setView(input)
                    .setPositiveButton("OK") { _, _ ->
                        val result = Bundle().apply {
                            putString("value", input.text.toString())
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
            }

            // 🔢 NUMBER INPUT
            "number" -> {
                val input = EditText(requireContext())
                input.inputType = InputType.TYPE_CLASS_NUMBER
                input.hint = ""
                AlertDialog.Builder(requireContext())
                    .setTitle("Enter Number")
                    .setView(input)
                    .setPositiveButton("OK") { _, _ ->
                        val result = Bundle().apply {
                            putString("value", input.text.toString())
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
            }

            // ✍️ TEXT INPUT
            "text" -> {
                val input = EditText(requireContext())
                input.inputType = InputType.TYPE_CLASS_TEXT
                input.hint = ""
                AlertDialog.Builder(requireContext())
                    .setTitle("Enter Text")
                    .setView(input)
                    .setPositiveButton("OK") { _, _ ->
                        val result = Bundle().apply {
                            putString("value", input.text.toString())
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
            }

            // 🖼️ CHANGE PHOTO PICKER
            "photo" -> {
                val options = arrayOf("Open Camera", "Select from Gallery")
                AlertDialog.Builder(requireContext())
                    .setTitle("Pick Profile Picture")
                    .setItems(options) { _, which ->
                        val choice = if (which == 0) "camera" else "gallery"
                        val result = Bundle().apply {
                            putString("value", choice)
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                    }
                    .create()
            }

            // 🚻 GENDER PICKER
            "gender" -> {
                val options = arrayOf("Male", "Female", "Other")
                AlertDialog.Builder(requireContext())
                    .setTitle("Select Gender")
                    .setSingleChoiceItems(options, -1) { dialog, which ->
                        val result = Bundle().apply {
                            putString("value", options[which])
                            putString("dialog_tag", dialogTag)
                        }
                        setFragmentResult(REQUEST_KEY, result)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
            }

            else -> {
                // Fallback dialog with more information for debugging
                AlertDialog.Builder(requireContext())
                    .setTitle("Input Error")
                    .setMessage("Unknown input type: $inputType")
                    .setPositiveButton("OK", null)
                    .create()
            }
        }
    }
}