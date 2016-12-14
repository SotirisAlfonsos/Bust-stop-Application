package pack_bus_stops.v1;

public class Nearest_Bus_Stop extends MainActivity{
	
	public String[] Nearest_stop_calc(double loc_lat, double loc_lon) {
		double latitude_min;
		double longitude_min;
		double best_stop;
		String name= "";
		String[] best_station_lat_lon = {"", "", "",""};
		// STASH 1 METKA//
		latitude_min = 39.37317;
		longitude_min = 22.91810;
		best_stop = ((Math.abs(loc_lat - 39.37317) + Math.abs(loc_lon - 22.91810))/2);
		name ="ΜΕΤΚΑ";
		best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		//System.out.println("best stop is :" + best_stop);
		
		// STASH 2 navarinou//
		if (((Math.abs(loc_lat - 39.37402) + Math.abs(loc_lon - 22.92050))/2) < best_stop){
			latitude_min = 39.37402;
			longitude_min = 22.92050;
			best_stop = ((Math.abs(loc_lat - 39.37402) + Math.abs(loc_lon - 22.92050))/2);
			name ="Ναυαρίνου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
		
		// STASH 3 kyrillou//
		if (((Math.abs(loc_lat - 39.37433) + Math.abs(loc_lon - 22.92173))/2) < best_stop){
			latitude_min = 39.37433;
			longitude_min = 22.92173;
			best_stop = ((Math.abs(loc_lat - 39.37433) + Math.abs(loc_lon - 22.92173))/2);
			name ="Κυρίλλου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 4 petrou kai pavlou//
		if (((Math.abs(loc_lat - 39.37192) + Math.abs(loc_lon - 22.92248))/2) < best_stop){
			latitude_min = 39.37192;
			longitude_min = 22.92248;
			best_stop = ((Math.abs(loc_lat - 39.37192) + Math.abs(loc_lon - 22.92248))/2);
			name ="Πέτρου & Παύλου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 5 euvoias//
		if (((Math.abs(loc_lat - 39.37065) + Math.abs(loc_lon - 22.92388))/2) < best_stop){
			latitude_min = 39.37065;
			longitude_min = 22.92388;
			best_stop = ((Math.abs(loc_lat - 39.37065) + Math.abs(loc_lon - 22.92388))/2);
			name ="Ευβοίας";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 6 isidwrou//
		if (((Math.abs(loc_lat - 39.37015) + Math.abs(loc_lon - 22.92684))/2) < best_stop){
			latitude_min = 39.37015;
			longitude_min = 22.92684;
			best_stop = ((Math.abs(loc_lat - 39.37015) + Math.abs(loc_lon - 22.92684))/2);
			name ="Ισιδώρου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
	
		// STASH 7 mesologgiou//
		if (((Math.abs(loc_lat - 39.36831) + Math.abs(loc_lon - 22.92585))/2) < best_stop){
			latitude_min = 39.36831;
			longitude_min = 22.92585;
			best_stop = ((Math.abs(loc_lat - 39.36831) + Math.abs(loc_lon - 22.92585))/2);
			name = "Μεσολογγίου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 8 ag. anargyroi//
		if (((Math.abs(loc_lat - 39.36655) + Math.abs(loc_lon - 22.92637))/2) < best_stop){
			latitude_min = 39.36655;
			longitude_min = 22.92637;
			best_stop = ((Math.abs(loc_lat - 39.36655) + Math.abs(loc_lon - 22.92637))/2);
			name = "Αγ. Ανάργυροι";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 9 papaflessa//
		if (((Math.abs(loc_lat - 39.36556) + Math.abs(loc_lon - 22.92574))/2) < best_stop){
			latitude_min = 39.36556;
			longitude_min = 22.92574;
			best_stop = ((Math.abs(loc_lat - 39.36556) + Math.abs(loc_lon - 22.92574))/2);
			name = "Παπαφλέσσα";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 10 taxydromeio//
		if (((Math.abs(loc_lat - 39.36452) + Math.abs(loc_lon - 22.92749))/2) < best_stop){
			latitude_min = 39.36452;
			longitude_min = 22.92749;
			best_stop = ((Math.abs(loc_lat - 39.36452) + Math.abs(loc_lon - 22.92749))/2);
			name = "Ταχυδρομείο";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 11 deh//
		if (((Math.abs(loc_lat - 39.36328) + Math.abs(loc_lon - 22.93030))/2) < best_stop){
			latitude_min = 39.36328;
			longitude_min = 22.93030;
			best_stop = ((Math.abs(loc_lat - 39.36328) + Math.abs(loc_lon - 22.93030))/2);
			name = "ΔΕΗ";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 12 ktel//
		if (((Math.abs(loc_lat - 39.36199) + Math.abs(loc_lon - 22.93322))/2) < best_stop){
			latitude_min = 39.36199;
			longitude_min = 22.93322;
			best_stop = ((Math.abs(loc_lat - 39.36199) + Math.abs(loc_lon - 22.93322))/2);
			name = "ΚΤΕΛ";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 13 palaia//
		if (((Math.abs(loc_lat - 39.36212) + Math.abs(loc_lon - 22.93519))/2) < best_stop){
			latitude_min = 39.36212;
			longitude_min = 22.93519;
			best_stop = ((Math.abs(loc_lat - 39.36212) + Math.abs(loc_lon - 22.93322))/2);
			name = "Παλαιά";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 14 dhmarxeio//
		if (((Math.abs(loc_lat - 39.36199) + Math.abs(loc_lon - 22.93968))/2) < best_stop){
			latitude_min = 39.36199;
			longitude_min = 22.93968;
			best_stop = ((Math.abs(loc_lat - 39.36199) + Math.abs(loc_lon - 22.93968))/2);
			name = "Δημαρχείο";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
			System.out.println(latitude_min +" "+ longitude_min);
			System.out.println(best_stop);
		}

		// STASH 15 solwnos//
		if (((Math.abs(loc_lat - 39.36133) + Math.abs(loc_lon - 22.94336))/2) < best_stop){
			latitude_min = 39.36133;
			longitude_min = 22.94336;
			best_stop = ((Math.abs(loc_lat - 39.36133) + Math.abs(loc_lon - 22.94336))/2);
			name = "Σόλωνος";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
			System.out.println(latitude_min +" "+ longitude_min);
			System.out.println(best_stop);
		}

		// STASH 16 //
		if (((Math.abs(loc_lat - 39.36082) + Math.abs(loc_lon - 22.94712))/2) < best_stop){
			latitude_min = 39.36082;
			longitude_min = 22.94712;
			best_stop = ((Math.abs(loc_lat - 39.36082) + Math.abs(loc_lon - 22.94712))/2);
			name = "Ερμού";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 17 //
		if (((Math.abs(loc_lat - 39.36356) + Math.abs(loc_lon - 22.94960))/2) < best_stop){
			latitude_min = 39.36356;
			longitude_min = 22.94960;
			best_stop = ((Math.abs(loc_lat - 39.36356) + Math.abs(loc_lon - 22.94960))/2);
			name = "Κωνσταντά";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 18 //
		if (((Math.abs(loc_lat - 39.36515) + Math.abs(loc_lon - 22.95107))/2) < best_stop){
			latitude_min = 39.36515;
			longitude_min = 22.95107;
			best_stop = ((Math.abs(loc_lat - 39.36515) + Math.abs(loc_lon - 22.95107))/2);
			name = "Νομαρχία";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
	
		// STASH 19 //
		if (((Math.abs(loc_lat - 39.36505) + Math.abs(loc_lon - 22.95320))/2) < best_stop){
			latitude_min = 39.36505;
			longitude_min = 22.95320;
			best_stop = ((Math.abs(loc_lat - 39.36505) + Math.abs(loc_lon - 22.95320))/2);
			name = "Κύπρου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
	
		// STASH 20 //
		if (((Math.abs(loc_lat - 39.36332) + Math.abs(loc_lon - 22.95619))/2) < best_stop){
			latitude_min = 39.36332;
			longitude_min = 22.95619;
			best_stop = ((Math.abs(loc_lat - 39.36332) + Math.abs(loc_lon - 22.95619))/2);
			name = "Μαυροκορδάτου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 21 //
		if (((Math.abs(loc_lat - 39.36199) + Math.abs(loc_lon - 22.95868))/2) < best_stop){
			latitude_min = 39.36199;
			longitude_min = 22.95868;
			best_stop = ((Math.abs(loc_lat - 39.36199) + Math.abs(loc_lon - 22.95868))/2);
			name = "Κασσαβέτη";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 22 //
		if (((Math.abs(loc_lat - 39.36094) + Math.abs(loc_lon - 22.96081))/2) < best_stop){
			latitude_min = 39.36094;
			longitude_min = 22.96081;
			best_stop = ((Math.abs(loc_lat - 39.36094) + Math.abs(loc_lon - 22.96081))/2);
			name = "Περραιβού";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
	
		// STASH 23 //
		if (((Math.abs(loc_lat - 39.35972) + Math.abs(loc_lon - 22.96342))/2) < best_stop){
			latitude_min = 39.35972;
			longitude_min = 22.96342;
			best_stop = ((Math.abs(loc_lat - 39.35972) + Math.abs(loc_lon - 22.96342))/2);
			name = "Θερμοπυλών";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 24 //
		if (((Math.abs(loc_lat - 39.35807) + Math.abs(loc_lon - 22.96483))/2) < best_stop){
			latitude_min = 39.35807;
			longitude_min = 22.96483;
			best_stop = ((Math.abs(loc_lat - 39.35807) + Math.abs(loc_lon - 22.96483))/2);
			name = "Αγ. Δημήτριος";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 25 //
		if (((Math.abs(loc_lat - 39.35648) + Math.abs(loc_lon - 22.96619))/2) < best_stop){
			latitude_min = 39.35648;
			longitude_min = 22.96619;
			best_stop = ((Math.abs(loc_lat - 39.35648) + Math.abs(loc_lon - 22.96619))/2);
			name = "Στάδιο";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
				
		// STASH 26 //
		if (((Math.abs(loc_lat - 39.35452) + Math.abs(loc_lon - 22.96585))/2) < best_stop){
			latitude_min = 39.35452;
			longitude_min = 22.96585;
			best_stop = ((Math.abs(loc_lat - 39.35452) + Math.abs(loc_lon - 22.96585))/2);
			name = "Κολυμβητήριο";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
	
		// STASH 27 //
		if (((Math.abs(loc_lat - 39.35187) + Math.abs(loc_lon - 22.96515))/2) < best_stop){
			latitude_min = 39.35187;
			longitude_min = 22.96515;
			best_stop = ((Math.abs(loc_lat - 39.35187) + Math.abs(loc_lon - 22.96515))/2);
			name = "Σταδίου";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}

		// STASH 28 //
		if (((Math.abs(loc_lat - 39.35071) + Math.abs(loc_lon - 22.96331))/2) < best_stop){
			latitude_min = 39.35071;
			longitude_min = 22.96331;
			best_stop = ((Math.abs(loc_lat - 39.35071) + Math.abs(loc_lon - 22.96331))/2);
			name = "Άναυρος";
			best_station_lat_lon[3] = Double.toString(best_stop*2763.96);
		}
			
		best_station_lat_lon[0] = name;
		best_station_lat_lon[1] = Double.toString(latitude_min);
		best_station_lat_lon[2] = Double.toString(longitude_min);
		return best_station_lat_lon;
	}
}