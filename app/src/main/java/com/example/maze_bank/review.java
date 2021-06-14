package com.example.maze_bank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class review extends AppCompatActivity {
    RatingBar ratingBar;
    Button btn;
    FirebaseFirestore fStore;
    Long i= Long.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ratingBar=findViewById(R.id.rating);

        btn=findViewById(R.id.submit);
        fStore=FirebaseFirestore.getInstance();


            DocumentReference documentReference1 = fStore.collection("reviews").document("Reviews");
            documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null) {
                            i = (Long) value.get("Total reviews");
                        }

                }
            });






    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String rating = String.valueOf(ratingBar.getRating());


            DocumentReference documentReference = fStore.collection("reviews").document("Reviews");
            Map<String, String> user = new HashMap<>();
            user.put("Review "+ i, rating);
            i=i+1;
            Map<String, Long> users = new HashMap<>();
            users.put("Total reviews", i);
            documentReference.set(user, SetOptions.merge());
            documentReference.set(users, SetOptions.merge());


        }
    });
    }
}