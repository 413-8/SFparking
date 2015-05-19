package com.example.ronald.sfparking;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Gravity;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Stack;

/**
 * Created by Toren on 4/25/2015
 * Purpose: The activity will display the saved parking location(s) saved in the SQLite database to the user
 */
public class SavedLocationsActivity extends ActionBarActivity {
    private SavedDbAccessor savedDbAccessor;
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
        savedDbAccessor = new SavedDbAccessor(this);
        savedDbAccessor.open();

        ScrollView sv = new ScrollView(this);
        TableLayout tableLayout = createTableLayout();
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

    private TableLayout createTableLayout() {
        // Get Display width
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        // Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.LTGRAY); //visible when a TextView doesn't have a set background color
        tableLayout.setMinimumWidth(displayWidth);

        // Create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1); //space between cells (aka border thickness)
        tableRowParams.weight = 1; //unknown

        // Create Stack with all locations from
        Stack<ParkLocationInfo> locations = new Stack<ParkLocationInfo>();
        locations = savedDbAccessor.getAllLocations();
        ParkLocationInfo currentLocation;


        
        for (int i = 0; i < 5; i++) {

            if (locations.isEmpty()) {
                break;
            } else {
                currentLocation = locations.pop();
            }

            for (int j = 0; j <= 3; j++) {

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
                        textView.setText("Saved on " + currentLocation.getTime());
                        System.out.println("case 1");
                        break;
                    case 1:
                        textView.setText(currentLocation.getStreetName());
                        System.out.println("case 2");
                        break;
                    case 2:
                        textView.setText("" + currentLocation.getOnOffStreet() + " Parking");
                        System.out.println("case 3");
                        break;
                    case 3:
                        textView.setText("Rates:" + currentLocation.getRates());
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


