package com.jasmeet.e_commerce.activities

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jasmeet.e_commerce.databinding.ActivityNewProductBinding
import com.jasmeet.e_commerce.model.Products
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class NewProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewProductBinding
    private var selectedImages = mutableListOf<Uri>()
    private val selectedColors = mutableListOf<Int>()
    private var productStorage = Firebase.storage.reference
    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedImagesActivityResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data

                //multiple images
                if (intent?.clipData !=null){
                    val count = intent.clipData?.itemCount?:0
                    (0 until count).forEach { index ->
                        val imageUri = intent.clipData?.getItemAt(index)?.uri
                        imageUri?.let { uri ->
                            selectedImages.add(uri)
                        }
                    }
                }else{
                    //single image
                    val imageUri = intent?.data
                    imageUri?.let { uri ->
                        selectedImages.add(uri)
                    }
                    updateImages()
                }
            }
        }

        binding.buttonImagePicker.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.type = "image/*"
            selectedImagesActivityResult.launch(intent)

        }

        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Select Color")
                .setPositiveButton("Select",object :ColorEnvelopeListener{
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {
                            selectedColors.add(it.color)
                            updateColors()
                        }
                    }
                })
                .setNegativeButton("Cancel"){colorPickerDialog ,_ ->
                    run {
                        colorPickerDialog.dismiss()
                    }

                }.show()

        }

        binding.uploadBtn.setOnClickListener {
            val productName = binding.etName.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()  //optional
            val price = binding.etPrice.text.toString().trim()
            val offerPercentage = binding.etPercentage.text.toString().trim() //optional
            val size = binding.etSize.text.toString().trim()  //optional

            validateProductDetails(productName, category, description, price, offerPercentage, size, selectedImages)
        }
    }

    private fun updateImages() {
        binding.tvSelectedImages.text = selectedImages.size.toString()
    }

    private fun updateColors() {
        var colors = ""
        selectedColors.forEach {
            colors = "$colors ${Integer.toHexString(it)}"
        }
        binding.tvSelectedColors.text = colors
    }

    private fun validateProductDetails(
        productName: String,
        category: String,
        description: String,
        price: String,
        offerPercentage: String,
        size: String,
        selectedImages: MutableList<Uri>
    ) {
        if (productName.isEmpty()) {
            binding.etName.error = "Product name is required"
            binding.etName.requestFocus()
        } else if (category.isEmpty()) {
            binding.etCategory.error = "Category is required"
            binding.etCategory.requestFocus()
        } else if (price.isEmpty()) {
            binding.etPrice.error = "Product price is required"
            binding.etPrice.requestFocus()
        } else if (selectedImages.isEmpty()) {
            Snackbar.make(binding.root, "Please select product image", Snackbar.LENGTH_SHORT).show()
        }
        else {
            //upload product details to firebase
            saveProduct()
        }
    }

    private fun saveProduct() {
        val productName = binding.etName.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()               //optional
        val price = binding.etPrice.text.toString().trim()
        val offerPercentage = binding.etPercentage.text.toString().trim()            //optional
        val size = getSizeList(binding.etSize.text.toString().trim())                //optional

        val imageByteArrays = getImagesByteArrays()
        val images = mutableListOf<String>()

        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                showLoading()
            }

            try {
                async {
                    imageByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imageStorage = productStorage.child("products/images/$id")
                            val result = imageStorage.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }.await()

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    hideLoading()
                    Snackbar.make(binding.root,"Error: ${e.message}",Snackbar.LENGTH_SHORT).show()
                }


            }
            val product = Products(
                UUID.randomUUID().toString(),
                productName ,
                category ,
                price.toInt(),
                if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                if (description.isEmpty()) null else description,
                if(selectedColors.isEmpty()) null else selectedColors,
                size,
                images
            )
            firestore.collection("products").add(product).addOnSuccessListener {
                hideLoading()
                Snackbar.make(binding.root,"Product uploaded successfully",Snackbar.LENGTH_SHORT).show()

            }.addOnFailureListener {
                hideLoading()
                Snackbar.make(binding.root,"Error: ${it.message}",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideLoading() {
        binding.spinKit.visibility = View.GONE
    }

    private fun showLoading() {
        binding.spinKit.visibility = View.VISIBLE
    }

    private fun getImagesByteArrays(): List<ByteArray> {
        val imageByteArrays = mutableListOf<ByteArray>()
        selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            if (imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                imageByteArrays.add(stream.toByteArray())
            }
        }
        return imageByteArrays
    }

    private fun getSizeList(sizeStr: String): List<String>? {
        if (sizeStr.isEmpty())
            return null
        return sizeStr.split(",")

    }




}