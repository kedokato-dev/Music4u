import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.compose.getCurrentColorScheme

@Composable
fun DialogAddPlaylist(
    onDismissRequest: () -> Unit,
    onCreate: (String) -> Unit,
    title: String,
    subTitle: String,
) {
    var playlistTitle by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = getCurrentColorScheme().background
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = getCurrentColorScheme().onBackground,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = playlistTitle,
                    onValueChange = { playlistTitle = it },
                    placeholder = { Text("Give your playlist a title", color = getCurrentColorScheme().primary) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = getCurrentColorScheme().background,
                        unfocusedContainerColor = getCurrentColorScheme().background,
                        focusedIndicatorColor = getCurrentColorScheme().onBackground,
                        unfocusedIndicatorColor = getCurrentColorScheme().onBackground,
                        focusedTextColor = getCurrentColorScheme().primary,
                        unfocusedTextColor = getCurrentColorScheme().primary,
                        cursorColor = getCurrentColorScheme().primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Cancel",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    TextButton(
                        onClick = { onCreate(playlistTitle) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Create",
                            color = Color(0xFF00BCD4),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddPlaylist() {
    DialogAddPlaylist(
        onDismissRequest = {},
        onCreate = {},
        title = "Create Playlist",
        subTitle = "Give your playlist a title"
    )
}