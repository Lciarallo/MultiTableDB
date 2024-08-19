import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.multitabledb.data.Movie
import com.example.multitabledb.viewmodel.AppViewModel

@Composable
fun FormScreenFilmes(navController: NavController, viewModel: AppViewModel) {
    val movieNameState = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Adicionar Novo Filme")

        // Caixa de texto para inserir o nome do filme
        OutlinedTextField(
            value = movieNameState.value,
            onValueChange = { movieNameState.value = it },
            label = { Text("Nome do Filme") },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(onClick = {
            // Inserindo o filme no banco de dados
            if (movieNameState.value.isNotEmpty()) {
                viewModel.insertMovie(Movie(name = movieNameState.value))
            }
            navController.popBackStack() // Volta para a tela anterior
        }) {
            Text(text = "Salvar")
        }
    }
}
