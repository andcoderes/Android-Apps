package crossline.cl.customimagecarrusel;

import java.util.ArrayList;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import crossline.cl.interfaces.OnCarouselClickListener;

public class Carousel extends View
{
    private ArrayList<Bitmap> listBitmap;
    private float startXMove = 0;
    private float finishXMove = 0;
    private boolean isFirstTime = true;
    private boolean isRight;
    private float startPositionXClick = 0;
    private float startPositionYClick = 0;
    private float marginLeft  = 0;
    private boolean isFirstTimeDraw = true;
    private OnCarouselClickListener listener;

    public Carousel(Context context)
    {
        super(context);
    }
    public Carousel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public Carousel(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

    @SuppressLint("DrawAllocation") @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(isFirstTimeDraw)
        {
            // resize images of the bitmap list
            listBitmap = resizeBitmapListBitmap(listBitmap);
            isFirstTimeDraw = false;
        }
        drawImage(canvas);
    }

    // resize images on the bitmap list
    private ArrayList<Bitmap> resizeBitmapListBitmap(ArrayList<Bitmap> listBitmap)
    {
        ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
        float width = getWidth()/6; // new width
        float height = getHeight(); // new height
        if(listBitmap != null)
        {
            for (int i = 0; i < listBitmap.size(); i++)
            {
                bm.add(resizeBmp(listBitmap.get(i), width, height));
            }
        }
        return bm;
    }

    // resize a bitmap
    public Bitmap resizeBmp(Bitmap mBitmap, double newWidth, double newHeight)
    {
        try
        {
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            float scaleWidth = ((float) newWidth)/width;
            float scaleHeight = ((float) newHeight)/height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
        }
        catch(Exception e)
        {
            Log.e("Exception", e.toString());
            return mBitmap;
        }
    }

    // move the carousel to the right
    private void moveArrayRight()
    {
        Bitmap[] listMap = new Bitmap[listBitmap.size()];
        int position = 0;
        for (int i = 0; i < listBitmap.size(); i++)
        {
            if(i%2 == 0)
            {
                position = i+2;
                if(position < listBitmap.size())
                {
                    listMap[position] = listBitmap.get(i);
                }
                else if(i == (listBitmap.size()-1))
                {
                    listMap[i-1] = listBitmap.get(i);
                }
                else
                {
                    position = i+1;
                    listMap[position] = listBitmap.get(i);
                }
            }
            else
            {
                if(i == 1)
                {
                    listMap[0] = listBitmap.get(i);
                }
                else
                {
                    position = i-2;
                    listMap[position] = listBitmap.get(i);
                }
            }
        }

        for(int i = 0; i < listMap.length; i++)
        {
            listBitmap.remove(i);
            listBitmap.add(i, listMap[i]);
        }
    }

    // mode the carousel to the left
    private void moveArrayLeft()
    {
        Bitmap[] listMap = new Bitmap[listBitmap.size()];
        int position = 0;
        for (int i = 0; i < listBitmap.size(); i++)
        {
            if(i == 0)
            {
                listMap[1] = listBitmap.get(i);
            }
            else if(i%2 == 0)
            {
                position = i-2;
                listMap[position] = listBitmap.get(i);
            }
            else
            {
                position = i+2;
                if(listBitmap.size() > position)
                {
                    listMap[position] = listBitmap.get(i);
                }
                else
                {
                    position = i+1;
                    listMap[position] = listBitmap.get(i);
                }
            }
        }

        for(int i = 0; i < listMap.length; i++)
        {
            listBitmap.remove(i);
            listBitmap.add(i, listMap[i]);
        }
    }

    private void drawImage(Canvas canvas)
    {
        try
        {
            if(listBitmap != null)
            {
                if (isFirstTime)
                {
                    putImage(canvas);
                    isFirstTime = false;
                }
                else
                {
                    if (isRight) { moveArrayRight(); }
                    else if (!isRight) { moveArrayLeft(); }
                    putImage(canvas);
                }
            }
        }
        catch(Exception e)
        {
            Log.e("Exception", e.getMessage().toString());
        }
    }

    private void putImage(Canvas canvas)
    {
        for(int i = (listBitmap.size()-1); i != -1; i--)
        {
            double valueDouble =(double) i/2;
            int value = (int) Math.ceil(valueDouble);
            float width = listBitmap.get(i).getWidth()/2;
            marginLeft = (getWidth()/2-width);
            if(i == 0)
            {
                canvas.drawBitmap(listBitmap.get(i), marginLeft, 0, null);
            }
            else if(i%2 == 0)
            {
                canvas.drawBitmap(listBitmap.get(i), marginLeft + (width * value), 0, null);
            }
            else if(i%2 != 0)
            {
                canvas.drawBitmap(listBitmap.get(i), marginLeft - (width * value), 0, null);
            }
        }
    }

    // motion event of carousel
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startXMove = event.getX();
                startPositionYClick = event.getY();
                startPositionXClick = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                finishXMove = event.getX();
                float move = finishXMove - startXMove;
                if(move > 100)
                {
                    isRight= true;
                    invalidate();
                    startXMove = event.getX();
                }
                else if(move < -100)
                {
                    isRight = false;
                    invalidate();
                    startXMove = event.getX();
                }
                return true;
            case MotionEvent.ACTION_UP:
                float finishPositionXClick = event.getX();
                float finishPositionYClick = event.getY();
                if(isValidateClick(startPositionXClick, finishPositionXClick))
                {
                    Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isValidateClick(float startPositionXClick2, float finishPositionXClick)
    {
        if(startPositionXClick2 < marginLeft + listBitmap.get(0).getWidth() && startPositionXClick2 > marginLeft &&
                finishPositionXClick < marginLeft + listBitmap.get(0).getWidth() && finishPositionXClick > marginLeft)
        {
            if(listener != null)
            {
                listener.onPictureClickListener();
            }
            return true;
        }
        return false;
    }

    // set bitmap list
    public void setListBitmap(ArrayList<Integer> listBitmap)
    {
        this.listBitmap = resizeBitmapList(listBitmap);
        invalidate();
        requestLayout();
    }

    private ArrayList<Bitmap> resizeBitmapList(ArrayList<Integer> innerListBitmap)
    {
        ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
        float width = getWidth()/6;
        float height = getHeight();
        if(innerListBitmap != null)
        {
            for (int i = 0; i < innerListBitmap.size(); i++)
            {
                bm.add(resizeBmp(BitmapFactory.decodeResource(getResources(), innerListBitmap.get(i)), width, height));
            }
        }
        return bm;
    }

    public void setOnCarouselClickListener(OnCarouselClickListener listener)
    {
        this.listener = listener;
    }

    public void removeCarouselClickListener()
    {
        this.listener = null;
    }
}