package com.example.iesgrao

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.iesgrao.ui.theme.IESGRAOTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IESGRAOTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(onLoginClicked = {_, _ -> })
                    linkPage()
                    TitleText()
                    InformationText()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginClicked: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
   // var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Espacio en blanco en la parte superior
        Spacer(modifier = Modifier.height(16.dp))
        // Imagen o logo de la empresa
        Image(
            painter = painterResource(id = R.drawable.grao2),
            contentDescription = null,
            modifier = Modifier
                .size(155.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Espacio en blanco entre la imagen y los campos de texto
        Spacer(modifier = Modifier.height(15.dp))

        // Campo de texto para el nombre de usuario
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            value = username,
            onValueChange = { username = it },
            label = { Text("Introduce tu nombre de usuario") },
            singleLine = true, // Establece si el texto es puede ocupar una línea o más. En caso de true, solo permite una línea
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                autoCorrect = true
            ),
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        )
        // Espacio en blanco entre el campo de texto de usuario y el de password
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para la password
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
            value = password,
            onValueChange = { password = it },
            label = { Text("Introduce la contraseña") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            }
        )

        // Espacio en blanco entre el campo de texto de contraseña y el botón de inicio de sesión
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión
        Button(
            onClick = {
                if (username.length > 20) {
                    errorMessage = "El nombre de usuario no puede tener más de 20 caracteres"
                } else if (password.length > 16) {
                    errorMessage = "La contraseña no puede tener más de 16 caracteres"
                } else if (username.length < 4) {
                    errorMessage = "Tu nombre de usuario debe ser de al menos 4 caracteres"
                } else if (password.length < 8) {
                    errorMessage = "La contraseña no puede tener menos de 8 caracteres"
                } else {
                    errorMessage = null
                    // Próxima lógica para pasar de pantalla
                    onLoginClicked(username, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                // Muestra un indicador de carga si está en progreso
                CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                // Texto del botón de inicio de sesión
                Text("Iniciar sesión")
            }
        }
        
        // Mostrar errores (si hay)
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .alpha(0.8f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        // Enlace o texto adicional (puede ser un enlace "¿Olvidaste tu contraseña?")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)) {
    }


    }
}

@Composable
fun linkPage() {
    // Obtener el contexto local
   val uriHandler = LocalUriHandler.current

ConstraintLayout(modifier = Modifier.fillMaxSize()) {
    val urlConstraint = createRef()

        Text(text = "Nuestra página web",
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(16.dp)
                .clickable { uriHandler.openUri(uri = "https://portal.edu.gva.es/ieselgrao/?page_id=690&lang=es") }
                .constrainAs(urlConstraint) {
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}



@Composable
fun InformationText() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val (text, divider, divider2) = createRefs()
        Divider(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .height(2.dp)
                .constrainAs(divider) {
                    bottom.linkTo(text.top)
                },
            color = Color.Gray
        )

        Divider(
            modifier = Modifier
                .padding(bottom = 90.dp)
                .height(2.dp)
                .constrainAs(divider2) {
                    bottom.linkTo(text.bottom)
                },
            color = Color.Gray
        )


        Text("Per a l'alumnat, el nom d'usuari coincideix amb el " +
                "NIA i la contrasenya són les tres primeres lletres del " +
                "cognom en minúscula seguides de la data de naixement, en format DDMMAA. ",
            modifier = Modifier
                .padding(bottom = 100.dp)
                .fillMaxWidth()
                .constrainAs(text) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })


    }
}

@Composable
fun TitleText() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val title = createRef()

        Text(
            text = "IES El Grao",
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(8.dp), // Añadir relleno para mejorar la apariencia
            style = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IESGRAOTheme {
        LoginScreen(onLoginClicked = {_, _ -> })
        linkPage()
        TitleText()
        InformationText()
    }
}
