package sn.flexzone.lourer;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 345;
    FirebaseFirestore db;
    CallbackManager mCallbackManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        mCallbackManager = CallbackManager.Factory.create();
        db = FirebaseFirestore.getInstance();

        EditText email = (EditText) findViewById(R.id.editemail);
        EditText password = (EditText) findViewById(R.id.editpass);
        EditText confirm = (EditText) findViewById(R.id.editconfirm);
        TextView login = (TextView) findViewById(R.id.btnbylogin);
        Button signup = (Button) findViewById(R.id.signup);
        ImageView btnfacebook = (ImageView) findViewById(R.id.btnfacebook);
        ImageView btngoogle = (ImageView) findViewById(R.id.btngoogle);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString() != "" && password.getText().toString() != "" && confirm.getText().toString()!= ""){
                    String textemail = email.getText().toString();
                    String textpass = password.getText().toString();
                    String textconfirm = confirm.getText().toString();
                    if(textpass.equals(textconfirm)){
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(textemail, textpass)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        toast("Vous avez été inscrit avec succès");
                                        User user = new User(textemail,textpass);
                                        user.setId(mAuth.getUid());
                                        CollectionReference usersCollection = db.collection("users");
                                        usersCollection.document(mAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intentlogin = new Intent(getApplicationContext(),LoginActivity.class);
                                                startActivity(intentlogin);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                toast("Vos informations personnelles ne sont pas enrigistrées");
                                            }
                                        });
                                    } else {
                                        toast("Une erreur s'est produite lors de votre inscription");
                                    }
                                });
                    }else{
                        toast("Les mots de passe ne correspondent pas");
                    }
                }else {
                    toast("Les identifiants ne sont pas normalement remplies");
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentlogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intentlogin);
            }
        });
        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Connexion réussie, obtenez le jeton d'accès Facebook
                        AccessToken accessToken = loginResult.getAccessToken();

                        // Créez une authentification avec Facebook
                        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

                        // Connectez-vous avec Firebase
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Connexion réussie avec Firebase
                                    } else {
                                        // Erreur lors de la connexion avec Firebase
                                    }
                                });
                    }

                    @Override
                    public void onCancel() {
                        // Connexion annulée
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // Erreur lors de la connexion
                    }
                });


            }
        });
        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SignupActivity.this, gso);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {

                            } else {
                                // No user is signed in
                                // ...
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // ...
                        }

                        // ...
                    }
                });
    }

}
