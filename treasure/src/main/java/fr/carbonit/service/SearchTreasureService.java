package fr.carbonit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import fr.carbonit.models.Adventurer;
import fr.carbonit.models.Carte;
import fr.carbonit.models.Mountain;
import fr.carbonit.models.Movement;
import fr.carbonit.models.Treasure;
import fr.carbonit.utils.Utils;

public class SearchTreasureService {
	
	Carte carte;
	List<Object> mountain;
	List<Object> treasure;
	List<Object> adventurer;
	String filePath;
	boolean skipScanner;

	public SearchTreasureService(Carte carte, List<Object> mountain, List<Object> treasure, List<Object> adventurer, String filePath) {
		super();
		this.carte = carte;
		this.mountain = mountain;
		this.treasure = treasure;
		this.adventurer = adventurer;
		this.filePath = filePath;
		this.skipScanner = false;
	}
	
	public void start() throws ExecutionException {
		String[][] maCarte = Utils.buildMap(carte);
		
		System.out.println("\nPrésentation de la carte "+carte.getX() +"x"+carte.getY());
		Utils.printCarte(maCarte);
		
		
		List<Mountain> mount = mountain.stream().map(elem->(Mountain)elem).collect(Collectors.toList());
		List<Treasure> treas = treasure.stream().map(elem->(Treasure)elem).collect(Collectors.toList());
		List<Adventurer> advent = adventurer.stream().map(elem->(Adventurer)elem).collect(Collectors.toList());
		List<Adventurer> newAdvent = new ArrayList<Adventurer>();
		Map<Integer,Integer> listOfBusyXY = new HashMap<Integer,Integer>();
		
		
		System.out.println("\nProcédons à l'ajout des éléments suivants sur la carte : \n");
		mount.forEach(s->System.out.println(s));
		treas.forEach(s->System.out.println(s));
		advent.forEach(s->System.out.println(s));
		
		
		if(!this.skipScanner) {
			try (Scanner sc = new Scanner(System.in)) {
				System.out.println("\nveuillez taper sur la touche 'Y' pour continuer ou 'N' pour quitter.");
				String response = sc.nextLine();
				if("N".equals(response.toUpperCase())) {
					System.out.println("EXIT...");
					System.exit(0);
				}
			}
		}
		
		
		maCarte = Utils.placeElements(maCarte, mount, treas, advent);
		Utils.printCarte(maCarte);
		
		System.out.println("\n*********** DEBUT DES ACTIONS *********** ");
		
		boolean canContinue = true;
		for(Adventurer ad : advent) {

			for(Movement m : ad.getMovement()) {
				if(!canContinue) {
					break;
				}
				System.out.println("\nNext Action => "+m);
				if(Movement.FORWARD.equals(m)) {
					canContinue = Utils.moveForward(ad,maCarte,m,treas,listOfBusyXY);
					System.out.println(ad.toString());
				}
				if(Movement.LEFT.equals(m)) {
					Utils.turnLeft(ad);
					System.out.println(ad.toString());
				}
				if(Movement.RIGHT.equals(m)) {
					Utils.turnRight(ad);
					System.out.println(ad.toString());
				}
			}
			newAdvent.add(ad);
			listOfBusyXY.put(ad.getPosX(), ad.getPosY());
		}
		
		System.out.println("\nFIN DE SIMULATION. NOUS OBTENONS LE TABLEAU SUIVANT: \n");
		maCarte = Utils.placeElements(Utils.buildMap(carte), mount, treas, newAdvent);
		Utils.printCarte(maCarte);
		
		
		Utils.writeOutputFile(maCarte, mount, treas, advent, filePath);
			
	}

	public void setSkipScanner(boolean skipScanner) {
		this.skipScanner = skipScanner;
	}

	
	
	
	
}
