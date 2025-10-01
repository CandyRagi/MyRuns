package cam.candy.myruns2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class FragmentA : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)

        // --- First Spinner ---
        val spinner1: Spinner = view.findViewById(R.id.mySpinner)
        val inputOptions = resources.getStringArray(R.array.input_type);

        val adapter1 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            inputOptions
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selected = inputOptions[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // --- Second Spinner ---
        val spinner2: Spinner = view.findViewById(R.id.mySpinner2)
        val activityOptions = resources.getStringArray(R.array.activity_type);

        val adapter2 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            activityOptions
        )
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selected = activityOptions[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val startButton: Button = view.findViewById(R.id.startButton)
        startButton.setOnClickListener {
            val selectedInput = spinner1.selectedItem.toString()

            if (selectedInput.equals("Manual Entry", ignoreCase = true)) {
                val intent = Intent(requireContext(), ManualEntryActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), MapEntryActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }
}
