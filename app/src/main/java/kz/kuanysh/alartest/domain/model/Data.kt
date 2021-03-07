package kz.kuanysh.alartest.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Data(
    val status: Status,
    val page: Int,
    val items: List<Item>
) {

    @Parcelize
    data class Item(
        val id: String,
        val name: String,
        val country: String,
        val latitude: Double,
        val longitude: Double,
    ) : Parcelable

    enum class Status {
        OK, UNKNOWN;

        companion object {
            fun create(value: String) = when (value) {
                "ok" -> OK
                else -> UNKNOWN
            }
        }
    }
}
