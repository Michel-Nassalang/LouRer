package sn.flexzone.lourer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

public class ObjetActivity extends AppCompatActivity {
    private int objetId = 0;
    private Objet sobjet = new Objet();
    FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.objet_activity);
        TextView nom = (TextView) findViewById(R.id.nom);
        TextView statut = (TextView) findViewById(R.id.statut);
        TextView username = (TextView) findViewById(R.id.username);
        TextView adresse = (TextView) findViewById(R.id.adresse);
        TextView tel = (TextView) findViewById(R.id.telephone);
        TextView description = (TextView) findViewById(R.id.description);
        TextView email = (TextView) findViewById(R.id.email);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ImageView icback = (ImageView) findViewById(R.id.ic_back);
        Button state = (Button) findViewById(R.id.state);
        Button semail = (Button) findViewById(R.id.semail);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Intent recapIntent = getIntent();
        if(recapIntent != null){
            objetId = recapIntent.getExtras().getInt("objetId");
        }

        CollectionReference contactsCollection = db.collection("objets");
        Query query = contactsCollection.whereEqualTo("id", objetId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.isEmpty()) {
                        toast("L'objet n'existe pas");
                    } else {
                        // Le document existe
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        Objet objet = documentSnapshot.toObject(Objet.class);
                        nom.setText(objet.getNom());
                        description.setText(objet.getDescription());
                        statut.setText(objet.getStatut());
                        adresse.setText(objet.getAdresse());
                        tel.setText(objet.getTel());
                        username.setText(objet.getUser());
                        email.setText(objet.getEmail());
                        if(objet.getImg()!=null){
                            if (objet.getImg()!=""){
                                Picasso.get().load(objet.getImg()).into(imageView);
                            }
                        }
                        sobjet = objet;
                        if (objet.getStatut()!= null && objet.getStatut().equals("Trouvé")){
                            state.setText("Déclarer perdu");
                        }else {
                            state.setText("Déclarer Trouvé");
                        }
                    }
                } else {
                    toast("La recherche de l'objet a occasionné une erreur");
                }
            }
        });

        CollectionReference objetsCollection = db.collection("objets");
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statut.getText().toString().equals("Trouvé")){
                    objetsCollection.document(String.valueOf(objetId)).update("statut", "Perdu")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    toast("Vous avez déclaré l'objet comme étant perdu");
                                    statut.setText("Perdu");
                                    state.setText("Déclarer trouvé");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast("La mise à jour a échouée");
                                }
                            });
                }else{
                    objetsCollection.document(String.valueOf(objetId)).update("statut", "Trouvé")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    toast("Vous avez déclaré l'objet comme étant trouvé");
                                    statut.setText("Trouvé");
                                    state.setText("Déclarer perdu");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast("La mise à jour a échouée");
                                }
                            });

                    //Envoie de notifications par message
                    Intent intentsms = new Intent(Intent.ACTION_SENDTO);
                    intentsms.setData(Uri.parse("smsto:" + sobjet.getTel()));
                    intentsms.putExtra("sms_body", "L'objet que vous avez déclaré perdu avec les caractéristiques suivantes a été trouvé: " + sobjet.toString());
                    startActivity(intentsms);

                }
            }
        });

        semail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + Uri.encode(email.getText().toString())));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Objet trouvé: "+nom.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, "L'objet que vous avez déclaré perdu avec les caractéristiques suivantes a été trouvé: " + sobjet.toString());
                PackageManager packageManager = getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(intent, "Envoyer le mail par:"));
                } else {
                    toast( "Aucune application de messagerie électronique n'est installée.");
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
}
