package com.android_development.women_safety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder> {

    ArrayList<String> phoneList;
    Context context;

    public PhoneAdapter(ArrayList<String> phoneList, Context context) {
        this.phoneList = phoneList;
        this.context = context;
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item, parent, false);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        String phoneNumber = phoneList.get(position);
        holder.phoneTextView.setText(phoneNumber);
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public static class PhoneViewHolder extends RecyclerView.ViewHolder {
        TextView phoneTextView;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }
    }
}
