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
import com.example.multitabledb.data.Actor
import com.example.multitabledb.viewmodel.AppViewModel

@Composable
fun FormScreenAtor(navController: NavController, viewModel: AppViewModel) {
    val actorNameState = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Adicionar Novo Ator")

        // Caixa de texto para inserir o nome do ator
        OutlinedTextField(
            value = actorNameState.value,
            onValueChange = { actorNameState.value = it },
            label = { Text("Nome do Ator") },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(onClick = {
            // Inserindo o ator no banco de dados
            if (actorNameState.value.isNotEmpty()) {
                viewModel.insertActor(Actor(name = actorNameState.value))
            }
            navController.popBackStack() // Volta para a tela anterior
        }) {
            Text(text = "Salvar")
        }
    }
}
