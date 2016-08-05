package com.example.krith.treebotestapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anandmishra on 05/08/16.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteViewHolder> {

    List<NoteTO> data;
    Activity a;
    static NoteList that;
    OnItemClickListener mItemClickListener;

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView date;

        NoteViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            title.setTypeface(that.Lato_Bold);
            description = (TextView) itemView.findViewById(R.id.description);
            description.setTypeface(that.Lato_Regular);
            date = (TextView) itemView.findViewById(R.id.date);
            date.setTypeface(that.Lato_Regular);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public RVAdapter(NoteList that, Activity a, List<NoteTO> data) {

        this.that = that;
        this.a = a;
        this.data = data;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.note_view, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTitle());
        holder.description.setText(data.get(position).getDescription());
        holder.date.setText(data.get(position).getDate());
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
