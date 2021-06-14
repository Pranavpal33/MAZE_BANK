package com.example.maze_bank;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class viewprofile extends AppCompatActivity {
    TextView name,Email,phone,dob,viewp_account;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disp_profile);


        name=findViewById(R.id.name_header);
        Email=findViewById(R.id.EmailDisp);
        phone=findViewById(R.id.PhoneDisp);
        dob=findViewById(R.id.dobprofile);
        viewp_account=findViewById(R.id.viewp_accnumber);



        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();

        DocumentReference documentReference=fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot value, @Nullable  FirebaseFirestoreException error) {

                                phone.setText(value.getString("PhoneNumber"));
                                name.setText(value.getString("fName"));
                                Email.setText(value.getString("Email"));
                                dob.setText(value.getString("Date Of Birth"));
            }
        });








        fStore.collection("users").document(userID).collection("Accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task){
                if(task.isSuccessful())
                {
                    List<String> accList= new ArrayList<String>();
                    List<String> balList= new ArrayList<String>();
                    for(QueryDocumentSnapshot accountlist: Objects.requireNonNull(task.getResult()))
                    {
                            accList.add(accountlist.getId());
                            balList.add(accountlist.getString("Balance"));
                    }


                    Constants.accountlistprofile=accList;
                    Constants.balancelistprofile=balList;

                    if(Constants.accountlistprofile!=null) {
                        int i = 0;
                        for (i = 0; i < Constants.accountlistprofile.size(); i++) {

                            viewp_account.setText(viewp_account.getText() + Constants.accountlist.get(i).toString() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + Constants.balancelist.get(i).toString() + "\n");

                        }
                    }
                 }
                else
                {
                    Log.d("Account",task.getException()+"");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
