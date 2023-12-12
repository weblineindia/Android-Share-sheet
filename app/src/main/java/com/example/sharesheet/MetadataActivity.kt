package com.example.sharesheet

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MetadataActivity : AppCompatActivity() {

    private lateinit var heightTextView: TextView
    private lateinit var lengthTextView: TextView
    private lateinit var orientationTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metadata)

        heightTextView = findViewById(R.id.heightTv)
        lengthTextView = findViewById(R.id.lengthTv)
        orientationTextView = findViewById(R.id.orientationTv)
        val array = intent.getStringArrayListExtra("metadata")

        if (array != null) {
            Log.d("metadataArray", array.toString())
            heightTextView.text = "Height : " + array[0]
            lengthTextView.text = "Length : " + array[1]
           orientationTextView.text = "Orientation : " + array[2]
        }
    }
}