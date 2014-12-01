package cl.crossline.customalphabeticalorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CustomAlphabetical extends View
{
    private float height;
    private float width;
    private Paint mPaintRect;
    private Paint mPaintLetter;
    private boolean showView;
    private int pressIndexRow;
    private int pressIndexColumn;
    private int squareColor;
    private int letterColor;
    private float marginLeftActual;
    private OnLetterClick onLetterClickListener;
    private ArrayList<String> enableList;
    private String letter;
    private float space;
    private boolean isHiding;

    public CustomAlphabetical(Context context)
    {
        super(context);
        init();
    }
    public CustomAlphabetical(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public CustomAlphabetical(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init()
    {
        mPaintRect = new Paint();
        mPaintRect.setStrokeWidth(3);

        mPaintLetter = new Paint();
        mPaintLetter.setStrokeWidth(3);


        showView = false;
        pressIndexRow = -1;
        pressIndexColumn = -1;
        marginLeftActual = 0;

        squareColor = Color.RED;
        letterColor = Color.WHITE;

        enableList = new ArrayList<String>();
        letter = "#abcdefghijklmnñopqrstuvwxyz";

        space = 0;
        isHiding = false;
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        getMeasured();
        if(showView)
        {
            drawRect(canvas);
        }
    }

    private void drawRect(Canvas canvas)
    {
        int counter=0;
        float largeRect = 0;
        for(int i=0;i<7;i++)
        {
            float marginTop = height * i;

            for(int x=0;x<4;x++)
            {
                putColor(x,i,letter.charAt(counter));
                mPaintLetter.setTextSize(height / 2);

                float marginLeft = (width * x) +marginLeftActual;
                float widthRect = ((width*(x+1))-10)+marginLeftActual;
                float heightRect = (height*(i+1))-10;
                float withLetter = marginLeft+10;
                float heightLetter = heightRect-10;
                largeRect = (heightRect - marginTop);

                canvas.drawRect(marginLeft, marginTop, widthRect,(heightRect -largeRect) + space, mPaintRect);
                canvas.drawText(letter, counter, ++counter, withLetter, heightLetter, mPaintLetter);
                if(space <=  (heightRect - marginTop))
                {
                    invalidate();
                }
            }
        }
        if(space <= largeRect && !isHiding)
        {
            space += 40;
            invalidate();
        }
        else if(isHiding && space > 0)
        {
            space -= 40;
            invalidate();
        }
        else if(isHiding && space <=0)
        {
            isHiding = false;
            showView = false;
            invalidate();
        }

    }

    private void putColor(int x, int i, char letter)
    {
        if(isEnabled(letter))
        {
            if (pressIndexColumn == x && pressIndexRow == i)
            {
                mPaintRect.setColor(letterColor);
                mPaintLetter.setColor(squareColor);
            }
            else
            {
                mPaintRect.setColor(squareColor);
                mPaintLetter.setColor(letterColor);
            }
        }
        else
        {
            mPaintRect.setColor(Color.GRAY);
            mPaintLetter.setColor(Color.DKGRAY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float downX;
        float downY;
        if(isShowView())
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    for (int i = 0; i < 7; i++)
                    {
                        float marginTop = height * i;
                        for (int x = 0; x < 4; x++)
                        {
                            float marginLeft = (width * x) + marginLeftActual;
                            float widthRect = ((width * (x + 1)) - 10) + marginLeftActual;
                            float heightRect = (height * (i + 1)) - 10;

                            if ((downX > marginLeft && downX < widthRect) && (downY > marginTop &&
                                    downY < heightRect))
                            {
                                pressIndexRow = i;
                                pressIndexColumn = x;
                                invalidate();
                            }
                        }
                    }

                    return true;

                case MotionEvent.ACTION_UP:
                    float upX = event.getX();
                    float upY = event.getY();
                    int counter = 0;
                    for (int i = 0; i < 7; i++)
                    {
                        float marginTop = height * i;
                        for (int x = 0; x < 4; x++)
                        {
                            float marginLeft = (width * x)+marginLeftActual;
                            float widthRect = ((width * (x + 1)) - 10)+marginLeftActual;
                            float heightRect = (height * (i + 1)) - 10;

                            if ((upX > marginLeft && upX < widthRect) && (upY > marginTop &&
                                    upY < heightRect) && pressIndexRow == i && pressIndexColumn == x)
                            {
                                if(onLetterClickListener!=null)
                                {
                                    char letterChar = letter.charAt(counter);
                                    onLetterClickListener.onLetterClickListener(this,letterChar,isEnabled(letterChar));
                                }
                                closeView();
                            }
                            counter++;


                        }
                    }
                    pressIndexRow = -1;
                    pressIndexColumn = -1;
                    break;
            }
        }
        invalidate();
        return false;
    }

    private boolean isEnabled(char c)
    {
        boolean isEnabled = false;
        for(String word : enableList)
        {
            if(word.charAt(0) == c)
            {
                isEnabled = true;
                break;
            }
        }
        return isEnabled;
    }

    private void getMeasured()
    {

        if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_PORTRAIT)
        {
            marginLeftActual = 0;
            width = getWidth()/4;
        }
        else
        {
            width = (getWidth()/8);
            marginLeftActual =getWidth()/4;
        }

        height = getHeight()/7;
    }

    public boolean isShowView()
    {
        return showView;
    }
    private void closeView()
    {
        isHiding = true;
    }

    public void setShowView(boolean showView)
    {
        this.showView = showView;
        invalidate();
        requestLayout();
    }

    public void setHideView(boolean hideView)
    {
        closeView();
        invalidate();
        requestLayout();
    }

    public void setOnLetterClickListener(OnLetterClick onLetterClickListener)
    {
        this.onLetterClickListener = onLetterClickListener;
    }
    public void removeOnLetterClickListener()
    {
        this.onLetterClickListener = null;
    }

    public int getLetterColor()
    {
        return letterColor;
    }

    public void setLetterColor(int letterColor)
    {
        this.letterColor = letterColor;
        invalidate();
        requestLayout();
    }

    public int getSquareColor()
    {
        return squareColor;
    }

    public void setSquareColor(int squareColor)
    {
        this.squareColor = squareColor;
        invalidate();
        requestLayout();
    }

    public ArrayList<String> getEnableList()
    {
        return enableList;
    }

    public void setEnableList(ArrayList<String> enableList)
    {
        this.enableList = enableList;
        invalidate();
        requestLayout();
    }
    public void addEnableWord(String word)
    {
        enableList.add(word);
    }
}
interface OnLetterClick
{
    public void onLetterClickListener(CustomAlphabetical view, char letterClick,boolean isEnabled);
}