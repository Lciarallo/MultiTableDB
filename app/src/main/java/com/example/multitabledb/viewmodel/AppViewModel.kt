package com.example.multitabledb.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.multitabledb.MovieApplication
import com.example.multitabledb.data.Actor
import com.example.multitabledb.data.ActorWithMovies
import com.example.multitabledb.data.AppDao
import com.example.multitabledb.data.Movie
import com.example.multitabledb.data.MovieActor
import com.example.multitabledb.data.MovieWithActors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val appDao: AppDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private var movieId = MutableStateFlow(0)
    private var actorId = MutableStateFlow(0)

    val movies: StateFlow<List<Movie>> =
        appDao.getAllMovies()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf(),
            )

    val actors: StateFlow<List<Actor>> =
        appDao.getAllActors()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf()
            )

    val movieWithActors: StateFlow<MovieWithActors> =
        movieId.flatMapLatest { id ->
            appDao.getMovieWithActors(id)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = MovieWithActors(Movie(0,""), listOf())
            )

    val actorWithMovies: StateFlow<ActorWithMovies> =
        actorId.flatMapLatest { id ->
            appDao.getActorWithMovies(id)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ActorWithMovies(Actor(0,""), listOf())
            )

    fun insertActor(actor: Actor){
        viewModelScope.launch {
            appDao.insertActor(actor)
        }
    }

    fun updateActor(actor: Actor){
        viewModelScope.launch {
            appDao.updateActor(actor)
        }
    }

    fun deleteActor(actor: Actor){
        viewModelScope.launch {
            appDao.deleteActor(actor)
        }
    }

    fun insertMovie(movie: Movie){
        viewModelScope.launch {
            appDao.insertMovie(movie)
        }
    }

    fun updateMovie(movie: Movie){
        viewModelScope.launch {
            appDao.updateMovie(movie)
        }
    }

    fun deleteMovie(movie: Movie){
        viewModelScope.launch {
            appDao.deleteMovie(movie)
        }
    }

    fun insertMovieActor(movieActor: MovieActor){
        viewModelScope.launch {
            appDao.insertMovieActor(movieActor)
        }
    }

    fun deleteMovieActor(movieActor: MovieActor){
        viewModelScope.launch {
            appDao.deleteMovieActor(movieActor)
        }
    }

    fun selectMovie(movie: Movie){
        movieId.value = movie.id
    }

    fun selectActor(actor: Actor){
        actorId.value = actor.id
    }

    fun insertActorsAndMovies() {
        viewModelScope.launch {

            val actors = listOf(
                Actor(name = "Leonardo DiCaprio"), Actor(name = "Brad Pitt"), Actor(name = "Johnny Depp"),
                Actor(name = "Tom Hanks"), Actor(name = "Morgan Freeman"), Actor(name = "Robert Downey Jr."),
                Actor(name = "Will Smith"), Actor(name = "Scarlett Johansson"), Actor(name = "Jennifer Lawrence"),
                Actor(name = "Tom Cruise"), Actor(name = "Chris Hemsworth"), Actor(name = "Chris Evans"),
                Actor(name = "Mark Ruffalo"), Actor(name = "Chris Pratt"), Actor(name = "Ryan Reynolds"),
                Actor(name = "Dwayne Johnson"), Actor(name = "Hugh Jackman"), Actor(name = "Christian Bale"),
                Actor(name = "Matthew McConaughey"), Actor(name = "Anne Hathaway"), Actor(name = "Emma Stone"),
                Actor(name = "Ryan Gosling"), Actor(name = "Natalie Portman"), Actor(name = "Gal Gadot"),
                Actor(name = "Henry Cavill"), Actor(name = "Ben Affleck"), Actor(name = "Robert Pattinson"),
                Actor(name = "Margot Robbie"), Actor(name = "Brie Larson"), Actor(name = "Zendaya")
            )

            val movies = listOf(
                Movie(name = "Inception"), Movie(name = "Fight Club"), Movie(name = "Pirates of the Caribbean"),
                Movie(name = "The Dark Knight"), Movie(name = "The Avengers"), Movie(name = "Interstellar"),
                Movie(name = "The Matrix"), Movie(name = "Titanic"), Movie(name = "Forrest Gump"),
                Movie(name = "Gladiator"), Movie(name = "The Wolf of Wall Street"), Movie(name = "Avatar"),
                Movie(name = "Black Panther"), Movie(name = "Mad Max: Fury Road"), Movie(name = "Wonder Woman")
            )

            val movieActorRelations = listOf(
                // Inception
                MovieActor(movieId = 1, actorId = 1), // Leonardo DiCaprio
                MovieActor(movieId = 1, actorId = 20), // Anne Hathaway

                // Fight Club
                MovieActor(movieId = 2, actorId = 2), // Brad Pitt
                MovieActor(movieId = 2, actorId = 18), // Edward Norton (não listado, mas presumido)

                // Pirates of the Caribbean
                MovieActor(movieId = 3, actorId = 3), // Johnny Depp
                MovieActor(movieId = 3, actorId = 28), // Orlando Bloom (não listado, mas presumido)

                // The Dark Knight
                MovieActor(movieId = 4, actorId = 18), // Christian Bale
                MovieActor(movieId = 4, actorId = 5), // Morgan Freeman

                // The Avengers
                MovieActor(movieId = 5, actorId = 8), // Scarlett Johansson
                MovieActor(movieId = 5, actorId = 6), // Robert Downey Jr.
                MovieActor(movieId = 5, actorId = 10), // Chris Evans
                MovieActor(movieId = 5, actorId = 11), // Mark Ruffalo

                // Interstellar
                MovieActor(movieId = 6, actorId = 1), // Matthew McConaughey
                MovieActor(movieId = 6, actorId = 20), // Anne Hathaway

                // The Matrix
                MovieActor(movieId = 7, actorId = 30), // Keanu Reeves (não listado, mas presumido)
                MovieActor(movieId = 7, actorId = 24), // Laurence Fishburne (não listado, mas presumido)

                // Titanic
                MovieActor(movieId = 8, actorId = 1), // Leonardo DiCaprio
                MovieActor(movieId = 8, actorId = 28), // Kate Winslet (não listado, mas presumido)

                // Forrest Gump
                MovieActor(movieId = 9, actorId = 4), // Tom Hanks

                // Gladiator
                MovieActor(movieId = 10, actorId = 29), // Russell Crowe (não listado, mas presumido)

                // The Wolf of Wall Street
                MovieActor(movieId = 11, actorId = 1), // Leonardo DiCaprio
                MovieActor(movieId = 11, actorId = 27), // Margot Robbie

                // Avatar
                MovieActor(movieId = 12, actorId = 24), // Sam Worthington (não listado, mas presumido)
                MovieActor(movieId = 12, actorId = 25), // Zoe Saldana (não listado, mas presumido)

                // Black Panther
                MovieActor(movieId = 13, actorId = 25), // Chadwick Boseman (não listado, mas presumido)

                // Mad Max: Fury Road
                MovieActor(movieId = 14, actorId = 30), // Tom Hardy (não listado, mas presumido)
                MovieActor(movieId = 14, actorId = 27), // Charlize Theron (não listado, mas presumido)

                // Wonder Woman
                MovieActor(movieId = 15, actorId = 24), // Gal Gadot
                MovieActor(movieId = 15, actorId = 28) // Chris Pine (não listado, mas presumido)
            )

            // Inserindo atores
            appDao.insertAllActors(actors)

            // Inserindo filmes
            appDao.insertAllMovies(movies)

            // Inserindo relações entre filmes e atores
            appDao.insertAllRelations(movieActorRelations)
        }
    }


    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return AppViewModel(
                    (application as MovieApplication).database.appDao(),
                    savedStateHandle,
                ) as T
            }
        }
    }
}