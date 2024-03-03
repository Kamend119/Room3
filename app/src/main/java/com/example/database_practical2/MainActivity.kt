package com.example.database_practical2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Content() {
    val controller = rememberNavController()

    val database = Room.databaseBuilder(
        LocalContext.current,
        AppBD::class.java, "AppDB"
    )
        .allowMainThreadQueries()
        .build()

    NavHost(navController = controller,
        startDestination = "AuthorizationPage")
    {
        composable("AuthorizationPage"){Authorization(controller, database)}
        composable("RegistrationPage"){Registration(controller, database)}
        composable("UsersPage/{logs}"){Users(controller, database, it.arguments?.getString("logs"))}
    }
}

@Composable
fun Authorization(controller : NavHostController, database : AppBD) {
    val ctx = LocalContext.current
    var login by  remember { mutableStateOf("") }
    var password by  remember { mutableStateOf("") }

    Column(
        Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column(
            Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Добро пожаловать", fontSize = 40.sp)

            Column {
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it},
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(Icons.Default.AccountCircle, contentDescription = "")

                            Text("Логин")
                        }
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it},
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(Icons.Default.Lock, contentDescription = "")

                            Text("Пароль")
                        }
                    }
                )
            }

            Button(
                onClick = {
                    val logs = database.dao().getUserByLogin(login)

                    if (logs != null) {
                        if (logs.password == password) {
                            controller.navigate("UsersPage/${logs.login}")
                        }else Toast.makeText( ctx ,"Не правильный пароль",Toast.LENGTH_SHORT).show()
                    }else Toast.makeText( ctx ,"Пользователя с таким логином не существует",Toast.LENGTH_SHORT).show()
                },
                Modifier
                    .height(60.dp)
                    .width(250.dp)
            ) {
                Text(
                    "Логин",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        Column(
            Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Забыл пароль")

            Text("или")

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Не зарегистрирован?")

                Button(onClick = { controller.navigate("RegistrationPage") }) {
                    Text("Зарегистрироваться", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun Registration(controller : NavHostController, database : AppBD) {
    val ctx = LocalContext.current
    var login by  remember { mutableStateOf("") }
    var mail by  remember { mutableStateOf("") }
    var password by  remember { mutableStateOf("") }
    var repeatpassword by  remember { mutableStateOf("") }
    var gender by  remember { mutableStateOf("Мужчина") }
    var politics by  remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.ArrowBack, "",
                    Modifier.clickable { controller.navigate("AuthorizationPage") })

                Text("Регистрация", fontSize = 40.sp)
            }


            Column {
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it},
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(Icons.Default.AccountCircle, contentDescription = "")

                            Text("Логин")
                        }
                    }
                )

                OutlinedTextField(
                    value = mail,
                    onValueChange = { mail = it},
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(Icons.Default.Email, contentDescription = "")

                            Text("Почта")
                        }
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it},
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(Icons.Default.Lock, contentDescription = "")

                            Text("Пароль")
                        }
                    }
                )

                OutlinedTextField(
                    value = repeatpassword,
                    onValueChange = { repeatpassword = it},
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(Icons.Default.Lock, contentDescription = "")

                            Text("Повторите пароль")
                        }
                    }
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == "Мужчина",
                    onClick = { gender = "Мужчина" })
                Text("Мужчина")
            }

            Text("Пол", fontSize = 20.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Женщина")
                RadioButton(
                    selected = gender == "Женщина",
                    onClick = { gender = "Женщина" })
            }
        }

        Row(
            Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Лицензионное соглашение", fontSize = 20.sp)

            Checkbox(
                checked = politics,
                onCheckedChange = {
                    politics = !politics
                })
        }


        Button(
            onClick = {
                if (login.isEmpty()) Toast.makeText( ctx ,"Логин не должен быть пустым",Toast.LENGTH_SHORT).show()
                else if (!mail.contains("@")) Toast.makeText( ctx ,"Почта должна содержать символ @",Toast.LENGTH_SHORT).show()
                else if (password.count() <= 3) Toast.makeText( ctx ,"Пароль должен быть длинее 3 символов",Toast.LENGTH_SHORT).show()
                else if (!password.any { it.isDigit() }) Toast.makeText( ctx ,"Пароль должен содержать цифру",Toast.LENGTH_SHORT).show()
                else if (password.lowercase() == password) Toast.makeText( ctx ,"Пароль должен содержать буквы разного регистра",Toast.LENGTH_SHORT).show()
                else if (password != repeatpassword) Toast.makeText( ctx ,"Пароли не совпадают",Toast.LENGTH_SHORT).show()
                else if (!politics) Toast.makeText( ctx , "Условия политики не приняты",Toast.LENGTH_SHORT).show()
                else {
                    val log = database.dao().getUserByLogin(login)
                    if (log != null) Toast.makeText( ctx ,"Логин занят",Toast.LENGTH_SHORT).show()
                    else {
                        database.dao().insert(User(login, mail, password, gender))
                        controller.navigate("AuthorizationPage")
                    }
                }
            },
            Modifier
                .height(40.dp)
                .width(400.dp)
        ) {
            Text(
                "Создать аккаунт",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun Users(controller : NavHostController, database : AppBD, logs:String?){
    Column(
        Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val user = logs?.let { database.dao().getUserByLogin(it) }

        Text("Привет, ${user?.login}")
        Text("Ваша почта: ${user?.email}")
        Text("Ваш пароль: ${user?.password}")
        Text("Ваш пол: ${user?.gender}")

        Button(onClick = { controller.navigate("AuthorizationPage") }) {
            Text("Выйти из аккаунта")
        }
    }
}