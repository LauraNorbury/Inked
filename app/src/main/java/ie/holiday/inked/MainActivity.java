package ie.holiday.inked;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;


    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageURI; //for pointing to image

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseref;

    private StorageTask mUploadTask;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener  //creating navigation
            = new BottomNavigationView.OnNavigationItemSelectedListener( ) {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId( )) {
                case R.id.navigation_home:

                    break;


                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(MainActivity.this, ImagesActivity.class);
                    startActivity(intent2);
                    break;




                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent3);
                    break;


            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads") ; //uploading to a folder i called uploads
        mDatabaseref = FirebaseDatabase.getInstance().getReference("uploads"); //same again here



        mButtonChooseImage.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });


         mButtonUpload.setOnClickListener(new View.OnClickListener( ) {
             @Override
             public void onClick(View v) {

                 if(mUploadTask != null && mUploadTask.isInProgress()){  //so user cant spamthe upload button and upload multiples

                     Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                 } else {

                     uploadFile( );
                 }

             }
         });

         mTextViewShowUploads.setOnClickListener(new View.OnClickListener( ) {
             @Override
             public void onClick(View v) {

                 openImagesActivity();

             }
         });
    }

    private void openFileChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null) {

            mImageURI = data.getData();

            Picasso.get().load(mImageURI).into(mImageView);

        }
    }

    private String getFileExtension(Uri uri) {  //taking image extension https://www.youtube.com/watch?v=lPfQN-Sfnjw

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime  = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){

        if(mImageURI!= null) {

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis( )
                    + "." + getFileExtension(mImageURI));//saving the file as current time in milliseconds will give each one a unique name       }

            mUploadTask = fileReference.putFile(mImageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable( ) {
                                @Override
                                public void run() {
                                      mProgressBar.setProgress(0 );
                                }
                                }, 500);   //this is to delay the progress bar from resarting without the user ever seeing it has completed successfully, for UX not for Functionality

                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());

                            String uploadID = mDatabaseref.push().getKey();
                            mDatabaseref.child(uploadID).setValue(upload) ;
                        }



                    })
                    .addOnFailureListener(new OnFailureListener( ) {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>( ) {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });

        }
            else {

            Toast.makeText( this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagesActivity() {

        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }

}
