package in.ac.gndec.tnp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import Model.Student;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginSignup extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN =7623 ;
    private static final int REQUEST_CODE =7567 ;
    EditText name,email,password;
    Button login,logintxt,signuptxt;
    TextView newAccount;
    GoogleSignInButton googleSignInButton;
    FirebaseAuth auth;
    ImageView logo;
    CircleImageView profile;
    ProgressBar progressBar;
    DialogLoading dialogLoading;
    private GoogleSignInClient signInClient;
    private GoogleSignInOptions gso;
    boolean loaded=false;
    private Uri url2;


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


        dialogLoading=new DialogLoading(LoginSignup.this);
        dialogLoading.setCancelable(false);
        login=(Button)findViewById(R.id.login);
        logintxt=(Button)findViewById(R.id.loginbtn);
        signuptxt=(Button)findViewById(R.id.signupbtn);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        googleSignInButton=(GoogleSignInButton)findViewById(R.id.googlebutton);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        logo=(ImageView)findViewById(R.id.logo);
        profile=(CircleImageView)findViewById(R.id.profile);
        newAccount=(TextView)findViewById(R.id.textsignup);
        profile.setOnClickListener(this);
        login.setOnClickListener(this);
        logintxt.setOnClickListener(this);
        signuptxt.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
        newAccount.setOnClickListener(this);

        auth=FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {

        if(view==profile){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE);
        }

        if(view == newAccount){
            logo.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            newAccount.setVisibility(View.GONE);
            logintxt.setBackgroundResource(android.R.color.transparent);
            signuptxt.setBackgroundResource(R.drawable.btnback);
            name.setVisibility(View.VISIBLE);
            googleSignInButton.setText("Sign Up with Google");
            login.setText("Sign Up");
        }

        if(view == signuptxt){

            logo.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            newAccount.setVisibility(View.GONE);
            logintxt.setBackgroundResource(android.R.color.transparent);
            signuptxt.setBackgroundResource(R.drawable.btnback);
            name.setVisibility(View.VISIBLE);
            googleSignInButton.setText("Sign Up with Google");
            login.setText("Sign Up");

        }

        if(view == logintxt){

            logo.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
            signuptxt.setBackgroundResource(android.R.color.transparent);
            logintxt.setBackgroundResource(R.drawable.btnback);
            newAccount.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            googleSignInButton.setText("Sign In with Google");
            login.setText("Login");
        }

        if(view == login){

            String e=email.getText().toString();
            String p=password.getText().toString();

            if(login.getText().equals("Login")){
                if (validate(e,p,null,true)){
                    dialogLoading.showdialog("Loging in..");
                    auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginSignup.this,DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }else{
                                dialogLoading.dismiss();
                                Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else{
                final String n=name.getText().toString();
                if(validate(e,p,n,loaded)){
                    dialogLoading.showdialog("Authenticating...");
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
                                                if(task.isSuccessful()){
                                                    upload(url2);
                                                }else{
                                                    Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }else{
                                dialogLoading.dismiss();
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

    private boolean validate(String e,String p,String n,boolean loaded1) {

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

        if(!loaded1){
            Toast.makeText(getApplicationContext(),"Image Required",Toast.LENGTH_SHORT).show();
            profile.requestFocus();
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

        if(requestCode==REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                if(data.getData()!=null){
                    Glide.with(getApplicationContext()).load(data.getData()).centerCrop().into(profile);
                    loaded=true;
                    url2=data.getData();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
            }
        }


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
        dialogLoading.showdialog("Authenticating..");
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseFirestore.getInstance().collection("Students").document(auth.getUid())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                           startActivity(new Intent(LoginSignup.this,DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                           finish();
                                        }else { Student student=new Student();
                                            student.setName(auth.getCurrentUser().getDisplayName());
                                            FirebaseFirestore.getInstance().collection("Students").document(auth.getUid()).set(student)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                register();
                                                            }else{
                                                                Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });

    }

    private void register() {

        startActivity(new Intent(LoginSignup.this, RegisterUser.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();

    }

    private void upload(Uri data) {


        FirebaseStorage.getInstance().getReference("Students/"+FirebaseAuth.getInstance().getUid()).putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getDownloadUrl()!=null){

                            change(taskSnapshot.getDownloadUrl());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogLoading.dismiss();
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void change(Uri url){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString())
                .setPhotoUri(url)
                .build();

        auth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            register();
                        }
                    }
                });
    }


}
