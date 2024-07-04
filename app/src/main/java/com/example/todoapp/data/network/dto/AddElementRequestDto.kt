package com.example.todoapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class AddElementRequestDto(
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val changedAt: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String
)