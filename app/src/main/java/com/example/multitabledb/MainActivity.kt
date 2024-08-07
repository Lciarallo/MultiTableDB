package com.example.multitabledb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multitabledb.data.Actor
import com.example.multitabledb.data.ActorWithMovies
import com.example.multitabledb.data.Movie
import com.example.multitabledb.data.MovieWithActors
import com.example.multitabledb.ui.theme.MultiTableDBTheme
import com.example.multitabledb.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiTableDBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier, innerPadding)
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues) {

    val viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
    val movies by viewModel.movies.collectAsState()
    val actors by viewModel.actors.collectAsState()
    val movieWithActors by viewModel.movieWithActors.collectAsState()
    val actorWithMovies by viewModel.actorWithMovies.collectAsState()

    val navController = rememberNavController()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "movies_actors",
            modifier = Modifier.padding(padding)
        ) {
            composable("movies_actors") {
                MoviesActorsScreen(
                    movies = movies,
                    actors = actors,
                    navController = navController,
                    onMovieSelection = viewModel::selectMovie,
                    onActorSelection = viewModel::selectActor
                )
            }
            composable("movie") {
                MovieDetailsScreen(
                    movieWithActors = movieWithActors
                )
            }
            composable("actor") {
                ActorDetailsScreen(
                    actorWithMovies = actorWithMovies
                )
            }
        }
    }

}

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    movieWithActors: MovieWithActors
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = movieWithActors.movie.name)
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn {
            items(movieWithActors.actors){ actor ->
                Card {
                    Text(text = actor.name)
                }
            }
        }
    }
}

@Composable
fun ActorDetailsScreen(
    modifier: Modifier = Modifier,
    actorWithMovies: ActorWithMovies,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = actorWithMovies.actor.name)
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn {
            items(actorWithMovies.movies){ movie ->
                Card {
                    Text(text = movie.name)
                }
            }
        }
    }
}

@Composable
fun MoviesActorsScreen(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    actors: List<Actor>,
    navController: NavController,
    onMovieSelection: (Movie) -> Unit,
    onActorSelection: (Actor) -> Unit,
) {
    Row(modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5F)) {
            Text(text = "Movies")
            LazyColumn {
                items(movies) { movie ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .clickable {
                            onMovieSelection(movie)
                            navController.navigate("movie")
                        }) {
                        Text(text = movie.name)
                    }
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
            Text(text = "Actors")
            LazyColumn {
                items(actors) { actor ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .clickable {
                            onActorSelection(actor)
                            navController.navigate("actor")
                        }) {
                        Text(text = actor.name)
                    }
                }
            }
        }
    }
}













