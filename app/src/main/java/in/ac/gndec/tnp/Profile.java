package in.ac.gndec.tnp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.Student;

public class Profile extends AppCompatActivity {

    DialogLoading dialogLoading;
    Student student;
    Card1 card1;
    Card2 card2;
    ViewPager viewPager;
    SectionAdapter sectionAdapter;
    TextView fname,foccupation,fcontact,mname,mcontact,moccupation;
    TextView school10,board10,percentage10,school12,board12,percentage12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dialogLoading=new DialogLoading(Profile.this);
        dialogLoading.showdialog("Loading");
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


        card1=new Card1();
        card2=new Card2();

        fcontact=(TextView) findViewById(R.id.fcontact);
        foccupation=(TextView) findViewById(R.id.foccupation);
        fname=(TextView) findViewById(R.id.fname);

        sectionAdapter=new SectionAdapter(getSupportFragmentManager());
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(sectionAdapter);
        viewPager.setPageTransformer(true,new FlipHorizontalTransformer());

        school10=(TextView)findViewById(R.id.i10school);
        board10=(TextView)findViewById(R.id.i10board);
        percentage10=(TextView)findViewById(R.id.i10percentage);
        school12=(TextView)findViewById(R.id.i12school);
        board12=(TextView)findViewById(R.id.i12board);
        percentage12=(TextView)findViewById(R.id.i12percentage);


        mcontact=(TextView) findViewById(R.id.mcontact);
        moccupation=(TextView) findViewById(R.id.moccupation);
        mname=(TextView) findViewById(R.id.mname);



        setvalues();

    }

    private void setvalues() {

        FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    student=documentSnapshot.toObject(Student.class);

                    card1.setvalues(student);
                    card2.setvalues(student);

                    if(student.father!=null){

                        foccupation.setText("Occupation : "+student.father.occupation);
                        fcontact.setText("Contact : "+student.father.contact);
                        fname.setText("Name : "+student.father.name);
                    }
                    if(student.mother!=null){

                        moccupation.setText("Occupation : "+student.mother.occupation);
                        mcontact.setText("Contact : "+student.mother.contact);
                        mname.setText("Name : "+student.mother.name);
                    }
                    FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid()).collection("Marksheets")
                            .document("10th").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                school10.setText(documentSnapshot.getString("school"));
                                board10.setText(documentSnapshot.getString("board"));
                                percentage10.setText(documentSnapshot.getString("percentage"));
                            }
                        }
                    });
                    FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid()).collection("Marksheets")
                            .document("12th").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                school12.setText(documentSnapshot.getString("school"));
                                board12.setText(documentSnapshot.getString("board"));
                                percentage12.setText(documentSnapshot.getString("percentage"));
                            }
                        }
                    });
                    FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid()).collection("Marksheets")
                            .document("Diploma").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                school12.setText(documentSnapshot.getString("school"));
                                board12.setText(documentSnapshot.getString("board"));
                                percentage12.setText(documentSnapshot.getString("percentage"));
                            }
                        }
                    });

                    dialogLoading.dismiss();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profilemeu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit){
           startActivity(new Intent(Profile.this,RegisterUser.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
           finish();
        }

        return super.onOptionsItemSelected(item);
    }

    class SectionAdapter extends FragmentPagerAdapter{

        public SectionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position==0){
                return card1;
            }else{
                return card2;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
