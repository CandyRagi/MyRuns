package cam.candy.myruns2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileOutputStream

class UserProfileActivity : AppCompatActivity() {

    // Vars for storing data
    private lateinit var profileImageView: ImageView
    private lateinit var changePhotoButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var editClass: EditText
    private lateinit var editMajor: EditText
    private lateinit var genderGroup: RadioGroup

    private lateinit var tempImgUri: Uri
    private lateinit var myViewModel: MyViewModel
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private lateinit var galleryResult: ActivityResultLauncher<Intent>

    private val tempImgFileName = "default_profile.jpg"
    private val savedImgFileName = "saved_profile.jpg"
    private val prefsName = "UserProfilePrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView)
        changePhotoButton = findViewById(R.id.changePhotoButton)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        editClass = findViewById(R.id.editClass)
        editMajor = findViewById(R.id.editMajor)
        genderGroup = findViewById(R.id.genderGroup)

        // Prepare file URI for camera
        val tempImgFile = File(getExternalFilesDir(null), tempImgFileName)
        tempImgUri = FileProvider.getUriForFile(this, "cam.candy.myruns2", tempImgFile)

        // Initialize ViewModel
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.userImage.observe(this) { bitmap ->
            bitmap?.let {
                profileImageView.setImageBitmap(it)
            }
        }

        // Register camera result
        cameraResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                myViewModel.userImage.value = bitmap
            }
        }

        // Register gallery result
        galleryResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    val bitmap = Util.getBitmap(this, it)
                    myViewModel.userImage.value = bitmap
                }
            }
        }

        // Set up fragment result listener for dialog results
        supportFragmentManager.setFragmentResultListener("input_dialog_result", this) { requestKey, bundle ->
            if (requestKey == "input_dialog_result") {
                val value = bundle.getString("value")
                val tag = bundle.getString("dialog_tag")

                // Handle photo dialog specifically
                if (tag == "changePhotoDialog") {
                    value?.let { handleDialogResult(it) }
                }
            }
        }

        // If temp image exists, show it
        if (tempImgFile.exists()) {
            val bitmap = Util.getBitmap(this, tempImgUri)
            profileImageView.setImageBitmap(bitmap)
        }

        loadProfileData()

        // Show dialog when Change Photo is clicked - FIXED: Added dialogTag parameter
        changePhotoButton.setOnClickListener {
            InputDialogFragment.newInstance("photo", "changePhotoDialog").show(supportFragmentManager, "changePhotoDialog")
        }

        // SAVE button
        saveButton.setOnClickListener {
            saveProfileData()
            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
            finish()
        }

        // CANCEL button
        cancelButton.setOnClickListener {
            clearForm()
            Toast.makeText(this, "Cancelled!!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handleDialogResult(value: String) {
        when (value) {
            "camera" -> openCamera()
            "gallery" -> openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
        cameraResult.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }

    private fun saveProfileData() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString("name", editName.text.toString().trim())
        editor.putString("email", editEmail.text.toString().trim())
        editor.putString("phone", editPhone.text.toString().trim())
        editor.putString("class", editClass.text.toString().trim())
        editor.putString("major", editMajor.text.toString().trim())

        val selectedGenderId = genderGroup.checkedRadioButtonId
        if (selectedGenderId != -1) {
            val gender = findViewById<RadioButton>(selectedGenderId).text.toString()
            editor.putString("gender", gender)
        }

        myViewModel.userImage.value?.let { bitmap ->
            val file = File(getExternalFilesDir(null), savedImgFileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            editor.putString("imagePath", file.absolutePath)
        }

        editor.apply()
    }

    private fun loadProfileData() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        editName.setText(prefs.getString("name", ""))
        editEmail.setText(prefs.getString("email", ""))
        editPhone.setText(prefs.getString("phone", ""))
        editClass.setText(prefs.getString("class", ""))
        editMajor.setText(prefs.getString("major", ""))

        val gender = prefs.getString("gender", "")
        if (gender == "Male") {
            genderGroup.check(R.id.radioMale)
        } else if (gender == "Female") {
            genderGroup.check(R.id.radioFemale)
        }

        val imagePath = prefs.getString("imagePath", null)
        if (imagePath != null) {
            val file = File(imagePath)
            if (file.exists()) {
                val bitmap = Util.getBitmap(this, Uri.fromFile(file))
                profileImageView.setImageBitmap(bitmap)
                myViewModel.userImage.value = bitmap
            }
        } else {
            profileImageView.setImageResource(R.drawable.default_profile)
        }
    }

    private fun clearForm() {
        // Optional: Clear form fields if needed
        // editName.text.clear()
        // editEmail.text.clear()
        // editPhone.text.clear()
        // editClass.text.clear()
        // editMajor.text.clear()
        // genderGroup.clearCheck()
        // profileImageView.setImageResource(R.drawable.default_profile)
    }
}