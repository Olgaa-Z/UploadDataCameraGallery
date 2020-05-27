package com.lauwba.uploaddatacameragallery

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.File

open class CameraFilePickerBaseActivity: AppCompatActivity()  {

    private var fileUri : Uri? = null
    private val FOLDER_NAME = "wisata"
    var realPath : String? = null
    var flagUpload = false

    fun launchCamera(){
        val intentCamera = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = getOutputMediaFileUri(1000)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

        startActivityForResult(intentCamera, 1001)
    }

    private fun getOutputMediaFileUri(type:Int): Uri? {
        return getOutputMediaFile(type)?.let {
            FileProvider.getUriForFile(this@CameraFilePickerBaseActivity, BuildConfig.APPLICATION_ID + ".provider", it)
        }
    }

    private fun getOutputMediaFile(type: Int): File? {
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME)

        if (!folder.exists()){
            if(!folder.mkdirs()){
                return null
            }
        }

        val mediaFile = File(folder.path + File.separator + "IMG" + System.nanoTime() + ".jpg")
        realPath = mediaFile.toString()
        return mediaFile
    }

    fun launchFilePicker(){
        val intentPicker = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentPicker.apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            type= "image/*"
        }

        startActivityForResult(intentPicker, 1002)
    }

    fun getMetaData(uri : Uri, imageView: ImageView){
        val c = contentResolver.query(uri, null, null, null,null)
        if(c?.moveToFirst()== true){
            imageView.setImageURI(uri)
        }else{
            Toast.makeText(this, "Uri tidak valid", Toast.LENGTH_SHORT).show()
        }

        c?.close()
    }

    fun previewImage(imageView: ImageView){
        Glide.with((this)).load(realPath).into(imageView)
    }

    fun imageViewToByte(imagePreview: ImageView, quality: Int): ByteArray {
        //ambil gambar dari imageview as bitmap
        val bmp = (imagePreview.drawable as BitmapDrawable).bitmap

        //buat object dari bytearratoutstream
        //variable ini yang dipakai untuk ubah bitmap ke bytearray
        val bs = ByteArrayOutputStream()

        //ini proses compress. Dalam hal ini kompres ke JPG dengan quality 100
        bmp.compress(Bitmap.CompressFormat.PNG, quality, bs)

        //variable untuk menampung bytearray dari bitmap
        val imageInByte = bs.toByteArray()

        return imageInByte
    }
}