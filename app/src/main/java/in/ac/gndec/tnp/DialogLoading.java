package in.ac.gndec.tnp;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by mrdis on 2/4/2018.
 */

public class DialogLoading {

    Context context;
    Dialog dialog;

    public DialogLoading(Context context) {
        dialog=new Dialog(context);
    }

    public void showdialog(String text){

        dialog.setContentView(R.layout.loadingdialog);
        TextView textView=(TextView)dialog.findViewById(R.id.message);
        textView.setText(text);
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void setCancelable(boolean value){
        dialog.setCancelable(value);
    }


}
