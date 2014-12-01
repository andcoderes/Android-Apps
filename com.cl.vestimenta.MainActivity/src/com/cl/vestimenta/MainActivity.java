package com.cl.vestimenta;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener
{
	ImageButton btnRopa;
	ImageButton btnConjunto;
	ImageButton btnShop;
	ImageButton btnCalendario;
	private static final String crearTablaCategoria = "create table if not exists "  
			  + " categoria ( "  
			  + " id INTEGER PRIMARY KEY AUTOINCREMENT,nombre text);";
	private static final String crearTablaRopa = "create table if not exists "  
			  + " ropa ( "  
			  + " id INTEGER PRIMARY KEY AUTOINCREMENT,tipoRopa text,nomRopa text);";
	private static final String crearTablaCategoriaRopa = "create table if not exists "  
			  + " categoria_ropa ( "  
			  + " id INTEGER PRIMARY KEY AUTOINCREMENT,tipoRopa text);";
	private static final String crearTablaConjunto = "create table if not exists "  
			  + " conjunto ( "  
			  + " id INTEGER PRIMARY KEY AUTOINCREMENT, nom_cat text, nomCategoria text);";
	private static final String crearTablaCalendario = "create table if not exists "  
			  + " calendario ( "  
			  + " id INTEGER PRIMARY KEY AUTOINCREMENT, fecha text, nom_foto text,animo text);";
	 private SQLiteDatabase baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarElementos();
        abrirBasedatos();
        baseDatos.close();
    }
    private void abrirBasedatos()
	{ 
	    try 
	    {   
	      baseDatos = openOrCreateDatabase("Test", MODE_WORLD_WRITEABLE, null);   
	      baseDatos.execSQL(crearTablaCategoria);
	      baseDatos.execSQL(crearTablaRopa);
	      baseDatos.execSQL(crearTablaConjunto);
	      baseDatos.execSQL(crearTablaCategoriaRopa);
	      baseDatos.execSQL(crearTablaCalendario);
	      baseDatos.execSQL("insert into categoria_ropa values(1,'polera');");
	      baseDatos.execSQL("insert into categoria_ropa values(2,'pantalon');");
	      baseDatos.execSQL("insert into categoria_ropa values(3,'zapato');");
	      baseDatos.execSQL("insert into categoria_ropa values(4,'accesorio');");
	      baseDatos.execSQL("insert into categoria values(1,'Casual');");
	      baseDatos.execSQL("insert into categoria values(2,'Formal');");
	      baseDatos.execSQL("insert into categoria values(3,'Deportivo');");
	      baseDatos.execSQL("insert into categoria values(4,'Trabajo');");
	      baseDatos.execSQL("insert into calendario values(1,'2012-12-20','foto1','Shokeado');");
	      baseDatos.execSQL("insert into calendario values(2,'2012-12-21','foto2','Genial');");
	      baseDatos.execSQL("insert into calendario values(3,'2012-12-22','foto3','Lindo');");   
	    }    
	    catch (Exception e)
	    {   
	      e.printStackTrace();   
	    }     
	    
	}

    private void inicializarElementos()
    {
		btnRopa = (ImageButton)findViewById(R.id.btnRopa);
		btnConjunto = (ImageButton)findViewById(R.id.btnConjunto);
		btnShop = (ImageButton)findViewById(R.id.btnShop);
		btnCalendario = (ImageButton)findViewById(R.id.btnCalendario);
		btnRopa.setOnClickListener(this);
		btnConjunto.setOnClickListener(this);
		btnCalendario.setOnClickListener(this);
		btnShop.setOnClickListener(this);	
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
		Intent abrirInicio;
		ImageView imgPronto;
		Bitmap bpm = null;
		switch (v.getId())
		{
			case R.id.btnCalendario:
				abrirInicio = new Intent("com.cl.vestimenta.CalendarView");
        		startActivity(abrirInicio);
				break;
			case R.id.btnConjunto:
					abrirInicio = new Intent("com.cl.vestimenta.Conjuntos");
	        		startActivity(abrirInicio);					
				break;
			case R.id.btnRopa:
				abrirInicio = new Intent("com.cl.vestimenta.Ropa");
        		startActivity(abrirInicio);
				break;
			case R.id.btnShop:
		    	Dialog dia = new Dialog(this);
		    	dia.setContentView(R.layout.pronto);
		    	dia.setCancelable(true);
		    	dia.show();
				break;
		}
		
	}
    
}


