package cl.crossline.customalphabeticalorder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class MyActivity extends Activity implements View.OnClickListener, OnLetterClick {
    private Button btnShow;
    private CustomAlphabetical alphabetical;
    private ListView lstExample;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        init();
    }

    private void init()
    {


        btnShow = (Button)findViewById(R.id.btnShowView);
        alphabetical = (CustomAlphabetical)findViewById(R.id.customAlphabetical);
        lstExample = (ListView)findViewById(R.id.lstExample);

        lstExample.setAdapter(new AdapterList(getList(),this,alphabetical));


        alphabetical.setLetterColor(Color.GREEN);
        alphabetical.setSquareColor(Color.BLUE);
        alphabetical.setOnLetterClickListener(this);
        alphabetical.setEnableList(getList());
        btnShow.setOnClickListener(this);
    }

    private ArrayList<String> getList()
    {
        ArrayList<String>listAdapter = new ArrayList<String>();
        listAdapter.add("a");
        listAdapter.add("aa");
        listAdapter.add("aa");
        listAdapter.add("b");
        listAdapter.add("bb");
        listAdapter.add("bb");
        listAdapter.add("e");
        listAdapter.add("ee");
        listAdapter.add("eee");
        listAdapter.add("q");
        listAdapter.add("qq");
        listAdapter.add("qqq");
        listAdapter.add("t");
        listAdapter.add("tt");
        listAdapter.add("ttt");
        listAdapter.add("y");
        listAdapter.add("yy");
        listAdapter.add("yyy");
        listAdapter.add("o");
        listAdapter.add("o");
        listAdapter.add("ooo");
        listAdapter.add("w");
        listAdapter.add("ww");
        listAdapter.add("www");
        listAdapter.add("r");
        listAdapter.add("rr");
        listAdapter.add("rrr");
        listAdapter.add("m");
        listAdapter.add("mm");
        listAdapter.add("mmm");
        listAdapter.add("v");
        listAdapter.add("vv");
        listAdapter.add("vvv");
        listAdapter.add("z");
        listAdapter.add("zz");
        listAdapter.add("zzz");
        listAdapter.add("ñ");
        listAdapter.add("ññ");

        return listAdapter;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnShowView:
                alphabetical.setShowView(true);
                break;
        }
    }


    @Override
    public void onLetterClickListener(CustomAlphabetical view, char letterClick, boolean isEnabled)
    {
        if(isEnabled)
        {
            int position =  getPosition(letterClick);
            lstExample.setSelection(position);
        }
            
    }

    private int getPosition(char letterClick)
    {
        ArrayList<String> listString = getList();
        Collator esCollator = Collator.getInstance(new Locale("es"));
        Collections.sort(listString, esCollator);
        for (int i=0;i<listString.size();i++)
        {
            if(listString.get(i).charAt(0)==letterClick)
            {
                return i;
            }
        }
        return 0;
    }
}
