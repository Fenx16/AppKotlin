package com.example.appdiseofinal

import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.appdiseofinal.datos.Liga
import com.example.appdiseofinal.datos.InstanciaPeli
import com.example.appdiseofinal.pantallitas.NavegacionItem
import com.example.appdiseofinal.ui.theme.AppDiseñoFinalTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppDiseñoFinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Color.Black) {
                    MainScreen()
                }}
            }
        }
    }


    @Composable
    fun MainScreen() {

        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
            drawerContent = {
                Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
            }
        ) {
            Navigation(navController = navController)
        }

    }

    @Composable
    fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {

        TopAppBar(
            title = { Text(text = "Final recuperación Junio", fontSize = 18.sp) },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Filled.Menu, "")
                }
            },
            backgroundColor = Color.Black,
            contentColor = Color.White
        )

    }

    @Composable
    fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {

        val items = listOf(
            NavegacionItem.Home,
            NavegacionItem.añadecampeon,
            NavegacionItem.borraelcampeon,
            NavegacionItem.interneto,
        )

        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF586A8F),
                            Color(0xFF000000),
                        )
                    )
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colors.onSurface),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                CargaImagenLarga(url = "https://lh3.googleusercontent.com/WebglHOYlW-2P7ADP9oUSSrgy12PHyAE6GP_jmJkQOZZ1XH7Pa_7216EK2qS7iJFvncqOaDjg40BrYdzPbB9qNwn")

            }



            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { items ->
                DrawerItem(item = items, selected = currentRoute == items.route, onItemClick = {

                    navController.navigate(items.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    scope.launch {
                        scaffoldState.drawerState.close()
                    }

                })
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Diseño Fede",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)

            )

        }
    }

    @Composable
    fun DrawerItem(item: NavegacionItem, selected: Boolean, onItemClick: (NavegacionItem) -> Unit) {
        val background = if (selected) Color.LightGray else Color.Transparent
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(item) }
                .height(65.dp)
                .padding(start = 10.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = item.title,
                colorFilter = ColorFilter.tint(Color.Black),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.White
            )

        }

    }

    @Preview
    @Composable
    fun HomeScreen() {

        var carte = sacaelJSON()


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.DarkGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            LazyColumn(
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start


            )
            {

                items(carte) { campeon ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .border(3.dp, Color.Black)
                                .height(300.dp)
                                .width(200.dp),
                        )

                        {

                            CargaImagen(url = campeon.imagen)

                        }

                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            )

                            {

                                Text(
                                    text = campeon.nombre,
                                    color = Color.Black,
                                    modifier = Modifier

                                        .fillMaxWidth()

                                        .padding(15.dp, 14.dp, 9.dp, 9.dp)
                                        .height(40.dp)
                                        .size(30.dp),
                                    style = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold


                                    )
                                )


                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(25.dp)
                            )

                            {

                                Text(
                                    text = campeon.autor,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(10.dp, 0.dp, 0.dp, 0.dp)
                                        .fillMaxWidth(),
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold

                                    )
                                )


                            }
                            Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(25.dp)
                                    )

                            {

                                Text(
                                    text = campeon.anio,
                                    color = Color.White,
                                    modifier = Modifier

                                        .fillMaxWidth()

                                        .padding(10.dp, 0.dp, 0.dp, 0.dp)                                        .height(40.dp)
                                        .size(30.dp),
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold

                                    )
                                )


                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )

                            {

                                Text(
                                    text = campeon.descripcion,
                                    color = Color.White,
                                    modifier = Modifier

                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(10.dp, 0.dp, 0.dp, 0.dp)
                                        ,
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold

                                    )
                                )


                            }



                        }


                    }


                }

            }
        }}


    @Composable
    fun añadecampeon() {

        var txtNombre by rememberSaveable { mutableStateOf("") }
        var region by rememberSaveable { mutableStateOf("") }
        var id by rememberSaveable { mutableStateOf("") }
        var txtImagen by rememberSaveable { mutableStateOf("") }
        var txtDescripcion by rememberSaveable { mutableStateOf("") }
        val contexto = LocalContext.current

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                   Color.Red
                ),

            ) {
            TextField(value = txtNombre, onValueChange = { nuevo -> txtNombre = nuevo }, label = {
                Text(text = "Nombre" , color = Color.Black)
            },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.DarkGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)

            )
            TextField(value = region, onValueChange = { nuevo -> region = nuevo }, label = {
                Text(text = "Region", color = Color.Black)
            },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.DarkGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)

            )
            TextField(value = id, onValueChange = { nuevo -> id = nuevo }, label = {
                Text(text = "ID", color = Color.Black)
            },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.DarkGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)

            )
            TextField(
                value = txtDescripcion,
                onValueChange = { nuevo -> txtDescripcion = nuevo },
                label = {
                    Text(text = "descripcion", color = Color.Black)
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.DarkGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)

            )
            TextField(value = txtImagen, onValueChange = { nuevo -> txtImagen = nuevo }, label = {
                Text(text = "Url a la imagen", color = Color.Black)
            },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.DarkGray
                    ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)

            )
            Spacer(Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(Modifier.width(65.dp))

                Button(
                    modifier = Modifier
                        .size(width = 100.dp, height = 50.dp),
                    onClick = {
                        insert(txtNombre, region, id, txtDescripcion, txtImagen)
                        txtNombre = ""
                        region = ""
                        id = ""
                        txtDescripcion = ""
                        txtImagen = ""
                        Toast.makeText(contexto, "Registro añadido", Toast.LENGTH_SHORT).show()

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Añade un campeon",
                        color = Color.White
                    )
                }

            }
        }


    }

    @Composable
    fun bajacampeon() {

        var txtTitulo by rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {


            TextField(
                value = txtTitulo,
                onValueChange = { nuevo ->
                    txtTitulo = nuevo
                },
                label = {
                    Text(text = "Introduce el nombre del campeon")
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)
            )

            Spacer(Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(Modifier.width(65.dp))

                Button(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(100.dp))
                        .size(width = 100.dp, height = 50.dp),
                    onClick = {
                        borrar(txtTitulo)
                        txtTitulo = ""
                        Toast.makeText(context, "Registro eliminado", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color.White
                    )
                }
            }
        }
    }

    @Composable
    fun interneto( url: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.primary)
            ) {
                AndroidView(
                    factory = {
                        WebView(it).apply {
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    return false
                                }
                            }
                        }
                    }
                ) {
                    it.loadUrl(url);
                    it.settings.javaScriptEnabled = true;
                    it.settings.loadWithOverviewMode = true;
                    it.settings.useWideViewPort = true;

                    it.settings.setSupportZoom(true);
                    it.settings.builtInZoomControls = true;
                    it.settings.displayZoomControls = false;

                    it.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY;
                    it.isScrollbarFadingEnabled = false;


                }
            }
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {

        NavHost(navController, startDestination = NavegacionItem.Home.route) {

            composable(NavegacionItem.Home.route) {
                HomeScreen()
            }

            composable(NavegacionItem.añadecampeon.route) {
                añadecampeon()
            }

            composable(NavegacionItem.borraelcampeon.route) {
                bajacampeon()
            }

            composable(NavegacionItem.interneto.route) {
                interneto("https://www.riotgames.com/es")
            }


        }

    }

    @Composable
    fun CargaImagen(url: String) {
        Image(
            painter = rememberImagePainter(url),
            contentDescription = "imagen",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(1.dp),
            contentScale = ContentScale.FillBounds
        )
    }
@Composable
   fun CargaImagenLarga(url: String) {
        Image(
            painter = rememberImagePainter(url),
            contentDescription = "imagen",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

                .padding(1.dp),
            contentScale = ContentScale.FillBounds,

            )
    }


    fun lee(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                URL(url)
                    .openStream()
                    .bufferedReader()
                    .use { it.readText() }
            } catch (e: IOException) {
                "Error with ${e.message}."
                Log.d("io", e.message.toString())
            } catch (e: Exception) {
                "Error with ${e.message}."
                Log.d("io", e.message.toString())
            }
        }

        return
    }

    fun insert(nombre: String, region: String, id: String, descripcion: String, imagen: String) {
        val url =
            "http://iesayala.ddns.net/fmartin/insert_campeones.php/?Nombre=$nombre&Descripción=$descripcion&Región=$region&Imagen=$imagen"
        lee(url)

    }


    fun borrar(nombre: String) {
        val url = "http://iesayala.ddns.net/fmartin/delete_campeones.php/?Nombre=$nombre"
        lee(url)

    }


    @Composable
    fun sacaelJSON(): Liga {
        val contexto = LocalContext.current

        var pelis by rememberSaveable { mutableStateOf(Liga()) }
        val peli = InstanciaPeli.pInterfa.info();

        peli.enqueue(object : Callback<Liga> {
            override fun onResponse(
                call: Call<Liga>,
                response: Response<Liga>
            ) {
                val peliculasInfo: Liga? = response.body()
                if (peliculasInfo != null) {

                    pelis = peliculasInfo

                }

            }

            override fun onFailure(call: Call<Liga>, t: Throwable) {

                Toast.makeText(contexto, t.toString(), Toast.LENGTH_SHORT).show()
            }

        })

        return pelis


    }


