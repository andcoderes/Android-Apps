package crossline.cl.customimagecarrusel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import crossline.cl.interfaces.OnCarouselClickListener;


public class MyActivity extends Activity implements OnCarouselClickListener {

    private Carousel carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        carouselView = (Carousel) findViewById(R.id.carrouselView);
        //set bitmap list
        carouselView.setListBitmap(getBitmapList());
        // set onclick listener
        carouselView.setOnCarouselClickListener(this);
    }

    // create bitmap list with the images
    private ArrayList<Integer> getBitmapList()
    {
        ArrayList<Integer> bitmapList = new ArrayList<Integer>();
        bitmapList.add(R.drawable.image1);
        bitmapList.add(R.drawable.image2);
        bitmapList.add(R.drawable.image3);
        bitmapList.add(R.drawable.image4);
        bitmapList.add(R.drawable.image5);
        bitmapList.add(R.drawable.image6);
        bitmapList.add(R.drawable.image7);
        return bitmapList;
    }

    // onclick picture listener
    @Override
    public void onPictureClickListener()
    {
        Toast.makeText(this,"Click First Picture",Toast.LENGTH_SHORT).show();
    }
}
