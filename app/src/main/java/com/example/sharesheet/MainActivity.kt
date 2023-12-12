package com.example.sharesheet

import android.app.PendingIntent
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.service.chooser.ChooserAction
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private var imageUri: Uri? = null
    private val metadataArrayList: ArrayList<String?> = arrayListOf()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize components
        imageView = findViewById(R.id.image_view)
        button  =findViewById(R.id.share_button)

        // getting Image Uri
        imageUri = getDrawableUri(imageView.drawable)

        // getting metadata of image and string them into ArrayList
        imageUri?.let {
            getImageMetadata(it)
        }


        val chooserActions = shareSheetActions(this)
        val chooserIntent = buildChooserIntent(chooserActions)



       button.setOnClickListener {
            startActivity(chooserIntent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun shareSheetActions(context: Context): Array<ChooserAction> {
        val metadataAction = ChooserAction.Builder(
            Icon.createWithResource(
                context,
                com.google.android.material.R.drawable.ic_search_black_24
            ),
            "Metadata",
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MetadataActivity::class.java).putExtra("metadata", metadataArrayList),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )
        ).build()

        val copyLinkAction = ChooserAction.Builder(
            Icon.createWithResource(context, R.drawable.copy_icon),
            "Copy link",
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(
                    context,
                    CopyToClipboardBroadcastReceiver::class.java
                ).putExtra(Intent.EXTRA_TEXT, imageUri.toString()),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )
        ).build()

        return arrayOf(copyLinkAction, metadataAction)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun buildChooserIntent(chooserActions: Array<ChooserAction>): Intent {
        val myIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            clipData = ClipData.newRawUri(null, imageUri)
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooserIntent = Intent.createChooser(myIntent, null).apply {
            putExtra(Intent.EXTRA_CHOOSER_CUSTOM_ACTIONS, chooserActions)
        }
        return chooserIntent
    }

    private fun getDrawableUri(drawable: Drawable): Uri? {
        val bitmapDrawable: BitmapDrawable = drawable as BitmapDrawable

        val bitmap = bitmapDrawable.bitmap

        val folder = File(cacheDir, "images")

        folder.mkdir()

        val file = File(folder, "image.jpg")


        val fileOutputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        return FileProvider.getUriForFile(this, packageName, file)
    }


    private fun getImageMetadata(uri: Uri) {
        try {

            contentResolver.openInputStream(uri).use { inputStream ->
                val exif: ExifInterface? = inputStream?.let { ExifInterface(it) }

                val width = exif?.getAttributeInt(
                    ExifInterface.TAG_IMAGE_WIDTH,
                    -1
                )

                val length = exif?.getAttributeInt(
                    ExifInterface.TAG_IMAGE_LENGTH, -1
                )

                val orientation = exif?.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    -1
                )

                metadataArrayList.add(width.toString())
                metadataArrayList.add(length.toString())
                metadataArrayList.add(orientation.toString())
            }
        } catch (e: IOException) {
            Log.d("orientation", "e : $e")

            e.printStackTrace()
        }
    }
}


