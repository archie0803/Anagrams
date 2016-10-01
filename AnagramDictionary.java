package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    ArrayList<String> wordList;
    HashSet<String> wordSet= new HashSet<>();
    HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String,ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<Integer,ArrayList<String>>();
    int currentWordSize = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        wordList = new ArrayList<String>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String key = sortLetter(word);
            if (lettersToWord.containsKey(key)) {
                lettersToWord.get(key).add(word);
            } else {
                ArrayList<String> mList = new ArrayList<>();
                mList.add(word);
                lettersToWord.put(key, mList);
            }
            Integer sizeKey = word.length();
            if (sizeToWords.containsKey(sizeKey)) {
                sizeToWords.get(sizeKey).add(word);
            }
            else {
                ArrayList<String> tList = new ArrayList<>();
                tList.add(word);
                sizeToWords.put(sizeKey, tList);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {

         return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord = sortLetter(targetWord);
        if (lettersToWord.containsKey(sortedWord)) {
            result.addAll(lettersToWord.get(sortedWord));
        }
//        int targetLength = targetWord.length();
//        for(String i:wordList)
//        {
//            if(i.length()==targetLength)
//            {
//                if(sortLetter(targetWord).equals(sortLetter(i)))
//                {
//                    result.add(i);
//                }
//            }
//    }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=97;i<123;i++){
            String key=word+(char)i;
            String keyActivate=sortLetter(key);
            if(lettersToWord.containsKey(keyActivate)){
            ArrayList<String> Anagrams= lettersToWord.get(keyActivate);
              for(String anagram:Anagrams)
              {
              if(isGoodWord(anagram,word))
              {
                  result.add(anagram);

              }
              }
            }
        }
        return result;
    }
    public String sortLetter(String word)
    {
        char[] letters=word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public String pickGoodStarterWord() {
        String wordToDo;
        ArrayList<String> check ;
        ArrayList<String> myList;

        myList = sizeToWords.get(currentWordSize);
        wordToDo = myList.get(random.nextInt(myList.size()-1));
        check = getAnagramsWithOneMoreLetter(wordToDo);
        if(check.size()< MIN_NUM_ANAGRAMS)
        {
            return pickGoodStarterWord();
        }
        else{
            if (currentWordSize < MAX_WORD_LENGTH){
                currentWordSize++;
            }
            return wordToDo;
        }
    }
}
