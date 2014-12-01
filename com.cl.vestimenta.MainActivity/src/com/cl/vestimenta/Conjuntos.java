package com.cl.vestimenta;

import java.io.File;

import com.cl.vestimenta.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Conjuntos extends Activity implements OnClickListener {
	
	
	Display display;
	private SQLiteDatabase baseDatos;
	Button btnConjuntos, btnCategoria;
	TableLayout tabla;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conjuntos);
        display=getWindowManager().getDefaultDisplay();
        inicializarElementos();
        rescatarCategoria();
        baseDatos.close();
    }
    
    private void inicializarElementos() 
    {
    	
		
		// Botones
		btnConjuntos = (Button)findViewById(R.id.btnAgregarConjunto);
		btnCategoria = (Button)findViewById(R.id.btnAgregarCategoria);
		btnConjuntos.setOnClickListener(this);
		btnCategoria.setOnClickListener(this);
		
		tabla = (TableLayout)findViewById(R.id.tablaAccesorio);		
		
		
	
		
	}
    
    private Bitmap redimensionarImagen(Bitmap bm) 
	{
		int ancho = display.getWidth();  //Ancho Pantalla
		int alto = display.getHeight(); //Alto Pantalla.
		bm = Bitmap.createScaledBitmap(bm, (ancho*40)/100, (alto*24)/100, true);
		return bm;
	
	}
    
    public void onClick(View v) 
	{
    	Intent abrirInicio;
		switch (v.getId())
		{
			case R.id.btnAgregarConjunto:
				abrirInicio = new Intent("com.cl.vestimenta.Conjunto");
        		startActivity(abrirInicio);
				break;
			case R.id.btnAgregarCategoria:
				abrirInicio = new Intent("com.cl.vestimenta.AgregarCategoria");
        		startActivity(abrirInicio);
				break;
		}	
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    private boolean rescatarCategoria()
	{
    	abrirBasedatos();
		String[] campos = new String[] {"id","nombre"};
		Cursor c = baseDatos.query("categoria", campos, null, null, null, null, null);
		if (c.moveToFirst())
		{
			do
			{
				TableRow linea1 = new TableRow(this);
				TableRow linea2 = new TableRow(this);
				TextView texto = new TextView(this);
				texto.setText(c.getString(1));
				texto.setTextColor(Color.WHITE);
				texto.setTextSize(35);
				linea1.addView(texto);
				tabla.addView(linea1);
				
				rescatarConjuntos(c.getString(1),linea2);
			}while(c.moveToNext());
			    return true;
		}
		else
		{
			Toast.makeText(this, "no se hiso nada", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
	}
    private void rescatarConjuntos(String conjunto, TableRow linea2) 
    {
    	
    	ImageView foto;
		File file;
        Bitmap image;    	
    	String[] variables = new String [] {conjunto};
    	String[] campos = new String[] {"id","nom_cat","nomCategoria"};
		Cursor cu = baseDatos.query("conjunto", campos, "nom_cat=?", variables, null, null, null);
		if (cu.moveToFirst())
		{
			HorizontalScrollView scroll = new HorizontalScrollView(this);
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			
			file = new File(Environment.getExternalStorageDirectory(),cu.getString(2)+".jpg");
			do
			{
				if (file.exists())
				{
					foto = new ImageView(this);
			        //Tenemos la foto guardada en la SD, asi que la cargamos
			        image = BitmapFactory.decodeFile("/sdcard/"+cu.getString(2)+".jpg");
			        foto.setImageBitmap(redimensionarImagen(image));
			        layout.addView(foto);
		        
		    	}
				
			}while(cu.moveToNext());
			 scroll.addView(layout);
			 linea2.addView(scroll);		
			 tabla.addView(linea2);
			
			
			
		}
		else
		{
			//Toast.makeText(this, "no se hiso nada", Toast.LENGTH_SHORT).show();
		}
		
	}

	private void abrirBasedatos()
  	{ 
  	    try 
  	    {   
  	      baseDatos = openOrCreateDatabase("Test", MODE_WORLD_WRITEABLE, null);    	            
  	    }    
  	    catch (Exception e)
  	    {   
  	      e.printStackTrace();   
  	    }     
  	    
  	}
}
