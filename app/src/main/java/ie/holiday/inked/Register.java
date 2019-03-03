package ie.holiday.inked;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register");
    }

    public void onSignUpClick(View v)
    {
        if(v.getId() == R.id.mButtonRegister)
        {
            EditText name = (EditText) findViewById(R.id.signUpName);
            EditText email = (EditText) findViewById(R.id.signUpEmail);
            EditText pass1 = (EditText) findViewById(R.id.signUpPassword);
            EditText pass2 = (EditText) findViewById(R.id.signUpConfirmPassword);

            String NameString = name.getText().toString();
            String EmailString = email.getText().toString();
            String Pass1String = pass1.getText().toString();
            String Pass2String = pass2.getText().toString();

            if(!Pass1String.equals(Pass2String))
            {
                //popup

                Toast password = Toast.makeText(Register.this, "Passwords Do Not Match", Toast.LENGTH_LONG);
                password.show();
            }

            else
            {
                //inserting into db

                Contact c = new Contact();
                c.setName(NameString);
                c.setEmail(EmailString);
                c.setPass(Pass1String);

                helper.insertContact(c);

            }
        }

    }
}
