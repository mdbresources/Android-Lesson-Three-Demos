package dev.mdb.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteTakingActivity extends AppCompatActivity {

    EditText txtNote;
    Button btnSave;
    boolean newNote;
    String noteTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taking_activity);

        // Initialize UI elements using their ID's set in the editor layout.
        txtNote = findViewById(R.id.txtNote);
        btnSave = findViewById(R.id.btnSave);

        // Determine if we should use the existing saved note,
        // or start from scratch.
        newNote = getIntent().getBooleanExtra("new_note", false);
        final String noteContent;

        // If this should be a new note, then keep the
        // txtNote field empty, otherwise fill it with
        // the text saved in the current note file.
        if (!newNote) {
            // If there is no existing, then tell this to the user.
            noteTitle = getIntent().getStringExtra("note_title");
            noteContent = getIntent().getStringExtra("note_content");
            txtNote.setText(noteContent);
        }

        // Set OnClickListener Behavior for buttons.
        // Implements the OnClickListener methods in-line.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current text in the txtNote field into the note.txt file.
                String newNoteData = txtNote.getText().toString();
                if (newNote) {
                    // If this note is a new note, this means no field exists in the json
                    // for this note. Add this note and make this no longer a new note.
                    addNote(newNoteData);
                    newNote = false;
                } else {
                    // Otherwise, save the note with the same name it already has.
                    saveNote(newNoteData);
                }

                // Show a toast that the file was saved.
                Toast fileSaved = Toast.makeText(getApplicationContext(), "File Saved.", Toast.LENGTH_SHORT);
                fileSaved.show();
            }
        });
    }

    private void addNote(final String newNoteData) {

        // Get a database reference for the user's indivdual notes storage place.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String uID = FirebaseAuth.getInstance().getUid();
        DatabaseReference myRef = database.getReference("notes").child(uID);

        // Create a single event value reader to get the current user notes and the to
        // append our new note to it, and then finally set all these for the user's notes
        // in firebase.
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Note> notes = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    notes = (ArrayList<Note>) dataSnapshot.getValue();
                }
                // Add our new note.
                noteTitle = "Note " + (notes.size() + 1);
                notes.add(new Note("Note " + (notes.size() + 1), newNoteData));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("notes").child(uID);
                // Update firebase with the new notes.
                myRef.setValue(notes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    private void saveNote(final String newNoteData) {

        // Get a database reference for the user's indivdual notes storage place.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String uID = FirebaseAuth.getInstance().getUid();
        DatabaseReference myRef = database.getReference("notes").child(uID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> notes = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    notes = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                }
                // Find the current note that is being edited and then update
                // its contents.
                for (HashMap<String, String> note: notes) {
                    if (note.get("name").equals(noteTitle)) {
                        note.put("contents", newNoteData);
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("notes").child(uID);
                // Update firebase with these new notes.
                myRef.setValue(notes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

}
