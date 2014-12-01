package com.cl.vestimenta;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class AgregarCategoria extends Activity implements OnClickListener
{
	Button btnAgregarCategoria;
	EditText categoria;
	private SQLiteDatabase baseDatos;
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_categoria);
        inicializarElementos();
       
    }

    private void inicializarElementos()
    {
		btnAgregarCategoria= (Button)findViewById(R.id.btnAgregarCategoria);
		categoria = (EditText)findViewById(R.id.edtCategoria);
		btnAgregarCategoria.setOnClickListener(this);
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
			case R.id.btnAgregarCategoria:
				guardarCategoria();
			break;
		}
		
	}
   private void guardarCategoria()
   {
	   String nomCategoria = categoria.getText().toString();
	   abrirBasedatos();
	   ContentValues values = new ContentValues();  
		  values.put("nombre", nomCategoria );   
		 		  
		  if(baseDatos.insert("categoria", null, values) > 0)
		  {
			  Intent abrirInicio = new Intent("com.cl.vestimenta.Conjuntos");
      			startActivity(abrirInicio);
		  }
		  else
		  {
			  Toast.makeText(this, "no se pudo insertar", Toast.LENGTH_SHORT).show();
		  }
		
   }
   
}

