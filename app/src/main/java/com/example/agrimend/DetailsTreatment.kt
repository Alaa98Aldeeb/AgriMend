package com.example.agrimend

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_details_treatment.*

class DetailsTreatment : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val reference = firebaseDatabase.getReference("Treatments")
    private lateinit var mClassifier: Classifier
    private val mInputSize = 256
    private val mModelPath = "generated.tflite"
    private val mLabelPath = "lables.txt"

    private lateinit var mBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_treatment)

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mClassifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
        val intent = getIntent()
        mBitmap = intent.getParcelableExtra("image")
        mPhotoImageView.setImageBitmap(mBitmap)

        val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
        results?.let {
            getTreatment(results)
    }
        openCommunity.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PostsLists::class.java))
            this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        })

        checkAnotherPlant.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DiagnosePlant::class.java))
            this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        })
}
    @SuppressLint("SetTextI18n")
    private fun getTreatment(results: Classifier.Recognition) {
        if (results.title.equals("Tomato___Early_blight")){
            DiseaseName.text = "Early Blight"
            reference.child("Early Blight").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }
            })
             }else if (results.title.equals("Tomato___Late_blight")){
            DiseaseName.text = "Late Blight"
            reference.child("Late Blight").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }

            })

        }else if (results.title.equals("Tomato___Leaf_Mold")){
            DiseaseName.text = "Leaf Mold"
            reference.child("Leaf Mold").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").value.toString()
                }
            })

        }else if (results.title.equals("Tomato___Septoria_leaf_spot")){
            DiseaseName.text = "Septoria Leaf Spot"
            reference.child("Septoria Leaf Spot").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString()
                }
            })

        }else if (results.title.equals("Tomato___Spider_mites Two-spotted_spider_mite")){
            DiseaseName.text = "Spider Mites"
            reference.child("Spider Mites").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }
            })
        }else if (results.title.equals("Tomato___Target_Spot")){
            DiseaseName.text = "Target Spot"
            reference.child("Target Spot").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }
            })
        }else if (results.title.equals("Tomato___Tomato_mosaic_virus")){
            DiseaseName.text = "Mosaic Virus"
            reference.child("Mosaic Virus").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }
            })
        }else if (results.title.equals("Tomato___healthy")){
            choose_treat.text = "Your Plant is Healthy."
            DiseaseName.text = "Healthy Plant"
            generalTreat.text = "No Treatment, Plant is Healthy."
            chemicalTreat.text = "No Treatment, Plant is Healthy."

        }else if (results.title.equals("Tomato___Tomato_Yellow_Leaf_Curl_Virus")){
            DiseaseName.text = "Yellow Leaf"
            reference.child("Yellow Leaf Curl Virus").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }
            })
            }else if (results.title.equals("Tomato___Bacterial_spot")){
            DiseaseName.text = "Bacterial Spot"
            reference.child("Bacterial Spot").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    generalTreat.text = p0.child("General Control").value.toString()
                    chemicalTreat.text = p0.child("Chemical Control").child("1").value.toString() + "\n" +
                            p0.child("Chemical Control").child("2").value.toString() + "\n" +
                            p0.child("Chemical Control").child("3").value.toString()
                }
            })
        }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
    }
