package com.example.ronald.sfparking;

import android.app.Activity;
import android.content.Context;
import android.widget.NumberPicker;

import java.util.concurrent.TimeUnit;

/**
 * NumberPicker extension to create the timer for parking spaces after users click the park button.
 */
public class TimerPicker extends NumberPicker {

    /* Timer picker reference variables */
    NumberPicker hoursPicker;
    NumberPicker minutesPicker;


    public TimerPicker(Context context) {
        super(context);
        Activity activity = (Activity) context;

        hoursPicker = (NumberPicker) activity.findViewById(R.id.hour_picker);
        minutesPicker = (NumberPicker) activity.findViewById(R.id.minute_picker);

    }

    /**
     * Sets the minimum and maximums for the hour in the timer
     * @param min the new minimum value
     * @param max the new maximum value
     */
    public void setHoursPicker(int min, int max){
        hoursPicker.setMinValue(min);
        hoursPicker.setMaxValue(max);
    }

    /**
     *
     * @return the value of the hour picker
     */
    public int getHoursPickerValue(){
        return hoursPicker.getValue();
    }

    /**
     * Sets the minimum and maximum value of the minute picker in the timer.
     * @param min the minimum value of the minute picker
     * @param max the maximum value of the minute picker
     */
    public void setMinutesPicker(int min, int max){
        minutesPicker.setMinValue(min);
        minutesPicker.setMaxValue(max);
    }

    /**
     *
     * @return the value of the minute picker
     */
    public int getMinutesPickerValue(){
        return minutesPicker.getValue();
    }

    /**
     * resets the minimum and maximum values of the number picker to default values
     * hours:0-99 minutes:1-59
     */
    public void defaultValues(){
        setHoursPicker(0, 99);
        setMinutesPicker(1, 59);
    }

    /**
     *
     * @return the value of the number picker in milliseconds.
     */
    public long getMilliseconds(){

        return (TimeUnit.HOURS.toMillis(getHoursPickerValue()) +
                TimeUnit.MINUTES.toMillis(getMinutesPickerValue()));
    }


}
