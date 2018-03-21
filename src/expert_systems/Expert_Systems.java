/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expert_systems;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Algreg M. Mata
 */
public class Expert_Systems {
    static Map<String, Integer> high = new HashMap<>();
    static Map<String, Integer>  low = new HashMap<>();
    static double probl,probh,lTotal=0,hTotal=0,total=0;
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException  {
        Map<String, FileReader> testingFiles = new HashMap<>();
        Map<String, FileReader> trainingFiles = new HashMap<>();
        
        Map<String, String[]> testingExamples = new HashMap<>();
        Map<String, String[]> trainingExamples = new HashMap<>();
        String [] s;
        int aH=0,aL=0,aHN=0,aLN=0,pH=0,pL=0;
        String output;
        
        trainingFiles.put("low", new FileReader("C:\\Users\\Algreg M. Mata\\Desktop\\Expert_Systems\\resource\\datasets\\train\\low.txt"));
        trainingFiles.put("high", new FileReader("C:\\Users\\Algreg M. Mata\\Desktop\\Expert_Systems\\resource\\datasets\\train\\high.txt"));
        
        //loading examples in memory
        for(Map.Entry<String, FileReader> entry : trainingFiles.entrySet()) {
            trainingExamples.put(entry.getKey(), readLines(entry.getValue(),entry.getKey()));
        }
        
        for(Map.Entry<String, Integer> entry : high.entrySet()) {
            hTotal+=entry.getValue();
        }
        for(Map.Entry<String, Integer> entry : low.entrySet()) {
            lTotal+=entry.getValue();
        }
        
        probl = (double)lTotal/(lTotal+hTotal);
        probh = (double)hTotal/(lTotal+hTotal);
        System.out.println(probl+"-"+probh);
        total = lTotal+hTotal;
        Map<String,Integer > hmap = sortByValues((HashMap)high); 
        Map<String,Integer > lmap = sortByValues((HashMap)low); 
        
//        displayFreqMatrix(hmap,lmap);
        
        testingFiles.put("low", new FileReader("C:\\Users\\Algreg M. Mata\\Desktop\\Expert_Systems\\resource\\datasets\\test\\low.txt"));
        testingFiles.put("high", new FileReader("C:\\Users\\Algreg M. Mata\\Desktop\\Expert_Systems\\resource\\datasets\\test\\high.txt"));
        
        
        for(Map.Entry<String, FileReader> entry : testingFiles.entrySet()) {
            if(entry.getKey() == "low"){
                s = readLines1(entry.getValue(),entry.getKey());
                pL = s.length;
                testingExamples.put(entry.getKey(), s);
            }else if(entry.getKey() == "high"){
                s = readLines1(entry.getValue(),entry.getKey());
                pH = s.length;
                testingExamples.put(entry.getKey(), s);
            }
        }
        
        for(Map.Entry<String, String[]> entry : testingExamples.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            for(int i = 0 ; i< value.length;i++){
                output = predict(value[i]);
                if(key == "low"){
                    if(output == key){
                        aL++;                        
                    }else{
                        aLN++;  
//                        System.out.println("Predicted "+key+" Actual "+ output +" ||Text : "+value[i]);
                    }
                }else if(key == "high"){
                    if(output == key){
                        aH++;
                    }else{
                        aHN++;
//                        System.out.println("Predicted "+key+" Actual "+ output +" ||Text : "+value[i]);
                    }
                }
                
            }
        }
        System.out.println("             LOW        ||      High");
        System.out.println("LOW     |   "+aL+"      ||      "+aLN);
        System.out.println("HIGH    |   "+aHN+"     ||      "+aH);
        System.out.println("Accuracy: "+((float)(aL+aH)/(pL+pH)));
        System.out.println("Misclassification Rate: "+((float)(aLN+aHN)/(pL+pH)));
        System.out.println("True Positive Rate: "+((float)(aH)/(aHN + aH)));
        System.out.println("False Positive Rate:  "+((float)(aLN)/(aLN + aL)));
        System.out.println("Specificity: "+((float)(aL)/(aLN + aL)));
        System.out.println("Precision: "+((float)(aH)/(aLN + aH)));
        System.out.println("Prevalence: "+((float)(aHN+ aH)/(pL + pH)));
       
    }
    public static void displayFreqMatrix(Map<String,Integer > hmap,Map<String,Integer > lmap){
        for(Map.Entry<String, Integer> entry : hmap.entrySet()) {
            if(lmap.containsKey(entry.getKey())){
                System.out.println(entry.getKey()+","+entry.getValue()+","+lmap.get(entry.getKey()));
                lmap.remove(entry.getKey());
            }else{
                System.out.println(entry.getKey()+","+entry.getValue()+",0");
            }
        }
        for(Map.Entry<String, Integer> entry : lmap.entrySet()) {
            System.out.println(entry.getKey()+",0,"+entry.getValue());
        }
    }
    public static String[] readLines(FileReader fr,String type) throws IOException {
        int x=0;
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fr)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        }
        for(int i =0;i<lines.size();i++){
            if(type == "low"){
                String [] words = lines.get(i).split(" ");
                for(x = 0;x<words.length;x++){
                    if(low.containsKey(words[x])){
                        low.put(words[x].toLowerCase(), low.get(words[x])+1);
                    }else{
                        low.put(words[x].toLowerCase(), 1);
                    }
                }  
            }else if(type == "high"){
                String [] words = lines.get(i).split(" ");
                for(x = 0;x<words.length;x++){
                    if(high.containsKey(words[x])){
                        high.put(words[x].toLowerCase(), high.get(words[x])+1);
                    }else{
                        high.put(words[x].toLowerCase(), 1);
                    }
                }
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
    public static String[] readLines1(FileReader fr,String type) throws IOException {
        int x=0;
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fr)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        }
        return lines.toArray(new String[lines.size()]);
    }
    
    private static HashMap sortByValues(HashMap map) { 
       List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o2)).getValue())
                  .compareTo(((Map.Entry) (o1)).getValue());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       } 
       return sortedHashMap;
  }
     private static void printSortByKey(Map asd){
         Map<String, Integer> map = new TreeMap<String, Integer>(asd); 
         System.out.println("After Sorting:");
         Set set2 = map.entrySet();
         Iterator iterator2 = set2.iterator();
         while(iterator2.hasNext()) {
              Map.Entry me2 = (Map.Entry)iterator2.next();
              System.out.print(me2.getKey() + ": ");
              System.out.println(me2.getValue());
         }
     }
     
     public static String predict(String value){
        int x;
        double l=Math.log(probl+1),h=Math.log(probh+1);
        String [] words = value.split(" ");
        //for low
        
        for(x = 0;x<words.length;x++){
            l+=(Math.log(getProbOfWord(words[x],"low")+1));
        } 
        //for high
        for(x = 0;x<words.length;x++){
            h+=Math.log(getProbOfWord(words[x],"high")+1);
        } 
        System.out.print("low "+l+"-  high "+h+"  ");
        if(l>h){
            System.out.print(" decision low ");
            System.out.println(value);
            return "low";
        }else{
            System.out.print(" decision high ");
            System.out.println(value);
            return "high";
        }
     }
     public static double getProbOfWord(String word,String type){
        double pWord=0, pR=0;  
        if(type == "low"){
            if(low.containsKey(word)){
                pWord = (double)low.get(word)/total;
                pR = (double)low.get(word)/probl;
                return (pWord*pR)/ probl;   
            }
        }else if(type == "high"){
            if(high.containsKey(word)){
                pWord = (double)high.get(word)/total;
                pR = (double)high.get(word)/probh;
                return (pWord*pR)/ probh;   
            }
        }
        return 0.0;
     }
    
    
}
