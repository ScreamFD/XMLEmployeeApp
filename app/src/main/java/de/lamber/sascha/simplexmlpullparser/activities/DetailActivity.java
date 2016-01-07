package de.lamber.sascha.simplexmlpullparser.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.lamber.sascha.simplexmlpullparser.R;
import de.lamber.sascha.simplexmlpullparser.data.Employee;
import de.lamber.sascha.simplexmlpullparser.xml.XMLPullParserHandler;

public class DetailActivity extends ActionBarActivity {

    EditText editTextId;
    EditText editTextName;
    EditText editTextDepartment;
    EditText editTextType;
    EditText editTextEmail;

    Button button_save;
    Button button_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        getData();

        button_save = (Button)findViewById(R.id.button_save);
        button_clear = (Button)findViewById(R.id.button_clear);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (areImportantTextFieldsFull() == true) {
                    List<Employee> employees = new ArrayList<Employee>();

                    for (int i = 0; i < Employee.EMPLOYEES.size(); i++) {
                        employees.add(Employee.EMPLOYEES.get(i));
                    }

                    String xmlString;

                    initializeEditTextViews();

                    Employee newEmployee = new Employee();
                    newEmployee.setId(Integer.parseInt(editTextId.getText().toString()));
                    newEmployee.setName(editTextName.getText().toString());
                    newEmployee.setDepartment(editTextDepartment.getText().toString());
                    newEmployee.setType(editTextType.getText().toString());
                    newEmployee.setEmail(editTextEmail.getText().toString());

                    employees.add(newEmployee);

                    XMLPullParserHandler xmlSerializer = new XMLPullParserHandler();
                    FileOutputStream fileOutputStream = null;
                    String filename = "employees.xml";

                    try {
                        xmlString = xmlSerializer.WriteXmlString(employees);

                        fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
                        fileOutputStream.write(xmlString.getBytes());
                        fileOutputStream.close();

                        // Toast.makeText(v.getContext(),xmlString,Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please fill the first 3 fields out", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextId.setText("");
                editTextName.setText("");
                editTextDepartment.setText("");
                editTextType.setText("");
                editTextEmail.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeEditTextViews(){
        editTextId = (EditText)findViewById(R.id.edit_Id);
        editTextName = (EditText)findViewById(R.id.edit_Name);
        editTextDepartment = (EditText)findViewById(R.id.edit_Department);
        editTextType = (EditText)findViewById(R.id.edit_Type);
        editTextEmail = (EditText)findViewById(R.id.edit_Email);
    }

    private Boolean areImportantTextFieldsFull(){
        Boolean bool = true;

        if (editTextId.getText().toString().equals("")) {
            bool = false;
        }
        if (editTextName.getText().toString().equals("")){
            bool = false;
        }
        if (editTextDepartment.getText().toString().equals("")){
            bool = false;
        }

        return bool;
    }

    private void getData(){
        int ID;

        Intent data = getIntent();
        Bundle bundle = data.getExtras();

        initializeEditTextViews();

        // ID = bundle.getInt("id"); //just for testing, to see what the value is.
        //works bundle works..

        //EditText fields data input or output in Strings !

        if (bundle != null) {
            try{
                editTextId.setText(Integer.toString(bundle.getInt("id", 0)));
                editTextName.setText(bundle.getString("name"));
                editTextDepartment.setText(bundle.getString("department"));
                editTextType.setText(bundle.getString("type"));
                editTextEmail.setText(bundle.getString("email"));

            }catch (Exception ex){
                Log.e("ERROR", ex.toString());
            }

        }
    }

}
