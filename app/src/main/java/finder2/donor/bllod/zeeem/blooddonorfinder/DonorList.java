package finder2.donor.bllod.zeeem.blooddonorfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonorList extends Activity {

    public static ArrayList<Donor> donorList_sv = new ArrayList<Donor>();
    ListView listView;
    //ArrayList<Donor> donorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        final Activity activity = this;
        activity.setTitle("DONOR SEARCH RESULT");

        listView = findViewById(R.id.listV);

        donorList_sv.clear();

        try {
            queryDatabase();
        }
        catch (Exception e){
            Log.e("TAG", e.getMessage().toString());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionIndex, long l) {

                //Toast.makeText(DonorList.this, "item clicked : " + positionIndex, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(DonorList.this,DonorDetailsActivity.class);
                intent.putExtra("INDEX",positionIndex);
                startActivity(intent);

            }
        });
    }



    public void queryDatabase(){

        //referencing initiated firebase from mainActivity
        FirebaseDatabase database = MainActivity.database;
        DatabaseReference mRef = MainActivity.mRef;


        Query query = mRef.orderByChild("blood").equalTo(MainActivity.selectedBloodGroup);


        if(!MainActivity.selectedArea.equals("")) {
            query = mRef.orderByChild("blood_area").equalTo(MainActivity.selectedBloodGroup+"_"+MainActivity.selectedArea);
        }

        query.addValueEventListener(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener()
    {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            if(dataSnapshot==null) {
                //if key or value does not exist
                Toast.makeText(DonorList.this, "No match found!", Toast.LENGTH_LONG).show();
            }

            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {

                String mobileNumber_ = postSnapshot.getKey().toString();
                String name_ = postSnapshot.child("name").getValue().toString();
                String age_ = postSnapshot.child("age").getValue().toString();
                String area_ = postSnapshot.child("area").getValue().toString();
                String blood_ = postSnapshot.child("blood").getValue().toString();
                String gender_ = "";
                String history_ = "Asthma : \t" + postSnapshot.child("history").child("asthma").getValue().toString() +
                        "\nDiabetes : \t" + postSnapshot.child("history").child("diabetes").getValue().toString() +
                        "\nHeart Disease : \t" + postSnapshot.child("history").child("heart_disease").getValue().toString() +
                        "\nSmoking : \t" + postSnapshot.child("history").child("smoking").getValue().toString();

                // Toast.makeText(MainActivity.this, mobileNumber_ + " found : " + history_, Toast.LENGTH_LONG).show();

                Donor donor = new Donor(name_,age_,area_,blood_,gender_,"","",history_,mobileNumber_);
                donorList_sv.add(donor);

               // Toast.makeText(DonorList.this, donorList_sv.size() + " items found : " + name_, Toast.LENGTH_LONG).show();

            }

            //if data parsed successfully
            Toast.makeText(DonorList.this, donorList_sv.size() + " results found ", Toast.LENGTH_LONG).show();

            DonorAdapter adapter = new DonorAdapter(getApplicationContext(), R.layout.row, donorList_sv);
            listView.setAdapter(adapter);

        }
        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            System.out.print("Database error : " + databaseError.getMessage().toString());
            Toast.makeText(DonorList.this, "Database Error!", Toast.LENGTH_LONG).show();

        }

    };



}
