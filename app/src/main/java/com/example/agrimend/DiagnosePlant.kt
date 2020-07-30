package com.example.agrimend

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_diagnose_plant.*
import java.io.IOException

class DiagnosePlant : AppCompatActivity() {
    private lateinit var mBitmap: Bitmap
    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2
    private val mInputSize = 256

    private fun scaleImage(bitmap: Bitmap?): Bitmap {
        val orignalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / orignalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = data.extras!!.get("data") as Bitmap
                mBitmap = scaleImage(mBitmap)
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == mGalleryRequestCode) {
            if (data != null) {
                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                println("Success!!!")
                mBitmap = scaleImage(mBitmap)
            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()
        }
        val intent = Intent(this, DetailsTreatment::class.java)
        intent.putExtra("image", mBitmap)
        startActivity(intent)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose_plant)

        mCameraButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCameraIntent, mCameraRequestCode)
        }

        mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = FirebaseAuth.getInstance()
        val current = user.currentUser
        when(item.itemId){
            R.id.news-> {
                startActivity(Intent(this, home_news::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }

            R.id.userProfile-> if (current !=null){
                startActivity(Intent(this,profileUser::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }else{
                startActivity(Intent(this,login_page::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }
            R.id.dissComm-> if (current !=null){
                startActivity(Intent(this,PostsLists::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }else{
                startActivity(Intent(this,login_page::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }
            R.id.news-> {
                startActivity(Intent(this, home_news::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }
            R.id.comm-> if (current !=null){
                startActivity(Intent(this,CommunityHome::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }else{
                startActivity(Intent(this,login_page::class.java))
                this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }
            R.id.testPlant-> return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
}