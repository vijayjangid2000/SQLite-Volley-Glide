package com.vijayjangid.cubereum;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {

    private ArrayList<ApiData> apiDataList;
    Context context;

    public Adapter(Context context, ArrayList<ApiData> apiData) {
        this.apiDataList = apiData;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {

        String name = "Name - " +
                apiDataList.get(position).getFirstName() + " "
                + apiDataList.get(position).getLastName();
        String email = "Email - " + apiDataList.get(position).getEmail();
        String avatarURL = apiDataList.get(position).getAvatarLink();

        holder.name.setText(name);
        holder.email.setText(email);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_error_outline_24);

        Glide.with(holder.imageView.getContext()).load(avatarURL).apply(options).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return apiDataList.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView name, email;
        ImageView imageView;

        public AdapterViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTV);
            email = itemView.findViewById(R.id.emailTV);
            imageView = itemView.findViewById(R.id.avatarIV);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDarkThemeCustom);
                    builder.setTitle("Delete?")
                            .setIcon(R.drawable.ic_baseline_delete_outline_24)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    apiDataList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            });
                    builder.show();
                    return true;
                }
            });

        }
    }
}
