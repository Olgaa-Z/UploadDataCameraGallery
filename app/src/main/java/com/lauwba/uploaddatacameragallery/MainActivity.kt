package com.lauwba.uploaddatacameragallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class MainActivity : CameraFilePickerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fotofromcamera.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                launchCamera()
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
                }
            }
        }

        fotofromgallery.setOnClickListener {
            launchFilePicker()
        }

        simpan.setOnClickListener {
            if(flagUpload){
                doUpload()
            }else{
                Toast.makeText(this@MainActivity, "Silahkan pilih file ! ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doUpload() {
        //buat inputan untuk file dengan mime type yang diterima adalah file image
        val files = RequestBody.create("image/*".toMediaTypeOrNull(), imageViewToByte(imagePreview, 40))

        //membuat body untuk input type file
        val imageInput = MultipartBody.Part.createFormData("files", "${System.nanoTime()}.jpg", files)

        //membuat input type text dengan value dari judul.text.toString()
        val namawisata = RequestBody.create("text/plain".toMediaTypeOrNull(), namawisata.text.toString())

        val deskripsi = RequestBody.create("text/plain".toMediaTypeOrNull(), deskripsi.text.toString())

        val alamat = RequestBody.create("text/plain".toMediaTypeOrNull(), alamat.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //preview foto
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            data?.data?.let { getMetaData(it, imagePreview) }
            flagUpload = true
        } else if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            previewImage(imagePreview)
        }
    }

    //request pemission decline or allow
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 200){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                launchCamera()
            }
        }
    }

}
