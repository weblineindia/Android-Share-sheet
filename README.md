# Android Sharesheet with Custom Actions

Demo project of Android 14 Sharesheet custom actions.
 
 
## Table of contents
- [Android Support](#android-support)
- [Demo](#demo)
- [Features](#features)
- [Getting started](#getting-started)
- [Usage](#usage)
- [Methods](#methods)
- [Want to Contribute?](#want-to-contribute)
- [Need Help / Support?](#need-help)
- [Collection of Components](#collection-of-Components)
- [Changelog](#changelog)
- [License](#license)
- [Keywords](#Keywords)


## Android Support

Android Version - Android 14

Android Studio Giraffe | 2022.3.1 Patch 4

We have tested our program in Android 14 version. You can not use it in below versions.


## Demo
![](./Share_Sheet_Custom_Action_Demo.gif)


## Features

* Android 14 Feature Android Sharesheet with custom Action. With Sharesheet lets users share information with the right person, with relevant app suggestions, all with a single tap.


## Getting started

* Download this sample project and import activity in your Android App. 
* Update UI based on your requirements. 


## Usage

Setup process is described below to integrate in sample project.

### Methods
    
Annotations
    
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)

Create BroadcastReceiver to recieve Copy Action intent and write below code
    
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val uri = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (uri!=null) {
                    val clipboardManager = context?.getSystemService(ClipboardManager::class.java)
                    val clipData = ClipData.newPlainText(null, uri)
                    clipboardManager?.setPrimaryClip(clipData)
                }
            }
        }
        
Write below code to get Uri of the image

        val bitmapDrawable: BitmapDrawable = imageView.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val folder = File(cacheDir, "images")
        folder.mkdir()
        val file = File(folder, "image.jpg")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    
        val imageUri = FileProvider.getUriForFile(this, packageName, file)
        
Create Sharesheet intent 

        val myIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            clipData = ClipData.newRawUri(null, imageUri)
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

Create Sharesheet actions

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
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
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
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
        ).build()

        val chooserActions:Array<ChooserActiuon> = arrayOf(copyLinkAction, metadataAction)

Pass actions to Sharesheet intent
    
        val chooserIntent = Intent.createChooser(myIntent, null).apply {
            putExtra(Intent.EXTRA_CHOOSER_CUSTOM_ACTIONS, chooserActions)
        }
        
        startActivity(chooserIntent)
    
Write below code to get image metadata

        val metaDataArrayList: ArrayList<String?> = arrayListOf()
        
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
            
Write below code to get metadata from Share Sheet action
    
        val array = intent.getStringArrayListExtra("metadata")


## Want to Contribute?

* Created something awesome, made this code better, added some functionality, or whatever (this is the hardest part).
* [Fork it](http://help.github.com/forking/).
* Create new branch to contribute your changes.
* Commit all your changes to your branch.
* Submit a [pull request](http://help.github.com/pull-requests/).

 
## Collection of Components
 We have built many other components and free resources for software development in various programming languages. Kindly click here to view our [Free Resources for Software Development.](https://www.weblineindia.com/software-development-resources.html)
 

## Changelog
Detailed changes for each release are documented in [CHANGELOG](./CHANGELOG).


## License
[MIT](LICENSE)

[mit]: ./LICENSE


## Keywords
Android Share Sheet, Share Sheet, Share Sheet Custom Action