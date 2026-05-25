package com.radhavallabh.naamsmaran.data.backup

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object AppBackupCodec {
    private val gson: Gson = GsonBuilder().create()

    fun toJson(payload: AppBackupPayload): String = gson.toJson(payload)

    fun fromJson(json: String): AppBackupPayload =
        gson.fromJson(json, AppBackupPayload::class.java)
}
