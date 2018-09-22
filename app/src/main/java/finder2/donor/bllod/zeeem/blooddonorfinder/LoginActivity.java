package finder2.donor.bllod.zeeem.blooddonorfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {


    EditText editMobileNumber, editPassword;
    TextView textView;
    String mobNo, passW;

    public static Boolean login_and_goBackToResultPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editMobileNumber = findViewById(R.id.logIn_mobile);
        editPassword =  findViewById(R.id.logIn_pass);

        TextView login_title = findViewById(R.id.login_title);



        if(login_and_goBackToResultPage){

            login_title.setVisibility(View.VISIBLE);
        }
        else login_title.setVisibility(View.GONE);



    }

    public void clickSignIn(View view){



        if(!editMobileNumber.getText().toString().trim().equals("") && !editPassword.getText().toString().trim().equals("")){

            StaticValues.usermobile_sv = editMobileNumber.getText().toString();
            StaticValues.userPass_sv = editPassword.getText().toString();


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRef = database.getReference();

            Query query = mRef.orderByKey().equalTo(editMobileNumber.getText().toString().trim());
            query.addValueEventListener(valueEventListener);

        }
        else  Toast.makeText(LoginActivity.this, "Both should be entered", Toast.LENGTH_LONG).show();


    }

    public void signUp_Btn(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();


    }

    ValueEventListener valueEventListener = new ValueEventListener()
    {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            try{

                //if Key does not exist
                if(!dataSnapshot.exists()){
                    Toast.makeText(LoginActivity.this, "User not Found!!", Toast.LENGTH_LONG).show();
                }


            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                mobNo = postSnapshot.getKey().toString();
                passW = postSnapshot.child("pass").getValue().toString();


                if(passW.equals(StaticValues.userPass_sv)){

                    if(!StaticValues.loggedIn){
                        Toast.makeText(LoginActivity.this, mobNo + " login successful!", Toast.LENGTH_LONG).show();
                    }

                    String mobileNumber_ = postSnapshot.getKey().toString();
                    String name_ = postSnapshot.child("name").getValue().toString();
                    String age_ = postSnapshot.child("age").getValue().toString();
                    String area_ = postSnapshot.child("area").getValue().toString();
                    String blood_ = postSnapshot.child("blood").getValue().toString();
                    String gender_ = postSnapshot.child("pass").getValue().toString(); //temp || gender = pass
                    String history_n = postSnapshot.child("history").child("smoking").getValue().toString() + ","
                            + postSnapshot.child("history").child("diabetes").getValue().toString() + ","
                            + postSnapshot.child("history").child("heart_disease").getValue().toString() + ","
                            + postSnapshot.child("history").child("asthma").getValue().toString();

                    StaticValues.donor_user_sv = new Donor(name_,age_,area_,blood_,gender_,"","",history_n,mobileNumber_);


                    StaticValues.loggedIn = true;
                    if(login_and_goBackToResultPage){
                        login_and_goBackToResultPage = false;
                        finish();
                    }
                    else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else Toast.makeText(LoginActivity.this, "Password Incorrect!!", Toast.LENGTH_LONG).show();

            }


        } catch (Exception e){
                Log.d("TAG", e.getMessage().toString());
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            System.out.print("Database error : " + databaseError.getMessage().toString());
            Toast.makeText(LoginActivity.this, "User not Found!!", Toast.LENGTH_LONG).show();

        }

    };
}
