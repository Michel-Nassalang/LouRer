package sn.flexzone.lourer;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnItemClickListener{

    FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        EditText searchobjet = (EditText) findViewById(R.id.searchobjet);
        Button btnsearch = (Button) findViewById(R.id.btnsearcho);
        ImageView icback = (ImageView) findViewById(R.id.ic_back);
        RecyclerView recyclerView = findViewById(R.id.recycler_s);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = FirebaseFirestore.getInstance();

        List<Objet> objetss  = new ArrayList<Objet>();
        CollectionReference objetsCollection = db.collection("objets");
        Query queryo = objetsCollection.orderBy("nom");
        queryo.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Objet objet = document.toObject(Objet.class);
                            objetss.add(objet);
                        }
                        recyclerView.setAdapter(new ObjetAdapter(objetss, SearchActivity.this));
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
                if (searchobjet.getText().toString()!=""){
                    List<Objet> objets = new ArrayList<>();
                    CollectionReference objetsCollection = db.collection("objets");
                    Query queryo = objetsCollection.whereEqualTo("nom", searchobjet.getText().toString());
                    queryo.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot querySnapshot) {
                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                        Objet objet = document.toObject(Objet.class);
                                        objets.add(objet);
                                    }
                                    Log.w(TAG, " "+objets.size());
                                    recyclerView.setAdapter(new ObjetAdapter(objets, SearchActivity.this));
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
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentback = new Intent(getApplicationContext(), SpaceActivity.class);
                startActivity(intentback);
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
