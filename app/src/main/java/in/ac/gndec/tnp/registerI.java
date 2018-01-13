package in.ac.gndec.tnp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
interface change{
     void changePage(int position);
}

public class registerI extends Fragment {

    Student student;

    MaterialEditText college,university,fname,mname,foocupation,moccupation,mcontact,fcontact;
    ProgressBar progressBar;

    public registerI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_register_i, container, false);

        college=(MaterialEditText)view.findViewById(R.id.college);
        university=(MaterialEditText)view.findViewById(R.id.university);
        fname=(MaterialEditText)view.findViewById(R.id.fname);
        mname=(MaterialEditText)view.findViewById(R.id.mname);
        foocupation=(MaterialEditText)view.findViewById(R.id.foccupation);
        moccupation=(MaterialEditText)view.findViewById(R.id.moccupation);
        mcontact=(MaterialEditText)view.findViewById(R.id.mcontact);
        fcontact=(MaterialEditText)view.findViewById(R.id.fcontact);

        progressBar=(ProgressBar)view.findViewById(R.id.progress);



        FloatingActionButton floatingActionButton=(FloatingActionButton)view.findViewById(R.id.fab1);
        floatingActionButton.setBackgroundColor(Color.TRANSPARENT);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                if(validate()){

                    FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                student=documentSnapshot.toObject(Student.class);
                                setValues();
                            }
                        }
                    });

                }

            }
        });


        return view;
    }

    private void setValues() {

        student.setCollegeroll(Long.parseLong(college.getText().toString()));
        student.setUniversityroll(Long.parseLong(university.getText().toString()));


        Guardian father=new Guardian();
        father.setName(fname.getText().toString());
        father.setOccupation(foocupation.getText().toString());
        father.setContact(fcontact.getText().toString());

        student.setFather(father);


        Guardian mother=new Guardian();
        mother.setName(mname.getText().toString());
        mother.setOccupation(moccupation.getText().toString());
        mother.setContact(mcontact.getText().toString());

        student.setMother(mother);

        savedata();
    }


    private void savedata() {


        FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                .set(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        change i=(change) getContext();
                        i.changePage(1);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }


    private boolean validate() {

        if(college.getText().toString().isEmpty()){
            college.setError("Roll No Required");
            college.requestFocus();
            return false;
        }
        if(university.getText().toString().isEmpty()){
            university.setError("Roll No Required");
            university.requestFocus();
            return false;
        }

        if(fname.getText().toString().isEmpty()){
            fname.setError("Name Required");
            fname.requestFocus();
            return false;
        }

        if(mname.getText().toString().isEmpty()){
            mname.setError("Name Required");
            mname.requestFocus();
            return false;
        }

        if(foocupation.getText().toString().isEmpty()){
            foocupation.setError("Occupation Required");
            foocupation.requestFocus();
            return false;
        }


        if(moccupation.getText().toString().isEmpty()){
            moccupation.setError("Occupation Required");
            moccupation.requestFocus();
            return false;
        }


        if(fcontact.getText().toString().length()<10){
            fcontact.setError("Valid Contact Required");
            fcontact.requestFocus();
            return false;
        }


        if(mcontact.getText().toString().length()<10){
            mcontact.setError("Valid Contact Required");
            mcontact.requestFocus();
            return false;
        }

        return true;

    }




}
