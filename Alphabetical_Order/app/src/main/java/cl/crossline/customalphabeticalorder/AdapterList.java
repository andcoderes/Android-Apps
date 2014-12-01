package cl.crossline.customalphabeticalorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Jaime on 16/09/2014.
 */
public class AdapterList extends BaseAdapter implements View.OnClickListener {
    private ArrayList<String> listData;
    private Context context;
    private CustomAlphabetical alphabetical;

    public AdapterList(ArrayList<String> listData, Context context, CustomAlphabetical alphabetical)
    {
        this.listData = listData;
        this.context = context;
        this.alphabetical = alphabetical;
        orderList();
    }

    private void orderList()
    {
        Collator esCollator = Collator.getInstance(new Locale("es"));
        Collections.sort(this.listData, esCollator);
    }

    @Override
    public int getCount()
    {
        return listData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater mInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(R.layout.item_list, null);

        ((TextView)v.findViewById(R.id.txtExample)).setText(listData.get(position));
        if(position==0)
        {
            ((Button)v.findViewById(R.id.btnLetter)).setText(listData.get(position).charAt(0) + "");
            ((Button)v.findViewById(R.id.btnLetter)).setTag(listData.get(position).charAt(0) + "");
            v.findViewById(R.id.btnLetter).setOnClickListener(this);
        }
        else
        {
            if(listData.get(position).charAt(0) == listData.get(position-1).charAt(0))
            {
                v.findViewById(R.id.btnLetter).setVisibility(View.GONE);
            }
            else
            {
                ((Button)v.findViewById(R.id.btnLetter)).setText(listData.get(position).charAt(0) + "");
                ((Button)v.findViewById(R.id.btnLetter)).setTag(listData.get(position).charAt(0) + "");
                v.findViewById(R.id.btnLetter).setOnClickListener(this);
            }
        }
        return v;
    }

    @Override
    public void onClick(View v)
    {
        alphabetical.setShowView(true);
    }
}
