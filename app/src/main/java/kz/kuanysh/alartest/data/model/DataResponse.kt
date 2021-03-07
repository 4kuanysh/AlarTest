package kz.kuanysh.alartest.data.model

import com.google.gson.annotations.SerializedName
import kz.kuanysh.alartest.domain.model.Data as DomainData

data class DataResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("data")
    val `data`: List<Data>
) {

    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lon")
        val lon: Double
    ) {

        fun toDomain() = DomainData.Item(
            id = id,
            name = name,
            country = country,
            latitude = lat,
            longitude = lon,
        )
    }

    fun toDomain() = DomainData(
        status = DomainData.Status.create(status),
        page = page,
        items = `data`.map { it.toDomain() }
    )
}