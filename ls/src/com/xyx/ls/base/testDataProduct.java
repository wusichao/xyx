package com.xyx.ls.base;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class testDataProduct {
	private static Connection getConn() {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://123.56.43.158:9600/tk";
	    String username = "ls";
	    String password = "Ls1107!$";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	private static int insert(String sql2) {
	    Connection conn = getConn();
	    int i = 0;
	    String sql = sql2;
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	
	public static void main(String[] args) {
		Integer day=00;
		int y=0;
		for(int ii=0;ii<100;ii++){
		Random rand = new Random();
		//base表数据
		Integer base = rand.nextInt(200000) + 1300000;
		 int channel1 = rand.nextInt(60000)+310000;
		 int channel1c = (int) (rand.nextInt((int) (channel1*0.001))+channel1*0.002);
		 int channel1l = (int) (rand.nextInt((int) (channel1c*0.1))+channel1c*0.7);
		 int channel1a = (int) (rand.nextInt((int) (channel1c*0.05))+channel1c*0.05);
		 
		  int channel2 = rand.nextInt(60000)+310000;
		  int channel2c = (int) (rand.nextInt((int) (channel2*0.001))+channel2*0.002);
		  int channel2l = (int) (rand.nextInt((int) (channel2c*0.1))+channel2c*0.7);
		  int channel2a = (int) (rand.nextInt((int) (channel2c*0.05))+channel2c*0.05);
		  
		  int creative1 = rand.nextInt(60000)+310000;
		  int creative1c = (int) (rand.nextInt((int) (creative1*0.001))+creative1*0.002);
		  int creative1l = (int) (rand.nextInt((int) (creative1c*0.1))+creative1c*0.7);
		  int creative1a = (int) (rand.nextInt((int) (creative1c*0.05))+creative1c*0.05);
		
		  int creative2 = base-channel1-channel2-creative1;
		  if(creative2<=1000){continue;}
		  int creative2c = (int) (rand.nextInt((int) (creative2*0.001))+creative2*0.002);
		  if(creative2c<=100){continue;}
		  int creative2l = (int) (rand.nextInt((int) (creative2c*0.1))+creative2c*0.7);
		  int creative2a = (int) (rand.nextInt((int) (creative2c*0.05))+creative2c*0.05);
		  
		  int countc=channel1c+channel2c+creative1c+creative2c;
		  int countl=channel1l+channel2l+creative1l+creative2l;
		  int counta=channel1a+channel2a+creative1a+creative2a;
		  //geo表数据
		  int beijing = rand.nextInt(70000)+430000;
		  int beijingc = (int) (rand.nextInt((int) (beijing*0.001))+beijing*0.002);
		  int beijingl = (int) (rand.nextInt((int) (beijingc*0.1))+beijingc*0.7);
		  int beijinga = (int) (rand.nextInt((int) (beijingc*0.05))+beijingc*0.05);
		  
		  int shanghai = rand.nextInt(70000)+430000;
		  int shanghaic = (int) (rand.nextInt((int) (shanghai*0.001))+shanghai*0.002);
		  int shanghail = (int) (rand.nextInt((int) (shanghaic*0.1))+shanghaic*0.7);
		  int shanghaia = (int) (rand.nextInt((int) (shanghaic*0.05))+shanghaic*0.05);
		  
		  int guangzhou = base-beijing-shanghai;
		  int guangzhouc = countc-beijingc-shanghaic;
		  int guangzhoul = countl-beijingl-shanghail;
		  int guangzhoua = counta-beijinga-shanghaia;
		  
		  //hour表数据
		  int a = rand.nextInt(5000)+20000;//0
		  int ac = (int) (rand.nextInt((int) (a*0.001))+a*0.002);//0
		  int al = (int) (rand.nextInt((int) (ac*0.1))+ac*0.7);//0
		  int aa = (int) (rand.nextInt((int) (ac*0.05))+ac*0.05);//0
		  
		  int b = rand.nextInt(5000)+20000;//1
		  int bc = (int) (rand.nextInt((int) (b*0.001))+b*0.002);//1
		  int bl = (int) (rand.nextInt((int) (bc*0.1))+bc*0.7);//1
		  int ba = (int) (rand.nextInt((int) (bc*0.05))+bc*0.05);//1
		  
		  int c = rand.nextInt(5000)+10000;//2
		  int cc = (int) (rand.nextInt((int) (c*0.001))+c*0.002);//2
		  int cl = (int) (rand.nextInt((int) (cc*0.1))+cc*0.7);//2
		  int ca = (int) (rand.nextInt((int) (cc*0.05))+cc*0.05);//2
		  
		  int d = rand.nextInt(5000)+10000;//3
		  int dc = (int) (rand.nextInt((int) (d*0.001))+d*0.002);//3
		  int dl = (int) (rand.nextInt((int) (dc*0.1))+dc*0.7);//3
		  int da = (int) (rand.nextInt((int) (dc*0.05))+dc*0.05);//3
		  
		  int e = rand.nextInt(5000)+10000;//4
		  int ec = (int) (rand.nextInt((int) (e*0.001))+e*0.002);//4
		  int el = (int) (rand.nextInt((int) (ec*0.1))+ec*0.7);//4
		  int ea = (int) (rand.nextInt((int) (ec*0.05))+ec*0.05);//4
		  
		  int f = rand.nextInt(5000)+10000;//5
		  int fc = (int) (rand.nextInt((int) (f*0.001))+f*0.002);//5
		  int fl = (int) (rand.nextInt((int) (fc*0.1))+fc*0.7);//5
		  int fa = (int) (rand.nextInt((int) (fc*0.05))+fc*0.05);//5
		  
		  int g = rand.nextInt(5000)+20000;//6
		  int gc = (int) (rand.nextInt((int) (g*0.001))+g*0.002);//6
		  int gl = (int) (rand.nextInt((int) (gc*0.1))+gc*0.7);//6
		  int ga = (int) (rand.nextInt((int) (gc*0.05))+gc*0.05);//6
		  
		  int h = rand.nextInt(5000)+30000;//7
		  int hc = (int) (rand.nextInt((int) (h*0.001))+h*0.002);//7
		  int hl = (int) (rand.nextInt((int) (hc*0.1))+hc*0.7);//7
		  int ha = (int) (rand.nextInt((int) (hc*0.05))+hc*0.05);//7
		  
		  int i = rand.nextInt(5000)+40000;//8
		  int ic = (int) (rand.nextInt((int) (i*0.001))+i*0.002);//8
		  int il = (int) (rand.nextInt((int) (ic*0.1))+ic*0.7);//8
		  int ia = (int) (rand.nextInt((int) (ic*0.05))+ic*0.05);//8
		  
		  int j = rand.nextInt(5000)+70000;//9
		  int jc = (int) (rand.nextInt((int) (j*0.001))+j*0.002);//9
		  int jl = (int) (rand.nextInt((int) (jc*0.1))+jc*0.7);//9
		  int ja = (int) (rand.nextInt((int) (jc*0.05))+jc*0.05);//9
		  
		  int k = rand.nextInt(5000)+100000;//10
		  int kc = (int) (rand.nextInt((int) (k*0.001))+k*0.002);//10
		  int kl = (int) (rand.nextInt((int) (kc*0.1))+kc*0.7);//10
		  int ka = (int) (rand.nextInt((int) (kc*0.05))+kc*0.05);//10
		  
		  int l = rand.nextInt(5000)+120000;//11
		  int lc = (int) (rand.nextInt((int) (l*0.001))+l*0.002);//11
		  int ll = (int) (rand.nextInt((int) (lc*0.1))+lc*0.7);//11
		  int la = (int) (rand.nextInt((int) (lc*0.05))+lc*0.05);//11
		  
		  int m = rand.nextInt(5000)+90000;//12
		  int mc = (int) (rand.nextInt((int) (m*0.001))+m*0.002);//12
		  int ml = (int) (rand.nextInt((int) (mc*0.1))+mc*0.7);//12
		  int ma = (int) (rand.nextInt((int) (mc*0.05))+mc*0.05);//12
		  
		  int n = rand.nextInt(5000)+100000;//13
		  int nc = (int) (rand.nextInt((int) (n*0.001))+n*0.002);//13
		  int nl = (int) (rand.nextInt((int) (nc*0.1))+nc*0.7);//13
		  int na = (int) (rand.nextInt((int) (nc*0.05))+nc*0.05);//13
		  
		  int o = rand.nextInt(5000)+100000;//14
		  int oc = (int) (rand.nextInt((int) (o*0.001))+o*0.002);//14
		  int ol = (int) (rand.nextInt((int) (oc*0.1))+oc*0.7);//14
		  int oa = (int) (rand.nextInt((int) (oc*0.05))+oc*0.05);//14
		  
		  int p = rand.nextInt(5000)+80000;//15
		  int pc = (int) (rand.nextInt((int) (p*0.001))+p*0.002);//15
		  int pl = (int) (rand.nextInt((int) (pc*0.1))+pc*0.7);//15
		  int pa = (int) (rand.nextInt((int) (pc*0.05))+pc*0.05);//15
		  
		  int q = rand.nextInt(5000)+80000;//16
		  int qc = (int) (rand.nextInt((int) (q*0.001))+q*0.002);//16
		  int ql = (int) (rand.nextInt((int) (qc*0.1))+qc*0.7);//16
		  int qa = (int) (rand.nextInt((int) (qc*0.05))+qc*0.05);//16
		  
		  int r = rand.nextInt(5000)+60000;//17
		  int rc = (int) (rand.nextInt((int) (r*0.001))+r*0.002);//17
		  int rl = (int) (rand.nextInt((int) (rc*0.1))+rc*0.7);//17
		  int ra = (int) (rand.nextInt((int) (rc*0.05))+rc*0.05);//17
		  
		  int s = rand.nextInt(5000)+40000;//18
		  int sc = (int) (rand.nextInt((int) (s*0.001))+s*0.002);//18
		  int sl = (int) (rand.nextInt((int) (sc*0.1))+sc*0.7);//18
		  int sa = (int) (rand.nextInt((int) (sc*0.05))+sc*0.05);//18
		  
		  int t = rand.nextInt(5000)+80000;//19
		  int tc = (int) (rand.nextInt((int) (t*0.001))+t*0.002);//19
		  int tl = (int) (rand.nextInt((int) (tc*0.1))+tc*0.7);//19
		  int ta = (int) (rand.nextInt((int) (tc*0.05))+tc*0.05);//19
		  
		  int u = rand.nextInt(5000)+80000;//20
		  int uc = (int) (rand.nextInt((int) (u*0.001))+u*0.002);//20
		  int ul = (int) (rand.nextInt((int) (uc*0.1))+uc*0.7);//20
		  int ua = (int) (rand.nextInt((int) (uc*0.05))+uc*0.05);//20
		  
		  int v = rand.nextInt(5000)+50000;//21
		  int vc = (int) (rand.nextInt((int) (v*0.001))+v*0.002);//21
		  int vl = (int) (rand.nextInt((int) (vc*0.1))+vc*0.7);//21
		  int va = (int) (rand.nextInt((int) (vc*0.05))+vc*0.05);//21
		  
		  int w = rand.nextInt(5000)+50000;//22
		  int wc = (int) (rand.nextInt((int) (w*0.001))+w*0.002);//22
		  int wl = (int) (rand.nextInt((int) (wc*0.1))+wc*0.7);//22
		  int wa = (int) (rand.nextInt((int) (wc*0.05))+wc*0.05);//22
		  
		  int x = base-a-b-c-d-e-f-g-h-i-j-k-l-m-n-o-p-q-r-s-t-u-v-w;//23
		  int xc = countc-ac-bc-cc-dc-ec-fc-gc-hc-ic-jc-kc-lc-mc-nc-oc-pc-qc-rc-sc-tc-uc-vc-wc;//23
		  int xl = countl-al-bl-cl-dl-el-fl-gl-hl-il-jl-kl-ll-ml-nl-ol-pl-ql-rl-sl-tl-ul-vl-wl;//23
		  int xa = counta-aa-ba-ca-da-ea-fa-ga-ha-ia-ja-ka-la-ma-na-oa-pa-qa-ra-sa-ta-ua-va-wa;//23
		  
//		if(x>0&&x<200000&&xc>0&&xc<500&&xl>0&&xl<300&&xa>0&&xa<100&&x>xc&&xc>xl&&xl>xa){ 
		 
			if(x>0&&xc>0&&xl>0&&xa>0&&xc>xl){ 
				y++;
				if(y>31){break;}
//				for(int y=0;y<31;y++){
			day++;
			String days;
			long campaignId = 34;
			long channelOne = 41;
			long channelTwo = 42;
			long creativeOne=60;
			long creativeTwo=61;
				if (day < 10) {
					days = "2016-03-0" + day;
				} else {
					days = "2016-03-" + day;
				}
			
		String sqlChannel1="insert into rpt_base (imp,click,reach,conversion,campaign_id,channel_id,creative_id,day,creation)values"
				+ "("+channel1+","+channel1c+","+channel1l+","+channel1a+","+campaignId+","+channelOne+","+creativeOne+",'"+days+"',now()"+")\r\n"
				+ ",("+channel2+","+channel2c+","+channel2l+","+channel2a+","+campaignId+","+channelOne+","+creativeTwo+",'"+days+"',now()"+")\r\n"
				+ ",("+creative1+","+creative1c+","+creative1l+","+creative1a+","+campaignId+","+channelTwo+","+creativeOne+",'"+days+"',now()"+")\r\n"
				+ ",("+creative2+","+creative2c+","+creative2l+","+creative2a+","+campaignId+","+channelTwo+","+creativeTwo+",'"+days+"',now()"+")\r\n";
//		campaignService.setBaseData(sqlChannel1);
		insert(sqlChannel1);
		String sqlbeijing="insert into rpt_geo (imp,click,reach,conversion,campaign_id,geo_id,day,creation)values"
				+ "("+beijing+","+beijingc+","+beijingl+","+beijinga+","+campaignId+",1156110000,'"+days+"',now()"+")\r\n"
				+ ",("+shanghai+","+shanghaic+","+shanghail+","+shanghaia+","+campaignId+",1156310000,'"+days+"',now()"+")\r\n"
				+ ",("+guangzhou+","+guangzhouc+","+guangzhoul+","+guangzhoua+","+campaignId+",1156440100,'"+days+"',now()"+")\r\n";
//		campaignService.setGeoData(sqlbeijing);
		insert(sqlbeijing);
		String sqla="insert into rpt_hour (imp,click,reach,conversion,campaign_id,hour,day,creation)values"
				+ "("+a+","+ac+","+al+","+aa+","+campaignId+",0,'"+days+"',now()"+")\r\n"
				+ ",("+b+","+bc+","+bl+","+ba+","+campaignId+",1,'"+days+"',now()"+")\r\n"
				+ ",("+c+","+cc+","+cl+","+ca+","+campaignId+",2,'"+days+"',now()"+")\r\n"
				+ ",("+d+","+dc+","+dl+","+da+","+campaignId+",3,'"+days+"',now()"+")\r\n"
				+ ",("+e+","+ec+","+el+","+ea+","+campaignId+",4,'"+days+"',now()"+")\r\n"
				+ ",("+f+","+fc+","+fl+","+fa+","+campaignId+",5,'"+days+"',now()"+")\r\n"
				+ ",("+g+","+gc+","+gl+","+ga+","+campaignId+",6,'"+days+"',now()"+")\r\n"
				+ ",("+h+","+hc+","+hl+","+ha+","+campaignId+",23,'"+days+"',now()"+")\r\n"
				+ ",("+i+","+ic+","+il+","+ia+","+campaignId+",7,'"+days+"',now()"+")\r\n"
				+ ",("+j+","+jc+","+jl+","+ja+","+campaignId+",8,'"+days+"',now()"+")\r\n"
				+ ",("+k+","+kc+","+kl+","+ka+","+campaignId+",9,'"+days+"',now()"+")\r\n"
				+ ",("+l+","+lc+","+ll+","+la+","+campaignId+",11,'"+days+"',now()"+")\r\n"
				+ ",("+m+","+mc+","+ml+","+ma+","+campaignId+",12,'"+days+"',now()"+")\r\n"
				+ ",("+n+","+nc+","+nl+","+na+","+campaignId+",13,'"+days+"',now()"+")\r\n"
				+ ",("+o+","+oc+","+ol+","+oa+","+campaignId+",14,'"+days+"',now()"+")\r\n"
				+ ",("+p+","+pc+","+pl+","+pa+","+campaignId+",15,'"+days+"',now()"+")\r\n"
				+ ",("+q+","+qc+","+ql+","+qa+","+campaignId+",16,'"+days+"',now()"+")\r\n"
				+ ",("+r+","+rc+","+rl+","+ra+","+campaignId+",17,'"+days+"',now()"+")\r\n"
				+ ",("+s+","+sc+","+sl+","+sa+","+campaignId+",18,'"+days+"',now()"+")\r\n"
				+ ",("+t+","+tc+","+tl+","+ta+","+campaignId+",19,'"+days+"',now()"+")\r\n"
				+ ",("+u+","+uc+","+ul+","+ua+","+campaignId+",20,'"+days+"',now()"+")\r\n"
				+ ",("+v+","+vc+","+vl+","+va+","+campaignId+",21,'"+days+"',now()"+")\r\n"
				+ ",("+w+","+wc+","+wl+","+wa+","+campaignId+",22,'"+days+"',now()"+")\r\n"
				+ ",("+x+","+xc+","+xl+","+xa+","+campaignId+",10,'"+days+"',now()"+")\r\n";
//		campaignService.setHourData(sqla);
		insert(sqla);
		
		byte[] buff=new byte[]{};  
	        try   
	        {  
	            String wsc=sqlChannel1+sqlbeijing+sqla;  
	            buff=wsc.getBytes();  
	            FileOutputStream out=new FileOutputStream("D://wsc/"+ii+"out.sql");  
	            out.write(buff,0,buff.length);  
	        }   
	        catch (FileNotFoundException yc)   
	        {  
	            yc.printStackTrace();  
	        }  
	        catch (IOException yc)   
	        {  
	            yc.printStackTrace();  
	        }  
			}}
		}
	}
