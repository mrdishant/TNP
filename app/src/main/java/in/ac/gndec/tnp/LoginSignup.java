package in.ac.gndec.tnp;

import android.content.Intent;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class LoginSignup extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN =7623 ;
    EditText name,email,password;
    Button login;
    TextView newAccount;
    GoogleSignInButton googleSignInButton;
    FirebaseAuth auth;
    ProgressBar progressBar;

    private GoogleSignInClient signInClient;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        initviews();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient= GoogleSignIn.getClient(getApplicationContext(),gso);

    }

    private void initviews() {

        login=(Button)findViewById(R.id.login);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        googleSignInButton=(GoogleSignInButton)findViewById(R.id.googlebutton);
        newAccount=(TextView)findViewById(R.id.newaccount);
        progressBar=(ProgressBar)findViewById(R.id.progress);

        login.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
        newAccount.setOnClickListener(this);

        auth=FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {

        if(view == newAccount){
            if(newAccount.getText().equals("Create New Account")){
                newAccount.setText("Already a Member? Login Here");
                name.setVisibility(View.VISIBLE);
                googleSignInButton.setText("Sign Up with Google");
                login.setText("Sign Up");
            }else{
                newAccount.setText("Create New Account");
                name.setVisibility(View.GONE);
                googleSignInButton.setText("Sign In with Google");
                login.setText("Login");
            }
        }

        if(view == login){

            String e=email.getText().toString();
            String p=password.getText().toString();

            if(login.getText().equals("Login")){
                if (validate(e,p,null)){
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginSignup.this,MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else{
                final String n=name.getText().toString();
                if(validate(e,p,n)){
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Student student=new Student();
                                student.setName(n);
                                FirebaseFirestore.getInstance().collection("Students").document(auth.getUid()).set(student)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.GONE);
                                                if(task.isSuccessful()){
                                                    register();
                                                }else{
                                                    Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }

        if(view == googleSignInButton){
                signIn();
        }

    }

    private boolean validate(String e,String p,String n) {

        if(e.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return false;
        }


        if(p.isEmpty()){
            password.setError("Password is Required");
            password.requestFocus();
            return false;
        }

        if(n != null){

            if(n.isEmpty()){
                name.setError("Name is Required");
                name.requestFocus();
                return false;
            }
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(e).find()){
            email.setError("Valid Email is Required");
            email.requestFocus();
            return false;
        }


        if(p.length()<6){
            password.setError("Minimum 6 Characters Required");
            password.requestFocus();
            return false;
        }

        return true;
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Google sign in failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseFirestore.getInstance().collection("Students").document(auth.getUid())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(!documentSnapshot.exists()){
                                            Student student=new Student();
                                            student.setName(auth.getCurrentUser().getDisplayName());
                                            FirebaseFirestore.getInstance().collection("Students").document(auth.getUid()).set(student)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressBar.setVisibility(View.GONE);
                                                            if(task.isSuccessful()){
                                                                register();
                                                            }else{
                                                                Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }else {
                                            startActivity(new Intent(LoginSignup.this,MainActivity.class));
                                            finish();
                                        }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void register() {

        startActivity(new Intent(LoginSignup.this, RegisterUser.class));
        finish();

    }


}
