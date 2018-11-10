package in.ac.gndec.tnp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.sendEmptyMessageDelayed(1234,1500);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==1234){

                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this,LoginSignup.class));
                    finish();
                }


            }

        }
    };

}
