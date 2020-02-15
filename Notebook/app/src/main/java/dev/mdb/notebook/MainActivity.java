package dev.mdb.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnNewNote;
    Button btnLogout;

    RecyclerView recycleNotes;
    NoteAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user is signed in, otherwise send them to login activity.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }

        // Initialize the new note button.
        btnNewNote = findViewById(R.id.btnNewNote);
        // Set the OnClickListener for the new note button.
        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent for NoteTakingActivity.
                Intent newNoteIntent = new Intent(MainActivity.this, NoteTakingActivity.class);
                // Tell the intent that this should be a brand new.
                newNoteIntent.putExtra("new_note", true);
                // Start the new note activity.
                startActivity(newNoteIntent);
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
        });

        // Initialize the recycler view.
        recycleNotes = findViewById(R.id.recycleNotes);
        // Initialize the recycler view adapter. If this causes a JSONException, then there
        // either no json file stored right now, or it is malformed-formed. Either way, it will
        // create a brand new .json file.

        // Call a function that will query Firebase for the notes of the current user
        // and set this as the data for the recycler view.
        getNotes();

        notesAdapter = new NoteAdapter(new ArrayList<Note>());
        // Set the adapter for the recycler view.
        recycleNotes.setAdapter(notesAdapter);
        // Set the layout of the recycler manager. This will determine how the
        // rows will be displayed. LinearLayout will set them to be vertically
        // linear (i.e one after the other, on top of each other).
        recycleNotes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getNotes() {
        // Get the database reference for the current user based on their
        // unique user ID (UID).
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String uID = FirebaseAuth.getInstance().getUid();
        DatabaseReference myRef = database.getReference("notes").child(uID);

        // Read the values of the user's notes. This code runs anytime the data changes
        // for this reference.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Covert the hashmap from firebase to an arraylist of notes.
                ArrayList<HashMap<String, String>> notesHashMap = new ArrayList<>();
                ArrayList<Note> notesArray = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    notesHashMap = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                }
                for (HashMap<String, String> note: notesHashMap) {
                    notesArray.add(new Note(note.get("name"), note.get("contents")));
                }
                // Set the adapter of the recycler view to use this new data.
                notesAdapter.setData(notesArray);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
