package de.lamber.sascha.simplexmlpullparser.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import de.lamber.sascha.simplexmlpullparser.data.Employee;

/**
 * Created by Sascha on 11.10.2014.
 */
public class XMLPullParserHandler {

    List<Employee> employees;
    private Employee employee;
    private String text;

    public XMLPullParserHandler(){
        employees = new ArrayList<Employee>();
    }

    public List<Employee> getEmployees(){
        return employees;
    }

    public List<Employee> parse(InputStream inputStream){
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(inputStream,null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagname = parser.getName();

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("employee")){
                            // create a new instance of employess
                            employee = new Employee();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("employee")){
                            //add employee object to list
                            employees.add(employee);
                        }else if (tagname.equalsIgnoreCase("name")){
                            employee.setName(text);
                        }else if (tagname.equalsIgnoreCase("id")){
                            employee.setId(Integer.parseInt(text));
                        }else if (tagname.equalsIgnoreCase("department")){
                            employee.setDepartment(text);
                        }else if (tagname.equalsIgnoreCase("email")){
                            employee.setEmail(text);
                        }else if (tagname.equalsIgnoreCase("type")){
                            employee.setType(text);
                        }
                        break;

                    default:
                        break;
                }

                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public String WriteXmlString(List<Employee> employees) throws IOException{

        String Tag_employees = "employees";
        String Tag_employee = "employee";
        String Tag_id = "id";
        String Tag_name = "name";
        String Tag_department = "department";
        String Tag_type = "type";
        String Tag_email = "email";

        //order of Tags: employees , employee, id, name, department, email

        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();

        xmlSerializer.setOutput(stringWriter);

        // start Document
        xmlSerializer.startDocument("UTF-8", true);
        // tag: employees
        xmlSerializer.startTag("", Tag_employees);

        for (int i = 0; i < employees.size(); i++){

            // tag: employee
            xmlSerializer.startTag("", Tag_employee);

            // tag: id
            xmlSerializer.startTag("", Tag_id);
            xmlSerializer.text(Integer.toString(employees.get(i).getId()));
            xmlSerializer.endTag("", Tag_id);

            // tag: name
            xmlSerializer.startTag("", Tag_name);
            xmlSerializer.text(employees.get(i).getName());
            xmlSerializer.endTag("", Tag_name);

            // tag: department
            xmlSerializer.startTag("", Tag_department);
            xmlSerializer.text(employees.get(i).getDepartment());
            xmlSerializer.endTag("", Tag_department);

            // tag: type
            xmlSerializer.startTag("", Tag_type);
            xmlSerializer.text(employees.get(i).getType());
            xmlSerializer.endTag("", Tag_type);

            // tag: email
            xmlSerializer.startTag("", Tag_email);
            xmlSerializer.text(employees.get(i).getEmail());
            xmlSerializer.endTag("", Tag_email);

            xmlSerializer.endTag("", Tag_employee);
        }

        // end Document
        xmlSerializer.endDocument();

        return stringWriter.toString();
    }
}
