package de.lamber.sascha.simplexmlpullparser.activities;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.lamber.sascha.simplexmlpullparser.fragments.EmployeeFragment;
import de.lamber.sascha.simplexmlpullparser.R;

public class MainActivity extends ActionBarActivity implements EmployeeFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {

//        }


        //Write down your code here.....

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_employee) {
            Intent intent = new Intent(this,DetailActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_list){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new EmployeeFragment()).commit();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

}
