package com.example.calendo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.EditCategoryActivity;
import com.example.calendo.MainActivity;
import com.example.calendo.NewCategoryActivity;
import com.example.calendo.R;

public class HorizontalAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private String[] list;
    private Context context;
    private OnItemClickListener listener;


    public HorizontalAdapter(Context c, String[] list, OnItemClickListener listener) {
        this.context = c;
        this.list = list;
        this.listener= listener;



    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //Set the description
        holder.taskCategory.setText(list[position]);

        //Category all
        if (position == 0 ){
            holder.buttonCategory.setImageResource(R.drawable.ic_all);
        } else {

            //Last element in the categories list
            if(position == list.length-1){
                holder.buttonCategory.setImageResource(R.drawable.ic_listadd);
            } else {
                //Retrieve the string from the DB and set the icon of the normal
                holder.buttonCategory.setImageResource(R.drawable.ic_book_black_24dp);
            }
        }



        //Filter items
        holder.buttonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),  "Position no: "+ position, Toast.LENGTH_LONG).show();

                listener.onItemClick(holder);


            }
        });

        holder.buttonCategory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //In the case of list added by the user, we skip the all category and the add new cateogry


                if(position != 0 && position != list.length-1){

                    Intent i = new Intent(context, EditCategoryActivity.class);
                    i.putExtra("categoryName", holder.taskCategory.getText().toString());
                    context.startActivity(i);

                }

                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.length;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder);

    }

}
