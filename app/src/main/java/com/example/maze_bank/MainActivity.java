package com.example.maze_bank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    View hView;
    TextView headername, headeraccount,headerbalance;
    Long i= Long.valueOf(0);
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String accno;
    View accnoheader;
    DocumentReference documentReference;
    CardView AddAcc,SelectAcc,TransferMoney,InvestmentOptions;
    List<String> accList = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);





        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();


        userID=fAuth.getCurrentUser().getUid();
        accnoheader=findViewById(R.id.account);

        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        nav=(NavigationView)findViewById(R.id.nav_view);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);


        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        //toggle.getDrawerArrowDrawable().setColor(R.color.white);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        hView=nav.getHeaderView(0);

        headername = hView.findViewById(R.id.name_header);
        headeraccount = hView.findViewById(R.id.accout_header);
        headerbalance=hView.findViewById(R.id.balance_header);


        AddAcc=findViewById(R.id.AccBtn);
        SelectAcc=findViewById(R.id.selectBtn);
        TransferMoney=findViewById(R.id.TransferBtn);
        InvestmentOptions=findViewById(R.id.fdBtn);


        AddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder accdailog = new AlertDialog.Builder(MainActivity.this);
                accdailog.setTitle("Enter Account Number");
                final EditText accountInput = new EditText(MainActivity.this);
                accountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                accdailog.setView(accountInput);

                accdailog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accno = accountInput.getText().toString().trim();
                        documentReference = fStore.collection("users").document(userID).collection("Accounts").document(accno);
                        Map<String, String> user = new HashMap<>();
                        user.put("Account Number", accno);
                        user.put("Balance", "0");
                        documentReference.set(user, SetOptions.merge());


                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);


                    }
                });






                accdailog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                accdailog.show();





            }
        });





        SelectAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentDailog().show(getSupportFragmentManager(),"fragmentDailog");
            }
        });


        TransferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TranasferMoney.class));
            }
        });


        InvestmentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Investment_Options.class));
            }
        });



        documentReference=fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot value,  FirebaseFirestoreException error) {
                if(value!=null) {
                    headername.setText(value.getString("fName"));

                }

            }

        });


              fStore.collection("users").document(userID).collection("Accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful())
                      {
                          List<String> acclist=new ArrayList<String>();
                          List<String> balancelist=new ArrayList<>();
                          for(QueryDocumentSnapshot accdocument : Objects.requireNonNull(task.getResult()))
                          {
                              acclist.add(accdocument.getId());
                              balancelist.add(accdocument.getString("Balance"));
                          }
                          Constants.accountlist=acclist;
                          Constants.balancelist=balancelist;
                          TransferVariables.accountlist=acclist;


                          if(RestorePref().isEmpty())
                          {
                              headeraccount.setText(Constants.accountlist.get(0));
                              headerbalance.setText(Constants.balancelist.get(0));
                          }
                            else
                                {
                              headeraccount.setText(RestorePref());
                              headerbalance.setText(RestorePrefBal());
                          }

                      }
                      else {
                          Log.d("Account",task.getException()+"");
                      }
                  }
              });





        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<String> user = new ArrayList<String>();

                    for(QueryDocumentSnapshot userli: Objects.requireNonNull(task.getResult()))
                    {
                        user.add(userli.getId());
                    }
                   Constants.userlist=user;

                }

            }
        });



//        for(int i=0;i<Constants.userlist.size();i=i+1)
//        {
//
//            Log.d("Count",i+ "Hello");
//            fStore.collection("users").document(Constants.userlist.get(i)).collection("Accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//
//                    for(QueryDocumentSnapshot acc: Objects.requireNonNull(task.getResult()))
//                    {
//                        accList.add(acc.getId());
//                    }
//                    Log.d("Count", accList+ "hello");
////                                        Log.d("Count", Constants.userlist.get(i)+ "hello");
//                }
//            });
//            Log.d("Count", Constants.userlist.get(i)+ "hello");
//
//        }














        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addmoney:
                        startActivity(new Intent(getApplicationContext(), AddMoney.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.account:

                        AlertDialog.Builder accdailog = new AlertDialog.Builder(MainActivity.this);
                        accdailog.setTitle("Enter Account Number");
                        final EditText accountInput = new EditText(MainActivity.this);
                        accountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                        accdailog.setView(accountInput);

                        accdailog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accno = accountInput.getText().toString().trim();
                                documentReference = fStore.collection("users").document(userID).collection("Accounts").document(accno);
                                Map<String, String> user = new HashMap<>();
                                user.put("Account Number", accno);
                                user.put("Balance", "0");
//                                i++;
//                                Map<String,Long> counter=new HashMap<>();
//                                counter.put("Counter",i);
                                documentReference.set(user, SetOptions.merge());


                                overridePendingTransition(0, 0);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        });






                        accdailog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                            }
                        });
                        accdailog.show();


                        break;

                    case R.id.viewprofile:
                        startActivity(new Intent(getApplicationContext(),viewprofile.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.accountdailog:
                        new FragmentDailog().show(getSupportFragmentManager(),"fragmentDailog");
                        break;
                    case R.id.weather:
                        startActivity(new Intent(getApplicationContext(),weather.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                        break;
                    case R.id.review:
                        startActivity(new Intent(getApplicationContext(),review.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }


//                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });








    }
    private String RestorePref() {
        SharedPreferences SP= getApplicationContext().getSharedPreferences("currantaccount",MODE_PRIVATE);
        String account=SP.getString("acc","");
        return account;

    }

    private String RestorePrefBal() {
        SharedPreferences SP= getApplicationContext().getSharedPreferences("currantbalance",MODE_PRIVATE);
        String balance=SP.getString("bal","");
        return balance;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}