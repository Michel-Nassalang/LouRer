package sn.flexzone.lourer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ProfilActivity extends AppCompatActivity {
    FirebaseFirestore db;

    int CAMERA_REQUEST_CODE = 43;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_activity);
        ImageView btnback = (ImageView) findViewById(R.id.btnback);

        ImageView imageprofil = (ImageView) findViewById(R.id.imageurl);
        TextView name = (TextView) findViewById(R.id.name);
        TextView pseudo = (TextView) findViewById(R.id.pseudo);
        TextView age = (TextView) findViewById(R.id.age);
        TextView points = (TextView) findViewById(R.id.points);

        ImageView pmprofil = (ImageView) findViewById(R.id.profilpm);
        TextView adresse = (TextView) findViewById(R.id.adresse);
        TextView adresseplus = (TextView) findViewById(R.id.adresseplus);
        TextView btnmodif = (TextView) findViewById(R.id.modif);
        ImageView btnicmodif = (ImageView) findViewById(R.id.icmodif);
        ImageView btnicmodifprofil = (ImageView) findViewById(R.id.icmodifprofil);

        TextView tel = (TextView) findViewById(R.id.tel);

        TextView email = (TextView) findViewById(R.id.email);

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
                        if (user.getPrenom() != null && user.getNom() != null && user.getPrenom() !="" && user.getNom() !=""){
                            name.setText(user.getPrenom() + " " + user.getNom());
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
                        if (user.getPoints()!=0){
                            points.setText(String.valueOf(user.getPoints()));
                        }
                        if (user.getAdresse()!=null && user.getAdresse()!=""){
                            adresse.setText(user.getAdresse());
                        }
                        if (user.getAdresseplus()!=null && user.getAdresseplus()!=""){
                            adresseplus.setText(user.getAdresseplus());
                        }
                        if(user.getImage()!=null){
                            if (user.getImage()!=""){
                                Picasso.get().load(user.getImage()).into(imageprofil);
                                Picasso.get().load(user.getImage()).into(pmprofil);
                            }
                        }
                    }
                } else {
                    // La requête a échoué
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentspace = new Intent(getApplicationContext(), SpaceActivity.class);
                startActivity(intentspace);
            }
        });
        btnicmodif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmodif = new Intent(getApplicationContext(), ModifprofilActivity.class);
                startActivity(intentmodif);
            }
        });
        btnmodif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmodif = new Intent(getApplicationContext(), ModifprofilActivity.class);
                startActivity(intentmodif);
            }
        });
        btnicmodifprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
    }
    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
