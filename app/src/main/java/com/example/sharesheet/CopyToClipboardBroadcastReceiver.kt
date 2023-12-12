package com.example.sharesheet

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

class CopyToClipboardBroadcastReceiver:BroadcastReceiver() {

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
}