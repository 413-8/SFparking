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
import java.util.Stack;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_locations, menu);
        return true;
    }

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

        dataSource = new Park_LocationDataSource(this);
        dataSource.open();

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

    /**
     * Creates the layout of the data stored in the table to be shown in the view,
     * @param rv the array of strings representing the rows requested to be viewed.
     * @param cv the columns requested to be viewed.
     * @param rowCount TODO:how will this be used?
     * @param columnCount TODO: how will this be used?
     * @return the Tablelayout created by the params.
     */
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
        tableRowParams.setMargins(1, 1, 1, 1); //space between cells (aka border thickness)
        tableRowParams.weight = 1; //unknown

        Stack<LocationInfo> locations = new Stack<LocationInfo>();
        locations = dataSource.getAllLocations();
        LocationInfo currentLocation;


        //single column table
        for (int i = 0; i < 5; i++) {

            if (locations.isEmpty()) {
                break;
            } else {
                currentLocation = locations.pop();
            }

            for (int j = 0; j <= 6; j++) {

                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundColor(Color.BLACK);
                TextView textView = new TextView(this);
                textView.setPadding(5, 5, 5, 5);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                tableRowParams.setMargins(0, 0, 0, 0); //for columns 1-j

                switch (j) {
                    case 0:
                        tableRowParams.setMargins(0, 5, 0, 0);
                        textView.setText("ID: " + currentLocation.getId());
                        System.out.println("case 0");
                        break;
                    case 1:
                        textView.setText(currentLocation.getStreetname());
                        System.out.println("case 1");
                        break;
                    case 2:
                        textView.setText("" + currentLocation.getOn_off_street() + "-street Parking");
                        System.out.println("case 2");
                        break;
                    case 3:
                        textView.setText(currentLocation.getTime());
                        System.out.println("case 3");
                        break;
                    case 4:
                        textView.setText("Lat: " + currentLocation.getLatitude());
                        System.out.println("case 4");
                        break;
                    case 5:
                        textView.setText("Long: " + currentLocation.getLongitude());
                        System.out.println("case 5");
                        break;
                }

                tableRow.addView(textView, tableRowParams);
                tableLayout.addView(tableRow, tableLayoutParams);

            }
            TextView textView = new TextView(this);
            textView.setText(null);
            textView.setPadding(0, 0, 0, 0);
            TableRow tableRow = new TableRow(this);
            tableRow.addView(textView, tableRowParams);
            tableLayout.addView(tableRow, tableLayoutParams);

        }

        return tableLayout;
    }

}


