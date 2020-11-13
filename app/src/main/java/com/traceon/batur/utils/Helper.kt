package com.traceon.batur.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.traceon.batur.R
import com.traceon.batur.data.response.ResponseLogin
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

object Helper {

    fun setRealmConfig(dbName: String?): RealmConfiguration {
        return RealmConfiguration.Builder().name(dbName.toString()).schemaVersion(1)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
    }

    //**********************************************************************************************
    fun getBool(key: String, context: Context): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key, false)
    }

    fun saveBool(key: String, value: Boolean?, context: Context?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun getString(key: String?, context: Context, defaults: String?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, defaults)
    }

    fun saveString(key: String?, value: String?, context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getInteger(key: String, context: Context, valueDef: Int): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt(key, valueDef)
    }

    fun saveInteger(key: String, value: Int?, context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putInt(key, value!!)
        editor.apply()
    }

    fun clearPreferences(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
    //**********************************************************************************************

    fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun setToolbarIconColor(context: Context, icon: Int): Drawable {
        var drawable =
            ResourcesCompat.getDrawable(context.resources, icon, null)
        drawable = DrawableCompat.wrap(drawable!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(drawable, context.getColor(R.color.colorPrimary))
        }
        return drawable
    }

    fun getSesiLogin(context: Context): ResponseLogin {
        val empty = ResponseLogin(
            "",
            0,
            "",
            0,
            "",
            "",
            "",
            0,
            "",
            "",
            "",
            0,
            "",
            "",
            false,
            "",
            0,
            "",
            0,
            "",
            "",
            0
        )
        return Gson().fromJson(
            getString(AppConstant.LOGIN, context, Gson().toJson(empty)),
            ResponseLogin::class.java
        )
    }

    fun setLightStatusBar(view: View, activity: Activity, on: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            val window = activity.window
            if (on) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
                window.statusBarColor = Color.TRANSPARENT
            } else {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                view.systemUiVisibility = flags
                window.clearFlags(flags)
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun setStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun darkMode(isDark: Boolean, now: Boolean, context: Context) {
        if (isDark) {
            if (now) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        } else {
            if (now) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun pesanBox(context: Context, title: String, message: String) {
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(title)
            .setContentText(message)
            .show()
    }

    fun onFetchBaseline(context: Context, url: String, path: String) {
        val file = url.substring(url.lastIndexOf("/") + 1)
        Log.d("DL", "URL : $url")
        Thread {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap?>(100, 100) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        saveImage(context, resource, file, path)
                    }
                })
        }.start()
    }

    private fun saveImage(context: Context, image: Bitmap, file: String, path: String): String? {
        var savedImagePath: String? = null
        val storageDir = File(Environment.DIRECTORY_PICTURES.plus("/").plus(path))

        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, file)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath, context)
            Log.d("DL", "IMAGE SAVED TO: $savedImagePath")
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?, context: Context) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath.toString())
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }

    fun setNotif(
        context: Context,
        channelId: String,
        notifId: Int,
        title: String,
        message: String,
        intermediate: Boolean
    ) {
        val ncb = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setProgress(0, 0, intermediate)
            .setSmallIcon(R.drawable.ic_notifications)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc = NotificationChannel(
                channelId,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nc.enableLights(true)
            nc.enableVibration(true)

            ncb.setChannelId(channelId)
            nm.createNotificationChannel(nc)
        }
        nm.notify(notifId, ncb.build())
    }

    fun formatCapacity(byte: Double, digits: Int): String? {
        var bytes = byte
        val dictionary = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
        var index = 0
        while (index < dictionary.size) {
            if (bytes < 1024) {
                break
            }
            bytes /= 1024
            index++
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index]
    }

    fun spaceDisk(context: Context?): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return Formatter.formatFileSize(context, availableBlocks * blockSize)
    }

}