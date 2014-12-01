package crossline.cl.customcalendar;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import crossline.cl.interfaces.OnDateClickListener;
import crossline.cl.interfaces.OnMonthChange;


public class Calendar extends Activity implements OnDateClickListener, OnMonthChange
{
    private CustomCalendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        calendar = new CustomCalendar(this);
        initCalendar();
        addHintDates();

        setContentView(calendar);

        //add listener for month and day's
        calendar.setOnDateClickListener(this);
        calendar.setOnMonthChangeListener(this);
    }

    private void addHintDates()
    {
        java.util.Calendar calendario = java.util.Calendar.getInstance();
        Long milisecond = ((Date)calendario.getTime()).getTime();
        ContentValues contentValues = new ContentValues();

        //date in milliseconds (long)
        contentValues.put(CustomCalendar.FECHA,milisecond);

        //change background color
        contentValues.put(CustomCalendar.COLOR, Color.RED);

        //change background image
        contentValues.put(CustomCalendar.BACKGROUND_REFERENCE, R.drawable.ic_launcher);

        //icon from date, for this option you need set showDivideVision to true
        contentValues.put(CustomCalendar.ICON_REFERENCE, R.drawable.ic_launcher);

        //show icon down number date
        calendar.setShowDivideVision(true);

        //add event to calendar
        calendar.addDate(contentValues);
    }

    private void initCalendar()
    {
        //change days
       /* ArrayList<String> days = new ArrayList<String>();
        days.add("Lun"); //monday
        days.add("Mar"); //tuesday
        days.add("Mie"); //wednesday
        days.add("Jue"); //Tuesday
        days.add("Vie"); //Friday
        days.add("Sab"); //Saturday
        days.add("Dom"); //Sunday
        calendar.setListDaysNameString(days);

        //change months
        ArrayList<String> months = new ArrayList<String>();
        months.add("Enero"); //January
        months.add("Febrero"); //February
        months.add("Marzo"); //March
        months.add("Abril"); //April
        months.add("Mayo"); //May
        months.add("Junio"); //June
        months.add("Julio"); //July
        months.add("Agosto"); //August
        months.add("Septiembre"); //September
        months.add("Octubre"); //October
        months.add("Noviembre"); //November
        months.add("Diciembre"); //December
        calendar.setListMonth(months);

        //if you want show lines of the calendar, you need set showDivideVision on true
        //calendar.setShowLines(true);

        //it's no necessary, but you can set the size of month, number and days
        /*calendar.setTitleMonthSize(50);
        calendar.setNumDaySize(50);
        calendar.setDayTextSize(50);*/

        //change the first day of week
        /*
            0.- monday
            1.- tuesday
            2.- wednesday
            3.- thursday
            4.- friday
            5.- saturday
            6.- sunday
         */
        //calendar.setFirstDay(3);

        //change the left an rigth arrow
        /*calendar.setRightArrow(R.drawable.ic_launcher);
        calendar.setLeftArrow(R.drawable.ic_launcher);*/

        //change the date
        /*calendar.setMonth(4); //this start from 0
        calendar.setYear(2016);*/

    }

    @Override
    public void onDateClickListener(java.util.Calendar calendar)
    {
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        Toast.makeText(this,format1.format(calendar.getTime()),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChange(String previousMonth, String actualMonth)
    {
        Toast.makeText(this,"previous Month: " + previousMonth +"\nActual Month: " + actualMonth,
                        Toast.LENGTH_SHORT).show();
    }
}
