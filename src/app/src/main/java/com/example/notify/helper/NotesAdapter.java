package com.example.notify.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.HomeActivity;
import com.example.notify.R;
import com.example.notify.UpdateNoteActivity;
import com.example.notify.entity.Notify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder>{
    private Context context;
    private ArrayList<Notify> noteList;

    public NotesAdapter(Context context, ArrayList<Notify> noteList) {
        this.context = context;
        this.noteList= noteList;
    }

    @NonNull
    @Override
    public NotesAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_note, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteHolder holder, int position) {
        Notify note = noteList.get(position);
        holder.lblNoteId.setText(String.valueOf(note.getId()));

        String title = note.getTitle();
        if (title.length() > 12) {
            title = title.substring(0, 12).trim() + " ...";
        }
        holder.lblTitle.setText(title);

        String content = note.getContent();
        if (content.length() > 10) {
            String firstLine = content.split("\n")[0].trim();
            content = firstLine.length() > 10 ? firstLine.substring(0, 10) + "..." : firstLine;
        }
        holder.lblContent.setText(content);

        String dateString = note.getDateTimeModified();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateString, inputFormatter);
            LocalDate now = LocalDate.now();

            DateTimeFormatter outputFormatter;
            if (dateTime.getYear() == now.getYear()) {
                outputFormatter = DateTimeFormatter.ofPattern("MMMM d | h:mm a");
            } else {
                outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy | h:mm a");
            }

            holder.lblDateModified.setText(dateTime.format(outputFormatter));
        } catch (Exception e) {
            holder.lblDateModified.setText(dateString);
        }

        holder.imgUpdate.setOnClickListener(v -> {
            Intent i = new Intent (context, UpdateNoteActivity.class);
            i.putExtra("id", String.valueOf(note.getId()));
            i.putExtra("title", note.getTitle());
            i.putExtra("content", note.getContent());
            i.putExtra("dateTimeCreated", note.getDateTimeCreated());
            i.putExtra("dateTimeModified", note.getDateTimeModified());
            context.startActivity(i);

        });

        holder.lnMain.setOnClickListener(v->{
            holder.imgUpdate.performClick();
        });

        holder.lnInfo.setOnClickListener(v->{
            holder.imgUpdate.performClick();
        });

        holder.lnImage.setOnClickListener(v->{
            holder.imgUpdate.performClick();
        });

        holder.lnMain.setOnLongClickListener(v -> {
            holder.imgDelete.performClick();
            return true;
        });

        holder.lnInfo.setOnLongClickListener(v -> {
            holder.imgDelete.performClick();
            return true;
        });

        holder.lnImage.setOnLongClickListener(v -> {
            holder.imgDelete.performClick();
            return true;
        });

        holder.imgDelete.setOnClickListener(v -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("Are You Sure?");
            adb.setIcon(R.drawable.delete);
            adb.setMessage("Delete " + note.getTitle() + " ?");
            adb.setNegativeButton("No", null);

            adb.setPositiveButton("Yes", (dialog,which)->{
                DatabaseHelper dh = new DatabaseHelper(context);
                long result = dh.deleteNotebyId(String.valueOf(note.getId()));

                if (result == -1)
                    Toast.makeText(context, "Failed to DELETE", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(context, "DELETED " + note.getTitle(), Toast.LENGTH_SHORT).show();
                    HomeActivity homeActivity = (HomeActivity) context;
                    homeActivity.displayNotes();
                }
            });

            adb.create().show();

        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder{
    TextView lblNoteId,lblTitle,lblContent,lblDateModified;
    ImageView imgUpdate, imgDelete;
    LinearLayout lnMain,lnInfo, lnImage;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            lblNoteId = itemView.findViewById(R.id.lblNoteId);
            lblTitle = itemView.findViewById(R.id.lblTitle);
            lblContent = itemView.findViewById(R.id.lblContent);
            lblDateModified = itemView.findViewById(R.id.lblDateModified);

            imgUpdate = itemView.findViewById(R.id.imgUpdate);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            lnMain = itemView.findViewById(R.id.lnMain);
            lnInfo = itemView.findViewById(R.id.lnInfo);
            lnImage = itemView.findViewById(R.id.lnImage);




        }
    }
}
