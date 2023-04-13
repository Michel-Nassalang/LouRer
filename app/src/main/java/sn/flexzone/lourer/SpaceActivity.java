package sn.flexzone.lourer;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SpaceActivity extends AppCompatActivity implements OnItemClickListener{
    Objet selectobjet = new Objet();
    FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_activity);

        ImageView profil = (ImageView) findViewById(R.id.idprofil);
        CardView cardsearch = (CardView) findViewById(R.id.cardsearch);
        CardView cardlost = (CardView) findViewById(R.id.cardlost);
        CardView cardfound = (CardView) findViewById(R.id.cardfound);
        EditText searchtext = (EditText) findViewById(R.id.searchtext);
        Button btnsearch = (Button) findViewById(R.id.btnsearch);
        TextView seeall = (TextView) findViewById(R.id.seeall);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Utilisez un LinearLayoutManager pour afficher les éléments dans une liste verticale

        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CollectionReference usersCollection = db.collection("users");
        Query query = usersCollection.whereEqualTo("id", mAuth.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.isEmpty()) {
                        toast("Votre compte ne figure pas dans les données enregistrées");
                    } else {
                        // Le document existe
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        User user = documentSnapshot.toObject(User.class);
                        if(user.getImage()!= null){
                            if (user.getImage()!=""){
                                Picasso.get().load(user.getImage()).into(profil);
                            }
                        }
                    }
                } else {
                    // La requête a échoué
                }
            }
        });
        List<Objet> objets  = new ArrayList<Objet>();
        CollectionReference objetsCollection = db.collection("objets");
        Query queryo = objetsCollection.limit(10);
        queryo.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Objet objet = document.toObject(Objet.class);
                            objets.add(objet);
                        }
                        recyclerView.setAdapter(new ObjetAdapter(objets, SpaceActivity.this));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents.", e);
                    }
                });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchtext.getText().toString()!=""){
                    List<Objet> objetss = new ArrayList<>();
                    CollectionReference objetsCollection = db.collection("objets");
                    Query queryo = objetsCollection.whereEqualTo("nom", searchtext.getText().toString());
                    queryo.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot querySnapshot) {
                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                        Objet objet = document.toObject(Objet.class);
                                        objetss.add(objet);
                                    }
                                    recyclerView.setAdapter(new ObjetAdapter(objetss,SpaceActivity.this));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error getting documents.", e);
                                }
                            });
                }else {
                    toast("Le nom d'objet à recherché n'est pas donné");
                }
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentprofil = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intentprofil);
            }
        });
        cardsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentsearch = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intentsearch);
            }
        });
        cardlost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentlost = new Intent(getApplicationContext(), LostActivity.class);
                startActivity(intentlost);
            }
        });
        cardfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentfound = new Intent(getApplicationContext(), FoundActivity.class);
                startActivity(intentfound);
            }
        });
        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentsearch = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intentsearch);
            }
        });
    }
    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int id) {
        Intent intent = new Intent(getApplicationContext(), ObjetActivity.class);
        intent.putExtra("objetId", id);
        startActivity(intent);
    }


}
