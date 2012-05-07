package org.beijingair.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.beijingair.model.AirDataInfo;
import org.beijingair.model.AirDataInfoLight;
import org.beijingair.model.AirDataSummary;
import org.beijingair.service.AirService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AirController {
	
	@Autowired
	private AirService airService;
	
	

	
	private String buildAqiArrowMessage (double value) {
		if(value<=0) {
			return "Air Quality Index has dropped " + (Math.abs((int)value)) +" points compared to yesterday";
		}
		else {
			return "Air Quality Index has increased " + value +"points compared to yesterday";
			
		}
	}
	
	private WidgetInfo buildWidgetInfo(int aqi, HttpServletRequest request) {
		WidgetInfo widgetInfo = new WidgetInfo();
		String[] thermometer = new String [8];
		for (int i = 0; i < thermometer.length; i++) {
			thermometer[i]="&nbsp;";
		}
		String target="<img src=\"" + request.getContextPath() + "/images/triangle.png\"/>";
		int selected=0;
		int fontSize=12; // default value is 12
		String fontColor =null;
		
		if (aqi <= 50) {
			thermometer[0]=target;
		}
		else if (aqi <= 100) {
			thermometer[1]=target;
			selected=1;
			fontSize=15; //moderate
			fontColor="#0069D6"; //blue over yellow is better than white over yellow
		}
		else if (aqi <= 150) {
			thermometer[2]=target;
			selected=2;
			fontSize=10; // label too big
			fontColor="#0069D6"; //blue over orange is better than white over orange
		}
		else if (aqi <= 200) {
			thermometer[3]=target;
			selected=3;			
		}
		else if (aqi <= 250) {
			thermometer[4]=target;
			selected=4;
		}
		else if (aqi <= 300) {
			thermometer[5]=target;
			selected=5;
		}
		else if (aqi <= 400) {
			thermometer[6]=target;
			selected=6;
		}
		else {
			thermometer[7]=target;
			selected=8;
		}

		widgetInfo.setThermometer(thermometer);
		
		String[] colors = new String[] {"#91DA56","#F3E168","#F3B568","#FF8000","#EE4B22","#B40404","#610B21","#610B21"};

		String color = colors[selected];
		
		String labelStyle="background-color:"+color+";font-size:"+(int)fontSize+"px;";
		if (fontColor != null && !fontColor.equals("")) {
			labelStyle+="color:" + fontColor +";";
		}
		
		widgetInfo.setLabelStyle(labelStyle);

		
		widgetInfo.setColors(colors);
		return widgetInfo;
	}
	
	
	@RequestMapping("/today/{city}")
	public String home(Model model, @PathVariable("city") String city, HttpServletRequest request)  throws Exception {
		// list
		List<AirDataInfo> list = this.airService.findAirDataInfoToday(city);
		model.addAttribute("label", "Time");
		model.addAttribute("city", StringUtils.capitalize(city));
		model.addAttribute("chartData", buildChartData(list, 7, "HH:mm"));
		
		this.prepareWidgetInfo(model, city, request);

		return "today";
	}
	
	@RequestMapping("/widget/{city}")
	public String widget(Model model, @PathVariable("city") String city, HttpServletRequest request)  throws Exception {
		prepareWidgetInfo(model, city, request);
		return "widget";
	}

	/**
	 * called by both the widget and the homepage
	 */
	private void prepareWidgetInfo(Model model, String city, HttpServletRequest request) {
		AirDataSummary summary = this.airService.findTodaySymmary(city);
		model.addAttribute("airDataSummary", summary); 
		WidgetInfo widgetInfo = buildWidgetInfo(summary.getAirDataInfo().getAirQualityIndex(), request);
		model.addAttribute("widgetInfo", widgetInfo);
	}
	
	
	@RequestMapping("/daily/{city}")
	public String airDataDaily(Model model, @PathVariable("city") String city)  throws Exception {
		List<AirDataInfoLight> list = this.airService.findDailyAverageAirDataInfo(city);
		String capCity = StringUtils.capitalize(city);
		
		Collections.sort(list);
		model.addAttribute("airDataList", list);
		model.addAttribute("title", capCity + "  Daily average");
		model.addAttribute("label", "Day");
		model.addAttribute("dateFormat", "dd-MM-yyyy");		
		model.addAttribute("city", capCity);
		model.addAttribute("chartData", buildChartData(list, 8, "Day", "dd-MMM"));
		return "airData";
	}
	
	@RequestMapping("/weekly/{city}")
	public String airDataWeekly(Model model, @PathVariable("city") String city)  throws Exception {
		List<AirDataInfoLight> list = this.airService.findWeeklyAverageAirDataInfo(city);
		String capCity = StringUtils.capitalize(city);
		model.addAttribute("airDataList", list);
		model.addAttribute("title", capCity + " Weekly average");
		model.addAttribute("label", "week starting on");
		model.addAttribute("dateFormat", "dd-MM-yyyy");
		model.addAttribute("city", StringUtils.capitalize(city));
		model.addAttribute("chartData", buildChartData(list, 8, "Week", "dd-MMM"));
		return "airData";
	}
	
	@RequestMapping("/monthly/{city}")
	public String airDataMonthly(Model model, @PathVariable("city") String city)  throws Exception {
		List<AirDataInfoLight> list = this.airService.findMonthlyAverageAirDataInfo(city);
		String capCity = StringUtils.capitalize(city);
		model.addAttribute("airDataList", list);
		model.addAttribute("title", capCity + " Monthly average");
		model.addAttribute("label", "Month");
		model.addAttribute("dateFormat", "yyyy - MM");		
		model.addAttribute("city", StringUtils.capitalize(city));
		model.addAttribute("chartData", buildChartData(list, 8, "Month", "MMMM"));
		return "airData";
	}
	

	
	/**
	 * Displays 2 charts: yearly average and monthly average
	 * @param model
	 * @param city
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/yearly/{city}")
	public String airDataYearly(Model model, @PathVariable("city") String city)  throws Exception {
		List<AirDataInfoLight> listYears = this.airService.findYearlyAverageAirDataInfo(city);
		model.addAttribute("airDataList", listYears);
		model.addAttribute("chartDataYears", buildChartData(listYears, listYears.size(), "year", "YYYY")); 
		
		List<AirDataInfoLight> listMonths = this.airService.findMonthlyAverageMax2011AirDataInfo(city);
		String[] chartDataMonthly = buildChartDataMonthly2YearsAQI(listMonths, listMonths.size());
		model.addAttribute("chartDataMonthsAQI", chartDataMonthly[0]); 
		model.addAttribute("chartDataMonthsPM25", chartDataMonthly[1]); 
		
		
		model.addAttribute("city", StringUtils.capitalize(city));
		return "yearlyAverage";
	}
	
	@RequestMapping("/about")
	public String about()   {
		return "about";
	}
	
	/**
	 * sample output: "Time,10:00,11:00,12:00,13:00,14:00,15:00,16:00,17:00 AQI,176,193,120,50,193,193,176,193"
	 * @param list
	 * @param numberOfElements
	 * @return
	 */	
	public String buildChartData(List<AirDataInfo> list, int numberOfElements, String pattern) {
		AirDataInfo[] elementsArray = buildArray(list);
		int startPoint= elementsArray.length - numberOfElements;
		
		StringBuilder time = new StringBuilder();
		StringBuilder aqi = new StringBuilder();
		StringBuilder pm25 = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		
		for (int i = startPoint; i < elementsArray.length; i++) {
			time.append(","+elementsArray[i].getDate().toString(formatter));			
			aqi.append(","+elementsArray[i].getAirQualityIndex());
			pm25.append(","+elementsArray[i].getFineParticleIndex());
		}
		String chartData = "Time" + time.toString() + " AQI" + aqi.toString()+ " PM2.5" + pm25.toString();
		return chartData;
	}

	private AirDataInfo[] buildArray(List<AirDataInfo> list) {
		AirDataInfo[] elementsArray = new AirDataInfo[list.size()];
		int i = list.size()-1;
		for (AirDataInfo airDataInfo : list) {
			elementsArray[i] = airDataInfo;
			i--;
		}		
		return elementsArray;
	}
	
	private AirDataInfoLight[] buildArrayLight(List<AirDataInfoLight> list) {
		AirDataInfoLight[] elementsArray = new AirDataInfoLight[list.size()];
		int i = list.size()-1;
		for (AirDataInfoLight airDataInfo : list) {
			elementsArray[i] = airDataInfo;
			i--;
		}		
		return elementsArray;
	}
	
	public String buildChartData(List<AirDataInfoLight> list, int desiredNumberOfElements, String unit, String pattern) {
		AirDataInfoLight[] elementsArray = buildArrayLight(list);
		
		// by default, we start from the first element of the list
		int startPoint = 0;	
		// if number of elements in the list is compatible with the desiredNumberOfElements
		if (desiredNumberOfElements <= list.size()) {
			startPoint= elementsArray.length - desiredNumberOfElements;			
		}
		
		StringBuilder time = new StringBuilder();
		StringBuilder aqi = new StringBuilder();
		StringBuilder pm25 = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		
		for (int i = startPoint; i < elementsArray.length; i++) {
			time.append(","+elementsArray[i].getDate().toString(formatter));			
			aqi.append(","+elementsArray[i].getAirQualityIndex());
			pm25.append(","+elementsArray[i].getFineParticleIndex());
		}
		String chartData = unit + time.toString() + " AQI" + aqi.toString() + " PM2.5" + pm25.toString();
		return chartData;
	}
	
	/**
	 * sample output: "Month,January,February 2010,176,193 2011,176,193"
	 * @param list
	 * @param numberOfElements
	 * @param pattern
	 * @return
	 */
	public String[] buildChartDataMonthly2YearsAQI(List<AirDataInfoLight> list, int numberOfElements) {
		AirDataInfoLight[] elementsArray = buildArrayLight(list);
		int startPoint= elementsArray.length - numberOfElements;
		
		String time = ",Jan,Feb,March,Apr,June,Jul,Aug,Sept,Oct,Nov,Dec";
		StringBuilder year2010AQI = new StringBuilder();
		year2010AQI.append(89); // no data for January 2010
		StringBuilder year2011AQI = new StringBuilder();
		
		StringBuilder year2010PM25 = new StringBuilder();
		year2010PM25.append(45); // no data for January 2010
		StringBuilder year2011PM25 = new StringBuilder();
		
		for (int i = startPoint; i < elementsArray.length; i++) {
			if(elementsArray[i].getDate().getYear()==2010) {
				year2010AQI.append(","+elementsArray[i].getAirQualityIndex());
				year2010PM25.append(","+elementsArray[i].getFineParticleIndex());
			} else if(elementsArray[i].getDate().getYear()==2011) {
				year2011AQI.append(","+elementsArray[i].getAirQualityIndex());
				year2011PM25.append(","+elementsArray[i].getFineParticleIndex());
			}
			else return null;
		}
		String chartDataAQI = "Month" + time.toString() + " 2010," + year2010AQI.toString() + " 2011" + year2011AQI.toString();
		String chartDataPM25 = "Month" + time.toString() + " 2010," + year2010PM25.toString() + " 2011" + year2011PM25.toString();
		return new String[]{chartDataAQI,chartDataPM25};
	}

}
