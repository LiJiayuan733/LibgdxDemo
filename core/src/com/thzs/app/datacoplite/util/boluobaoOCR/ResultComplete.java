package com.thzs.app.datacoplite.util.boluobaoOCR;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;

public class ResultComplete{
    //上一部分是否结束
    public boolean LastNotEnd=false;
    public ArrayList<ArrayList<ResultLine>> pages=new ArrayList<>();
    public ResultComplete(JsonNode root){
        for (Iterator<JsonNode> it = root.elements(); it.hasNext(); ) {
            JsonNode jsonNodeI = it.next();
            ArrayList<ResultLine> parts=new ArrayList<>();
            for(Iterator<JsonNode> it2 = jsonNodeI.elements(); it2.hasNext();){
                JsonNode jsonNodeII=it2.next();
                parts.add(new ResultLine(jsonNodeII));
            }
            pages.add(parts);
        }
    }
    public ArrayList<ArrayList<ResultLine>> pagesLevel2=new ArrayList<>();
    public void Complete(){
        pagesLevel2=new ArrayList<>();
        for(ArrayList<ResultLine> i:pages){
            ArrayList<ResultLine> resultLine=new ArrayList<>();
            ResultLine resultLevel1=i.get(0);
            for (int j = 1; j < i.size(); j++) {
                if(resultLevel1.isSameLine(i.get(j))){
                    resultLevel1.convertToOne(i.get(j));
                    if(j==i.size()-1){
                        resultLine.add(resultLevel1);
                    }
                }else {
                    resultLine.add(resultLevel1);
                    resultLevel1=i.get(j);
                }
            }
            pagesLevel2.add(resultLine);
        }
    }
    public void printLevel2(){
        for (ArrayList<ResultLine> i : pagesLevel2) {
            for (ResultLine j:i) {
                System.out.println(j.message);
            }
        }
    }
}
