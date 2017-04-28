package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stefan on 23/04/2017.
 */

class GameGridAdapter extends BaseAdapter {
    private Context mContext;
    private int cardNum;
    private List<Integer> cardFronts;
    private String difficulty;

    GameGridAdapter(Context context, int cardNum, String difficulty) {
        this.mContext = context;
        this.cardNum = cardNum;
        this.difficulty = difficulty;

        loadCards(difficulty, cardNum);

    }

    private void loadCards(String difficulty, int cardNum) {
        cardFronts = new ArrayList<>();
        for (int i = 0; i < cardNum/2; i++) {
            cardFronts.add(mContext.getResources().getIdentifier(difficulty + "_" + i, "drawable", mContext.getPackageName()));
            cardFronts.add(mContext.getResources().getIdentifier(difficulty + "_" + i, "drawable", mContext.getPackageName()));
        }
        Collections.shuffle(cardFronts);
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
        ImageView imageView;
        if (convertView != null) {
            imageView = (ImageView) convertView;
        } else {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        imageView.setImageResource(cardFronts.get(position));

        return imageView;
    }
}
