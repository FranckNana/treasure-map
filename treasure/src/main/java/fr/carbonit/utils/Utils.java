package fr.carbonit.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;

import fr.carbonit.models.Adventurer;
import fr.carbonit.models.Carte;
import fr.carbonit.models.Mountain;
import fr.carbonit.models.Movement;
import fr.carbonit.models.Orientation;
import fr.carbonit.models.Treasure;

public class Utils {
	
	public static String FILE_PATH;
	
	//public static String FILE_DIR;
	
	/***
	 * Lecture du fichier d'entrée contenant le jeu de données pour la simulation
	 * 
	 * @return Map<String,List<Object>>
	 * @throws Exception
	 */
	public static Map<String,List<Object>> readFileAndGetInformations() throws Exception {
		
		List<Object> listOfMountain = new ArrayList<Object>();
		List<Object> listOfTreasure = new ArrayList<Object>();
		List<Object> listOfAdventurer = new ArrayList<Object>();
		Map<String,List<Object>> myMapInfo = new HashMap<String, List<Object>>();
		
		JFileChooser dialogue;
		File file;
				
		if(Objects.isNull(FILE_PATH)) {
			
			dialogue = new JFileChooser(new File("."));
			
			if (dialogue.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				System.out.println("EXIT...");
				System.exit(0);
			}
			
			file = dialogue.getSelectedFile();
		}else {
			file = new File(FILE_PATH);
		}

		myMapInfo.put("filePath", List.of(file.getParent()));
		System.out.println("Mon fichier : "+file.getPath());
		
		try (FileReader fr = new FileReader(file);
			 BufferedReader br = new BufferedReader(fr);){
			String line;
			
			while ((line = br.readLine()) != null) {
			   if(line.startsWith("C")) {
				   String[] split = line.replaceAll("\\s", "").split("-");
				   myMapInfo.put("C", List.of(new Carte(Integer.parseInt(split[1]), Integer.parseInt(split[2]))));
			   }
			   if(line.startsWith("M")) {
				   String[] split = line.replaceAll("\\s", "").split("-");
				   listOfMountain.add(new Mountain(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
			   }
			   if(line.startsWith("T")) {
				   String[] split = line.replaceAll("\\s", "").split("-");
				   listOfTreasure.add(new Treasure(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
			   }
			   if(line.startsWith("A")) {
				   String[] split = line.replaceAll("\\s", "").split("-");
				   listOfAdventurer.add(new Adventurer(split[1], Integer.parseInt(split[2]), 
						   Integer.parseInt(split[3]), Orientation.forValues(split[4]), getMovements(split[5]),0));
			   }
			}

		} catch (Exception e) {
			throw new Exception(Constantes.EXCEPTION_FILE_READ);
		}
		
		myMapInfo.put("M", listOfMountain);
		myMapInfo.put("T", listOfTreasure);
		myMapInfo.put("A", listOfAdventurer);
		
		return myMapInfo;

	}
	
	
	
	/***
	 * Obtenir  le mouvement associé à la string passée en paramètre
	 * 
	 * @param string movement example A, G, D
	 * @return List<Movement>
	 */
	public static List<Movement> getMovements(String str) {
		List<Movement> movements = new ArrayList<Movement>();
		
		for(int i=0; i<str.length();i++) {
			movements.add(Movement.forValues(Character.toString(str.charAt(i))));
		}
		return movements;
	}
	
	
	
	/***
	 * Constuire la carte vide en fonction des coordonnées X, Y
	 * 
	 * @param carte
	 * @return String[][]
	 */
	public static String[][] buildMap(Carte carte) {
		String[][] c = new String[carte.getY()][carte.getX()];
		
		for(int i=0;i<c.length; i++) {
			
			for(int j=0; j<c[i].length; j++) {
				c[i][j]="* ";
			}
		}
		
		return c;
	}
	
	
	
	/***
	 * ajouter les éléments sur la carte comme les Montagnes, les trésors et les aventuriers
	 * 
	 * @param carte
	 * @param mountain
	 * @param treasure
	 * @param adventurer
	 * @return String[][] 
	 */
	public static String[][] placeElements(String[][] carte, List<Mountain> mountain, List<Treasure> treasure, List<Adventurer> adventurer) {
		
		for(Mountain mount : mountain) {
			carte[mount.getPosY()][mount.getPosX()]="M";
		}
		
		for(Treasure treas : treasure) {
			carte[treas.getPosY()][treas.getPosX()]="T"+"("+treas.getNbTreas()+")";
		}
		
		for(Adventurer advent : adventurer) {
			carte[advent.getPosY()][advent.getPosX()]="A"+"("+advent.getName()+")";
		}
		
		return carte;
	}
	
	
	
	/***
	 * afficher la carte sur la console
	 * 
	 * @param carte
	 */
	public static void printCarte(String[][] carte) {
		System.out.println();
		for(String[] row : carte) {
			
			for(String element : row) {
				System.out.print(element + "\t");
			}
			System.out.println();
		}
	}
	
	
	
	/***
	 * Simuler les mouvements des aventuriers en fonction de leurs orientations et 
	 * des léments sur la carte
	 * 
	 * @param advent
	 * @param maCarte
	 * @param m
	 * @param treasure
	 * @param listOfBusyXY
	 * @return boolean
	 */
	public static boolean moveForward(Adventurer advent, String[][] maCarte, Movement m, List<Treasure> treasure, Map<Integer,Integer> listOfBusyXY) {
		
		int x = maCarte[0].length;
		int y = maCarte.length;
			
		if(Orientation.SOUTH.equals(advent.getOrientation()) && Movement.FORWARD.equals(m)) {
			int posY = (advent.getPosY()<y && (maCarte[advent.getPosY()+1][advent.getPosX()].contains("*") || maCarte[advent.getPosY()+1][advent.getPosX()].contains("T"))) 
					    ? advent.getPosY()+1 : advent.getPosY();
			
			if(posY == advent.getPosY() || !isFreeXY(listOfBusyXY, advent.getPosX(), posY)) {
				return false;
			}
			advent.setPosY(posY);
			
			if(!maCarte[advent.getPosY()][advent.getPosX()].contains("*") && !maCarte[advent.getPosY()][advent.getPosX()].contains("M")) {
				decrementNbTreausure(advent.getPosX(),advent.getPosY(),treasure);
				advent.setNbTreasure(advent.getNbTreasure()+1);
			}
			
		}
		if(Orientation.NORTH.equals(advent.getOrientation()) && Movement.FORWARD.equals(m)) {
			int posY = (advent.getPosY()>0 && (maCarte[advent.getPosY()-1][advent.getPosX()].contains("*") || maCarte[advent.getPosY()-1][advent.getPosX()].contains("T"))) 
					? advent.getPosY()-1 : advent.getPosY();
			
			if(posY == advent.getPosY() || !isFreeXY(listOfBusyXY, advent.getPosX(), posY)) {
				return false;
			}
			
			advent.setPosY(posY);
			
			if(!maCarte[advent.getPosY()][advent.getPosX()].contains("*") && !maCarte[advent.getPosY()][advent.getPosX()].contains("M")) {
				decrementNbTreausure(advent.getPosX(),advent.getPosY(),treasure);
				advent.setNbTreasure(advent.getNbTreasure()+1);
			}
			
		}
		if(Orientation.EAST.equals(advent.getOrientation()) && Movement.FORWARD.equals(m)) {
			int posX = (advent.getPosX()<x && (maCarte[advent.getPosY()][advent.getPosX()+1].contains("*") || maCarte[advent.getPosY()][advent.getPosX()+1].contains("T"))) 
					? advent.getPosX()+1 : advent.getPosX();
			
			if(posX == advent.getPosX() || !isFreeXY(listOfBusyXY, posX, advent.getPosY())) {
				return false;
			}
			
			advent.setPosX(posX);
			
			if(!maCarte[advent.getPosY()][advent.getPosX()].contains("*") && !maCarte[advent.getPosY()][advent.getPosX()].contains("M")) {
				decrementNbTreausure(advent.getPosX(),advent.getPosY(),treasure);
				advent.setNbTreasure(advent.getNbTreasure()+1);
			}
			
		}
		if(Orientation.WEST.equals(advent.getOrientation()) && Movement.FORWARD.equals(m)) {
			int posX = (advent.getPosX()>0 && (maCarte[advent.getPosY()][advent.getPosX()-1].contains("*") || maCarte[advent.getPosY()][advent.getPosX()-1].contains("T"))) 
					? advent.getPosX()-1 : advent.getPosX();
			
			if(posX == advent.getPosX() || !isFreeXY(listOfBusyXY, posX, advent.getPosY())) {
				return false;
			}
			
			advent.setPosX(posX);
			
			if(!maCarte[advent.getPosY()][advent.getPosX()].contains("*") && !maCarte[advent.getPosY()][advent.getPosX()].contains("M")) {
				decrementNbTreausure(advent.getPosX(),advent.getPosY(),treasure);
				advent.setNbTreasure(advent.getNbTreasure()+1);
			}
		}
		return true;
				
	}
	
	
	/***
	 * vérifie si une case n'est pas occupée par un aventurier
	 * 
	 * @param listOfBusyXY
	 * @param posX
	 * @param posY
	 * @return boolean
	 */
	public static boolean isFreeXY(Map<Integer,Integer> listOfBusyXY, int posX, int posY) {
		for (Map.Entry<Integer, Integer> entry : listOfBusyXY.entrySet()) {
            if (entry.getKey()==posX && entry.getValue() == posY) {
               return false;
            }
        }
		return true;
	}
	
	
	/***
	 * décrémenter le nombre de trésors en cas de prise par un aventurier
	 * 
	 * @param x
	 * @param y
	 * @param treasures
	 */
	public static void decrementNbTreausure(int x, int y, List<Treasure> treasures) {
		for(Treasure treas : treasures ) {
			if(treas.getPosX() == x && treas.getPosY() == y) {
				treas.setNbTreas(treas.getNbTreas()-1);
			}
		}
	
	}
	
	
	/***
	 * Simuler un mouvement tourner à gauche en fonction de l'orientation
	 * 
	 * @param advent
	 * @return
	 */
	public static boolean turnLeft(Adventurer advent) {

		if(Orientation.SOUTH.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.EAST);
			return true;
		}
		if(Orientation.NORTH.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.WEST);
			return true;
		}
		if(Orientation.EAST.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.NORTH);
			return true;
		}
		if(Orientation.WEST.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.SOUTH);
			return true;
		}
		return false;
	
	}
	
	
	/***
	 * Simuler un mouvement tourner à droite en fonction de l'orientation
	 * 
	 * @param advent
	 * @return
	 */
	public static boolean turnRight(Adventurer advent) {

		if(Orientation.SOUTH.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.WEST);
			return true;
		}
		if(Orientation.NORTH.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.EAST);
			return true;
		}
		if(Orientation.EAST.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.SOUTH);
			return true;
		}
		if(Orientation.WEST.equals(advent.getOrientation())) {
			advent.setOrientation(Orientation.NORTH);
			return true;
		}
		return false;
	
	}
	
	
	/***
	 * produire le fichier de sortie selon un format spécifique
	 * 
	 * @param maCarte
	 * @param mountains
	 * @param treasures
	 * @param advent
	 * @throws ExecutionException
	 */
	public static void writeOutputFile(String[][] maCarte, List<Mountain> mountains, List<Treasure> treasures, List<Adventurer> advent, String pathDir) throws ExecutionException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String formatedDate = ts.toLocalDateTime().format(formatter);
		String filePath = pathDir.replace("\\", "/").concat("/result_"+formatedDate+".txt");
		String x = Integer.toString(maCarte[0].length);
		String y = Integer.toString(maCarte.length);
		
		File outputFile = new File(filePath);

		try(FileWriter fw = new FileWriter(outputFile);
			BufferedWriter writer = new BufferedWriter(fw);) {
			writer.append("C - ").append(x).append(" - ").append(y);
			
			for(Mountain m : mountains) {
				writer.append("\nM - ").append(Integer.toString(m.getPosX())).append(" - ").append(Integer.toString(m.getPosY()));
			}
			
			writer.append("\n# {T comme Trésors} - ").append("{Axe horizontal} - ").append("{Axe vertical} - ").append("{Nb. trésors restants}");
			
			for(Treasure t : treasures) {	
				writer.append("\nT - ").append(Integer.toString(t.getPosX())).append(" - ").append(Integer.toString(t.getPosY())).append(" - ").append(Integer.toString(t.getNbTreas()));
			}
			
			writer.append("\n# {A comme Aventurier} - ").append("{Nom de l'aventurier} - ").append("{Axe horizontal} - ").append("{Axe vertical} - ")
				  .append("{Orientation} - ").append("{Nb. trésors ramassé}");
			
			for(Adventurer a : advent) {
				writer.append("\nA - ").append(a.getName()).append(" - ").append(Integer.toString(a.getPosX())).append(" - ").append(Integer.toString(a.getPosY()))
					  .append(" - ").append(a.getOrientation().getValue()).append(" - ").append(Integer.toString(a.getNbTreasure()));
			}
			
			writer.append("\n\nQue l'on peut représenter sous la forme suivante\n\n");
			
			for(String[] row : maCarte) {
				
				for(String element : row) {
					writer.append(element + "\t");
				}
				writer.append("\n");
			}
			
			System.out.println("\nFICHIER CREE AVEC SUCCESS...VOUS LE TROUVEREZ DANS LE REPERTOIR => "+pathDir.replace("\\", "/"));
			
			
		} catch (IOException e) {
			throw new ExecutionException(Constantes.EXCEPTION_FILE_WRITE, e);
		}
				
	}
	

}
