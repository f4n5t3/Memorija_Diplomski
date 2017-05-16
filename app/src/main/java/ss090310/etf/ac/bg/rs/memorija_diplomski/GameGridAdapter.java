package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
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
    private int backImage;
    private boolean[] flipped, matched;
    private int flippedCard;
    private boolean busy = false;
    private int matchedNum;
    private int attemptsNum;

    GameGridAdapter(Context context, int cardNum, String difficulty) {
        this.mContext = context;
        this.cardNum = cardNum;
        this.backImage = R.drawable.back_image;
        flipped = new boolean[cardNum];
        matched = new boolean[cardNum];
        for (int i = 0; i < cardNum; i++) {
            flipped[i] = false;
            matched[i] = false;
        }
        flippedCard = -1;
        matchedNum = 0;
        attemptsNum = 0;
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
        if (matched[position]) {
            imageView.setImageDrawable(null);
        }
        else if (flipped[position]) {
                imageView.setImageResource(cardFronts.get(position));
        } else {
            imageView.setImageResource(backImage);
        }
        return imageView;
    }

    void flip(final int i) {
        if (!flipped[i] && !matched[i] && !busy) {
            if (flippedCard == -1) {
                // No cards are flipped, so flip the clicked one
                flippedCard = i;
                flipped[i] = true;
                notifyDataSetChanged();
            } else {
                // One card is flipped, flip the second one
                flipped[i] = true;
                attemptsNum++;
                notifyDataSetChanged();

                busy = true;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cardFronts.get(flippedCard).equals(cardFronts.get(i))) {
                            matched[flippedCard] = true;
                            matched[i] = true;
                            matchedNum++;
                            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(250);
                        }
                        else {
                            flipped[flippedCard] = false;
                            flipped[i] = false;
                        }
                        flippedCard = -1;
                        busy = false;
                        notifyDataSetChanged();
                        if (matchedNum == cardNum / 2) {
                            Intent endGameIntent = new Intent(mContext, EndGameActivity.class);
                            endGameIntent.putExtra("score", calculateScore());
                            mContext.startActivity(endGameIntent);
                        }
                    }
                }, 500);
            }
        }
    }

    private int calculateScore() {
        return cardNum * 10 - attemptsNum;
    }
}
