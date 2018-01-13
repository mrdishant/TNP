package in.ac.gndec.tnp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    ImageView imageView;
    TextView details;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initviews();
        inittoolbar();
    }

    private void inittoolbar() {
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initviews() {

        imageView=(ImageView)findViewById(R.id.image);
        details=(TextView)findViewById(R.id.details);

        setvalues();

    }

    private void setvalues() {

        FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    student=documentSnapshot.toObject(Student.class);
                    details.setText(student.toString());
                    Glide.with(Profile.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imageView);
                }
            }
        });

    }


}
