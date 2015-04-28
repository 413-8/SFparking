package com.example.ronald.sfparking;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Toren on 4/25/2015
 * Purpose: The activity will display the saved parking location(s) saved in the SQLite database to the user
 */
public class SavedLocations extends ActionBarActivity {
    private Park_LocationDataSource dataSource;
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);
    }


*//*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_locations, menu);
        return true;
    }*//*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] row = {"Location 1", "Location 2", "Location 3", "Location 4", "Location 5"};
        String[] column = {"Longitude", "Latitude", "Street Name"};
        int rl = row.length;
        int cl = column.length;
        //dataSource = new Park_LocationDataSource(this);
        //dataSource.write();
        //dataSource.read();

        ScrollView sv = new ScrollView(this);
        TableLayout tableLayout = createTableLayout(row, column, rl, cl);
        sv.addView(tableLayout);
        setContentView(sv);

    }

    public void makeCellEmpty(TableLayout tableLayout, int rowIndex, int columnIndex) {
        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView) tableRow.getChildAt(columnIndex);

        // make it black
        textView.setBackgroundColor(Color.BLACK);
    }

    public void setHeaderTitle(TableLayout tableLayout, int rowIndex, int columnIndex) {

        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView) tableRow.getChildAt(columnIndex);

        textView.setText("Hello");
    }

    private TableLayout createTableLayout(String[] rv, String[] cv, int rowCount, int columnCount) {
        // 0) Get Display width
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.LTGRAY); //visible when a TextView doesn't have a set background color
        tableLayout.setMinimumWidth(displayWidth);


        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(2, 2, 2, 2); //space between cells (aka border thickness)
        tableRowParams.weight = 1; //unknown

        LocationInfo locationInfo = new LocationInfo();
        //locationInfo = dataSource.getLocationInfo(0);
        //ArrayList <LocationInfo> Locations = new ArrayList<LocationInfo>();
        //for(int i = 0; i < 4; i++ ){
        //     Locations.add(i,dataSource.getLocationInfo(i));
        //}

        //tableRowParams.setMargins(1, 1, 1, 1); //space between cells (aka border thickness)
        tableRowParams.weight = 1; //unknown

        //////////single column table
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {

                // 3) create tableRow
                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.BLACK);
                // 4) create textView
                TextView textView = new TextView(this);
                //  textView.setText(String.valueOf(j));
                textView.setPadding(5, 5, 5, 5);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                String s1 = rv[i];
                String s2 = cv[j];
                String s3 = s1 + s2;

                textView.setText(s3);
                if (j == 0) {
                    tableRowParams.setMargins(0, 5, 0, 0);
                } else {
                    tableRowParams.setMargins(0, 0, 0, 0);
                }
                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams);
                // 6) add tableRow to tableLayout
                tableLayout.addView(tableRow, tableLayoutParams);

            }
            TextView textView = new TextView(this);
            textView.setText(null);
            textView.setPadding(0, 0, 0, 0);
            TableRow tableRow = new TableRow(this);
            tableRow.addView(textView, tableRowParams);
            tableLayout.addView(tableRow, tableLayoutParams);


        }

        //////////matrix table sample
        /*for (int i = 0; i < rowCount; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.BLACK);
            //locationInfo = dataSource.getLocationInfo(i);
            //String [] LocationInfoString = {Double.toString(locationInfo.getLongitude()), Double.toString(locationInfo.getLatitude()), locationInfo.getStreetname()};
            for (int j= 0; j < columnCount; j++) {

                // 4) create textView
                TextView textView = new TextView(this);
                //  textView.setText(String.valueOf(j));
                textView.setPadding(5, 5, 5, 5);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                String s1 = Integer.toString(i);
                String s2 = Integer.toString(j);
                String s3 = s1 + s2;
                int id = Integer.parseInt(s3);

                //textView.setText(LocationInfoString[j]);
                //Log.d("TAG", "-___>" + id);
               // if (i ==0 && j==0){
                  //  textView.setText("");
                //} else if(i==0){
                    //Log.d("TAAG", "set Column Headers");
                  //  textView.setText(cv[j-1]);
                //}else if( j==0){
                    //Log.d("TAAG", "Set Row Headers");
                  //  textView.setText(rv[i-1]);
                //}else {
                  //  textView.setText(""+id);
                    // check id=23
                    //if(id==23){
                      //  textView.setText("ID=23");

                    //}
                //}

                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams);
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }*/

        return tableLayout;
    }
}


