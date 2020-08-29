package com.example.checkoutsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> itemsList = new ArrayList();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        updateData();

        ref.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String xName = ds.child("itemName").getValue().toString();
                    String xAvailability = ds.child("itemAvailability").getValue().toString();
                    Log.d("Checkout", "🔥🔥 "+ xName + " - " + xAvailability);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        updateData();

        String username = "Efaz";
        TextView welcomeTitle = findViewById(R.id.welcomeTitle);
        welcomeTitle.append(" " + username);

        final String[] items = {"Drone", "Laptop", "Camera", "Arduino"};
        final String[] itemsAvailability = {"Available", "Available", "Available", "Available"};

        final ArrayList<String> itemsCheckedOut = new ArrayList<>();

        final TextView timerView = findViewById(R.id.timer);

        final TextView instruction = findViewById(R.id.textView1);

        final CheckedTextView itemscheckedbyuser = findViewById(R.id.itemscheckedbyuser);

        final AlertDialog.Builder AD = new AlertDialog.Builder(this);
        AD.setTitle("Confirmation").setIcon(android.R.drawable.ic_dialog_alert).setNegativeButton("Cancel", null);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        final ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsAvailability);

        ListView listView1 = (ListView) findViewById(R.id.itemsList);
        listView1.setAdapter(itemsAdapter);

        final ListView listView2 = (ListView) findViewById(R.id.availability);
        listView2.setAdapter(availabilityAdapter);

        final CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerView.setTextColor(Color.BLACK);
                timerView.setText("Please return the item by: " + millisUntilFinished / 1000 + " seconds");
            }
            public void onFinish() {
                timerView.setText("Item is overdue!");
                timerView.setTextColor(Color.RED);
            }
        };

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long l) {

                final String x = parent.getItemAtPosition(i).toString();

                final TextView text1 = view.findViewById(android.R.id.text1);

                if (itemsCheckedOut.size() < 1){
                    AD
                    .setMessage("Are you sure?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            itemsAvailability[i] = "Not Available";
                            text1.setTextColor(Color.RED);
                            itemsCheckedOut.add(0, x);
                            itemscheckedbyuser.setText("• "+itemsCheckedOut.get(0));
                            Toast.makeText(getApplicationContext(), "You checked out 1 " + x, Toast.LENGTH_SHORT).show();
                            listView2.setAdapter(availabilityAdapter);
                            instruction.setText("Tap on item to return");
                            timerView.setVisibility(View.VISIBLE);
                            countDownTimer.start();
                        }});
                    AD.show();
                }
                else {
                    if (itemsAvailability[i] == "Not Available"){
                        AD
                        .setMessage("Are you returning the item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                itemsAvailability[i] = "Available";
                                text1.setTextColor(Color.BLACK);
                                itemsCheckedOut.remove(0);
                                itemscheckedbyuser.setText("None");
                                instruction.setText("Tap on item to checkout");
                                listView2.setAdapter(availabilityAdapter);
                                timerView.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Thanks for returning", Toast.LENGTH_SHORT).show();
                            }});
                        AD.show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You can only check out 1 item at a time", Toast.LENGTH_SHORT).show();
                    }
                }

                listView2.setAdapter(availabilityAdapter);

            }
        });

    }

    private void updateData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("items/1/itemAvailability").setValue("Available");
    }


}
