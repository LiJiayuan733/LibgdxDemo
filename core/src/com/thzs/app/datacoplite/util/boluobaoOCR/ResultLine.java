package com.thzs.app.datacoplite.util.boluobaoOCR;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.awt.*;

public class ResultLine {
    public JsonNode points;
    public String message;
    public double score;
    public ResultLine(JsonNode result){
        points=result.get(0);
        message=result.get(1).asText();
        score=result.get(2).asDouble();
    }
    //TODO: code is ready to be written
    public boolean isSameLine(ResultLine line){
        Point lp0=line.getPoint(0);
        Point lp3=line.getPoint(3);
        Point p1=getPoint(1);
        Point p2=getPoint(2);
        int centerY=(lp0.y+lp3.y)/2;
        //The center of the rectangle.
        if(centerY>p1.y&&centerY<p2.y){
            return true;
        }
        return false;
    }
    public ResultLine convertToOne(ResultLine line){
        boolean behind=true;
        Point lp0=line.getPoint(0);
        Point lp1=line.getPoint(1);
        Point p0=getPoint(0);
        Point p1=getPoint(1);
        int l_centerX=(lp0.x+lp1.x)/2;
        int centerX=(p0.x+p1.x)/2;
        behind=centerX<l_centerX;
        if(behind){
            setPoint(1,lp1);
            setPoint(2,line.getPoint(2));
            message+=" "+line.message;
        }else {
            setPoint(0,line.getPoint(0));
            setPoint(3,line.getPoint(3));
            message= line.message+" "+message;
        }
        return this;
    }
    public Point getPoint(int id){
        switch (id){
            case 0:
                return new Point(points.get(0).get(0).asInt(),points.get(0).get(1).asInt());
            case 1:
                return new Point(points.get(1).get(0).asInt(),points.get(1).get(1).asInt());
            case 2:
                return new Point(points.get(2).get(0).asInt(),points.get(2).get(1).asInt());
            case 3:
                return new Point(points.get(3).get(0).asInt(),points.get(3).get(1).asInt());
            default:
                return null;
        }
    }
    public void setPoint(int id,Point point){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode  temp= mapper.createArrayNode();
        temp.add(point.x);
        temp.add(point.y);
        switch (id){
            case 0:
                ((ArrayNode)points).set(0,temp);
            case 1:
                ((ArrayNode)points).set(1,temp);
            case 2:
                ((ArrayNode)points).set(2,temp);
            case 3:
                ((ArrayNode)points).set(3,temp);
        }
    }
    public boolean isHead(int x){
        return getPoint(0).x>x;
    }
    public boolean isEnd(int x){
        return getPoint(1).x<x;
    }
}
