package com.cl.vestimenta;

import java.io.File;

import com.cl.vestimenta.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Shop extends Activity {
	
	ImageView imgPronto;
	Bitmap bpm = null;
	Display display;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronto);
        display=getWindowManager().getDefaultDisplay();
        //inicializarElementos();
    }
    
    private void inicializarElementos() 
    {
    	/*Dialog dia = new Dialog(this);
    	dia.setContentView(R.layout.pronto);
    	dia.show();
    	imgPronto = (ImageView)dia.findViewById(R.id.);
    	imgPronto.setImageBitmap(bpm);*/
	}
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}

