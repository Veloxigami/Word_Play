package com.example.veloxigami.wp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private TextView playerNameText, scoreText, mainWordText, timerText;
    private EditText inputField;
    private String mainword;
    private Timer timer;
    private Button restartBtn, exitBtn;

    private String[] list = {"This", "is", "a", "summary", "of", "what", "I", "think", "is", "the", "most", "important",
            "and", "insightful", "parts", "of", "the", "book", "I", "can’t", "speak", "for", "anyone", "else", "and",
            "I", "strongly", "recommend", "you", "to", "read", "the", "book", "in", "order", "to", "grasp", "the",
            "concepts", "written", "here", "My", "notes", "should", "only", "be", "seen", "as", "an", "addition",
            "that", "can", "be", "used", "to", "refresh", "your", "memory", "after", "you", "read", "the", "book",
            "Use", "my", "words", "as", "anchors", "to", "remember", "the", "vitals", "parts", "of", "this", "extraordinary",
            "book", "I", "know", "I", "will", "If", "you", "like", "this", "free", "summary", "you", "are", "more", "than",
            "welcome", "to", "send", "me", "an", "email", "just", "to", "say", "thanks", "That", "would", "make", "my", "day",
            "If", "you", "dig", "it", "I", "may", "put", "up", "summaries", "of", "other", "similar", "books", "Or", "if", "you",
            "like", "to", "have", "a", "chat", "about", "the", "content", "of", "the", "book", "or", "things", "within", "the",
            "same", "area", "I", "am", "up", "for", "that", "as", "well"};

    private Random rand;
    private int score;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1)
                update();
            Log.v("TAG","handler");
            super.handleMessage(msg);
        }
    };

    private boolean check = false, stop = false;
    long stime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerNameText = findViewById(R.id.player_name);
        scoreText = findViewById(R.id.score);
        mainWordText = findViewById(R.id.main_word);
        timerText = findViewById(R.id.timer);
        inputField = findViewById(R.id.answer);
        restartBtn = findViewById(R.id.restart);
        exitBtn = findViewById(R.id.exit);

        restartBtn.setEnabled(false);
        exitBtn.setEnabled(false);

        rand = new Random();
        score = 0;

        scoreText.setText(score+"");
        update();

        stime = 60;
        timerText.setText("60");

        Log.v("TAG", Integer.toString(list.length)); //146

        inputField.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(check == false){
                    new TimeKeeper().execute();
                    check = true;
                }
                mainword = mainWordText.getText().toString();
                if (s.length() <= mainword.length() && s.length()!=0) {
                    if (!(s.toString().equals(mainword.substring(0, s.length())))) {
                        String htmlText = "<font color=#ff1111>" + mainword.substring(0, s.length()) + "</font>" + "<font color=#111111>" + mainword.substring(s.length()) + "</font>";
                        mainWordText.setText(Html.fromHtml(htmlText));
                        //if space pressed then -ve points
                    } else if (s.toString().equals(mainword.substring(0, s.length()))) {
                        String htmlText = "<font color=#22ff22>" + mainword.substring(0, s.length()) + "</font>" + "<font color=#111111>" + mainword.substring(s.length()) + "</font>";
                        mainWordText.setText(Html.fromHtml(htmlText));
                    }
                }
                if(s.toString().equals(mainword)){
                    update();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void update(){
        mainWordText.setText(list[rand.nextInt(140)]);
        Log.v("TAG","changed");
        inputField.setText("");
        scoreText.setText(""+ ++score);
    }

    private class TimeKeeper extends AsyncTask<Void,Long,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            while(stime !=0){
                try {
                    publishProgress(stime);
                    Thread.sleep(1000);
                    Log.v("TAG",stime + " awesome");
                } catch (InterruptedException e) {
                    //Log.v("TAG",stime + "not awesome");
                    e.printStackTrace();
                }
                stime--;
                //Log.v("TAG",stime + "not awesome");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            timerText.setText(""+values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopGame();
        }
    }

    public void stopGame(){
        timerText.setText("0");
        inputField.setText("");
        inputField.setEnabled(false);
        restartBtn.setEnabled(true);
        exitBtn.setEnabled(true);
        restartBtn.setVisibility(View.VISIBLE);
        exitBtn.setVisibility(View.VISIBLE);
    }

    public void onRestartBtnClick(View v){
        check = false;
        stime = 60;
        inputField.setEnabled(true);
        restartBtn.setVisibility(View.INVISIBLE);
        exitBtn.setVisibility(View.INVISIBLE);
        restartBtn.setEnabled(false);
        exitBtn.setEnabled(false);
        scoreText.setText("0");
        score = 0;
        timerText.setText("60");
    }

    public void onExitBtnClick(View v){
        System.exit(0);
    }
}
