package crossline.cl.customcalendar;

import crossline.cl.customcalendar.R;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import crossline.cl.interfaces.OnDateClickListener;
import crossline.cl.interfaces.OnMonthChange;

/**
 * Created by Jaime on 28/08/2014.
 * OwnCalendarView is a Calendar for android
 * I hope this widget can be useful for all people
 */
@SuppressWarnings("ALL")
public class CustomCalendar extends View
{
    public static final String FECHA="date";
    public static final String COLOR="color";
    public static final String BACKGROUND_REFERENCE="background";
    public static final String ICON_REFERENCE="icon";
    private Paint mTextPaint;
    private Paint mTextTitleMothYearPaint;
    private Paint mTextDay;
    private Paint mTextNumberDay;
    private Paint mCalendarPaint;
    private float height;
    private float width;
    private ArrayList<Integer> days;
    private ArrayList<String> listDaysNameString;
    private ArrayList<String> listMonth;
    private Calendar calendar;
    private int firstDay;
    private Rect rectButtonLeft;
    private Rect rectButtonRight;
    private OnMonthChange monthChangeListener;
    private OnDateClickListener dateClickListener;
    private ArrayList<ContentValues> listDates;
    private boolean showLines;
    private boolean showDivideVision;
    private float dayTextSize;
    private float numDaySize;
    private float titleMonthSize;
    private int rightArrow;
    private int leftArrow;


    public CustomCalendar(Context context)
    {
        super(context);
        init();
    }

    public CustomCalendar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getAttribute(context,attrs);
        init();
    }

    private void getAttribute(Context context, AttributeSet attrs)
    {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomCalendar,
                0, 0);
        try
        {
            firstDay = a.getInt(R.styleable.CustomCalendar_firstDay,0);
            dayTextSize = a.getFloat(R.styleable.CustomCalendar_titleFontSize, 0);
            numDaySize = a.getFloat(R.styleable.CustomCalendar_numberFontSize,0);
            titleMonthSize = a.getFloat(R.styleable.CustomCalendar_monthFontSize,0);
            showDivideVision = a.getBoolean(R.styleable.CustomCalendar_isSeparatedView,false);
            showLines = a.getBoolean(R.styleable.CustomCalendar_shownLines,true);
        }
        finally
        {
            a.recycle();
        }
    }

    public CustomCalendar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        getAttribute(context, attrs);
        init();
    }


    private void init()
    {

        listDates  = new ArrayList<ContentValues>();
        if(calendar ==null)
        {
            calendar = Calendar.getInstance();
        }
        chargeDays();
        chargueMonth();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);

        mTextTitleMothYearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextTitleMothYearPaint.setColor(Color.BLACK);

        mTextNumberDay = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextNumberDay.setColor(Color.BLACK);

        mTextDay = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextDay.setColor(Color.BLACK);

        mCalendarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCalendarPaint.setColor(Color.BLACK);
        mCalendarPaint.setStrokeWidth(3);

    }

    private void chargueMonth()
    {
        listMonth = new ArrayList<String>();
        listMonth.add("Enero");
        listMonth.add("Febrero");
        listMonth.add("Marzo");
        listMonth.add("Abril");
        listMonth.add("Mayo");
        listMonth.add("Junio");
        listMonth.add("Julio");
        listMonth.add("Agosto");
        listMonth.add("Septiembre");
        listMonth.add("Octubre");
        listMonth.add("Noviembre");
        listMonth.add("Diciembre");
    }

    private void chargeDays()
    {
        listDaysNameString = new ArrayList<String>();
        listDaysNameString.add("Lun");
        listDaysNameString.add("Mar");
        listDaysNameString.add("Mie");
        listDaysNameString.add("Jue");
        listDaysNameString.add("Vie");
        listDaysNameString.add("Sab");
        listDaysNameString.add("Dom");
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        initMeasure();
        if(showLines)
        {
            canvas.drawLines(getInnerLines(), mCalendarPaint);
        }
        createButtons(canvas);
        writeTitle(canvas);
        putMonthYear(canvas);
        fullArrayNumber();
        drawColor(canvas);
        drawBitmapMarker(canvas);
        if(showDivideVision)
        {
            putNumberIcon(canvas);
        }
        putNumber(canvas);
    }

    private void putNumberIcon(Canvas canvas)
    {
        for(ContentValues values : listDates)
        {
            long calendarIntern = (Long) values.get(FECHA);

            Calendar calendarInner = Calendar.getInstance();
            calendarInner.setTimeInMillis(calendarIntern);
            if(calendarInner.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)&&values.containsKey(ICON_REFERENCE))
            {
                for (int i = 0; i < days.size(); i++)
                {
                    if (days.get(i) != 0)
                    {
                        Calendar innerCalendar = Calendar.getInstance();
                        innerCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), days.get(i));
                        if(getZeroTimeDate(calendarInner.getTime()).compareTo(getZeroTimeDate(innerCalendar.getTime())) == 0)
                        {
                            float innerHeight = getInnerHeight(i);
                            float innerWidth = getInnerWidth(i);
                            int resourceBackgroundBitmapMarker = (Integer)values.get(ICON_REFERENCE);
                            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), resourceBackgroundBitmapMarker);
                            canvas.drawBitmap(redimensionarBmp(mBitmap,width/2,height/2),innerWidth+(float)(width/3.5),innerHeight+(height/3),null);

                            break;
                        }

                    }
                }
            }
        }
    }

    private void drawBitmapMarker(Canvas canvas)
    {
        for(ContentValues values : listDates)
        {
            long calendarIntern = (Long) values.get(FECHA);

            Calendar calendarInner = Calendar.getInstance();
            calendarInner.setTimeInMillis(calendarIntern);
            if(calendarInner.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)&&values.containsKey(BACKGROUND_REFERENCE))
            {
                for (int i = 0; i < days.size(); i++)
                {
                    if (days.get(i) != 0)
                    {
                        Calendar innerCalendar = Calendar.getInstance();
                        innerCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), days.get(i));
                        if(getZeroTimeDate(calendarInner.getTime()).compareTo(getZeroTimeDate(innerCalendar.getTime())) == 0)
                        {
                            float innerHeigth = getInnerHeight(i);
                            float innerWidth = getInnerWidth(i);
                            int resourceBackgroundBitmapMarker = (Integer)values.get(BACKGROUND_REFERENCE);
                            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), resourceBackgroundBitmapMarker);
                            canvas.drawBitmap(redimensionarBmp(mBitmap,width,height),innerWidth,innerHeigth,null);

                            break;
                        }

                    }
                }
            }
        }
    }
    private Bitmap redimensionarBmp(Bitmap mBitmap, double newWidth, double newHeight){

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }
    private void fullArrayNumber()
    {
        days = null;
        days = new ArrayList<Integer>();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        int firstDayCalendar = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDayWeek = getFirstDayCalendar(firstDayCalendar);
        int amountDaysMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int counter = 1;
        week:
        for(int x = 2;x<=7;x++)
        {
            for (int i = 1; i < 8; i++)
            {
                if (x==2)
                {
                    if(i >= firstDayWeek)
                    {
                        days.add(counter);
                        counter++;
                    }
                    else
                    {
                        days.add(0);
                    }
                }
                else
                {
                    days.add(counter);

                    if(counter==amountDaysMonth)
                    {
                        break week;
                    }
                    counter++;
                }
            }
        }
    }

    private void putMonthYear(Canvas canvas)
    {

        float innerWidth = (getWidth()- mCalendarPaint.getStrokeWidth()) - (width*2);
        if(titleMonthSize == 0)
        {
            float innerHeight = height / 2;
            mTextTitleMothYearPaint.setTextSize((float) (innerHeight / 1.5));
        }
        else
        {
            mTextTitleMothYearPaint.setTextSize(titleMonthSize);
        }
        canvas.drawText(listMonth.get(calendar.get(Calendar.MONTH))+ " " + calendar.get(Calendar.YEAR), (width/2) + (innerWidth/3), height/2, mTextPaint);
    }

    private void createButtons(Canvas canvas)
    {
        int largo = (int)(height-(height/3));
        int ancho = (int)(width - (width/1.5));
        int anchoClick = (int)(width);
        int largoClick = (int)(height);
        int initRight = (int)(width*7)-ancho;
        int initRightClick = (int)(width)*7-anchoClick;
        //left, top rigth bottom
        rectButtonLeft = new Rect(0,0,anchoClick,largoClick);
        rectButtonRight = new Rect(initRight,0,initRightClick+anchoClick,largoClick);

        Bitmap bmLeft;
        Bitmap bmRight;
        if(leftArrow==0)
        {
            bmLeft = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_left);
        }
        else
        {
            bmLeft = BitmapFactory.decodeResource(getResources(),leftArrow);
        }
        if(rightArrow == 0)
        {
            bmRight = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_rigth);
        }
        else
        {
            bmRight = BitmapFactory.decodeResource(getResources(),rightArrow);
        }

        bmLeft = redimensionarBmp(bmLeft,ancho,largo);
        bmRight = redimensionarBmp(bmRight,ancho,largo);

        canvas.drawBitmap(bmRight,0,3,null);
        canvas.drawBitmap(bmLeft,initRight,3,null);

    }


    private void initMeasure()
    {
        width  = (getWidth()- mCalendarPaint.getStrokeWidth())/7;
        height = (getHeight()- mCalendarPaint.getStrokeWidth())/8;
    }

    private ArrayList<String> getDaysOfWeek()
    {
        ArrayList<String> listDaysNameStringNew = new ArrayList<String>();
        while (listDaysNameStringNew.size() != listDaysNameString.size())
        {
            listDaysNameStringNew.add(listDaysNameString.get(firstDay));
            firstDay++;
            if(firstDay==7)
            {
                firstDay = 0;
            }

        }
        return listDaysNameStringNew;
    }
    private void writeTitle(Canvas canvas)
    {
        float innerHeight = height/2;
        float heightSize = height + innerHeight;
        mTextPaint.setTextSize((float) (innerHeight/1.5));
        float center = (float)(width/3.5);

        ArrayList<String> listString = getDaysOfWeek();
        if(dayTextSize == 0)
        {
            mTextDay.setTextSize((float) (innerHeight / 1.5));
        }
        else
        {
            mTextDay.setTextSize(titleMonthSize);
        }

        for(int i=0;i<7;i++)
        {
            float widthActual = width * i;
            //String text, int Start, int End
            if(listString.get(i).length()>1)
            {
                canvas.drawText(listString.get(i), 0, listString.get(i).length(), widthActual + (center-10), heightSize, mTextDay);
            }
            else
            {
                canvas.drawText(listString.get(i), 0, listString.get(i).length(), widthActual + center, heightSize, mTextDay);
            }
        }

    }

    private float[]getInnerLines()
    {
        float[] listPoint = new float[64];
        float height = (getHeight()- mCalendarPaint.getStrokeWidth())/8;
        float width  = (getWidth()- mCalendarPaint.getStrokeWidth())/7;
        int counter = 0;
        for(int i=1;i<9;i++)
        {
            float heightActual = height * i;
            listPoint[counter++] = 0f;
            listPoint[counter++] = heightActual;
            listPoint[counter++] = getWidth() - 3f;
            listPoint[counter++] = heightActual ;
        }
        //float startX, float startY, float stopX, float stopY, Paint paint
        for(int i=0;i<8;i++)
        {
            float widthActual = width * i;
            listPoint[counter++] = widthActual;
            listPoint[counter++] = height;
            listPoint[counter++] = widthActual;
            listPoint[counter++] = getHeight() ;
        }
        return listPoint;

    }
    private void putNumber(Canvas canvas)
    {

        int firstDayCalendar = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDayWeek = getFirstDayCalendar(firstDayCalendar);

        float marginTop = (float)(height/3.5);
        if(!showDivideVision)
        {
            marginTop = height / 2;
        }
        if(numDaySize == 0)
        {
            float innerHeight = height / 2;
            mTextNumberDay.setTextSize((float) (innerHeight / 1.5));
        }
        else
        {
            mTextNumberDay.setTextSize(titleMonthSize);
        }

        int amountDaysMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        float center = width/2;
        int counter = 1;
        Calendar innerCalendar = Calendar.getInstance();

        week:
        for(int x = 2;x<=7;x++)
        {
            float heightActual = height*x;
            for (int i = 1; i < 8; i++)
            {
                if (x==2)
                {
                    if(i >= firstDayWeek)
                    {
                        String counter2 = counter + "";
                        float widthActual = width * (i - 1);
                        //String text, int Start, int End
                        innerCalendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),counter);
                        canvas.drawText(counter2, 0, counter2.length(), widthActual + (center - 10), (heightActual) + marginTop, mTextNumberDay);
                        counter++;
                    }
                }
                else
                {
                    String counter2 = counter + "";
                    float widthActual = width * (i - 1);
                    //String text, int Start, int End
                    canvas.drawText(counter2, 0, counter2.length(), widthActual + (center - 10), (heightActual) + marginTop, mTextNumberDay);

                    if(counter==amountDaysMonth)
                    {
                        break week;
                    }
                    counter++;
                }
            }

        }
    }

    private void drawColor(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        for(ContentValues values : listDates)
        {

            long calendarIntern = (Long) values.get(FECHA);
            Calendar calendarInner = Calendar.getInstance();
            calendarInner.setTimeInMillis(calendarIntern);
            if(calendarInner.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)&&values.containsKey(COLOR))
            {
                for (int i = 0; i < days.size(); i++)
                {
                    if (days.get(i) != 0)
                    {
                        Calendar innerCalendar = Calendar.getInstance();
                        innerCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), days.get(i));

                        if(getZeroTimeDate(calendarInner.getTime()).compareTo(getZeroTimeDate(innerCalendar.getTime())) == 0)
                        {
                            float innerHeight = getInnerHeight(i);
                            float innerWidth = getInnerWidth(i);
                            int color = (Integer)values.get(COLOR);
                            paint.setColor(color);
                            float strokeWidth = mCalendarPaint.getStrokeWidth();
                            canvas.drawRect(innerWidth+strokeWidth-1,innerHeight+strokeWidth-1,innerWidth+width-strokeWidth+2,innerHeight+height-strokeWidth+2,paint);
                            break;
                        }

                    }
                }
            }
        }
    }

    private float getInnerWidth(int i)
    {
        switch (i)
        {
            case 0:
            case 7:
            case 14:
            case 21:
            case 28:
            case 35:
                return width*0;
            case 1:
            case 8:
            case 15:
            case 22:
            case 29:
            case 36:
                return width*1;
            case 2:
            case 9:
            case 16:
            case 23:
            case 30:
            case 37:
                return width*2;
            case 3:
            case 10:
            case 17:
            case 24:
            case 31:
            case 38:
                return width*3;
            case 4:
            case 11:
            case 18:
            case 25:
            case 32:
            case 39:
                return width*4;
            case 5:
            case 12:
            case 19:
            case 26:
            case 33:
            case 40:
                return width*5;
            case 6:
            case 13:
            case 20:
            case 27:
            case 34:
            case 41:
                return width*6;

        }
        return 0;
    }

    private float getInnerHeight(int i)
    {

        if(i>=0 && i<=6)
        {
            return height*2;
        }
        else if(i>=7 && i<=13)
        {
            return height*3;
        }
        else if(i>=14 && i<=20)
        {
            return height*4;
        }
        else if(i>=21 && i<=27)
        {
            return height*5;
        }
        else if(i>=28 && i<=34)
        {
            return height*6;
        }
        else if(i>=34 && i<=41)
        {
            return height*7;
        }
        return 0;
    }

    private Date getZeroTimeDate(Date fecha)
    {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( fecha );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

    private int getFirstDayCalendar(int firstDayCalendar)
    {
        int firstDayWeek = firstDayCalendar - firstDay - 1;
        if (firstDayWeek <= 0)
        {
            firstDayWeek = 7 - (firstDayWeek * -1);
        }


        return firstDayWeek;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                getPoint(event);
                break;
        }
        return super.onTouchEvent(event);

    }

    private void getPoint(MotionEvent event)
    {
        float height = (getHeight()- mCalendarPaint.getStrokeWidth())/8;
        float width  = (getWidth()- mCalendarPaint.getStrokeWidth())/7;
        float widthLeft = 0;
        float heightUp = 0;
        int linea = 0;
        int fila = 0;


        if(event.getX()>= rectButtonRight.left && event.getX() <= rectButtonRight.right
                && event.getY()>= rectButtonRight.top && event.getY()<= rectButtonRight.bottom)
        {
            if(monthChangeListener !=null)
            {
                int index = calendar.get(Calendar.MONTH) + 1;
                if(index >=listMonth.size())
                {
                    index = 0;
                }
                monthChangeListener.onMonthChange(listMonth.get(calendar.get(Calendar.MONTH)), listMonth.get(index));
            }
            calendar.add(Calendar.MONTH,1);
            invalidate();
            return;
        }
        if(event.getX()>=rectButtonLeft.left && event.getX() <= rectButtonLeft.right
                && event.getY()>=rectButtonLeft.top && event.getY()<=rectButtonLeft.bottom)
        {
            if(monthChangeListener !=null)
            {
                int index = calendar.get(Calendar.MONTH) - 1;
                if(index < 0)
                {
                    index = 11;
                }
                monthChangeListener.onMonthChange(listMonth.get(calendar.get(Calendar.MONTH)), listMonth.get(index));
            }
            calendar.add(Calendar.MONTH,-1);
            invalidate();
            return;
        }

        for(int i = 1; i<9;i++)
        {
            float heightAlgorithm = height *(i+1);
            if(event.getY()>heightUp && event.getY()<heightAlgorithm)
            {
                linea = i;
                break;
            }
            heightUp = heightAlgorithm;
        }
        for(int i = 1; i<9; i++)
        {
            float widthAlgorithm = width *i;
            if(event.getX()>widthLeft && event.getX()<widthAlgorithm)
            {
               fila = i;
                break;
            }
            widthLeft = widthAlgorithm;
        }
        int slot = (((linea-1)*7)+fila)-8;
        if(slot<days.size()&& slot> -1 &&days.get(slot)!=0)
        {

            Calendar internCalendar = Calendar.getInstance();
            internCalendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), days.get(slot));

            if(dateClickListener!=null)
            {
                dateClickListener.onDateClickListener(internCalendar);
            }
        }
    }

    public int getFirstDay()
    {
        return firstDay;
    }

    public void setFirstDay(int firstDay)
    {
        this.firstDay = firstDay;
        invalidate();
        requestLayout();
    }

    public ArrayList<String> getListDaysNameString()
    {
        return listDaysNameString;
    }

    public void setListDaysNameString(ArrayList<String> listDaysNameString)
    {
        this.listDaysNameString = listDaysNameString;
        invalidate();
        requestLayout();
    }

    public int getDay()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setDay(int day)
    {
        calendar.set(Calendar.DAY_OF_MONTH,day);
        invalidate();
        requestLayout();
    }

    public int getMonth()
    {
        return calendar.get(Calendar.MONTH);
    }

    public void setMonth(int month)
    {
        calendar.set(Calendar.MONTH,month);
        invalidate();
        requestLayout();
    }

    public int getYear()
    {
        return calendar.get(Calendar.MONTH);
    }

    public void setYear(int year)
    {
        calendar.set(Calendar.YEAR,year);
        invalidate();
        requestLayout();
    }

    public ArrayList<String> getListMonth()
    {
        return listMonth;
    }

    public void setListMonth(ArrayList<String> listMonth)
    {
        this.listMonth = listMonth;
        invalidate();
        requestLayout();
    }
    public void setOnDateClickListener(OnDateClickListener onDateClickListener)
    {
        this.dateClickListener = onDateClickListener;
    }
    public void removeOnDateClickListener()
    {
        this.dateClickListener=null;
    }
    public void setOnMonthChangeListener(OnMonthChange onMonthChangeListener)
    {
        this.monthChangeListener = onMonthChangeListener;
    }
    public void removeOnMonthChangeListener()
    {
        this.monthChangeListener = null;
    }

    public ArrayList<ContentValues> getListDates()
    {
        return listDates;
    }

    public void setListDates(ArrayList<ContentValues> listDates)
    {
        this.listDates = listDates;
        invalidate();
        requestLayout();
    }
    public void addDate(ContentValues values)
    {
        this.listDates.add(values);
        invalidate();
        requestLayout();
    }

    public boolean isShowLines()
    {
        return showLines;
    }

    public void setShowLines(boolean showLines)
    {
        this.showLines = showLines;
        invalidate();
        requestLayout();
    }
    public boolean isShowDivideVision()
    {
        return showDivideVision;
    }

    public void setShowDivideVision(boolean showDivideVision)
    {
        this.showDivideVision = showDivideVision;
        invalidate();
        requestLayout();
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();

        DateToSave ss = new DateToSave(superState);

        ss.dateToSave = this.calendar.getTime().getTime();

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(!(state instanceof DateToSave)) {
            super.onRestoreInstanceState(state);
            return;
        }

        DateToSave ss = (DateToSave)state;
        super.onRestoreInstanceState(ss.getSuperState());

        calendar.setTimeInMillis(ss.dateToSave);
    }

    public int getRightArrow()
    {
        return rightArrow;
    }

    public void setRightArrow(int rightArrow)
    {
        this.rightArrow = rightArrow;
        invalidate();
        requestLayout();
    }

    public int getLeftArrow()
    {
        return leftArrow;
    }

    public void setLeftArrow(int leftArrow)
    {
        this.leftArrow = leftArrow;
        invalidate();
        requestLayout();
    }

    public void setDayTextSize(float dayTextSize)
    {
        this.dayTextSize = dayTextSize;
        invalidate();
        requestLayout();
    }
    public float getDayTextSize()
    {
        return dayTextSize;
    }
    public float getNumDaySize()
    {
        return numDaySize;
    }
    public void setNumDaySize(float numDaySize)
    {
        this.numDaySize = numDaySize;
        invalidate();
        requestLayout();
    }
    public float getTitleMonthSize()
    {
        return titleMonthSize;
    }
    public void setTitleMonthSize(float titleMonthSize)
    {
        this.titleMonthSize = titleMonthSize;
        invalidate();
        requestLayout();
    }
    static class DateToSave extends BaseSavedState
    {
        long dateToSave;
        public DateToSave(Parcel source)
        {
            super(source);
            this.dateToSave = source.readLong();
        }

        public DateToSave(Parcelable superState)
        {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.dateToSave);
        }

    }
}
