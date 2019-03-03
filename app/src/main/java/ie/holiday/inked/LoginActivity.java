package ie.holiday.inked;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private TextView Info;
    private Button Login;


    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Log in");

        Email = (EditText) findViewById(R.id.loginEmail);
        Password = (EditText) findViewById(R.id.loginPassword);
        Login = (Button) findViewById(R.id.mButtonLogin);

    }

    public void onButtonClick(View v)
    {




        if(v.getId() == R.id.registerButton)

        {
            Intent i = new Intent(LoginActivity.this, Register.class);
            startActivity(i);
        }



        // Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        //startActivity(i);

        //this is my db
        if(v.getId() == R.id.mButtonLogin)
        {
            EditText a = (EditText) findViewById(R.id.loginEmail);
            String str = a.getText().toString();

            EditText b = (EditText) findViewById(R.id.loginPassword);
            String pass = b.getText().toString();

            String password = helper.searchPass(str);
            if(pass.equals(password))

            {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);

                startActivity(i);

            }
            else
            {
                Toast temp = Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG);
                temp.show();
            }
        }

        if(v.getId() == R.id.registerButton)

        {
            Intent i = new Intent(LoginActivity.this, Register.class);
            startActivity(i);
        }


    }


}
