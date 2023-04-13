package sn.flexzone.lourer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ObjetAdapter extends RecyclerView.Adapter<ObjetAdapter.ObjetViewHolder> {
    List<Objet> objets; // mData est la liste de données que vous voulez afficher dans le RecyclerView

    OnItemClickListener onItemClickListener;


    public ObjetAdapter(List<Objet> data, OnItemClickListener onItemClickListener) {
        objets = data;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ObjetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_objet, parent, false);
        return new ObjetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjetViewHolder holder, int position) {
        Objet currentData = objets.get(position);
        holder.nomprod.setText(currentData.getNom());
        holder.adresse.setText(currentData.getAdresse());
        holder.statut.setText(currentData.getStatut());
        Picasso.get().load(currentData.getImg()).into(holder.profil);

    }

    @Override
    public int getItemCount() {
        return objets.size();
    }
    class ObjetViewHolder extends RecyclerView.ViewHolder {
        ImageView profil;
        TextView nomprod, adresse, statut;
        CardView card;

        public ObjetViewHolder(@NonNull View itemView) {
            super(itemView);
            profil = itemView.findViewById(R.id.profil);
            nomprod = itemView.findViewById(R.id.produit);
            adresse = itemView.findViewById(R.id.adresse);
            statut = itemView.findViewById(R.id.statut);
            card = itemView.findViewById(R.id.card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Objet currentData = objets.get(position);
                    onItemClickListener.onItemClick(currentData.getId());
                }
            });
        }
    }
}

