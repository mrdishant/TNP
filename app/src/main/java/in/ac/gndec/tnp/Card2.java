package in.ac.gndec.tnp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import Model.Student;


/**
 * A simple {@link Fragment} subclass.
 */
public class Card2 extends Fragment {

    TextView height,weight,category,blood,details;
    public Card2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_card2, container, false);
        category=(TextView)view.findViewById(R.id.category);
        height=(TextView)view.findViewById(R.id.height);
        weight=(TextView)view.findViewById(R.id.weight);
        blood=(TextView)view.findViewById(R.id.blood);
        details=(TextView)view.findViewById(R.id.details);

        return view;
    }


    void setvalues(Student student){

        category.setText("Category : "+student.category);
        height.setText("Height : "+student.height);
        weight.setText("Weight : "+student.weight);
        blood.setText("Blood : "+student.blood_group);

        details.setText("UID : "+ FirebaseAuth.getInstance().getUid()+"\nEmail : "+FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

}
