package com.cl.vestimenta;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class AgregarConjunto extends Activity implements OnClickListener
{
	String [] animo = new String []{"Feliz","Enojado","Confundido","Enamorado",
			"Furia","Genial","lindo","Shokeado","Sorprendido","Triste"};
	
	Spinner spnAnimos;
	Button btnBuscar;
	Button btnGuardar;
	EditText txtDia;
	EditText txtMes;
	EditText txtAno;
	ImageView imagenNueva;
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	SQLiteDatabase baseDatos;
	int code;
	LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_conjunto_calendario);
        spnAnimos = (Spinner)findViewById(R.id.spAnimo);
        imagenNueva = (ImageView)findViewById(R.id.imagenNueva);
        btnBuscar = (Button)findViewById(R.id.btnBuscarConjunto);
        btnGuardar = (Button)findViewById(R.id.btnGuardarConjunto);
        txtDia = (EditText)findViewById(R.id.txtDia);
        txtMes = (EditText)findViewById(R.id.txtMes);
        txtAno = (EditText)findViewById(R.id.txtAno);
        btnBuscar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        layout = (LinearLayout)findViewById(R.id.layoutRopaConjunto);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                    animo);
        spnAnimos.setAdapter(spinnerArrayAdapter);

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
		switch (v.getId())
		{
			case R.id.btnBuscarConjunto:
				Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				code = SELECT_PICTURE;
   				startActivityForResult(intent, code);
				break;
			case R.id.btnGuardarConjunto:
				
				guardarLayout();
				break;

		}
	
	}
	 	private void guardarLayout() 
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
	 		   String dia = txtDia.getText().toString();
	 		   String mes = txtMes.getText().toString();
	 		   String ano = txtAno.getText().toString();
	 		   String fecha = ano +"-"+mes+"-"+dia;
	 		   
	 		   //Se obtiene el bitmap con la imagen
	 		   layout.setDrawingCacheEnabled(true);
	 		   layout.buildDrawingCache();
	 		   Bitmap bm = layout.getDrawingCache();
	 		   insertarFila(spnAnimos.getSelectedItem().toString(),conjunto+"",fecha);
	 		   baseDatos.close();
	 		   escribirConjunto(bm, conjunto);
	 		    		   
	 		   conjunto ++;
	 		   
	 		 escribirFicheroMemoriaInterna(conjunto);
	 	   
	 	   }
		
	 	}
	 	private int leerFicheroMemoriaInterna()
	    {
	    	InputStreamReader flujo=null;
	    	BufferedReader lector=null;
	    	try
	    	{
	    		flujo= new InputStreamReader(openFileInput("numeroConjuntoCalendario.txt"));
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
	    	    escritor=new OutputStreamWriter(openFileOutput("numeroConjuntoCalendario.txt", Context.MODE_PRIVATE));
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

		@Override 
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	    {
		   Bitmap bitmap;
		   BufferedInputStream bis;
		   		Uri selectedImage = data.getData();
		   		InputStream is;
				try 
				{
					is = getContentResolver().openInputStream(selectedImage);
					bis = new BufferedInputStream(is);
		   	    	bitmap = BitmapFactory.decodeStream(bis);           
		   	    	imagenNueva.setImageBitmap(bitmap);
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	   	
	    }
		private void insertarFila(String animo, String nomImg,String fecha) 
		{   
			
			abrirBasedatos();
			ContentValues values = new ContentValues();  
			values.put("fecha", fecha );   
			values.put("nom_foto", nomImg);
			values.put("animo", animo);
		  
			if(baseDatos.insert("calendario", null, values) > 0)
			{
			
			}
			else
			{
				Toast.makeText(this, "hubo un problema al insertar los datos", Toast.LENGTH_SHORT).show();
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
		   private void escribirConjunto(Bitmap bm ,int conjunto) 
		   {
			   File sdCard = Environment.getExternalStorageDirectory();
		       File file = new File(sdCard, conjunto+"Calendario.jpg");
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
}


