package com.example.maze_bank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class AddMoney extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText addmoney;
    Button ADD;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference documentReference;
    String userID;
    Integer amount;
    Integer curramt =0;
    android.widget.Spinner spinner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addmoney);

        spinner=findViewById(R.id.accounts);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Constants.accountlist);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        addmoney=findViewById(R.id.amount);

        ADD=findViewById(R.id.add);
        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        userID=fAuth.getCurrentUser().getUid();


//        DocumentReference documentReference1=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
//        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                curramt=Integer.parseInt(value.getString("Balance"));
//
//            }
//        });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        DocumentReference documentReference1=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                curramt=Integer.parseInt(value.getString("Balance"));

            }
        });



        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                android.app.AlertDialog.Builder builder=new AlertDialog.Builder(AddMoney.this);
                builder.setMessage("Are You sure you want to add Money in account "+Constants.accountlist.get(position)).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        amount=(addmoney.getText().toString()+Constants.balancelist.get(position));
                        amount=Integer.parseInt(addmoney.getText().toString())+curramt;

                        documentReference=fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
                        documentReference.update( "Balance",amount.toString());
                        Toast.makeText(getApplicationContext(), "Account Updated" , Toast.LENGTH_SHORT).show();

//                        overridePendingTransition(0, 0);
//                        startActivity(getIntent());
//                        overridePendingTransition(0, 0);

                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog alert=builder.create();
                alert.show();

                //Toast.makeText(getApplicationContext(), Constants.balancelist.get(position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
