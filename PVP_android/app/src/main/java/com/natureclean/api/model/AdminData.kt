package com.natureclean.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdminData(
    @Json(name = "total_trash")
    val totalTrash: Int,
    @Json(name = "total_trash_by_size")
    val totalTrashBySize: TotalTrashBySize,
    @Json(name = "total_trash_by_type")
    val totalTrashByType: TotalTrashByType,
    @Json(name = "total_trash_cleaned")
    val totalTrashCleaned: Int,
    @Json(name = "total_trash_cleaned_by_size")
    val totalTrashCleanedBySize: TotalTrashCleanedBySize,
    @Json(name = "total_trash_cleaned_by_type")
    val totalTrashCleanedByType: List<TotalTrashCleanedByType>,
    @Json(name = "total_trash_not_cleaned_by_type")
    val totalTrashNotCleanedByType: List<TotalTrashNotCleanedByType>,
    @Json(name = "total_trash_not_cleaned_by_size")
    val totalTrashNotCleanedBySize: TotalTrashNotCleanedBySize,
    @Json(name = "user_count")
    val userCount: Int,
    @Json(name = "user_total_walked")
    val userTotalWalked: Int
)