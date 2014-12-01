package com.cl.vestimenta;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;






import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarView extends Activity implements OnClickListener{

	public Calendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	private String currentMonth;
	private String prevMonth;
	SQLiteDatabase baseDatos;
	ImageView imgAnimo;
	ImageView imgConjunto;
	Bitmap bpm = null;
	Bitmap image=null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		// Boton Agregar Conjunto
		Button btnAgregarConjunto;
		btnAgregarConjunto = (Button)findViewById(R.id.btnAgregarCon);
		btnAgregarConjunto.setOnClickListener(this);
		//
		month = Calendar.getInstance();
		itemmonth = (Calendar) month.clone();
		currentMonth = String.valueOf((month.get(Calendar.MONTH) + 1));
		prevMonth = String.valueOf((month.get(Calendar.MONTH)));
		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		
		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				String monthString = separatedTime[1];
				if (monthString.contains("0")) {
					monthString = monthString.replaceFirst("^0*", "");
				}
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				if (currentMonth.toString().trim()
						.equalsIgnoreCase(monthString)
						|| prevMonth.toString().trim()
								.equalsIgnoreCase(monthString)) {
					adapter.currentView
							.setBackgroundResource(R.drawable.calendar_cel_current);
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				llamarObjeto(selectedGridDate);

			}

			
		});
	}
	private void llamarObjeto(String selectedGridDate) 
	{
    	Dialog dia = new Dialog(this);
    	dia.setContentView(R.layout.tenida_animo);
    	dia.setCancelable(true);
    	dia.show();
    	rescatarDatos(selectedGridDate);
    	imgAnimo = (ImageView)dia.findViewById(R.id.imgEstado);
    	imgConjunto = (ImageView)dia.findViewById(R.id.imgConjunto);
	    imgAnimo.setImageBitmap(bpm);
	    imgConjunto.setImageBitmap(image);
    	
    	
		
	}
	protected void setNextMonth() {
		if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
			month.set((month.get(Calendar.YEAR) + 1),
					month.getActualMinimum(Calendar.MONTH), 1);
		} else {
			month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
			month.set((month.get(Calendar.YEAR) - 1),
					month.getActualMaximum(Calendar.MONTH), 1);
		} else {
			month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		public void run() {
			items.clear();
			rescatarFecha();
			baseDatos.close();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String itemvalue;
			
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(Calendar.DATE, 1);
				
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};


	public void onClick(View v) 
	{
		Intent abrirInicio;
		switch (v.getId())
		{
			case R.id.btnAgregarCon:
				abrirInicio = new Intent("com.cl.vestimenta.AgregarConjunto");
        		startActivity(abrirInicio);
				break;
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
	 private boolean rescatarFecha()
	{
		
    	abrirBasedatos();
		String[] campos = new String[] {"fecha"};
		Cursor c = baseDatos.query("calendario", campos, null, null, null, null, null);
		if(c.moveToFirst())
		{
			do
			{
				items.add(c.getString(0));
			}while(c.moveToNext());
		       
		}
		else {
			Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show();
		}
		return true;
		
		
		
	}
	 private boolean rescatarDatos(String fecha2)
		{
			
	    	abrirBasedatos();
	    	String[] variables = new String []{fecha2};
			String[] campos = new String[] {"animo","nom_foto"};
			Cursor c = baseDatos.query("calendario", campos, "fecha=?", variables, null, null, null);
			if(c.moveToFirst())
			{
				marcarImagen(c.getString(1));
				marcarEmocion(c.getString(0));
			}
			else {
				bpm = null;
			}
			return true;
			
			
			
		}
	 private void marcarEmocion(String emocion)
	 {
		 
		 if(emocion.compareToIgnoreCase("Feliz")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.feliz);
		 }
		 if(emocion.compareToIgnoreCase("Enojado")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.enojada); 
		 }
		 if(emocion.compareToIgnoreCase("Confundido")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.confundida);
		 }
		 if(emocion.compareToIgnoreCase("Enamorado")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.enamorada);
		 }
		 if(emocion.compareToIgnoreCase("Furia")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.furiosa); 
		 }
		 if(emocion.compareToIgnoreCase("Genial")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.genial);
		 }
		 if(emocion.compareToIgnoreCase("Shokeado")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.shockeada);
		 }
		 if(emocion.compareToIgnoreCase("Sorprendido")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.sorprendida);
		 }
		 if(emocion.compareToIgnoreCase("Triste")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.triste);
		 }	
		 if(emocion.compareToIgnoreCase("lindo")==0)
		 {
			 bpm = BitmapFactory.decodeResource(getResources(), R.drawable.linda);
		 }	
	 }
	 private void marcarImagen(String imagen)
	 {
		File file = new File(Environment.getExternalStorageDirectory(),imagen+"Calendario.jpg");
		if (file.exists())
		{
	        //Tenemos la foto guardada en la SD, asi que la cargamos
	        image = BitmapFactory.decodeFile("/sdcard/"+imagen+"Calendario.jpg");
	        
        
    	}
	 }
	
}
