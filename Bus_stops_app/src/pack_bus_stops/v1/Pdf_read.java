package pack_bus_stops.v1;

import java.io.IOException;
import java.util.Calendar;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Pdf_read {
	
	public String read_pdf(String url, String bus_stop_name) throws IOException {
		
		String finding_curr_rout[];
		double delaytimer=0;
		int my_day_set;
		int hour_of_arrival[]={-1,-1};
		//double result[];
		//int checkingfornexthour=0;
		double your_bus_time[]={-1,-1};
		//get time 
		Calendar c = Calendar.getInstance(); 
        int hour = c.get(Calendar.HOUR);
        int AM_OR_PM = c.get(Calendar.AM_PM);
        double minute = c.get(Calendar.MINUTE);
        if  (AM_OR_PM==1){
        	hour=hour+12;
        }
        System.out.println("time is : "+ hour + ":" + minute);
		
        PdfReader reader = new PdfReader(url);
        //find how far the bus stop is//
        Rectangle rect_gia_stash = new Rectangle(10, 660, 590, 710);
        RenderFilter filter_gia_stash = new RegionTextRenderFilter(rect_gia_stash);
        TextExtractionStrategy strategy_gia_stash;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy_gia_stash = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter_gia_stash);
            String curr_stop[] = PdfTextExtractor.getTextFromPage(reader, i, strategy_gia_stash).split("\n");
            for(int j=0; j<curr_stop.length; j++){
            	//finding_curr_rout = curr_stop[j].split(" ");
            	delaytimer=delaytimer+1;
            	if (curr_stop[j].equals(bus_stop_name)){
            		//minute=minute + delaytimer;
            		//System.out.println("arrival at :" +hour + " " + minute);
            		break;
            	}
            }
            //System.out.println(PdfTextExtractor.getTextFromPage(reader, i, strategy_gia_stash));
        }
        
        int day = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("day :" + day);
        if (day==1){
        	my_day_set=2;
        }else if (day==7) {
        	my_day_set=1;
        }else{
        	my_day_set=0;
        }
        //find the time depending on the day and hour
        Rectangle rect = new Rectangle(10, 300, 590, 610);
        RenderFilter filter = new RegionTextRenderFilter(rect);
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
            String curr_rout[] = PdfTextExtractor.getTextFromPage(reader, i, strategy).split("\n");
            if (minute<delaytimer-5){
            	hour=hour-1;
            	minute=minute+60;
            }
	        while (your_bus_time[0]==-1 || your_bus_time[1]==-1){
	            for(int j=0; j<curr_rout.length; j++){
	            	
	            	finding_curr_rout = curr_rout[j].split(" ");
	            	if (Integer.parseInt(finding_curr_rout[0])==hour){
	            		
	            		your_bus_time = the_rout_times(finding_curr_rout,my_day_set,delaytimer,minute,your_bus_time);
	            		//System.out.println(your_bus_time[0] + " " + your_bus_time[1] + " " + hour + " " + curr_rout.length);
	            		if(your_bus_time[0] != -1 && hour_of_arrival[0] == -1) {
	            			hour_of_arrival[0]=hour;
	            		}
	            		if(your_bus_time[1] != -1 && hour_of_arrival[1] == -1) {
	            			hour_of_arrival[1]=hour;
	            		}
	            		if (your_bus_time[0]==-1 || your_bus_time[1]==-1 ) {
	            			if (hour==24){
	            				hour=1;
	            				minute=-1;
	            			}else{
		            			hour++;
		            			minute=-1;
		            			//checkingfornexthour=1;
	            			
	            			}
	            		}
	            		//System.out.println(curr_rout[j]);
	            		//break;
	            	}
	            }
	            hour++;
            }
	        if (your_bus_time[0]>=60){
	        	hour_of_arrival[0]=hour_of_arrival[0]+1;
	        	your_bus_time[0]=your_bus_time[0]-60;
	        }else if (your_bus_time[1]>=60){
	        	hour_of_arrival[1]=hour_of_arrival[1]+1;
	        	your_bus_time[1]=your_bus_time[1]-60;
	        }
            System.out.println(hour_of_arrival[0] + ":" + your_bus_time[0] + "    " + hour_of_arrival[1] + ":" + your_bus_time[1]);
        }
        
        String changed_timer_form[]={" "," "};
        
        if (your_bus_time[0]<10){
        	changed_timer_form[0]=("0" + Integer.toString((int)your_bus_time[0]));
        }else {
        	changed_timer_form[0]=Integer.toString((int)your_bus_time[0]);
        }
        
        if (your_bus_time[1]<10){
        	changed_timer_form[1]=("0" + Integer.toString((int)your_bus_time[1]));
        }else {
        	changed_timer_form[1]=Integer.toString((int)your_bus_time[1]);
        }
        reader.close();
		return (hour_of_arrival[0] + ":" + changed_timer_form[0] + " " + hour_of_arrival[1] + ":" + changed_timer_form[1]);
	}
	
	public double[] the_rout_times(String finding_curr_rout[],int my_day_set,double delaytimer,double minute,double your_bus_time[]) {
		
		double old_wresleoforioumedelay=-1;
		double wresleoforioumedelay;
		int flag=0;
		
		
		for (int f=1; f<finding_curr_rout.length-1; f++ ) {
			wresleoforioumedelay = Double.parseDouble(finding_curr_rout[f])+delaytimer;
			//System.out.println(old_wresleoforioumedelay + " " + wresleoforioumedelay + " " + flag);
			if(old_wresleoforioumedelay>wresleoforioumedelay) {
				flag++;
			}
			//deftera me paraskevh//
			if ( flag==0 && 0==my_day_set) {
				if(wresleoforioumedelay <= minute){
				
				}else if(your_bus_time[0]==-1) {
					your_bus_time[0] = wresleoforioumedelay;
				}else if(your_bus_time[1]==-1) {
					your_bus_time[1] = wresleoforioumedelay;
				}
				
			}else if ( flag==1 && my_day_set==1 ) {				//savvato//
				//System.out.println(your_bus_time[0] + " " + your_bus_time[1] + " " + minute);
				if(wresleoforioumedelay <= minute){
				
				}else if(your_bus_time[0]==-1) {
					your_bus_time[0] = wresleoforioumedelay;
				}else if(your_bus_time[1]==-1) {
					your_bus_time[1] = wresleoforioumedelay;
				}
				
			}else if ( flag==2 && 2==my_day_set) {					//kyriakh//
				if(wresleoforioumedelay <= minute){
				
				}else if(your_bus_time[0]==-1) {
					your_bus_time[0] = wresleoforioumedelay;
				}else if(your_bus_time[1]==-1) {
					your_bus_time[1] = wresleoforioumedelay;
				}
			}
			
			old_wresleoforioumedelay=wresleoforioumedelay;
		}
		return your_bus_time;
	}
}