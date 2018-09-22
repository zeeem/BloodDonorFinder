package finder2.donor.bllod.zeeem.blooddonorfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class SignUpActivity extends Activity {

    public FirebaseDatabase database;
    public DatabaseReference mDatabaseRef;
    public String smoking, diabetes, asthma, heart_disease, bloodET, areaET, nameETs, passETs, mobileETs, ageETs;

    public boolean isSignUpPossible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();

        //getting the spinner from the xml.
        final Spinner dropdownBloodGroup = findViewById(R.id.blood_spinner);
        final Spinner dropdownArea = findViewById(R.id.area_spinner);

        final Spinner smoking_spinner = findViewById(R.id.smoking_Spinner);
        final Spinner diabetes_spinner = findViewById(R.id.diabetes_spinner);
        final Spinner heart_disease_spinner = findViewById(R.id.heart_disease_spinner);
        final Spinner asthma_spinner = findViewById(R.id.asthma_spinner);

        //creating a list of items for the spinner.
        String[] items_bloodGroup = new String[]{"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"};
        String[] items_area = new String[]{"Banani", "Dhanmondi", "Gulshan", "Mirpur", "Motijheel", "Mohammadpur", "Shahbag", "Shamoli", "Savar", "Uttara"};
        String[] items_historyOption = new String[]{"no", "yes"};

        //creating an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter_bloodGroup = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_bloodGroup);
        ArrayAdapter<String> adapter_area = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_area);
        ArrayAdapter<String> adapter_history = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_historyOption);

        //setting the spinners adapter to the created one.
        dropdownBloodGroup.setAdapter(adapter_bloodGroup);
        dropdownArea.setAdapter(adapter_area);

        smoking_spinner.setAdapter(adapter_history);
        diabetes_spinner.setAdapter(adapter_history);
        heart_disease_spinner.setAdapter(adapter_history);
        asthma_spinner.setAdapter(adapter_history);


        final EditText nameET = findViewById(R.id.nameET);
        final EditText ageET = findViewById(R.id.ageET);
        final EditText mobileET = findViewById(R.id.mobileET);
        final EditText passET = findViewById(R.id.passET);



        Button signUp_Btn = findViewById(R.id.signUp_Btn);

        //if user logged in
        if(StaticValues.loggedIn){

            signUp_Btn.setText("UPDATE PROFILE");

            nameET.setText(StaticValues.donor_user_sv.getName());
            ageET.setText(StaticValues.donor_user_sv.getAge());
            mobileET.setText(StaticValues.donor_user_sv.getMobile());
            passET.setText(StaticValues.donor_user_sv.getGender()); //temporarily using gender for pass storing

            dropdownBloodGroup.setSelection(Arrays.asList(items_bloodGroup).indexOf(StaticValues.donor_user_sv.getBlood()));
            dropdownArea.setSelection(Arrays.asList(items_area).indexOf(StaticValues.donor_user_sv.getArea()));

            String[] historyChart = StaticValues.donor_user_sv.getHistory().split(",");
            smoking_spinner.setSelection(Arrays.asList(items_historyOption).indexOf(historyChart[0]));
            diabetes_spinner.setSelection(Arrays.asList(items_historyOption).indexOf(historyChart[1]));
            heart_disease_spinner.setSelection(Arrays.asList(items_historyOption).indexOf(historyChart[2]));
            asthma_spinner.setSelection(Arrays.asList(items_historyOption).indexOf(historyChart[3]));


        }







        signUp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameET.getText().toString().trim().equals("") && mobileET.getText().toString().trim().equals("") && passET.getText().toString().trim().equals("") && ageET.getText().toString().trim().equals("")) {

                    Toast.makeText(SignUpActivity.this, "Name, Password, Age and Mobile Number must not be Empty!", Toast.LENGTH_LONG).show();
                } else {


                    nameETs = nameET.getText().toString();
                    ageETs = ageET.getText().toString();
                    mobileETs = mobileET.getText().toString();
                    passETs = passET.getText().toString();

                    bloodET = dropdownBloodGroup.getSelectedItem().toString();
                    areaET = dropdownArea.getSelectedItem().toString();

                    smoking = smoking_spinner.getSelectedItem().toString();
                    diabetes = diabetes_spinner.getSelectedItem().toString();
                    heart_disease = heart_disease_spinner.getSelectedItem().toString();
                    asthma = asthma_spinner.getSelectedItem().toString();


                    //queryDatabasetoSIGNUP();
                    //if (isSignUpPossible || StaticValues.loggedIn) {

                        mDatabaseRef = database.getReference().child(mobileETs);


                        HashMap<String, String> dataMap_doc = new HashMap<String, String>();
                        dataMap_doc.put("name", nameETs.toString());
                        dataMap_doc.put("age", ageETs);
                        dataMap_doc.put("pass", passETs);

                        dataMap_doc.put("area", areaET);
                        dataMap_doc.put("blood", bloodET);

                        dataMap_doc.put("blood_area", bloodET + "_" + areaET);
                        mDatabaseRef.setValue(dataMap_doc);


                        HashMap<String, String> dataMap_history = new HashMap<String, String>();
                        dataMap_history.put("smoking", smoking);
                        dataMap_history.put("diabetes", diabetes);
                        dataMap_history.put("heart_disease", heart_disease);
                        dataMap_history.put("asthma", asthma);

                        mDatabaseRef.child("history").setValue(dataMap_history).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Operation Successful!", Toast.LENGTH_LONG).show();
                                    Intent intent;

                                    if (!StaticValues.loggedIn) {
                                        intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    } else {
                                        intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    }

                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });
                    }
                }

            //}
        });


    }


    public void queryDatabasetoSIGNUP(){

        //referencing initiated firebase from mainActivity
        DatabaseReference mRef = MainActivity.mRef;


        Query query = mRef.orderByKey().equalTo(mobileETs.trim());
        query.addValueEventListener(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener()
    {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            if(dataSnapshot.exists()) {
                //if key or value does not exist
                Toast.makeText(SignUpActivity.this, "Mobile Number is already in use! Please log in.", Toast.LENGTH_LONG).show();
                isSignUpPossible = false;

                Intent intent;
                intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else isSignUpPossible = true;


        }
        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            System.out.print("Database error : " + databaseError.getMessage().toString());

        }

    };



}
