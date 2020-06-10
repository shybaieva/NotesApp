package net.shybaieva.notes.note;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.shybaieva.notes.R;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {

    FirebaseFirestore fireStore;
    EditText noteTitle, noteContent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireStore = FirebaseFirestore.getInstance();
        noteContent = findViewById(R.id.addContent);
        noteTitle = findViewById(R.id.addTitle);
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String nTitle = noteTitle.getText().toString();
               String nContent = noteContent.getText().toString();

                //save note
               if(nTitle.isEmpty() || nContent.isEmpty())
               {Toast.makeText(AddNote.this, "Message with empty field cannot be saved ", Toast.LENGTH_SHORT).show();
               return;}
               else{

                   DocumentReference docRef = fireStore.collection("notes").document();
                   Map<String, Object> note = new HashMap<>();
                   note.put("title", nTitle);
                   note.put("content", nContent);

                   progressBar.setVisibility(View.VISIBLE);

                   docRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(AddNote.this, "Note is saved", Toast.LENGTH_SHORT).show();
                           onBackPressed();
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(AddNote.this, "Not saved", Toast.LENGTH_SHORT).show();
                       }
                   });
                   }


            }
        });
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.close_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.close){
            onBackPressed();
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
