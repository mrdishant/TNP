package in.ac.gndec.tnp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import Model.Student;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Card1 extends Fragment {


    TextView name,branch,college,university,shift;
    CircleImageView imageView;

    public Card1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_card1, container, false);

        shift=(TextView)view.findViewById(R.id.shift);
        branch=(TextView)view.findViewById(R.id.branch);
        university=(TextView)view.findViewById(R.id.uroll);
        name=(TextView)view.findViewById(R.id.name);
        college=(TextView)view.findViewById(R.id.croll);
        imageView=(CircleImageView)view.findViewById(R.id.image);



        return view;
    }

    void setvalues(Student student){

        shift.setText("Shift : "+student.shift);
        branch.setText("Branch : "+student.branch);
        college.setText("College Roll No : "+student.collegeroll);
        university.setText("University Roll No : "+student.universityroll);
        name.setText("Name : "+student.name);

        Glide.with(getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).centerCrop().into(imageView);

    }

}
