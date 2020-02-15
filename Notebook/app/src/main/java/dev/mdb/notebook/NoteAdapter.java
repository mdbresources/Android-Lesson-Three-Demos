package dev.mdb.notebook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> notes;

    // Provide a suitable constructor for the adapter. This would be a good
    // place to have the dataset be initialized.
    public NoteAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Get the context from parent and then make an inflater from this context.
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout of a individual cell.
        View noteView = inflater.inflate(R.layout.note_row, parent, false);

        // Return a new holder instance
        NoteViewHolder viewHolder = new NoteViewHolder(noteView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        // Get an element from your dataset at this position
        // Replace the contents of the view with that element
        holder.txtName.setText(notes.get(position).getName());
        holder.txtDate.setText(notes.get(position).getContents());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editNote = new Intent(holder.itemView.getContext(), NoteTakingActivity.class);
                editNote.putExtra("new_note", false);
                editNote.putExtra("note_title", notes.get(position).getName());
                editNote.putExtra("note_content", notes.get(position).getContents());
                holder.itemView.getContext().startActivity(editNote);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Set the data to be something totally new.
    public void setData(ArrayList<Note> notes) {
        this.notes = notes;
        // This line is very important. The recycler view will not visually update until
        // call this notifyDataSetChanged.
        this.notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        // Initialized all the UI components of an individual row.
        TextView txtName;
        Button btnEdit;
        TextView txtDate;
        public NoteViewHolder(View rowView) {
            super(rowView);

            txtName = rowView.findViewById(R.id.txtNoteName);
            btnEdit = rowView.findViewById(R.id.btnEditNote);
            txtDate = rowView.findViewById(R.id.txtNoteInto);
        }
    }
}
