package com.g.tragosapp.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName




data class Cocktail(
    @SerializedName("idDrink")
    val cocktailId: String = "",
    @SerializedName("strDrinkThumb")
    val image: String = "",
    @SerializedName("strDrink")
    val name: String = "",
    @SerializedName("strInstructions")
    val description: String = "",
    @SerializedName("strAlcoholic")
    val hasAlcohol: String = "Non_Alcoholic",
    var isFav: Boolean = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Cocktail> {
        override fun createFromParcel(parcel: Parcel): Cocktail {
            return Cocktail(parcel)
        }

        override fun newArray(size: Int): Array<Cocktail?> {
            return arrayOfNulls(size)
        }
    }
}

data class CocktailList(
    @SerializedName("drinks")
    val cocktailList: List<Cocktail> = listOf()
)

@Entity(tableName = "cocktailTable")
data class CocktailEntity(
    @PrimaryKey
    val cocktailId: String,
    @ColumnInfo(name = "trago_imagen")
    val image: String = "",
    @ColumnInfo(name = "trago_nombre")
    val name: String = "",
    @ColumnInfo(name = "trago_descripcion")
    val description: String = "",
    @ColumnInfo(name = "trago_has_alcohol")
    val hasAlcohol: String = "Non_Alcoholic",

)

@Entity(tableName = "favoritesTable")
data class FavoritesEntity(
    @PrimaryKey
    val cocktailId: String,
    @ColumnInfo(name = "trago_imagen")
    val image: String = "",
    @ColumnInfo(name = "trago_nombre")
    val name: String = "",
    @ColumnInfo(name = "trago_descripcion")
    val description: String = "",
    @ColumnInfo(name = "trago_has_alcohol")
    val hasAlcohol: String = "Non_Alcoholic"
)

fun List<FavoritesEntity>.asDrinkList(): List<Cocktail> = this.map {
    Cocktail(it.cocktailId, it.image, it.name, it.description, it.hasAlcohol)
}

fun List<CocktailEntity>.asCocktailList(): List<Cocktail> = this.map {
    Cocktail(it.cocktailId, it.image, it.name, it.description, it.hasAlcohol)
}

fun Cocktail.asCocktailEntity(): CocktailEntity =
    CocktailEntity(this.cocktailId, this.image, this.name, this.description, this.hasAlcohol)

fun Cocktail.asFavoriteEntity(): FavoritesEntity =
    FavoritesEntity(this.cocktailId, this.image, this.name, this.description, this.hasAlcohol)