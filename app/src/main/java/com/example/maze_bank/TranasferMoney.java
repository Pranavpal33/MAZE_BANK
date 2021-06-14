package com.example.maze_bank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TranasferMoney extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    android.widget.Spinner spinner;
    TextView BalanceV;
    EditText Amount, Raccount;
    Button Transfer;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    int a;
    Integer samt = 0, scurramt = 0, ramt = 0, rcurramt = 0;
    int f = 0, counter = 0, i = 0;
    List<String> accList = new ArrayList<String>();
    int count = 0;
    String b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranasfer_money);


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.accountlist);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        BalanceV = findViewById(R.id.BalanceView);
        Amount = findViewById(R.id.amountransfer);
        Transfer = findViewById(R.id.transfer);
        Raccount = findViewById(R.id.Raccount);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        DocumentReference documentReference1 = fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                scurramt = Integer.parseInt(value.getString("Balance"));
                BalanceV.setText(value.getString("Balance"));

            }
        });
        Transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("Count", Constants.userlist+ "hello");
                i = 0;
                for (i = 0; i < Constants.userlist.size(); i++) {

                    Log.d("Count", i + " User");
                    fStore.collection("users").document(Constants.userlist.get(i)).collection("Accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            accList = new ArrayList<>();
                            for (QueryDocumentSnapshot acc : Objects.requireNonNull(task.getResult())) {
                                accList.add((acc.getId()));
                            }

                            for (int j = 0; j < accList.size(); j++) {
                                b=accList.get(j);
                                f=0;
                                if(b.equals(Raccount.getText().toString())) {
//                                    count = i;
//                                    a = Raccount.getText().toString();
//                                    break;
                                    f=1;
                                    a=j;
                                    break;
                                }
                            }
                            if (f==1)
                            {
                                Log.d("Successfully found",accList.get(a)+" Hello");

                                DocumentReference documentReference3=fStore.collection("Users").document(Constants.userlist.get(count)).collection("Accounts").document(Raccount.getText().toString());
//                                documentReference3.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
//                                        ramt=Integer.parseInt(value.getString("Balance"));
//                                    }
//                                });
                            }
                        }
                    });



//                    samt = scurramt - Integer.parseInt(Amount.getText().toString());
//                    rcurramt = ramt + Integer.parseInt(Amount.getText().toString());

//
//                    DocumentReference documentReference = fStore.collection("users").document(userID).collection("Accounts").document(spinner.getSelectedItem().toString());
//                    documentReference.update("Balance", samt.toString());
//
//
//                    DocumentReference documentReference2 = fStore.collection("Users").document(Constants.userlist.get(count)).collection("Accounts").document(Raccount.getText().toString());
//                    documentReference2.update("Balance", rcurramt.toString());
                }
            }
        });
    }

        @Override
        public void onNothingSelected (AdapterView < ? > parent)
        {

        }
}