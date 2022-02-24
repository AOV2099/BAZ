package com.aov2099.baz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aov2099.baz.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDB: RoomDatabase() {

    abstract  fun  movieDao() : MovieDao

    companion object{

        @Volatile
        private var INSTANCE: MovieDB? = null

        fun getDatabase( context:Context ): MovieDB{
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDB::class.java,
                    "movie_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }

    }
}