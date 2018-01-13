package in.ac.gndec.tnp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class registerIV extends Fragment implements View.OnClickListener {


    private static final int REQUEST_CODE = 1234 ;
    MaterialEditText marks,total,board,school,percentage;
    TextView details;
    ImageView imageView;
    MarkSheet markSheet;
    ProgressDialog progressDialog;
    FloatingActionButton floatingActionButton;
    boolean loaded=false;
    String doc;
    CardView cardView;
    RadioButton plus2,diploma;


    public registerIV() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register_iv, container, false);

        initViews(view);

        return view;
    }

    private void initViews(final View view) {

        marks=(MaterialEditText)view.findViewById(R.id.marks);
        board=(MaterialEditText)view.findViewById(R.id.board);
        school=(MaterialEditText)view.findViewById(R.id.school);
        percentage=(MaterialEditText)view.findViewById(R.id.percentage);
        total=(MaterialEditText)view.findViewById(R.id.total);
        cardView=view.findViewById(R.id.plus2);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Saving");
        progressDialog.setCancelable(false);

        plus2=(RadioButton)view.findViewById(R.id.radio1);
        diploma=(RadioButton)view.findViewById(R.id.radio2);


        details=(TextView)view.findViewById(R.id.details);

        imageView=(ImageView)view.findViewById(R.id.imageview);

        markSheet=new MarkSheet();
        imageView.setOnClickListener(this);

        plus2.setOnClickListener(this);
        diploma.setOnClickListener(this);


        view.findViewById(R.id.mywidget).setSelected(true);

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(this);

        marks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    if(total.getText().toString().isEmpty()){
                        return;
                    }
                    percentage.setText(""+Float.parseFloat(marks.getText().toString())*100/Float.parseFloat(total.getText().toString()));
                }else{
                    percentage.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    if(marks.getText().toString().isEmpty()){
                        return;
                    }
                    percentage.setText(""+Float.parseFloat(marks.getText().toString())*100/Float.parseFloat(total.getText().toString()));
                }else{
                    percentage.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view == imageView){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE);
        }

        if(view == floatingActionButton){
            if(validate()){
                setvalues();
            }

        }

        if(view==diploma){
            cardView.setVisibility(View.VISIBLE);
            details.setText("Diploma:");
            doc="Diploma";

        }

        if(view==plus2){
            cardView.setVisibility(View.VISIBLE);
            details.setText("12th Standard:");
            doc="12th";
        }

    }

    private boolean validate() {

        if(marks.getText().toString().isEmpty()){
            marks.setError("Required Field");
            marks.requestFocus();
            return false;
        }


        if(school.getText().toString().isEmpty()){
            school.setError("Required Field");
            school.requestFocus();
            return false;
        }


        if(board.getText().toString().isEmpty()){
            board.setError("Required Field");
            board.requestFocus();
            return false;
        }


        if(total.getText().toString().isEmpty()){
            total.setError("Required Field");
            total.requestFocus();
            return false;
        }

        if(!loaded){
            Toast.makeText(getContext(),"Image Required",Toast.LENGTH_SHORT).show();
            imageView.requestFocus();
            return false;
        }

        return true;
    }

    private void setvalues() {

        markSheet.setBoard(board.getText().toString());
        markSheet.setMarksobtained(marks.getText().toString());
        markSheet.setTotal(total.getText().toString());
        markSheet.setPercentage(percentage.getText().toString());
        markSheet.setSchool(school.getText().toString());

        progressDialog.show();

        FirebaseFirestore.getInstance().collection("Students").document(FirebaseAuth.getInstance().getUid()).collection("Marksheets").document(doc)
                .set(markSheet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    change i=(change)getActivity();
                    i.changePage(3);
                }else{
                    Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            if(resultCode==getActivity().RESULT_OK){
                if(data.getData()!=null){
                    imageView.setImageURI(data.getData());
                    loaded=true;
                    upload(data.getData());
                }
            }else{
                Toast.makeText(getContext(),"Cancelled",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void upload(Uri data) {
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("Marksheets/"+FirebaseAuth.getInstance().getUid()+"/"+System.currentTimeMillis()).putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getDownloadUrl()!=null){
                            progressDialog.dismiss();
                            markSheet.setPicture(taskSnapshot.getDownloadUrl().toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}
