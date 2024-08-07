package com.example.multitabledb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    //MovieActor
    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovieWithActors(id: Int): Flow<MovieWithActors>

    @Query("SELECT * FROM actor WHERE id = :id")
    fun getActorWithMovies(id: Int): Flow<ActorWithMovies>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllRelations(relations: List<MovieActor>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieActor(movieActor: MovieActor)

    @Delete()
    suspend fun deleteMovieActor(movieActor: MovieActor)

    //Movie
    @Query("SELECT * FROM movie")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: Movie)

    @Update()
    suspend fun updateMovie(movie: Movie)

    @Delete()
    suspend fun deleteMovie(movie: Movie)

    //Actor
    @Query("SELECT * FROM actor")
    fun getAllActors(): Flow<List<Actor>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllActors(actors: List<Actor>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActor(actor: Actor)

    @Update()
    suspend fun updateActor(actor: Actor)

    @Delete()
    suspend fun deleteActor(actor: Actor)

}