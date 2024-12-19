package com.hazal.socialapp.data.remote.model
import com.google.gson.annotations.SerializedName

class PlacePhotosResponse : ArrayList<PlacePhotoResponseItem>()

data class PlacePhotoResponseItem(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("prefix")
    val prefix: String?,
    @SerializedName("suffix")
    val suffix: String?,
    @SerializedName("width")
    val width: Int?
) {
    val imageUrl: String
        get() = "${prefix}original${suffix}"
}