package com.example.multitabledb.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
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

@Entity(tableName = "movie_actor", primaryKeys = ["movie_id", "actor_id"])
data class MovieActor(
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "actor_id") val actorId: Int,
)

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