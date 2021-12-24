package id.lidya.vaksinasi.MyRoom.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import id.lidya.vaksinasi.MyRoom.DAO.DAOFormVaksinasi;
import id.lidya.vaksinasi.MyRoom.Entity.FormVaksinasi;

@Database(entities = {
        FormVaksinasi.class
},version = 7,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase INSTANCE;
    public abstract DAOFormVaksinasi daoFormVaksinasi();

    public static MyDatabase createDatabase(Context context){
        synchronized (MyDatabase.class){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,MyDatabase.class,"db_volunteen")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }
}