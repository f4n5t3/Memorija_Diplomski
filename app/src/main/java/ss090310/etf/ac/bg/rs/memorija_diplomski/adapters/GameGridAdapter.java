package ss090310.etf.ac.bg.rs.memorija_diplomski.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ss090310.etf.ac.bg.rs.memorija_diplomski.R;
import ss090310.etf.ac.bg.rs.memorija_diplomski.activities.EndGameActivity;
import ss090310.etf.ac.bg.rs.memorija_diplomski.activities.MultiplayerResultsActivity;

/**
 * Created by Stefan on 23/04/2017.
 */

public class GameGridAdapter extends BaseAdapter {

    private static final int CARD_FLIP_TIME = 500;

    private Context mContext;
    private int cardNum;
    private List<Integer> cardFronts;
    private int backImage;
    private boolean[] flipped, matched;
    private int flippedCard;
    private boolean busy = false;
    private int p1MatchedNum, p2MatchedNum;
    private int p1AttemptsNum, p2AttemptsNum;
    private boolean localMultiplayer;
    private boolean lanMultiplayer;
    private int seed;
    private int turn;
    private String player1, player2;

    public GameGridAdapter(Context context, int cardNum, String difficulty, boolean isMultiplayer) {
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
        p1MatchedNum = 0;
        p1AttemptsNum = 0;
        localMultiplayer = isMultiplayer;
        if (localMultiplayer) {
            p2MatchedNum = 0;
            p2AttemptsNum = 0;
        }
        lanMultiplayer = false;
        turn = 1;
        loadCards(difficulty, cardNum);
    }

    public GameGridAdapter(Context context, int cardNum, String difficulty, int seed) {
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
        p1MatchedNum = 0;
        p1AttemptsNum = 0;
        p2MatchedNum = 0;
        p2AttemptsNum = 0;
        lanMultiplayer = true;
        localMultiplayer = false;
        this.seed = seed;
        turn = 1;
        loadCards(difficulty, cardNum);
    }

    private void loadCards(String difficulty, int cardNum) {
        cardFronts = new ArrayList<>();
        for (int i = 0; i < cardNum/2; i++) {
            cardFronts.add(mContext.getResources().getIdentifier(difficulty.toLowerCase() + "_" + i, "drawable", mContext.getPackageName()));
            cardFronts.add(mContext.getResources().getIdentifier(difficulty.toLowerCase() + "_" + i, "drawable", mContext.getPackageName()));
        }
        if (lanMultiplayer) {
            Random r = new Random(seed);
            Collections.shuffle(cardFronts, r);
        } else {
            Collections.shuffle(cardFronts);
        }
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
            imageView.setLayoutParams(getCardDimensions());
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

    private GridView.LayoutParams getCardDimensions() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

        int cardWidth = Math.round(displayMetrics.widthPixels / 4);
        int cardHeight = Math.round(displayMetrics.heightPixels / (cardNum / 3));

        return new GridView.LayoutParams(cardWidth, cardHeight);
    }

    public void flip(final int i) {
        if (!flipped[i] && !matched[i] && !busy) {
            if (flippedCard == -1) {
                // No cards are flipped, so flip the clicked one
                flippedCard = i;
                flipped[i] = true;
                notifyDataSetChanged();
            } else {
                // One card is flipped, flip the second one
                flipped[i] = true;
                if (localMultiplayer && turn == 2) {
                    p2AttemptsNum++;
                }
                else {
                    p1AttemptsNum++;
                }
                notifyDataSetChanged();

                busy = true;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cardFronts.get(flippedCard).equals(cardFronts.get(i))) {
                            matched[flippedCard] = true;
                            matched[i] = true;
                            if (localMultiplayer && turn == 2) p2MatchedNum++;
                            else p1MatchedNum++;
                            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(250);
                        }
                        else {
                            flipped[flippedCard] = false;
                            flipped[i] = false;
                            if (turn == 1) turn = 2;
                            else turn = 1;
                        }
                        flippedCard = -1;
                        busy = false;
                        notifyDataSetChanged();
                        if ((p1MatchedNum + p2MatchedNum) == cardNum / 2) {
                            if (!localMultiplayer) {
                                Intent endGameIntent = new Intent(mContext, EndGameActivity.class);
                                endGameIntent.putExtra("score", calculateScore());
                                mContext.startActivity(endGameIntent);
                            } else {
                                Intent endGameIntent = new Intent(mContext, MultiplayerResultsActivity.class);
                                endGameIntent.putExtra("player1", player1)
                                        .putExtra("player2", player2)
                                        .putExtra("score1", getP1MatchedNum())
                                        .putExtra("score2", getP2MatchedNum());
                                mContext.startActivity(endGameIntent);
                            }
                        }
                    }
                }, CARD_FLIP_TIME);
            }
        }
    }

    private int calculateScore() {
        // TODO: Change scoring system
        return cardNum * 10 - p1AttemptsNum;
    }

    int getP1AttemptsNum() {
        return p1AttemptsNum;
    }

    int getP2AttemptsNum() { return p2AttemptsNum; }

    public int getP1MatchedNum() { return p1MatchedNum; }

    public int getP2MatchedNum() { return p2MatchedNum; }

    public int getTurn() { return turn; }

    public void setPlayer1(String p1) { player1 = p1; }

    public void setPlayer2(String p2) { player2 = p2; }
}
