package com.example.notes;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteRoomDatabase database;
    private NoteDao noteDao;
    private LiveData<List<Note>> listNotes;

    public NoteRepository(Application application) {
        database = NoteRoomDatabase.getDatabase(application);
        noteDao = database.noteDao();
    }

    public LiveData<List<Note>> getListNotes() {
        listNotes = noteDao.getAllLiveData();
        return listNotes;
    }

    public LiveData<List<Note>> getListNotesSortUpdate() {
        listNotes = noteDao.getAllSortUpdate();
        return listNotes;
    }

    public LiveData<List<Note>> getListNotesSortAlphabetically() {
        listNotes = noteDao.getAllSortAlphabetically();
        return listNotes;
    }

    public void insert(Note note) {
        NoteRoomDatabase.executorService.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        NoteRoomDatabase.executorService.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        NoteRoomDatabase.executorService.execute(() -> noteDao.delete(note));
    }
}