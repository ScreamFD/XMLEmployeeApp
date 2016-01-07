package de.lamber.sascha.simplexmlpullparser.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.lamber.sascha.simplexmlpullparser.activities.DetailActivity;
import de.lamber.sascha.simplexmlpullparser.R;
import de.lamber.sascha.simplexmlpullparser.data.Employee;
import de.lamber.sascha.simplexmlpullparser.xml.XMLPullParserHandler;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class EmployeeFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static EmployeeFragment newInstance(String param1, String param2) {
        EmployeeFragment fragment = new EmployeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EmployeeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        File file = getActivity().getFileStreamPath("employees.xml");

          try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            Employee.EMPLOYEES.clear(); //clear List

              if (file.exists() == false) {
                  // not exists use the employees asset file
                  Employee.EMPLOYEES = parser.parse(getActivity().getAssets().open("employees.xml"));
                  ArrayAdapter<Employee> adapter =
                          new ArrayAdapter<Employee>(getActivity(), R.layout.list_item, Employee.EMPLOYEES);
                  setListAdapter(adapter);

              }
              else
              {
                  // exists use the employees internal stored file, which you have created
                  Employee.EMPLOYEES = parser.parse(getActivity().openFileInput("employees.xml"));
                  ArrayAdapter<Employee> adapter =
                          new ArrayAdapter<Employee>(getActivity(), R.layout.list_item, Employee.EMPLOYEES);
                  setListAdapter(adapter);

              }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.

            mListener.onFragmentInteraction(Integer.toString(Employee.EMPLOYEES.get(position).getId()));

            String item;
            item = Employee.EMPLOYEES.get(position).toString();

            int id1 = Employee.EMPLOYEES.get(position).getId();
            String name = Employee.EMPLOYEES.get(position).getName();
            String department = Employee.EMPLOYEES.get(position).getDepartment();
            String type = Employee.EMPLOYEES.get(position).getType();
            String email = Employee.EMPLOYEES.get(position).getEmail();

            Toast toast = Toast.makeText(getActivity(),item,Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("name",name)
                    .putExtra("id",id1)
                    .putExtra("department", department)
                    .putExtra("type",type)
                    .putExtra("email", email);
            startActivity(intent);
            getActivity().finish();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.list_layout, container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        registerForContextMenu(getListView());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menu_context_delete:
                deleteListItem(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void deleteListItem(AdapterView.AdapterContextMenuInfo item){
        Object position;

        position = getListAdapter().getItem(item.position);
        ArrayAdapter adapter = (ArrayAdapter) getListAdapter();
        adapter.remove(position);

        XMLPullParserHandler xmlSerializer = new XMLPullParserHandler();

        String xmlString;
        FileOutputStream fileOutputStream = null;
        String filename = "employees.xml";

        List<Employee> employees = new ArrayList<Employee>();

        for (int i = 0; i < adapter.getCount(); i++){
            employees.add((Employee)adapter.getItem(i));
        }

        try {
            xmlString = xmlSerializer.WriteXmlString(employees);

            fileOutputStream = getActivity().openFileOutput(filename, getActivity().MODE_PRIVATE);
            fileOutputStream.write(xmlString.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
