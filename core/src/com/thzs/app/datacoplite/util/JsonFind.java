package com.thzs.app.datacoplite.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 用于便捷提取数据
 * */
public class JsonFind {
	public JSONObject jso;
	public JsonFind(String jsonStr) {
		this.jso=JSONObject.parseObject(jsonStr);
	}
	public JSONObject get(String xpath) {
		String[] m=xpath.split("/");
		JSONObject temp=jso.getJSONObject(m[0]);
		for(int i=1;i<m.length;i++) {
			temp=temp.getJSONObject(m[i]);
		}
		return temp;
	}
	public Object getObj(String key){
		return jso.get(key);
	}
	@Override
	public String toString() {
		return jso.toString();
	}
}
