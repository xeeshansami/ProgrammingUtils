package com.comsol.fleamarket_demo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class LaunchApp extends AppCompatActivity {
    EditText editText;
    Button submit;
    DatabaseReference rootRef,demoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fierbasecheck);
        editText = (EditText) findViewById(R.id.editText);
        submit = (Button) findViewById(R.id.button2);
        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        demoRef = rootRef.child("demo");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString();
                //push creates a unique id in database
                demoRef.push().setValue(value);
            }
        });
    }
}