package com.example.ronald.sfparking;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import java.util.concurrent.TimeUnit;

/**
 * NumberPicker extension to create the timer for parking spaces after users click the park button.
 */
public class myNumberPicker extends NumberPicker {

    /* Timer picker reference variables */
    NumberPicker hrs_picker;
    NumberPicker mins_picker;


    public myNumberPicker(Context context) {
        super(context);
        Activity activity = (Activity) context;

        hrs_picker = (NumberPicker) activity.findViewById(R.id.hour_picker);
        mins_picker = (NumberPicker) activity.findViewById(R.id.minute_picker);

    }

    /**
     * Sets the minimum and maximums for the hour in the timer
     * @param min the new minimum value
     * @param max the new maximum value
     */
    public void setHrsPicker(int min, int max){
        hrs_picker.setMinValue(min);
        hrs_picker.setMaxValue(max);
    }

    /**
     *
     * @return the value of the hour picker
     */
    public int getHrsPickerValue(){
        return hrs_picker.getValue();
    }

    /**
     * Sets the minimum and maximum value of the minute picker in the timer.
     * @param min the minimum value of the minute picker
     * @param max the maximum value of the minute picker
     */
    public void setMinsPicker(int min, int max){
        mins_picker.setMinValue(min);
        mins_picker.setMaxValue(max);
    }

    /**
     *
     * @return the value of the minute picker
     */
    public int getMinsPickerValue(){
        return mins_picker.getValue();
    }

    /**
     * resets the minimum and maximum values of the number picker to default values
     * hours:0-99 minutes:1-59
     */
    public void defaultValues(){
        setHrsPicker(0, 99);
        setMinsPicker(1, 59);
    }

    /**
     *
     * @return the value of the number picker in milliseconds.
     */
    public long getMilliseconds(){

        return (TimeUnit.HOURS.toMillis(getHrsPickerValue()) +
                TimeUnit.MINUTES.toMillis(getMinsPickerValue()));
    }


}
