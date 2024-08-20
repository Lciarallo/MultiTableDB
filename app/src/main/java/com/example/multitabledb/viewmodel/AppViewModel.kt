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
import kotlinx.coroutines.flow.Flow
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
            if(id == 0)
                MutableStateFlow(MovieWithActors(Movie(0,""), listOf()))
            else
                appDao.getMovieWithActors(id)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = MovieWithActors(Movie(0,""), listOf())
            )

    val actorWithMovies: StateFlow<ActorWithMovies> =
        actorId.flatMapLatest { id ->
            if(id == 0)
                MutableStateFlow(ActorWithMovies(Actor(0,""), listOf()))
            else
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
        actorId.value = 0
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
        movieId.value = 0
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