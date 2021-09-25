package com.example.notes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class NoteRoomDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static volatile NoteRoomDatabase database;
    static final ExecutorService executorService =
            Executors.newFixedThreadPool(4);

    static NoteRoomDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (NoteRoomDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "database_notes3")
                            .addMigrations(NoteRoomDatabase.MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return database;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notes ADD COLUMN todoLists TEXT");
        }
    };
}