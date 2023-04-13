package sn.flexzone.lourer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ModifprofilActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private int points;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifprofil_layout);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        EditText prenom = (EditText) findViewById(R.id.prenom);
        EditText nom = (EditText) findViewById(R.id.nom);
        EditText pseudo = (EditText) findViewById(R.id.pseudo);
        EditText age = (EditText) findViewById(R.id.age);
        EditText adresse = (EditText) findViewById(R.id.adresse);
        EditText adresseplus = (EditText) findViewById(R.id.adresseplus);
        EditText tel = (EditText) findViewById(R.id.tel);
        EditText email = (EditText) findViewById(R.id.email);
        Button save = (Button) findViewById(R.id.save);
        Button cancel = (Button) findViewById(R.id.cancel);

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

                        if (user.getPrenom()!=null && user.getPrenom()!=""){
                            prenom.setText(user.getPrenom());
                        }
                        if (user.getNom()!=null && user.getNom()!=""){
                            nom.setText(user.getNom());
                        }
                        if (user.getPseudo()!=null && user.getPseudo()!=""){
                            pseudo.setText(user.getPseudo());
                        }
                        if (user.getTel()!=null && user.getTel()!=""){
                            tel.setText(user.getTel());
                        }
                        if (user.getEmail()!=null && user.getEmail()!=""){
                            email.setText(user.getEmail());
                        }
                        if (user.getAge()!=0){
                            age.setText(String.valueOf(user.getAge()));
                        }
                        if (user.getAdresse()!=null && user.getAdresse()!=""){
                            adresse.setText(user.getAdresse());
                        }
                        if (user.getAdresseplus()!=null && user.getAdresseplus()!=""){
                            adresseplus.setText(user.getAdresseplus());
                        }
                        setPoints(user.getPoints());
                    }
                } else {
                    // La requête a échoué
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersCollection.document(mAuth.getUid()).update("prenom", prenom.getText().toString(),"nom", nom.getText().toString(),
                                "pseudo", pseudo.getText().toString(),"adresse", adresse.getText().toString(),"tel", tel.getText().toString(),
                                "adresseplus", adresseplus.getText().toString(), "email", email.getText().toString(), "age", Integer.parseInt(age.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                toast("Votre profil a été bien mis à jour");
                                Intent intentprofil = new Intent(getApplicationContext(), ProfilActivity.class);
                                startActivity(intentprofil);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toast("La mise à jour a échouée");
                            }
                        });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentprofil = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intentprofil);
            }
        });
    }
    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
