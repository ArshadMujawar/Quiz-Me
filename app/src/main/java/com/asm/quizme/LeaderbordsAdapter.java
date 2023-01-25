package com.asm.quizme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asm.quizme.databinding.RowLeaderbordsBinding;

import java.util.ArrayList;

public class LeaderbordsAdapter extends RecyclerView.Adapter<LeaderbordsAdapter.LeaderbordViewHolder>  {

    Context context;
    ArrayList<User> users;

    public  LeaderbordsAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public LeaderbordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderbords, parent, false);
        return new LeaderbordViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LeaderbordViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.name.setText(user.getName());
        holder.binding.coins.setText(String.valueOf(user.getCoins()));
        holder.binding.index.setText(String.format("#%d", position+1));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderbordViewHolder extends RecyclerView.ViewHolder {

        RowLeaderbordsBinding binding;
        public LeaderbordViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderbordsBinding.bind(itemView);

        }
    }

}

