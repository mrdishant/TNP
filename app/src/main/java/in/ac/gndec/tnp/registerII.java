package in.ac.gndec.tnp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import Interface.change;
import Model.Student;


/**
 * A simple {@link Fragment} subclass.
 */
public class registerII extends Fragment {


    MaterialEditText height,weight,category;
    MaterialAutoCompleteTextView branch;
    Spinner shift,blood_group;
    Student student;
    DialogLoading dialogLoading;

    public registerII() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register_ii, container, false);

        dialogLoading=new DialogLoading(getContext());
        dialogLoading.setCancelable(false);
        ImageView floatingActionButton=(ImageView) view.findViewById(R.id.fab1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change i=(change) getContext();
                i.changePage(0);
            }
        });

        ImageView floatingActionButton2=(ImageView) view.findViewById(R.id.fab2);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    dialogLoading.showdialog("Loading..");
                                    student=documentSnapshot.toObject(Student.class);
                                    setValues();
                                }else{
                                    Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }

            }
        });


        initviews(view);


        FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    student=documentSnapshot.toObject(Student.class);
                    setValues1();
                }else{
                    Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    private void setValues1() {


        if(student==null){
            return;
        }

        category.setText(student.category);
        height.setText(""+student.height);
        weight.setText(""+student.weight);
        if(student.shift!=null && student.shift.equals("Evening")){
            shift.setSelection(1);
        }else{
            shift.setSelection(0);
        }
        branch.setText(student.branch);

    }

    private boolean validate() {

        if(branch.getText().toString().isEmpty()){
            branch.setError("Required Field");
            branch.requestFocus();
            return  false;
        }


        if(category.getText().toString().isEmpty()){
            category.setError("Required Field");
            category.requestFocus();
            return  false;
        }


        if(height.getText().toString().isEmpty()){
            height.setError("Required Field");
            height.requestFocus();
            return  false;
        }

        if(weight.getText().toString().isEmpty()){
            weight.setError("Required Field");
            weight.requestFocus();
            return  false;
        }


        return true;
    }

    private void setValues() {

        student.setBranch(branch.getText().toString());
        student.setShift(shift.getSelectedItem().toString());
        student.setBlood_group(blood_group.getSelectedItem().toString());
        student.setCategory(category.getText().toString());
        student.setHeight(Float.parseFloat(height.getText().toString()));
        student.setWeight(Float.parseFloat(weight.getText().toString()));

        savedata();


    }

    private void savedata() {

        FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid())
                .set(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialogLoading.dismiss();
                    if(task.isSuccessful()){
                        change i=(change) getContext();
                        i.changePage(2);
                    }
            }
        });

    }

    private void initviews(View view) {

        height=(MaterialEditText)view.findViewById(R.id.height);
        weight=(MaterialEditText)view.findViewById(R.id.weight);
        category=(MaterialEditText)view.findViewById(R.id.category);



        blood_group=(Spinner)view.findViewById(R.id.blood);
        shift=(Spinner)view.findViewById(R.id.shift);

        branch=(MaterialAutoCompleteTextView) view.findViewById(R.id.branch);


        ArrayAdapter<String> bloodgroup=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line);
        bloodgroup.add("A+");
        bloodgroup.add("A-");
        bloodgroup.add("B+");
        bloodgroup.add("B-");
        bloodgroup.add("O+");
        bloodgroup.add("O-");
        bloodgroup.add("AB+");
        bloodgroup.add("AB-");

        blood_group.setAdapter(bloodgroup);


        ArrayAdapter<String> shift1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line);
        shift1.add("Morning");
        shift1.add("Evening");

        shift.setAdapter(shift1);


        ArrayAdapter<String> Branches=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line);
        Branches.add("Computer Science");
        Branches.add("Information Technology");
        Branches.add("Mechanical Engineering");
        Branches.add("Production Engineering");
        Branches.add("Civil Engineering");

        branch.setAdapter(Branches);

    }

}
