package com.example.maze_bank;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDailog extends DialogFragment{

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Account");
        builder.setItems(Constants.accountlist.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "you choose"+Constants.accountlist.get(which), Toast.LENGTH_SHORT).show();



                saveContext(Constants.accountlist.get(which));
                saveContextBal(Constants.balancelist.get(which));



                getActivity().finish();

                getActivity().overridePendingTransition(0, 0);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(getActivity().getIntent());
                getActivity().overridePendingTransition(0, 0);





            }
        });
        return builder.create();


    }

    private void saveContext(String curac) {
        SharedPreferences SP= getActivity().getSharedPreferences("currantaccount",MODE_PRIVATE);
        SharedPreferences.Editor editor= SP.edit();
        editor.putString("acc",curac);
        editor.commit();

    }

    private void saveContextBal(String curbal) {
        SharedPreferences SP= getActivity().getSharedPreferences("currantbalance",MODE_PRIVATE);
        SharedPreferences.Editor editor= SP.edit();
        editor.putString("bal",curbal);
        editor.commit();

    }


}
