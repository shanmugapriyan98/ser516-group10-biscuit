package com.biscuit.models.services;

public class CommandService {
    public static String convertStrArrayToStr(String []words){
        StringBuilder wordBuffer = new StringBuilder();
        for (int i=0;i<words.length-1;i++){
            wordBuffer.append(words[i]);
            wordBuffer.append(" ");
        }
        wordBuffer.append(words[words.length-1]);
        return wordBuffer.toString();
    }

    public static boolean checkCommand(String[] words, String[] commandsArray){
        String wordString = convertStrArrayToStr(words);
        for(String str : commandsArray){
            if(wordString.equals(str)) return true;
        }
        System.out.println("Invalid syntax for the command. Check commands after typing 'help'");
        return false;
    }

}
