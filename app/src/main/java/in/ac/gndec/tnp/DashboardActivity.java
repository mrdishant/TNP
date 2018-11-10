package in.ac.gndec.tnp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initauth();
        initlisteners();
    }

    private void initlisteners() {

        findViewById(R.id.profile).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.notifications).setOnClickListener(this);
        findViewById(R.id.tips).setOnClickListener(this);
        findViewById(R.id.contactus).setOnClickListener(this);
        findViewById(R.id.cvupload).setOnClickListener(this);
        findViewById(R.id.members).setOnClickListener(this);
        findViewById(R.id.jobs).setOnClickListener(this);

    }

    private void initauth() {

        auth=FirebaseAuth.getInstance();

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(auth.getCurrentUser()==null){
                    startActivity(new Intent(DashboardActivity.this,LoginSignup.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }else{

                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_settings){
          auth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.profile){
            startActivity(new Intent(DashboardActivity.this,Profile.class));
            return;
        }else if(view.getId()==R.id.logout){
            auth.signOut();
            return;
        }else{
            Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
        }

    }
}
