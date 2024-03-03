package com.example.database_practical2

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "users")
class User(
    var login:String,
    var email:String,
    var password:String,
    var gender: String
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}

@Dao
interface UserDao{

    @Insert
    fun insert(us:User)

    @Query("select * from users where login = :logs")
    fun getUserByLogin(logs: String):User
}

@Database(entities = [User::class], version = 1)
abstract class AppBD:RoomDatabase(){
    abstract fun dao():UserDao
}