package net.shybaieva.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {

    Intent data;
    EditText editNoteTitle, editNoteContext;
    Toolbar toolbar;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        data = getIntent();

        final String noteTitle = data.getStringExtra("title");
        final String noteContent = data.getStringExtra("contain");

        editNoteTitle = findViewById(R.id.editTitle);
        editNoteContext = findViewById(R.id.editContext);

        editNoteTitle.setText(noteTitle);
        editNoteContext.setText(noteContent);
        //toolbar = findViewById(R.id)

        fstore = FirebaseFirestore.getInstance();

        FloatingActionButton fab = findViewById(R.id.editButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nTitle = editNoteTitle.getText().toString();
                String nContent = editNoteContext.getText().toString();

                if(nTitle.isEmpty() || nContent.isEmpty())
                {
                    Toast.makeText(EditNote.this, "Message with empty field cannot be saved", Toast.LENGTH_SHORT).show();
                    return;}


                    DocumentReference docRef = fstore.collection("notes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", nTitle);
                    note.put("content", nContent);

                 //   progressBar.setVisibility(View.VISIBLE);

                    docRef.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditNote.this, "Note is saved", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditNote.this, "Not saved", Toast.LENGTH_SHORT).show();
                        }
                    });


            }
        });



    }
}
