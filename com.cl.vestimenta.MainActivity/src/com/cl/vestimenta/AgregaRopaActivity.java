package com.cl.vestimenta;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.cl.vestimenta.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class AgregaRopaActivity extends Activity implements OnClickListener {
	
	ImageView ropa1;
	Bitmap bmRopa;
	Display display;
	Button btnBuscar, btnGuardar;
	Spinner spTipo;
	int code;
	LinearLayout layout;
	private static int SELECT_PICTURE = 2;
	int ropa=0;
	private SQLiteDatabase baseDatos;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agrega_ropa);
        display=getWindowManager().getDefaultDisplay();
        inicializarElementos();
    }
    
    private void inicializarElementos() 
    {
    	// Ropa Nueva
    	ropa1 = (ImageView)findViewById(R.id.imagenNueva);
    	bmRopa = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		
		// Botones
    	btnBuscar = (Button)findViewById(R.id.btnBuscar);
    	btnGuardar = (Button)findViewById(R.id.btnGuardar);
    	btnBuscar.setOnClickListener(this);
    	btnGuardar.setOnClickListener(this);
    	
    	// Spinner
    	spTipo = (Spinner)findViewById(R.id.spTipo);
		
		renderizar();
		layout = (LinearLayout)findViewById(R.id.layoutRopa);
		ropa1.setImageBitmap(bmRopa);
		rescatarCategoria();
		baseDatos.close();
		
	}
    
    private void renderizar() 
	{
		int ancho = display.getWidth();  //Ancho Pantalla
		int alto = display.getHeight(); //Alto Pantalla.
		
		bmRopa = Bitmap.createScaledBitmap(bmRopa, (ancho*40)/100, (alto*24)/100, true);
		
	}
    
    public void onClick(View v) 
	{
    	Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		switch (v.getId())
		{
			case R.id.btnBuscar:
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				
   				startActivityForResult(intent, code);
				break;
			case R.id.btnGuardar:
				guardarRopa();
				break;
		}	
	}

    private void guardarRopa() 
    {
    	 int conjunto = leerFicheroMemoriaInterna();
  	   if(conjunto==0)
  	   {
  		   Toast.makeText(this, "hubo un problema leyendo el archivo", Toast.LENGTH_SHORT).show();
  		   conjunto++;
  		   escribirFicheroMemoriaInterna(conjunto);
  		   
  	   }
  	   else
  	   {
  		   //Se obtiene el bitmap con la imagen
  		   layout.setDrawingCacheEnabled(true);
  		   layout.buildDrawingCache();
  		   Bitmap bm = layout.getDrawingCache();
  		   insertarFila(spTipo.getSelectedItem().toString(),conjunto);
  		   baseDatos.close();
  		   escribirConjunto(bm, conjunto);
  		    		   
      		   conjunto ++;
  		   
  		 escribirFicheroMemoriaInterna(conjunto);
  	   
  	   }

	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
	   Bitmap bitmap;
	   BufferedInputStream bis;
	   code = SELECT_PICTURE;
	   
	   		Uri selectedImage = data.getData();
	   		InputStream is;
	   		
			try {
				is = getContentResolver().openInputStream(selectedImage);
				bis = new BufferedInputStream(is);
	   	    	bitmap = BitmapFactory.decodeStream(bis);           
	   	    	ropa1.setImageBitmap(bitmap);
			}
			catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    private int leerFicheroMemoriaInterna()
    {
       	InputStreamReader flujo=null;
       	BufferedReader lector=null;
       	try
       	{
       		flujo= new InputStreamReader(openFileInput("numeroRopa.txt"));
       		lector= new BufferedReader(flujo);
       	    String texto = lector.readLine();
       	  	return Integer.parseInt(texto);
       	    
       	}
       	catch (Exception ex)
       	{
       	    return 0;
       	}
    }
    private void escribirFicheroMemoriaInterna(int conjunto)
    {
    	OutputStreamWriter escritor=null;
    	try
    	{
    	    escritor=new OutputStreamWriter(openFileOutput("numeroRopa.txt", Context.MODE_PRIVATE));
    	    escritor.write(conjunto+"");
    	}
    	catch (Exception ex)
    	{
    	    Log.e("ivan", "Error al escribir fichero a memoria interna");
    	}
    	finally
    	{
    		try {
    			if(escritor!=null)
    				escritor.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
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
    private void insertarFila(String nombreCat, int nomImg) 
	{   
		abrirBasedatos();
		ContentValues values = new ContentValues();  
		values.put("tipoRopa", nombreCat );   
		values.put("nomRopa", nomImg);
	  
		if(baseDatos.insert("ropa", null, values) > 0)
		{
		
		}
		else
		{
			Toast.makeText(this, "hubo un problema al insertar los datos", Toast.LENGTH_SHORT).show();
		}
	}
    private void escribirConjunto(Bitmap bm ,int conjunto) 
    {
 	   File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard, "ropa"+conjunto+".png");
        FileOutputStream fos;
 	     try 
 	     {
 	      fos = new FileOutputStream(file);
 	      bm.compress(CompressFormat.JPEG, 95,fos);
 	     }
 	     catch (FileNotFoundException e) 
 	     {
 	      // TODO Auto-generated catch block
 	      e.printStackTrace();
 	     }
    }
    private boolean rescatarCategoria()
	{
		ArrayAdapter <CharSequence> adapter = null;
    	abrirBasedatos();
		String[] campos = new String[] {"id","tipoRopa"};
		Cursor c = baseDatos.query("categoria_ropa", campos, null, null, null, null, null);
		if(c.moveToFirst())
		{
			adapter =new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			do
			{
				adapter.add(c.getString(1));
			}while(c.moveToNext());
		        
		 
		}
		spTipo.setAdapter(adapter);
		return true;
		
		
		
	}
}
