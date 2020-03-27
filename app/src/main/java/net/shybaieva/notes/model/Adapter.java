/*package net.shybaieva.notes.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.shybaieva.notes.NoteDetails;
import net.shybaieva.notes.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> titles;
    List<String> content;

    public Adapter(List<String> title,List<String> content){
        this.titles = title;
        this.content = content;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layuot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.noteTitle.setText(titles.get(position));
        holder.noteContain.setText((content.get(position)));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(v.getContext(), "The item is clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), NoteDetails.class);
                i.putExtra("title", titles.get(position));
                i.putExtra("contain", content.get(position));
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (titles.size()>=1)
        {return titles.size();}
        else {return 0;}
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContain;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.tvTaskName);
            noteContain = itemView.findViewById(R.id.tvTaskDescription);
            view = itemView;
        }
    }
}
*/