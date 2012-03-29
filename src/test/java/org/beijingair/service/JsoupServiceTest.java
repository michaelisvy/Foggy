package org.beijingair.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupServiceTest {
	
	@Test
	public void test() throws Exception {
		Document doc = Jsoup.connect("http://zx.bjmemc.com.cn/Charts/PM25.aspx").get();
		Elements resultLinks = doc.select("html > body > form > div:eq(1) > table > tbody > tr:eq(1) > td > table > tbody");
		// /html/body/form/div[2]/table/tbody/tr[2]/td/table/tbody/tr/td
		Elements row = resultLinks.last().select("tbody > tr");
		
		String time = row.select("td:eq(0)").text();
		//removing non breaking space
		time=time.replace(String.valueOf((char) 160), "");

		String pm25 =  row.select("td:eq(1)").text();
		pm25=pm25.replace(String.valueOf((char) 160), "");
		
		System.out.println(time + " " + pm25);

	}

}
