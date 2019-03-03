package ie.holiday.inked;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;

    private ProgressBar mProgressCircle ;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;


    //making NAV Bar i added into what was created as an empty activity
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener( ) {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId( )) {
                case R.id.navigation_home:
                    Intent intent = new Intent(ImagesActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;


                case R.id.navigation_dashboard:   //no case here because you are already on this page so dont want to navigate there twice

                    break;




                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(ImagesActivity.this, MapsActivity.class);
                    startActivity(intent3);
                    break;


            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");  //filling our list with contents of the uploads folder

        mDatabaseRef.addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {   //the snapshot is what was uploaded to firebase

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Upload upload = postSnapshot.getValue(Upload.class);  //put the snapshot into the upload object type
                    mUploads.add(upload);
                }

                mImageAdapter = new ImageAdapter(ImagesActivity.this, mUploads);  //calling  adapter

                mRecyclerView.setAdapter(mImageAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE); //making the circular progress bar go away once everything is loaded

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}
