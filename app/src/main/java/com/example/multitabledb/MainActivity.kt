package com.example.multitabledb

import FormAssociarFilmeComAtor
import FormScreenAtor
import FormScreenFilmes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { navController.navigate("form_filmes") },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                ) {
                    Text(text = "+ Filme")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = { navController.navigate("form_atores") },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                ) {
                    Text(text = "+ Ator")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = { navController.navigate("form_associar") },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                ) {
                    Text(text = "Associar")
                }
            }
        }
    ) { padding ->
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
                    onActorSelection = viewModel::selectActor,
                    viewModel = viewModel
                )
            }
            composable("movie") {
                MovieDetailsScreen(
                    navController = navController,
                    movieWithActors = viewModel.movieWithActors.collectAsState().value,
                    viewModel = viewModel
                )
            }
            composable("actor") {
                ActorDetailsScreen(
                    navController = navController,
                    actorWithMovies = viewModel.actorWithMovies.collectAsState().value,
                    viewModel = viewModel
                )
            }
            composable("form_filmes") {
                FormScreenFilmes(navController = navController, viewModel = viewModel)
            }
            composable("form_atores") {
                FormScreenAtor(navController = navController, viewModel = viewModel)
            }
            composable("form_associar") {
                FormAssociarFilmeComAtor(navController = navController, viewModel = viewModel)
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
    viewModel: AppViewModel
) {
    val selectedMovie = remember { mutableStateOf<Movie?>(null) }

    Row(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5F)
        ) {
            Text(
                text = "Movies",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn {
                items(movies) { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                selectedMovie.value = movie
                                onMovieSelection(movie)
                                navController.navigate("movie")
                            },
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = movie.name,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            selectedMovie.value?.let { movie ->
                Button(
                    onClick = {
                        viewModel.deleteMovie(movie)
                        selectedMovie.value = null
                        if (!movies.contains(movie)) {
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Excluir ${movie.name}")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Text(
                text = "Actors",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn {
                items(actors) { actor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                onActorSelection(actor)
                                navController.navigate("actor")
                            },
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = actor.name,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailsScreen(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    movieWithActors: MovieWithActors
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = movieWithActors.movie.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(movieWithActors.actors) { actor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = actor.name,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.popBackStack()
                viewModel.deleteMovie(movieWithActors.movie)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Excluir ${movieWithActors.movie.name}")
        }
    }
}

@Composable
fun ActorDetailsScreen(
    modifier: Modifier = Modifier,
    actorWithMovies: ActorWithMovies,
    viewModel: AppViewModel,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = actorWithMovies.actor.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(actorWithMovies.movies) { movie ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = movie.name,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.deleteActor(actorWithMovies.actor)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Excluir ${actorWithMovies.actor.name}")
        }
    }
}
