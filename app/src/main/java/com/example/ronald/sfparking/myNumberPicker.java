package com.example.ronald.sfparking;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import java.util.concurrent.TimeUnit;

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

    public void setHrsPicker(int min, int max){
        hrs_picker.setMinValue(min);
        hrs_picker.setMaxValue(max);
    }

    public int getHrsPickerValue(){
        return hrs_picker.getValue();
    }

    public void setMinsPicker(int min, int max){
        mins_picker.setMinValue(min);
        mins_picker.setMaxValue(max);
    }
    public int getMinsPickerValue(){
        return mins_picker.getValue();
    }

    public void defaultValues(){
        setHrsPicker(0, 99);
        setMinsPicker(1, 59);
    }

    public long getMilliseconds(){

        return (TimeUnit.HOURS.toMillis(getHrsPickerValue()) +
                TimeUnit.MINUTES.toMillis(getMinsPickerValue()));
    }


}
