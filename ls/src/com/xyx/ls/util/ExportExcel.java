package com.xyx.ls.util;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportExcel {
	public final static String exportExcel(int i, String fileName,
			List<Channel> channelList, List<Creative> creativeList,
			List<String> iCode, List<String> cCode, HttpServletResponse response) {
		String result = "系统提示：Excel文件导出成功！";
		try {
			SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String time = times.format(new Date());
			String name=fileName+"_"+time+".xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			if (i == 3) {
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				if (creativeList.size() > 0) {
					int q = 0;
					int p = 0;
					for (int w = 0; w < channelList.size(); w++) {
						WritableSheet sheet = workbook.createSheet(channelList
								.get(w).getName(), w);
						CellView cellView = new CellView();  
						cellView.setAutosize(true); //设置自动大小  
						sheet.setColumnView(1, cellView);//根据内容自动设置列宽
						sheet.addCell(new Label(0, 0, "创意"));
						sheet.addCell(new Label(1, 0, "展示代码"));
						sheet.addCell(new Label(2, 0, "点击代码"));
						for (int y = 0; y < 3; y++) {
							for (int z = 1; z < creativeList.size() + 1; z++) {
								if (y == 0) {
									sheet.addCell(new Label(y, z, creativeList
											.get(z - 1).getName()));
								} else if (y == 1) {
									sheet.addCell(new Label(y, z, iCode.get(q)));
									q++;
								} else {
									sheet.addCell(new Label(y, z, cCode.get(p)));
									p++;
								}
							}
						}
					}
				} else {
					for (int w = 0; w < channelList.size(); w++) {
						WritableSheet sheet = workbook.createSheet(channelList
								.get(w).getName(), w);
						sheet.addCell(new Label(0, 0, "展示代码"));
						sheet.addCell(new Label(1, 0, "点击代码"));
						sheet.addCell(new Label(0, 1, iCode.get(w)));
						sheet.addCell(new Label(1, 1, cCode.get(w)));

					}
				}

				workbook.write();
				workbook.close();
			} else {
				if (creativeList.size() > 0) {
					for (int w = 0; w < channelList.size(); w++) {
						WritableWorkbook workbook = Workbook.createWorkbook(os);
						WritableSheet sheet = workbook.createSheet(channelList
								.get(0).getName(), 0);
						sheet.addCell(new Label(0, 0, "创意"));
						sheet.addCell(new Label(1, 0, "展示代码"));
						sheet.addCell(new Label(2, 0, "点击代码"));
						for (int y = 0; y < 3; y++) {
							for (int z = 1; z < creativeList.size() + 1; z++) {
								if (y == 0) {
									sheet.addCell(new Label(y, z, creativeList
											.get(z - 1).getName()));
								} else if (y == 1) {
									sheet.addCell(new Label(y, z, iCode
											.get(z - 1)));
								} else {
									sheet.addCell(new Label(y, z, cCode
											.get(z - 1)));
								}
							}
						}
						workbook.write();
						workbook.close();
					}
				} else {
					for (int w = 0; w < channelList.size(); w++) {
						WritableWorkbook workbook = Workbook.createWorkbook(os);
						WritableSheet sheet = workbook.createSheet(channelList
								.get(w).getName(), w);
						sheet.addCell(new Label(0, 0, "展示代码"));
						sheet.addCell(new Label(1, 0, "点击代码"));
						sheet.addCell(new Label(0, 1, iCode.get(w)));
						sheet.addCell(new Label(1, 1, cCode.get(w)));
						workbook.write();
						workbook.close();
					}
				}
			}
			;

		} catch (Exception e) {
		}
		return result;
	}
}
