package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Stefan on 23/04/2017.
 */

public class GameGridAdapter extends BaseAdapter {
    private Context mContext;
    private int cardNum;
    private List<Drawable> cardFronts;


    public GameGridAdapter(Context context, int cardNum) {
        this.mContext = context;
        this.cardNum = cardNum;
    }

    @Override
    public int getCount() {
        return cardNum;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
