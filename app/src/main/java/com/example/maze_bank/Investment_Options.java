package com.example.maze_bank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Investment_Options extends AppCompatActivity implements AdapterView.OnItemSelectedListener {




    Spinner spinner;
    TextView baldisp,calcamt;
    EditText principal;
    int p=0,t=2,bal=0;
    double r=1.5;
    double returnn;
    Button calc,invstbtn;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_options);



        spinner=findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        baldisp=findViewById(R.id.bal);
        principal=findViewById(R.id.Investment);
        calc=findViewById(R.id.calculate);
        calcamt=findViewById(R.id.calcamt);
        invstbtn=findViewById(R.id.investbtn);
        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        userID=fAuth.getCurrentUser().getUid();
        cal=Calendar.getInstance();

       ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Constants.accountlist);
       spinner.setAdapter(adapter);
       spinner.setOnItemSelectedListener(this);



        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=Integer.parseInt(principal.getText().toString());
                returnn=((p*r*t)/100)+p;
                String fresult= Double.toString(returnn);
                calcamt.setText( fresult);



            }
        });












//        invstbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Toast.makeText(Investment_Options.this, "yes", Toast.LENGTH_SHORT).show();
//                android.app.AlertDialog.Builder alertt=new AlertDialog.Builder(Investment_Options.this);
//                alertt.setMessage("Do You Want to Invest "+p+" amount from account "+spinner.getSelectedItem().toString()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        double actamt=0;
//                        actamt=bal-p;
//
//
//                        Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
//                        DocumentReference documentReference1=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
//                       documentReference1.update("Balance",Double.toString(actamt));
//                    }
//                });
//                AlertDialog alert=alertt.create();
//                alert.show();
//            }
//        });







    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //baldisp.setText(Constants.balancelist.get(position));



        DocumentReference documentReference=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                bal= Integer.parseInt(value.getString("Balance"));
                baldisp.setText(value.getString("Balance"));
            }
        });



        invstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Investment_Options.this, "yes", Toast.LENGTH_SHORT).show();
                android.app.AlertDialog.Builder alertt=new AlertDialog.Builder(Investment_Options.this);
                alertt.setMessage("Do You Want to Invest "+p+" amount from account "+spinner.getSelectedItem().toString()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer actamt=0;
                        actamt= (int)(bal-p);


                        Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                        DocumentReference documentReference1=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
                        documentReference1.update("Balance",actamt.toString());



                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog alert=alertt.create();
                alert.show();
            }
        });



        Integer actualamt= (int) (returnn+p);
        int year=cal.get(Calendar.YEAR);
        if(year==year+2)
        {
            DocumentReference documentReference1=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
            documentReference1.update("Balance",actualamt.toString());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}