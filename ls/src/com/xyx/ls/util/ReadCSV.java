package com.xyx.ls.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadCSV {
		  
	    private InputStreamReader fr = null;  
	    private BufferedReader br = null;  
	  
	    public ReadCSV(String f) throws IOException {  
	        fr = new InputStreamReader(new FileInputStream(f),"utf-8");  
	    }  
	  
	    /** 
	     * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中 
	     */  
	    public   HashMap<String, List> readCSVFile() throws IOException {  
	        br = new BufferedReader(fr);  
	        String rec = null;// 一行  
	        String str;// 一个单元格  
//	        List<List<String>> listFile = new ArrayList<List<String>>();  
	        HashMap<String, List> geoMap = new HashMap<String, List>();
	        try {  
	            // 读取一行  
	            while ((rec = br.readLine()) != null) {  
	                Pattern pCells = Pattern  
	                        .compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*");  
	                Matcher mCells = pCells.matcher(rec);  
	                List<String> cells = new ArrayList<String>();// 每行记录一个list  
	                // 读取每个单元格  
	                while (mCells.find()) {  
	                    str = mCells.group();  
	                    str = str.replaceAll(  
	                            "(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");  
	                    str = str.replaceAll("(?sm)(\"(\"))", "$2");  
	                    cells.add(str);  
	                }  
//	                listFile.add(cells); 
	                geoMap.put(cells.get(4), cells);
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (fr != null) {  
	                fr.close();  
	            }  
	            if (br != null) {  
	                br.close();  
	            }  
	        }  
	        return geoMap;  
	    }  
	  
	    public static void main(String[] args)  {  
	    	ReadCSV test;
			try {
				test = new ReadCSV("C:/Users/Administrator/Desktop/geo.csv");
				  HashMap<String, List> csvList = test.readCSVFile(); 
				  System.out.println(csvList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	       
	      
	    }  
	  
}
