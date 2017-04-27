package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
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
    private List<Integer> cardFronts;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public List<Integer> getCardFronts() {
        return cardFronts;
    }

    public void setCardFronts(List<Integer> cardFronts) {
        this.cardFronts = cardFronts;
    }

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
