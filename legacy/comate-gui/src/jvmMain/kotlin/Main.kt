import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.Navigator
import component.MessageList
import component.TextInput
import io.github.cdimascio.dotenv.Dotenv
import model.ConversationViewModel
import org.archguard.comate.connector.OPENAI_MODEL
import org.archguard.comate.connector.OpenAIConnector
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import theme.LightTheme
import java.io.File

class MainScreen(val di: DI) : Screen {
    override val key: ScreenKey = uniqueScreenKey
    private val viewModel: ConversationViewModel by di.instance()

    @Composable
    override fun Content() {
        LightTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxSize()) {
                        MessageList(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            conversationViewModel = viewModel
                        )
                        TextInput(viewModel)
                    }
                }
            }
        }
    }
}

fun main() = application {
    // todo: move this to a better place
    val appDir = File(System.getProperty("user.home"), ".comate")
    if (!appDir.exists()) {
        appDir.mkdir()
    }

    // for now, create ~/.comate/.env, and put OPENAI_API_KEY=... in it
    val dotenv = Dotenv.configure().directory(appDir.toString()).load()
    val apiKey = dotenv["OPENAI_API_KEY"]
    val apiProxy = dotenv["OPENAI_API_PROXY"] ?: null


    val di = DI {
        bindSingleton<ConversationViewModel> {
            ConversationViewModel(OpenAIConnector(apiKey, OPENAI_MODEL[0], apiProxy))
        }
    }

    Window(onCloseRequest = ::exitApplication, title = "Comate") {
        Navigator(MainScreen(di))
    }
}
