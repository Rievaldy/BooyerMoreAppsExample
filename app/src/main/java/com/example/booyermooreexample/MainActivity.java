package com.example.booyermooreexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText mInputText;
    EditText mInputPattern;
    Button mButtonProcess;
    TextView mTextViewOutput;

    private static final String TAG = "MainActivity";
    private ArrayList<IndexLocationtoHighlight> listIndexLocationtoHighlight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputText = findViewById(R.id.input_text);
        mInputPattern = findViewById(R.id.input_pattern);
        mButtonProcess = findViewById(R.id.process);
        mTextViewOutput = findViewById(R.id.output_text);
        listIndexLocationtoHighlight = new ArrayList<>();

        mButtonProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + mButtonProcess.getText());
                if(mButtonProcess.getText().equals("start")){
                    BoyerMooreBadCharacter(mInputText.getText().toString(), mInputPattern.getText().toString());
                    HighlightText(mInputText.getText().toString(), listIndexLocationtoHighlight);
                    mButtonProcess.setText("try again");
                }else{
                    listIndexLocationtoHighlight = new ArrayList<>();
                    mTextViewOutput.setText("");
                    mButtonProcess.setText("start");
                }
            }
        });

    }

    public void BoyerMooreBadCharacter(String text, String pattern){
        int sizeOfTextArray = text.length();
        int sizeOfPatternArray = pattern.length();
        String[] textOnArray = text.split("(?!^)");
        String[] patternOnArray = pattern.split("(?!^)");
        int startTextIndex = 0;
        int farSkipedIndex = 0;
        String missMatchCharacter;


        BoyerMoreBadCharacterInterface someFunctionNeeded = new BoyerMoreBadCharacterInterface() {
            @Override
            public int FindAMatchIndexCharacterOnPattern(String missMatchCharacter, String[] pattern, int lastIndexMatched) {
                for(int i = lastIndexMatched; i >=  0; i--){
                    if(pattern[i].equals(missMatchCharacter)){
                        return i;

                    }
                }
                return -1;
            }
        };
        while(startTextIndex < sizeOfTextArray){
            for(int j = sizeOfPatternArray-1; j >= 0  ; j-- ){
                try{
                    if(!patternOnArray[j].equals(textOnArray[startTextIndex + j])){
                        missMatchCharacter = textOnArray[startTextIndex + j];
                        if(someFunctionNeeded.FindAMatchIndexCharacterOnPattern(missMatchCharacter, patternOnArray, j) == -1){
                            startTextIndex += (j+1);
                        }else {
                            farSkipedIndex = j - someFunctionNeeded.FindAMatchIndexCharacterOnPattern(missMatchCharacter, patternOnArray, j);
                            startTextIndex += farSkipedIndex;
                        }

                        break;
                    }else if(j == 0 && patternOnArray[j].equals(textOnArray[startTextIndex + j])){
                        Log.d(TAG, "BoyerMooreBadCharacter: matched at " + startTextIndex + " until " + (startTextIndex+(sizeOfPatternArray-1)) );
                        IndexLocationtoHighlight mIndexLocationtoHighlight = new IndexLocationtoHighlight();
                        mIndexLocationtoHighlight.setStartIndex(startTextIndex);
                        mIndexLocationtoHighlight.setEndIndex((startTextIndex+(sizeOfPatternArray-1)));
                        listIndexLocationtoHighlight.add(mIndexLocationtoHighlight);
                        startTextIndex += sizeOfPatternArray;
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    Log.d(TAG, "BoyerMooreBadCharacter: Job Is Done");
                    return;
                }
            }
        }
    }

    //TextInput = GCTTCTGCTACCTTTTGCGCGCGCGCGGAA
    //Pattern = CCTTTTGC

    public void HighlightText(String text, ArrayList<IndexLocationtoHighlight> listIndedxLocationtoHighlight){
        Spannable wordToHighlight = new SpannableString(text);
        for(int i = 0 ; i < listIndedxLocationtoHighlight.size();i++){
            wordToHighlight.setSpan(new BackgroundColorSpan(0xFFFFFF00),listIndedxLocationtoHighlight.get(i).startIndex,listIndedxLocationtoHighlight.get(i).endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTextViewOutput.setText(wordToHighlight);
    }

    interface BoyerMoreBadCharacterInterface{
        int FindAMatchIndexCharacterOnPattern(String missMatchCharacter, String[] pattern, int lastIndexMatched);
    }

    class IndexLocationtoHighlight{
        int startIndex;
        int endIndex;

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }
    }
}