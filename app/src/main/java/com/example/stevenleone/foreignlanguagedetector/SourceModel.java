package com.example.stevenleone.foreignlanguagedetector;

import android.app.Activity;
import android.widget.Toast;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.File;

/**
 *Predicts language of input with a markov chain
 *@author Steven Leone
 *@version 4
 **/
public class SourceModel {

    private double[][] charCount;
    private String mName;
    /**
     *SourceModel Constructor, creates matrix
     *@param modelName is used to print language training
     *@param corpusFile is the file read to make a matrix
     */
    public SourceModel(Activity page, String modelName, String corpusFile) throws Exception {

        mName = modelName;
        int[][] chrCount = new int[26][26];
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'};
        char previous = 0;
        boolean alphabetic = false;
        InputStream fis;
        AssetManager assetManager = page.getAssets();
        fis = assetManager.open(corpusFile);
        int row = 0;
        int column = 0;
        while (previous == 0 && fis.available() > 0) {
            char character = (char) fis.read();
            character = Character.toLowerCase(character);
            if (Character.isAlphabetic(character)) {
                previous = character;
            }
        }
        while (fis.available() > 0) {
            char character = (char) fis.read();
            character = Character.toLowerCase(character);
            for (int i = 0; i < 26; i++) {
                if (character == alphabet[i]) {
                    column = i;
                    alphabetic = true;
                }
            }
            for (int i = 0; i < 26; i++) {
                if (previous == alphabet[i]) {
                    row = i;
                }
            }
            if (alphabetic) {
                chrCount[row][column] += 1;
                previous = character;
                alphabetic = false;
            }
        }
        double[][] probs = new double[26][26];
        for (int i = 0; i < 26; i++) {
            int total = 0;
            for (int j = 0; j < 26; j++) {
                total += chrCount[i][j];
            }
            for (int j = 0; j < 26; j++) {
                if (total > 0) {
                    probs[i][j] =  (1.0 * chrCount[i][j] / total);
                }
                if (chrCount[i][j] == 0) {
                    probs[i][j] = 0.01;
                }
            }
        }
        charCount = probs;


    }
    /**
     *GetName method returns the name
     *@return returns the name of the model
     */

    public String getName() {
        return mName;
    }

    /**
     *Converts the matrix to a string to
     *@return returns the matrix in a String
     */

    public String toString() {
        String sCharCount = "    ";
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'};
        for (int i = 0; i < 26; i++) {
            sCharCount += alphabet[i] + "    ";
        }
        sCharCount += "\n";
        for (int i = 0; i < 26; i++) {
            sCharCount += alphabet[i] + " ";
            for (int j = 0; j < 26; j++) {
                double np = ((double) Math.round(charCount[i][j] * 100));
                np = np / 100.00;
                String nextProb = String.format("%1$,.2f", np);
                sCharCount += String.format("%5s" , nextProb);
            }
            sCharCount += "\n";
        }
        return sCharCount;
    }
    /**
     *Probability method determines probability a string is a language
     *@param test is the input that the probability is determined for
     *@return returns the exact probability
     */

    public double probability(String test) {
        double probability = 1.00;
        int row = 0;
        int column = 0;
        int counter = 0;
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'};
        String[] testArray = new String[test.length()];
        for (int i = 0; i < test.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char letter = test.charAt(i);
                letter = Character.toLowerCase(letter);
                if (letter == alphabet[j]) {
                    testArray[counter] = Character.toString(letter);
                    counter++;
                }
            }
        }
        for (int i = 0; i < (counter - 1); i++) {
            String first = testArray[i];
            String second = testArray[i + 1];
            for (int j = 0; j < 26; j++) {
                if (first.equals(Character.toString(alphabet[j]))) {
                    row = j;
                }
            }
            for (int j = 0; j < 26; j++) {
                if (second.equals(Character.toString(alphabet[j]))) {
                    column = j;
                }
            }
            probability = probability * charCount[row][column];
        }
        return probability;
    }

    public double[][] getCharCount() {
        return charCount;
    }
}
