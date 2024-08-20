package com.example.multitabledb.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)

@Entity(tableName = "actor")
data class Actor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)

@Entity(tableName = "movie_actor", primaryKeys = ["movie_id", "actor_id"], foreignKeys = [
    ForeignKey(
        entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
        onDelete = ForeignKey.CASCADE

    ),
    ForeignKey(
        entity = Actor::class,
        parentColumns = ["id"],
        childColumns = ["actor_id"],
        onDelete = ForeignKey.CASCADE

    ),
])
data class MovieActor(
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "actor_id") val actorId: Int,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(movieId)
        parcel.writeInt(actorId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieActor> {
        override fun createFromParcel(parcel: Parcel): MovieActor {
            return MovieActor(parcel)
        }

        override fun newArray(size: Int): Array<MovieActor?> {
            return arrayOfNulls(size)
        }
    }
}

data class MovieWithActors(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(MovieActor::class, parentColumn = "movie_id", entityColumn = "actor_id")
    )
    val actors: List<Actor>
)

data class ActorWithMovies(
    @Embedded val actor: Actor,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(MovieActor::class, parentColumn = "actor_id", entityColumn = "movie_id")
    )
    val movies: List<Movie>
)