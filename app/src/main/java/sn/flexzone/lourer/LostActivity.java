package sn.flexzone.lourer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LostActivity extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_layout);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        EditText nom = (EditText) findViewById(R.id.nom);
        Spinner statut = (Spinner) findViewById(R.id.spinner);
        EditText adresse = (EditText) findViewById(R.id.adresse);
        EditText tel = (EditText) findViewById(R.id.tel);
        EditText email = (EditText) findViewById(R.id.email);
        EditText description = (EditText) findViewById(R.id.description);
        Button save = (Button) findViewById(R.id.save);
        Button cancel = (Button) findViewById(R.id.cancel);
        ImageView icback = (ImageView) findViewById(R.id.ic_back);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objet objet = new Objet();
                objet.setNom(nom.getText().toString());
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
                                if (user.getPrenom()!= null && user.getNom()!=null && user.getPrenom()!="" && user.getNom()!=""){
                                    objet.setUser(user.getPrenom() + " " + user.getNom());
                                }
                                if (adresse.getText().toString().equals("")){
                                    objet.setAdresse(user.getAdresse());
                                }else{
                                    objet.setAdresse(adresse.getText().toString());
                                }
                                if(tel.getText().toString().equals("")){
                                    objet.setTel(user.getTel());
                                }else {
                                    objet.setTel(tel.getText().toString());
                                }
                                if (email.getText().toString().equals("")) {
                                    objet.setEmail(user.getEmail());
                                } else {
                                    objet.setEmail(email.getText().toString());
                                }
                                objet.setDescription(description.getText().toString());
                                objet.setStatut(statut.getSelectedItem().toString());
                            }
                        } else {
                            // La requête a échoué
                        }
                    }
                });
                CollectionReference objetsCollection = db.collection("objets");
                objetsCollection.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                int nombreDocuments = querySnapshot.size();
                                objet.setId(nombreDocuments+1);
                                objetsCollection.document(String.valueOf(nombreDocuments+1)).set(objet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        toast("L'objet a été ajouté avec succès");
                                        nom.setText("");
                                        adresse.setText("");
                                        tel.setText("");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toast("L'objet n'a pas été ajouté");
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toast("La recupération du nombre de documents a échoué");
                            }
                        });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentspace = new Intent(getApplicationContext(),SpaceActivity.class);
                startActivity(intentspace);
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
}
