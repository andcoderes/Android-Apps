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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class Conjunto extends Activity implements OnClickListener
{
	ImageView imgPolera;
	ImageView imgPantalon;
	ImageView imgZapato;
	ImageView imgAccesorio;
	ImageButton btnPoleraConjunto;
	ImageButton btnPantalonConjunto;
	ImageButton btnZapatoConjunto;
	ImageButton btnAccesorioConjunto;
	Button btnGuardar;
	Spinner spnCategoria;
	 LinearLayout layout;
	int code;
	int codeCosa;
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private static int Polera = 1;
	private static int Pantalon = 2;
	private static int Zapato = 3;
	private static int Accesorio = 4;
	private SQLiteDatabase baseDatos;
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_conjunto);
        inicializarElementos();
        
        rescatarCategoria();
		baseDatos.close();
    }

    private void inicializarElementos()
    {
		imgPolera = (ImageView)findViewById(R.id.imgPolera);
		imgPantalon = (ImageView)findViewById(R.id.imgPantalon);
		imgZapato = (ImageView)findViewById(R.id.imgZapato);
		imgAccesorio = (ImageView)findViewById(R.id.imgAccesorio);
		btnAccesorioConjunto= (ImageButton)findViewById(R.id.btnAccesorioConjunto);
		btnPoleraConjunto= (ImageButton)findViewById(R.id.btnPoleraConjunto);
		btnPantalonConjunto= (ImageButton)findViewById(R.id.btnPantalonConjunto);
		btnZapatoConjunto= (ImageButton)findViewById(R.id.btnZapatoConjunto);
		btnGuardar = (Button)findViewById(R.id.btnGuardarConjunto);
		layout = (LinearLayout)findViewById(R.id.layoutFotos);
		spnCategoria = (Spinner)findViewById(R.id.spnCategorias);
	
		
		btnAccesorioConjunto.setOnClickListener(this);
		btnPantalonConjunto.setOnClickListener(this);
		btnPoleraConjunto.setOnClickListener(this);
		btnZapatoConjunto.setOnClickListener(this);
		btnGuardar.setOnClickListener(this);
		
	
		
		
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
	
	private boolean rescatarCategoria()
	{
		ArrayAdapter <CharSequence> adapter = null;
    	abrirBasedatos();
		String[] campos = new String[] {"id","nombre"};
		Cursor c = baseDatos.query("categoria", campos, null, null, null, null, null);
		if(c.moveToFirst())
		{
			adapter =new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			do
			{
				adapter.add(c.getString(1));
			}while(c.moveToNext());
		        
		 
		}
		spnCategoria.setAdapter(adapter);
		return true;
		
		
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) 
	{
		Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		switch (v.getId())
		{
			case R.id.btnPoleraConjunto:
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				code = SELECT_PICTURE;
   				codeCosa = Polera;
   				startActivityForResult(intent, code);
				break;
			case R.id.btnPantalonConjunto:
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				code = SELECT_PICTURE;
   				codeCosa = Pantalon;
   				startActivityForResult(intent, code);
				break;
			case R.id.btnZapatoConjunto:
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				code = SELECT_PICTURE;
   				codeCosa = Zapato;
   				startActivityForResult(intent, code);
				break;
			case R.id.btnAccesorioConjunto:
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				code = SELECT_PICTURE;
   				codeCosa = Accesorio;
   				startActivityForResult(intent, code);	
				break;			
			case R.id.btnGuardarConjunto:
				guardarConjunto();
			break;
		}
		
	}
   private void guardarConjunto()
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
		   insertarFila(spnCategoria.getSelectedItem().toString(),conjunto);
		   baseDatos.close();
		   escribirConjunto(bm, conjunto);
		   
		  
		   
		   Toast.makeText(this, "se guardo el conjunto", Toast.LENGTH_SHORT).show();
		   
		   conjunto ++;
		   
		 escribirFicheroMemoriaInterna(conjunto);
	   
	   }
   }
   private void escribirConjunto(Bitmap bm ,int conjunto) 
   {
	   File sdCard = Environment.getExternalStorageDirectory();
       File file = new File(sdCard, conjunto+".jpg");
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

   private void escribirFicheroMemoriaInterna(int conjunto)
   {
   	OutputStreamWriter escritor=null;
   	try
   	{
   	    escritor=new OutputStreamWriter(openFileOutput("numeroConjunto.txt", Context.MODE_PRIVATE));
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
   private int leerFicheroMemoriaInterna()
   {
   	InputStreamReader flujo=null;
   	BufferedReader lector=null;
   	try
   	{
   		flujo= new InputStreamReader(openFileInput("numeroConjunto.txt"));
   		lector= new BufferedReader(flujo);
   	    String texto = lector.readLine();
   	  	return Integer.parseInt(texto);
   	    
   	}
   	catch (Exception ex)
   	{
   	    return 0;
   	}
   }


@Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
	   Bitmap bitmap;
	   BufferedInputStream bis;
	    if (requestCode == SELECT_PICTURE)
	   	{
	   		Uri selectedImage = data.getData();
	   		InputStream is;
	   		try 
	   		{
	   			switch (codeCosa) 
	   			{
					case 1:
						is = getContentResolver().openInputStream(selectedImage);
			   	    	bis = new BufferedInputStream(is);
			   	    	bitmap = BitmapFactory.decodeStream(bis);           
			   	    	imgPolera.setImageBitmap(bitmap);
						break;
					case 2:
						is = getContentResolver().openInputStream(selectedImage);
			   	    	bis = new BufferedInputStream(is);
			   	    	bitmap = BitmapFactory.decodeStream(bis);           
			   	    	imgPantalon.setImageBitmap(bitmap);		
						break;
					case 3:
						is = getContentResolver().openInputStream(selectedImage);
			   	    	bis = new BufferedInputStream(is);
			   	    	bitmap = BitmapFactory.decodeStream(bis);           
			   	    	imgZapato.setImageBitmap(bitmap);
						break;
					case 4:
						is = getContentResolver().openInputStream(selectedImage);
			   	    	bis = new BufferedInputStream(is);
			   	    	bitmap = BitmapFactory.decodeStream(bis);           
			   	    	imgAccesorio.setImageBitmap(bitmap);
						break;
				}					
	   		} 
	   		catch (FileNotFoundException e) 
	   		{
	   			e.getStackTrace();
	   		}
	   	}
    }
	private void insertarFila(String nombreCat, int nomImg) 
	{   
		abrirBasedatos();
		ContentValues values = new ContentValues();  
		values.put("nom_cat", nombreCat );   
		values.put("nomCategoria", nomImg);
	  
		if(baseDatos.insert("conjunto", null, values) > 0)
		{
		
		}
		else
		{
			Toast.makeText(this, "hubo un problema al insertar los datos", Toast.LENGTH_SHORT).show();
		}
	}
    
}

