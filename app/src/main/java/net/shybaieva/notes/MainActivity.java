package net.shybaieva.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import net.shybaieva.notes.auth.Register;
import net.shybaieva.notes.model.Note;
import net.shybaieva.notes.note.AddNote;
import net.shybaieva.notes.note.NoteDetails;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView noteList;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddNote.class);
                view.getContext().startActivity(i);
            }
        });

    }

    private void init (){
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        Query query = fStore.collection("notes").orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note>  allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull final Note note) {
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContain.setText(note.getContent());
                final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(v.getContext(), "The item is clicked", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(v.getContext(), NoteDetails.class);
                        i.putExtra("title" , note.getTitle());
                        i.putExtra("contain", note.getContent());
                        i.putExtra("noteId", docId);
                        v.getContext().startActivity(i);
                    }
                });

                ImageView menuIcon = noteViewHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu  = new PopupMenu(v.getContext(), v);
                        menu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(MainActivity.this, "Edit clicked", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layuot, parent, false);
                return new NoteViewHolder(view);
            }
        };

        noteList = findViewById(R.id.noteList);

        //Переключатель выпадающего меню
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        //Отображение Recycler View в 2 колонки
        noteList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteList.setAdapter(noteAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case(R.id.add):{
                startActivity(new Intent(this, AddNote.class));
                break;
            }

            case(R.id.logout):{
              //  FirebaseAuth.getInstance().signOut();

                checkUser();

                break;
            }

            default://Всплывающее сообщение
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void checkUser(){
        //Real user or not

        if(user.isAnonymous()){
            displayAlert();
        }
        else{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Splash.class));
            finish();
        }

    }

    private void displayAlert(){
        AlertDialog.Builder warning = new AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setMessage("If you will logout from the Anonymous account all your data will be deleted\nAre you sure to logout?")
                .setPositiveButton("Sync Notes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent (getApplicationContext(), Register.class));
                        finish();
                    }
                })
                .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete all notes



                        //Delete the anonym user
                        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(), Splash.class));
                            }
                        });

                    }
                });
            warning.show();
    }

    //Настройки в правом верхнем углу
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.settings){
            Toast.makeText(this, "Settings menu is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public  class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteContain;
        View view;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.tvTaskName);
            noteContain = itemView.findViewById(R.id.tvTaskDescription);
            view = itemView;
        }



    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
}
