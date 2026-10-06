import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

//this handles the game map for cities and routes, part of the background UML
public class GameMap {
	HashMap<City, ArrayList<EdgeRoute>> citiesRoutesMap; 
	ArrayList<City> TTRECities; 
	ArrayList<EdgeRoute> TTRERoutes;
	
	public GameMap() {
		citiesRoutesMap = new HashMap<City, ArrayList<EdgeRoute>>(); 
		TTRECities = new ArrayList<City>();  
		TTRERoutes = new ArrayList<EdgeRoute>();
	//	initializeGameMap();  this is called by GameState
		
		
		
		
	}
	
	//add cities and routes
	public void initializeGameMap() {
		City Amsterdam, London, Edinburgh, Angora, Athina, Barcelona, Berlin, Brest, Brindisi, Bruxelles, Budapest, Bucuresti, Cadiz, Constantinople, Kobenhavn;
		City Danzig, Dieppe, Erzurum, Essen, Frankfurt, Kharkov, Kyiv, Lisboa,Madrid, Marseille, Moskva, Munchen, Palermo, Pamplona, Paris;
		City Petrograd, Riga, Roma, Rostov, Sarajevo, Sevastopol, Smolensk, Smyrna, Sochi, Sofia, Stockholm, Venezia, Warszawa, Wilno, Zurich, Zagrab;
		City Wien;
		//Amsterdam = new City ("Amsterdam", 240,130);
		Amsterdam = new City ("Amsterdam", 430,210);
		TTRECities.add(Amsterdam);
		//London = new City ("London", 168, 128);
		London = new City ("London", 305, 210);
		TTRECities.add(London); 
		//Edinburgh = new City ("Edinburgh", 110, 55);
		Edinburgh = new City ("Edinburgh", 230, 60);
		TTRECities.add (Edinburgh);
		//Bruxelles = new City ("Bruxelles", 252, 160);
		Bruxelles = new City ("Bruxelles", 400, 265);
		TTRECities.add (Bruxelles);
		//Paris = new City ("Paris", 224, 210); 
		Paris = new City ("Paris", 360, 360);
		TTRECities.add (Paris);
		//Dieppe = new City ("Dieppe", 192, 210);
		Dieppe = new City ("Dieppe", 295, 300);
		TTRECities.add (Dieppe);
		//Brest = new City ("Brest", 138, 224);
		Brest = new City ("Brest", 180, 330);
		TTRECities.add (Brest);
		//Pamplona = new City ("Pamplona", 158, 310);
		Pamplona = new City ("Pamplona", 260, 500);// changed y
		TTRECities.add (Pamplona); 
		//Madrid = new City ("Madrid", 130, 390);
		Madrid = new City ("Madrid", 140, 590);
		TTRECities.add (Madrid); 
		//Cadiz = new City ("Cadiz", 128, 456);
		Cadiz = new City ("Cadiz", 140, 665);
		TTRECities.add (Cadiz);
		//Lisboa = new City ("Lisboa", 68, 408);
		Lisboa = new City ("Lisboa", 60, 630);
		TTRECities.add (Lisboa);
		//Barcelona = new City ("Barcelona", 190, 390);
		Barcelona = new City ("Barcelona", 280, 600);
		TTRECities.add (Barcelona);
		//Marseille = new City ("Marseille", 238, 335);
		Marseille = new City ("Marseille", 460, 500);
		TTRECities.add (Marseille);	
		//Zurich = new City ("Zurich", 295, 290);
		Zurich = new City ("Zurich", 500, 420);
		TTRECities.add(Zurich);
		//Frankfurt = new City ("Frankfurt", 300, 220); 
		Frankfurt = new City ("Frankfurt", 510, 310);
		TTRECities.add (Frankfurt);
		//Essen = new City ("Essen", 290, 150);
		Essen = new City ("Essen", 530, 230);
		TTRECities.add (Essen);
		//Berlin = new City ("Berlin", 365, 165);
		Berlin = new City ("Berlin", 650, 250);
		TTRECities.add (Berlin);
		//Munchen = new City ("Munchen", 340, 260);
		Munchen = new City ("Munchen", 580, 340);
		TTRECities.add (Munchen);
		//Venezia = new City ("Venezia", 350, 315);
		Venezia = new City ("Venezia", 595, 440);
		TTRECities.add (Venezia);
		//Roma = new City ("Roma", 340, 400);
		Roma = new City ("Roma", 600, 530);
		TTRECities.add (Roma);
		//Palermo = new City ("Palermo", 340, 460);
		Palermo = new City ("Palermo", 645, 670);
		TTRECities.add (Palermo);
		//Brindisi = new City ("Brindisi", 400, 410);
		Brindisi = new City ("Brindisi", 710, 555);
		TTRECities.add (Brindisi);
		//Sarajevo = new City ("Sarajevo", 400, 340);
		Sarajevo = new City ("Sarajevo", 800, 510);
		TTRECities.add (Sarajevo);
		//Zagreb = new City ("Zagreb", 370, 305);
		Zagrab = new City ("Zagrab", 700, 450);
		TTRECities.add (Zagrab);
		//Budapest = new City ("Budapest", 430, 280);
		Budapest = new City ("Budapest", 770, 385);
		TTRECities.add (Budapest);
		//Wien = new City ("Wien", 390, 250);
		Wien = new City ("Wien", 710, 360);
		TTRECities.add (Wien);
		//Warszawa = new City ("Warszawa", 470, 180);
		Warszawa = new City ("Warszawa", 840, 240);
		TTRECities.add (Warszawa);
		//Danzig = new City ("Danzig", 435, 145);
		Danzig = new City ("Danzig", 785, 175);
		TTRECities.add (Danzig);
		//Riga = new City ("Riga", 550, 95);
		Riga = new City ("Riga", 880, 80);
		TTRECities.add (Riga);
		//Wilno = new City ("Wilno", 510, 150);
		Wilno = new City ("Wilno", 980, 220);
		TTRECities.add (Wilno);
		//Smolensk = new City ("Smolensk", 600, 170);
		Smolensk = new City ("Smolensk", 1090, 220);
		TTRECities.add (Smolensk);
		//Petrograd = new City ("Petrograd", 675, 70);
		Petrograd = new City ("Petrograd", 1080, 80);
		TTRECities.add (Petrograd);
		//Moskva = new City ("Moskva", 680, 160);
		Moskva = new City ("Moskva", 1190, 200);
		TTRECities.add (Moskva); 
		//Kyiv = new City ("Kyiv", 600, 230);
		Kyiv = new City ("Kyiv", 1020, 290);
		TTRECities.add (Kyiv);
		//Kharkov = new City ("Kharkov", 675, 240);
		Kharkov = new City ("Kharkov", 1170, 345);
		TTRECities.add (Kharkov);
		//Rostov = new City ("Rostov", 730, 310);
		Rostov = new City ("Rostov", 1220, 400);
		TTRECities.add (Rostov);
		//Sochi = new City ("Sochi", 740, 365);
		Sochi = new City ("Sochi", 1200, 485);
		TTRECities.add (Sochi);
		//Sevastopol = new City ("Sevastopol", 670, 340);
		Sevastopol = new City ("Sevastopol", 1110, 470);
		TTRECities.add (Sevastopol);
		//Bucuresti = new City ("Bucuresti", 540, 330);
		Bucuresti = new City ("Bucuresti", 950, 455);
		TTRECities.add (Bucuresti);
		//Sofia = new City ("Sofia", 500, 370);
		Sofia = new City ("Sofia", 875, 520);
		TTRECities.add (Sofia);
		//Athina = new City ("Athina", 520, 450);
		Athina = new City ("Athina", 860, 650);
		TTRECities.add (Athina);
		//Erzurum = new City ("Erzurum", 750, 430);
		Erzurum = new City ("Erzurum", 1190, 610);
		TTRECities.add (Erzurum);
		//Stockholm = new City ("Stockholm", 470, 40);
		Stockholm = new City ("Stockholm", 740, 50);
		TTRECities.add (Stockholm);
		//Kobenhavn = new City ("Kobenhavn", 360, 90);
		Kobenhavn = new City ("Kobenhavn", 610, 125);
		TTRECities.add (Kobenhavn);
		//Constantinople = new City ("Constantinople", 600, 410);
		Constantinople = new City ("Constantinople", 1010, 580);
		TTRECities.add (Constantinople);
		//Smyrna = new City ("Smyrna", 650, 470);
		Smyrna = new City ("Smyrna", 960, 660);
		TTRECities.add (Smyrna);
		//Angora = new City ("Angora", 690,440);
		Angora = new City ("Angora", 1100,640);
		TTRECities.add (Angora);
		
			
		EdgeRoute routeEL = new EdgeRoute (Edinburgh, London, Color.DARK_GRAY, 4, false, false,  -1,0);
		EdgeRoute routeEL2 = new EdgeRoute (Edinburgh, London, new Color(255, 140, 0), 4, false, false,  1,0); //bright orange
		TTRERoutes.add(routeEL);
		TTRERoutes.add(routeEL2);

		
		EdgeRoute routeSK = new EdgeRoute(Stockholm, Kobenhavn,Color.YELLOW,3,false,false, -1,0); 
		TTRERoutes.add(routeSK);
		EdgeRoute routeSK2 = new EdgeRoute(Stockholm, Kobenhavn,Color.WHITE,3,false,false, 1,0);
		TTRERoutes.add(routeSK2);
		
		EdgeRoute routeBE = new EdgeRoute(Berlin, Essen,Color.BLUE,2,false,false, 0,0);
		TTRERoutes.add(routeBE);
		
		EdgeRoute lisboaToCadiz = new EdgeRoute(Lisboa, Cadiz, Color.BLUE, 2, false, false,  0, new Point(70, 710),0);
		TTRERoutes.add(lisboaToCadiz);	
				
		EdgeRoute routeML = new EdgeRoute (Madrid,Lisboa,Color.MAGENTA, 3,false, false, 0, new Point(70, 530),0);
		TTRERoutes.add(routeML);
		
		EdgeRoute routePaMa = new EdgeRoute(Paris, Marseille, Color.LIGHT_GRAY, 4, false, false, 0,new Point(380,440),0);
		TTRERoutes.add(routePaMa);
		
		EdgeRoute routePaPa = new EdgeRoute(Pamplona,Paris,Color.BLUE, 4, false, false,  1, new Point (340,450),0);
		TTRERoutes.add(routePaPa);
		
		EdgeRoute routePaPa1 = new EdgeRoute(Pamplona,Paris,Color.GREEN, 4, false, false, -1, new Point (340,450),0);
		TTRERoutes.add(routePaPa1);
		
		EdgeRoute routeVR = new EdgeRoute (Venezia, Roma, Color.DARK_GRAY, 2, false, false,  0,0);
		TTRERoutes.add(routeVR);
		
		EdgeRoute routeZM = new EdgeRoute (Zurich, Munchen, Color.YELLOW, 2, true, false,  0,0);
		TTRERoutes.add(routeZM);
		
		EdgeRoute routeZV = new EdgeRoute (Zurich, Venezia, Color.GREEN, 2, true, false,  0,0);
		TTRERoutes.add(routeZV);
		
		EdgeRoute routeMC = new EdgeRoute (Madrid,Cadiz,new Color(255, 140, 0), 3,false, false, 0, new Point (250,680),0); //bright orange
		TTRERoutes.add(routeMC);
		
		EdgeRoute routeMP = new EdgeRoute (Madrid,Pamplona,Color.DARK_GRAY, 3,true, false, 1, new Point(180,550),0); //right side
		TTRERoutes.add(routeMP);
		
		EdgeRoute routeMP2 = new EdgeRoute (Madrid,Pamplona,Color.WHITE, 3,true, false,  -1, new Point(180,550),0); 
		TTRERoutes.add(routeMP2);
		
		EdgeRoute routeMB= new EdgeRoute (Madrid,Barcelona,Color.YELLOW, 2,false, false,  0,0); //bright orange
		TTRERoutes.add(routeMB);

		EdgeRoute routeMaPa= new EdgeRoute (Marseille,Pamplona,Color.RED, 4,false, false,  0, new Point(360,520),0); 
		TTRERoutes.add(routeMaPa);
		
		
		EdgeRoute routePaBa= new EdgeRoute (Pamplona,Barcelona,Color.LIGHT_GRAY, 2,true, false,  0,0); 
		TTRERoutes.add(routePaBa);
		
		EdgeRoute routeBaMa= new EdgeRoute (Barcelona,Marseille,Color.LIGHT_GRAY, 4,false, false,  0,0); 
		TTRERoutes.add(routeBaMa);

		EdgeRoute routePaBr= new EdgeRoute (Pamplona,Brest,Color.MAGENTA, 4,false, false,  0,new Point(300,400),0); 
		TTRERoutes.add(routePaBr);
		
		EdgeRoute routeBrDi= new EdgeRoute (Brest,Dieppe,new Color(255, 140, 0), 2,false, false,  0,new Point(240,300),0); 
		TTRERoutes.add(routeBrDi);
		EdgeRoute routeBrPa= new EdgeRoute (Brest,Paris,Color.DARK_GRAY,3,false, false, 0,0); 
		TTRERoutes.add(routeBrPa);

		EdgeRoute routeDiLo= new EdgeRoute (Dieppe,London,Color.LIGHT_GRAY,2,false, true,  -1,1); 
		TTRERoutes.add(routeDiLo);
		EdgeRoute routeDiLo2= new EdgeRoute (Dieppe,London,Color.LIGHT_GRAY,2,false, true, 1,1); 
		TTRERoutes.add(routeDiLo2);
		
		EdgeRoute routeDiBr= new EdgeRoute (Dieppe,Bruxelles,Color.GREEN,2,false, false, 0,0); 
		TTRERoutes.add(routeDiBr);
		
		EdgeRoute routeDiPa= new EdgeRoute (Dieppe,Paris,Color.MAGENTA,1,false, false, 0,0); 
		TTRERoutes.add(routeDiPa);
		
		EdgeRoute routePaZu= new EdgeRoute (Paris,Zurich,Color.LIGHT_GRAY,3,true, false, 0, new Point(420, 420),0); 
		TTRERoutes.add(routePaZu);
	
		EdgeRoute routePaBru= new EdgeRoute (Paris,Bruxelles,Color.YELLOW,2,false, false, 1, 0); 
		TTRERoutes.add(routePaBru);
		EdgeRoute routePaBru2= new EdgeRoute (Paris,Bruxelles,Color.RED,2,false, false, -1, 0); 
		TTRERoutes.add(routePaBru2);

		EdgeRoute routePaFr= new EdgeRoute (Paris,Frankfurt,Color.YELLOW,3,false, false, 1, 0); 
		TTRERoutes.add(routePaFr);
		EdgeRoute routePaFr2= new EdgeRoute (Paris,Frankfurt,Color.WHITE,3,false, false, -1, 0); 
		TTRERoutes.add(routePaFr2);

		
		
		EdgeRoute routeLoAm= new EdgeRoute (Amsterdam,London,Color.LIGHT_GRAY,2,false, true, 0, 2); 
		TTRERoutes.add(routeLoAm);
		EdgeRoute routeAmBr= new EdgeRoute (Amsterdam,Bruxelles,Color.DARK_GRAY,1,false, false, 0, 0); 
		TTRERoutes.add(routeAmBr);
		EdgeRoute routeAmEs= new EdgeRoute (Amsterdam,Essen,Color.YELLOW,3,false, false, 0,new Point(440,160),0); 
		TTRERoutes.add(routeAmEs);
		EdgeRoute routeBrFr= new EdgeRoute (Bruxelles,Frankfurt,Color.BLUE,2,false, false, 0, 0); 
		TTRERoutes.add(routeBrFr);
		EdgeRoute routeEsKo= new EdgeRoute (Kobenhavn, Essen,Color.LIGHT_GRAY,3,false, true, 1, 1); 
		TTRERoutes.add(routeEsKo);
		EdgeRoute routeEsKo2= new EdgeRoute (Kobenhavn,Essen,Color.LIGHT_GRAY,3,false, true, -1, 1); 
		TTRERoutes.add(routeEsKo2);

		EdgeRoute routeStPe= new EdgeRoute (Stockholm,Petrograd,Color.LIGHT_GRAY,8,true, false, 0, new Point(900,20),0); 
		TTRERoutes.add(routeStPe);
		EdgeRoute routeAmFr= new EdgeRoute (Amsterdam,Frankfurt,Color.WHITE,2,false, false, 0, 0); 
		TTRERoutes.add(routeAmFr);

		EdgeRoute routeEsFr= new EdgeRoute (Essen,Frankfurt,Color.GREEN,2,false, false, 0, 0); 
		TTRERoutes.add(routeEsFr);

		EdgeRoute routeFrMu= new EdgeRoute (Frankfurt,Munchen,Color.MAGENTA,2,false, false, 0, new Point(520,380),0); 
		TTRERoutes.add(routeFrMu);

		EdgeRoute routeZuMa= new EdgeRoute (Zurich, Marseille,Color.MAGENTA,2,true, false, 0, 0); 
		TTRERoutes.add(routeZuMa);
		EdgeRoute routeMuWi= new EdgeRoute (Munchen, Wien,new Color(255, 140, 0),3,false, false, 0, new Point(650,420),0); 
		TTRERoutes.add(routeMuWi);
		EdgeRoute routeMuVe= new EdgeRoute (Munchen, Venezia,Color.BLUE,2,true, false, 0, 0); 
		TTRERoutes.add(routeMuVe);
		EdgeRoute routeMaRo= new EdgeRoute (Marseille, Roma,Color.LIGHT_GRAY,4,true, false, 0, new Point (550, 450), 0); 
		TTRERoutes.add(routeMaRo);
		EdgeRoute routeRoPa= new EdgeRoute (Roma, Palermo,Color.LIGHT_GRAY,4,false,true, 0, new Point (720, 600), 1); 
		TTRERoutes.add(routeRoPa);
		EdgeRoute routePaBrin= new EdgeRoute (Brindisi,Palermo,Color.LIGHT_GRAY,3,false,true, 0, new Point (720, 640), 1); 
		TTRERoutes.add(routePaBrin);
		EdgeRoute routeBrRo= new EdgeRoute (Brindisi,Roma,Color.WHITE,2,false,false, 0, new Point (670, 500), 0); 
		TTRERoutes.add(routeBrRo);
		EdgeRoute routePeRi= new EdgeRoute (Petrograd,Riga,Color.LIGHT_GRAY,4,false,false, 0, 0); 
		TTRERoutes.add(routePeRi);
		EdgeRoute routeRiDa= new EdgeRoute (Riga,Danzig,Color.DARK_GRAY,3,false,false, 0, new Point (800,100),0); 
		TTRERoutes.add(routeRiDa);
		EdgeRoute routeRiWi = new EdgeRoute(Riga, Wilno, Color.GREEN, 4, false, false, 0, new Point(850,160),0);
		TTRERoutes.add(routeRiWi);
		EdgeRoute routeDaBe = new EdgeRoute(Danzig, Berlin, Color.LIGHT_GRAY, 4, false, false, 0, new Point(660,140),0);
		TTRERoutes.add(routeDaBe);
		EdgeRoute routeDaWa = new EdgeRoute(Danzig, Warszawa, Color.LIGHT_GRAY, 2, false, false, 0,new Point(830,200), 0);
		TTRERoutes.add(routeDaWa);
		EdgeRoute routePeMo = new EdgeRoute(Petrograd, Moskva, Color.WHITE, 4, false, false, 0, new Point(1160,100),0);
		TTRERoutes.add(routePeMo);
		EdgeRoute routePeWi = new EdgeRoute(Petrograd, Wilno, Color.BLUE, 4, false, false, 0, 0);
		TTRERoutes.add(routePeWi);
		EdgeRoute routeWiWa = new EdgeRoute(Wilno, Warszawa, Color.RED, 3, false, false, 0, new Point(880,190), 0);
		TTRERoutes.add(routeWiWa);
		EdgeRoute routeWiSm = new EdgeRoute(Wilno, Smolensk, Color.YELLOW, 3, false, false, 0, new Point(1020,190),0);
		TTRERoutes.add(routeWiSm);
		EdgeRoute routeBeWi = new EdgeRoute(Berlin, Wien, Color.GREEN, 3, false, false, 0, 0);
		TTRERoutes.add(routeBeWi);
		EdgeRoute routeWaWi = new EdgeRoute(Warszawa, Wien, Color.BLUE, 4, false, false, 0, 0);
		TTRERoutes.add(routeWaWi);	
		EdgeRoute routeBeFr = new EdgeRoute(Berlin, Frankfurt, Color.DARK_GRAY, 3, false, false, -1, 0);
		TTRERoutes.add(routeBeFr);
		EdgeRoute routeBeFr2 = new EdgeRoute(Berlin, Frankfurt, Color.RED, 3, false, false, 1, 0);
		TTRERoutes.add(routeBeFr2);
		EdgeRoute routeBeWa = new EdgeRoute(Berlin, Warszawa, Color.MAGENTA, 4, false, false, 1, 0);
		TTRERoutes.add(routeBeWa);
		EdgeRoute routeBeWa2 = new EdgeRoute(Berlin, Warszawa, Color.YELLOW, 4, false, false, -1, 0);
		TTRERoutes.add(routeBeWa2);
		EdgeRoute routeWiKy = new EdgeRoute(Wilno, Kyiv, Color.LIGHT_GRAY, 2, false, false, 0, 0);
		TTRERoutes.add(routeWiKy);
		EdgeRoute routeWaKy = new EdgeRoute(Warszawa, Kyiv, Color.LIGHT_GRAY, 4, false, false, 0, 0);
		TTRERoutes.add(routeWaKy);
		EdgeRoute routeKyBu = new EdgeRoute(Kyiv, Budapest, Color.LIGHT_GRAY, 6, true, false, 0, new Point(860,300),0);
		TTRERoutes.add(routeKyBu);
		EdgeRoute routeKySm = new EdgeRoute(Kyiv, Smolensk, Color.RED, 3, false, false, 0, new Point(1100,300),0);
		TTRERoutes.add(routeKySm);
		EdgeRoute routeSmMo = new EdgeRoute(Smolensk, Moskva, new Color(255, 140, 0), 2, false, false, 0, 0);
		TTRERoutes.add(routeSmMo);
		EdgeRoute routeKyBuc = new EdgeRoute(Kyiv, Bucuresti, Color.LIGHT_GRAY, 4, false, false, 0, 0);
		TTRERoutes.add(routeKyBuc);
		EdgeRoute routeKyKh = new EdgeRoute(Kyiv, Kharkov, Color.LIGHT_GRAY, 4, false, false, 0,new Point(1100,400),0);
		TTRERoutes.add(routeKyKh);
		EdgeRoute routeMoKh = new EdgeRoute(Moskva, Kharkov, Color.LIGHT_GRAY, 4, false, false, 0, new Point(1240,280),0);
		TTRERoutes.add(routeMoKh);
		EdgeRoute routeKhRo = new EdgeRoute(Kharkov, Rostov, Color.GREEN, 2, false, false, 0, new Point(1250,360),0);
		TTRERoutes.add(routeKhRo);
		EdgeRoute routeWiBu = new EdgeRoute(Wien, Budapest, Color.RED, 1, false, false, 1, 0);
		TTRERoutes.add(routeWiBu);
		
		EdgeRoute routeZaBu = new EdgeRoute(Zagrab, Budapest, new Color(255, 140, 0), 2, false, false, 0, 0);
		TTRERoutes.add(routeZaBu);
		
		EdgeRoute routeWiBu2 = new EdgeRoute(Wien, Budapest, Color.WHITE, 1, false, false, -1, 0);
		TTRERoutes.add(routeWiBu2);
		EdgeRoute routeWiZa = new EdgeRoute(Wien, Zagrab, Color.LIGHT_GRAY, 2, false, false, 0, 0);
		TTRERoutes.add(routeWiZa);
		EdgeRoute routeZaVe = new EdgeRoute(Zagrab, Venezia, Color.LIGHT_GRAY, 2, false, false, 0, 0);
		TTRERoutes.add(routeZaVe);
		EdgeRoute routeZaSa = new EdgeRoute(Zagrab, Sarajevo, Color.RED, 3, false, false, 0, new Point(720,560),0);
		TTRERoutes.add(routeZaSa);
		EdgeRoute routeSaSo = new EdgeRoute(Sarajevo, Sofia, Color.LIGHT_GRAY, 2, true, false, 0, new Point(860,500), 0);
		TTRERoutes.add(routeSaSo);
		EdgeRoute routeBuSa = new EdgeRoute(Budapest, Sarajevo, Color.MAGENTA, 3, false, false, 0, 0);
		TTRERoutes.add(routeBuSa);
		EdgeRoute routeBrAt = new EdgeRoute(Athina, Brindisi, Color.LIGHT_GRAY, 4, false, true, 0, new Point(760,650),1);
		TTRERoutes.add(routeBrAt);
		EdgeRoute routeSoAt = new EdgeRoute(Sofia, Athina, Color.MAGENTA, 3, false, false, 0, new Point(820,580), 0);
		TTRERoutes.add(routeSoAt);
		EdgeRoute routeSaAt = new EdgeRoute(Sarajevo, Athina, Color.GREEN, 4, false, false, 0, new Point(750,640),0);
		TTRERoutes.add(routeSaAt);
		EdgeRoute routePaSm = new EdgeRoute(Palermo, Smyrna, Color.LIGHT_GRAY, 6, false, true, 0, new Point(800, 680), 2);
		TTRERoutes.add(routePaSm);
		EdgeRoute routeAtSm = new EdgeRoute(Athina, Smyrna, Color.LIGHT_GRAY, 2, false, true, 0, new Point(920,620),1);
		TTRERoutes.add(routeAtSm);
		EdgeRoute routeCoAn = new EdgeRoute(Constantinople, Angora, Color.LIGHT_GRAY, 2, true, false, 0, 0);
		TTRERoutes.add(routeCoAn);
		EdgeRoute routeCoSm = new EdgeRoute(Constantinople, Smyrna, Color.LIGHT_GRAY, 2, true, false, 0, 0);
		TTRERoutes.add(routeCoSm);
		EdgeRoute routeBuCo = new EdgeRoute(Bucuresti, Constantinople, Color.YELLOW, 3, true, false, 0, 0);
		TTRERoutes.add(routeBuCo);
		EdgeRoute routeSmAn = new EdgeRoute(Smyrna, Angora, new Color(255, 140, 0), 3, true, false, 0, 0);
		TTRERoutes.add(routeSmAn);
		EdgeRoute routeAnEr = new EdgeRoute(Angora, Erzurum, Color.DARK_GRAY, 3, false, false, 0, new Point(1200,700),0);
		TTRERoutes.add(routeAnEr);
		EdgeRoute routeSoCo = new EdgeRoute(Sofia, Constantinople, Color.BLUE, 3, false, false, 0, 0);
		TTRERoutes.add(routeSoCo);
		EdgeRoute routeCoSe = new EdgeRoute(Constantinople, Sevastopol, Color.LIGHT_GRAY, 4, false, true, 0, 2);
		TTRERoutes.add(routeCoSe);
		EdgeRoute routeSeSo = new EdgeRoute(Sevastopol, Sochi, Color.LIGHT_GRAY, 2, false, true, 0, 1);
		TTRERoutes.add(routeSeSo);
		EdgeRoute routeSoEr = new EdgeRoute(Sochi, Erzurum, Color.RED, 3, true, false, 0, 0);
		TTRERoutes.add(routeSoEr);
		EdgeRoute routeRoSe = new EdgeRoute(Rostov, Sevastopol, Color.LIGHT_GRAY, 3, false, false, 0, new Point(1120,350),0);
		TTRERoutes.add(routeRoSe);
		EdgeRoute routeRoSo = new EdgeRoute(Rostov, Sochi, Color.LIGHT_GRAY, 2, false, false, 0, 0);
		TTRERoutes.add(routeRoSo);
		EdgeRoute routeBaSe = new EdgeRoute(Bucuresti, Sevastopol, Color.WHITE, 4, false, false, 0, new Point(1050,400),0);
		TTRERoutes.add(routeBaSe);
		EdgeRoute routeSeEz = new EdgeRoute(Erzurum, Sevastopol, Color.LIGHT_GRAY, 4, false, true, 0, new Point(1100, 550),2);
		TTRERoutes.add(routeSeEz);
		EdgeRoute routeBuBu = new EdgeRoute(Bucuresti, Budapest, Color.LIGHT_GRAY, 4, true, false, 0, 0);
		TTRERoutes.add(routeBuBu);
		EdgeRoute routeBuSo = new EdgeRoute(Bucuresti, Sofia, Color.LIGHT_GRAY, 2, true, false, 0, new Point(940,520), 0);
		TTRERoutes.add(routeBuSo);


		
		populateAdjList();
		

	}
	
	
	public void findRoute (City startCity, City endCity) {
		//need to implement how to find the route with the cities
		
	}
	
	public void addToMap (City cityName, EdgeRoute routeName) {
		//need to implement how to add a new city and route
		
	}

	public ArrayList<City> getAllCities() {
		return TTRECities;
	}
	
	public ArrayList<EdgeRoute> getAllRoutes() {
		return TTRERoutes; 
	}
	
	
	public City findCityByName (String cityName) {
	     for (int i = 0; i< TTRECities.size(); i++) {
            if (TTRECities.get(i).getCityName().equals(cityName)) {
            	return TTRECities.get(i); 
            }
           
        }
       return null; 
	} // end of findcity Method
	
	//to populate cityRoutesMap based on Cities and Routes
	public void populateAdjList() {
		for (EdgeRoute route : TTRERoutes) {
			citiesRoutesMap
		        .computeIfAbsent(route.getStart(), k -> new ArrayList<>())
		        .add(route);
			citiesRoutesMap
		        .computeIfAbsent(route.getDestination(), k -> new ArrayList<>())
		        .add(route);
		}
		
		//print out of adjacency list
		for (Map.Entry<City, ArrayList<EdgeRoute>> entry : citiesRoutesMap.entrySet()) {
		    City city = entry.getKey();
		    ArrayList<EdgeRoute> routes = entry.getValue();
//		    System.out.println("City: " + city.getCityName());
		    for (EdgeRoute route : routes) {
		        // determine the "other" city in the route
		        City otherCity = route.getStart().equals(city) ? route.getDestination() : route.getStart();
	//	        System.out.println("  --> connects to: " + otherCity.getCityName() +
	//	                           " | Length: " + route.getLength() +
	//	                           " | Color: " + route.getColor());
		    }
		} // end of print out loop
		
	}//end of populateAdjList method
	
}  // end of GameMap class
