package xami.xeeshan.db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getSimpleName();
    DatabseHelper databseHelper;
    EditText editText;
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databseHelper=new DatabseHelper(this,databseHelper.DB_NAME,null,1);
        editText=(EditText)findViewById(R.id.EDITTEXT1);
        btn1=(Button)findViewById(R.id.savebtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean isInserted = databseHelper.insertData(editText.getText().toString());

                    if (isInserted == true)
                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                }catch (android.database.SQLException e)
                {
                    Log.e(TAG,e.getMessage());
                }
            }
        });
    }
}
