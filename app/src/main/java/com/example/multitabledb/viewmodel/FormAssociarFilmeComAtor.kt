import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.multitabledb.data.Actor
import com.example.multitabledb.data.Movie
import com.example.multitabledb.data.MovieActor
import com.example.multitabledb.viewmodel.AppViewModel

@Composable
fun FormAssociarFilmeComAtor(navController: NavController, viewModel: AppViewModel) {
    val movies by viewModel.movies.collectAsState()
    val actors by viewModel.actors.collectAsState()

    // Armazenando o filme e ator selecionados
    val selectedMovie = remember { mutableStateOf<Movie?>(null) }
    val selectedActor = remember { mutableStateOf<Actor?>(null) }

    val movieDropdownExpanded = remember { mutableStateOf(false) }
    val actorDropdownExpanded = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Associar Filme com Ator")

        // Caixa de seleção para filmes
        OutlinedTextField(
            value = selectedMovie.value?.name ?: "Selecione um filme",
            onValueChange = {},
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { movieDropdownExpanded.value = true },
            readOnly = true,
            label = { Text("Filme") },
            trailingIcon = {
                IconButton(onClick = {movieDropdownExpanded.value =  !movieDropdownExpanded.value}) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Filme" )

                }
            }

        )

        DropdownMenu(
            expanded = movieDropdownExpanded.value,
            onDismissRequest = { movieDropdownExpanded.value = false }
        ) {
            movies.forEach { movie ->
                DropdownMenuItem(
                    text = { Text(movie.name) },
                    onClick = {
                        selectedMovie.value = movie
                        movieDropdownExpanded.value = false
                    }
                )
            }
        }

        // Caixa de seleção para atores
        OutlinedTextField(
            value = selectedActor.value?.name ?: "Selecione um ator",
            onValueChange = {},
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { actorDropdownExpanded.value = true },
            readOnly = true,
            label = { Text("Ator") }
        )

        DropdownMenu(
            expanded = actorDropdownExpanded.value,
            onDismissRequest = { actorDropdownExpanded.value = false }
        ) {
            actors.forEach { actor ->
                DropdownMenuItem(
                    text = { Text(actor.name) },
                    onClick = {
                        selectedActor.value = actor
                        actorDropdownExpanded.value = false
                    }
                )
            }
        }

        // Botão para associar
        Button(onClick = {
            val selectedMovieId = selectedMovie.value?.id
            val selectedActorId = selectedActor.value?.id

            if (selectedMovieId != null && selectedActorId != null) {
                viewModel.insertMovieActor(MovieActor(movieId = selectedMovieId, actorId = selectedActorId))
            }

            navController.popBackStack() // Volta para a tela anterior
        }) {
            Text(text = "Associar")
        }
    }
}
