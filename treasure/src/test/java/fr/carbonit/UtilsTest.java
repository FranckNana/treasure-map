package fr.carbonit;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import fr.carbonit.models.Adventurer;
import fr.carbonit.models.Carte;
import fr.carbonit.models.Mountain;
import fr.carbonit.models.Movement;
import fr.carbonit.models.Orientation;
import fr.carbonit.models.Treasure;
import fr.carbonit.utils.Utils;

@TestInstance(Lifecycle.PER_CLASS)
public class UtilsTest {
	
	Carte carte;
	List<Mountain> mount;
	List<Treasure> treas;
	List<Adventurer> advent;
	String FILE_PREFIXE = "result_";
	String PATH_DIR = "src/test/resources";
	
	@BeforeAll
	void deletefILE() {
		this.removeFileWithFilePrefixeName(FILE_PREFIXE);
	}
	
	@BeforeEach
	void init() {
		Utils.FILE_PATH = PATH_DIR.concat("/monfichier.txt");
		
		carte = new Carte(3, 4);
		mount = List.of(new Mountain(1,0), new Mountain(2,1));
		treas = List.of(new Treasure(0,3,2), new Treasure(1,3,3));
		List<Movement> movements = List.of(Movement.FORWARD);
		advent = List.of(new Adventurer("LARA" ,1 , 1, Orientation.SOUTH, movements, 0));
	}
	
	
	@Test
	void readFileAndGetInformationsTest() throws Exception {
		Map<String, List<Object>> map = Utils.readFileAndGetInformations();
		
		Carte carte = (Carte)map.get("C").get(0);
		List<Object> mountain = map.get("M");
		List<Object> treasure = map.get("T");
		List<Object> adventurer = map.get("A");
		
		Assertions.assertEquals(4,carte.getY());
		Assertions.assertEquals(3,carte.getX());
		Assertions.assertNotNull(mountain);
		Assertions.assertNotNull(treasure);
		Assertions.assertNotNull(adventurer);
	}
	
	@Test
	void getMovementsTest() {
		List<Movement> movements = Utils.getMovements("AADADAGGA");
		Assertions.assertNotNull(movements);
		Assertions.assertEquals(9,movements.size());
	}
	
	
	@Test
	void buildMapTest() {
		String[][] cart = Utils.buildMap(carte);
		Assertions.assertEquals(4,cart.length);
		Assertions.assertEquals(3,cart[0].length);
	}
	
	
	@Test
	void placeElementsTest() {
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		Assertions.assertTrue(maCarte[1][2].contains("M"));
		Assertions.assertTrue(maCarte[0][1].contains("M"));
	}
	
	
	@Test
	void moveForwardTest_SOUTH_Orientation() {
		Map<Integer,Integer> listOfBusyXY = new HashMap<Integer,Integer>();
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		List<Movement> movements = Utils.getMovements("AA");
		for(Movement m : movements) {
			boolean bool = Utils.moveForward(advent.get(0), maCarte, m, treas, listOfBusyXY);
			Assertions.assertTrue(bool);
		}
	}
	
	@Test
	void moveForwardTest_WEST_Orientation() {
		Map<Integer,Integer> listOfBusyXY = new HashMap<Integer,Integer>();
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		List<Movement> movements = Utils.getMovements("A");
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.WEST);
		for(Movement m : movements) {
			boolean bool = Utils.moveForward(adventurer, maCarte, m, treas, listOfBusyXY);
			Assertions.assertTrue(bool);
		}
	}
	
	@Test
	void moveForwardTest_WEST_Orientation_TREASURE() {
		Map<Integer,Integer> listOfBusyXY = new HashMap<Integer,Integer>();
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		List<Movement> movements = Utils.getMovements("A");
		Adventurer adventurer = advent.get(0);
		adventurer.setPosY(3);
		adventurer.setOrientation(Orientation.WEST);
		for(Movement m : movements) {
			boolean bool = Utils.moveForward(adventurer, maCarte, m, treas, listOfBusyXY);
			Assertions.assertTrue(bool);
		}
	}
	
	@Test
	void moveForwardTest_NORTH_Orientation() {
		Map<Integer,Integer> listOfBusyXY = new HashMap<Integer,Integer>();
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		List<Movement> movements = Utils.getMovements("A");
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.NORTH);
		for(Movement m : movements) {
			boolean bool = Utils.moveForward(adventurer, maCarte, m, treas, listOfBusyXY);
			Assertions.assertFalse(bool);
		}
	}
	
	@Test
	void moveForwardTest_EAST_Orientation() {
		Map<Integer,Integer> listOfBusyXY = new HashMap<Integer,Integer>();
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		List<Movement> movements = Utils.getMovements("A");
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.EAST);
		for(Movement m : movements) {
			boolean bool = Utils.moveForward(adventurer, maCarte, m, treas, listOfBusyXY);
			Assertions.assertFalse(bool);
		}
	}
	
	
	@Test
	void turnLefTest_EAST_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.EAST);
		boolean isTurnLeft = Utils.turnLeft(adventurer);
		Assertions.assertTrue(isTurnLeft);
	}
	
	@Test
	void turnLefTest_WEST_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.WEST);
		boolean isTurnLeft = Utils.turnLeft(adventurer);
		Assertions.assertTrue(isTurnLeft);
	}
	
	@Test
	void turnLefTest_NORTH_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.NORTH);
		boolean isTurnLeft = Utils.turnLeft(adventurer);
		Assertions.assertTrue(isTurnLeft);
	}
	
	@Test
	void turnLefTest_SOUTH_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.SOUTH);
		boolean isTurnLeft = Utils.turnLeft(adventurer);
		Assertions.assertTrue(isTurnLeft);
	}
	
	
	@Test
	void turnRightTest_EAST_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.EAST);
		boolean isTurnRight = Utils.turnRight(adventurer);
		Assertions.assertTrue(isTurnRight);
	}
	
	@Test
	void turnRightTest_WEST_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.WEST);
		boolean isTurnRight = Utils.turnRight(adventurer);
		Assertions.assertTrue(isTurnRight);
	}
	
	@Test
	void turnRightTest_NORTH_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.NORTH);
		boolean isTurnRight = Utils.turnRight(adventurer);
		Assertions.assertTrue(isTurnRight);
	}
	
	@Test
	void turnRightTest_SOUTH_Orientation() {
		Adventurer adventurer = advent.get(0);
		adventurer.setOrientation(Orientation.SOUTH);
		boolean isTurnRight = Utils.turnRight(adventurer);
		Assertions.assertTrue(isTurnRight);
	}
	
	@Test
	void writeOutputFileTest() throws ExecutionException {
		//Utils.FILE_DIR = "src/test/resources";
		String[][] cart = Utils.buildMap(carte);
		String[][] maCarte = Utils.placeElements(cart, mount, treas, advent);
		Utils.writeOutputFile(maCarte, mount, treas, advent, PATH_DIR);
		Assertions.assertTrue(this.isFilePrefixNameExist(FILE_PREFIXE));
	}
	
	private boolean isFilePrefixNameExist(String filePrefixName) {
		File repertoire = new File("src/test/resources");
        List<String> list = Arrays.asList(repertoire.list());      
 
        if (list != null) {         
        	return list.stream().filter(fileName -> fileName.startsWith(filePrefixName)).findFirst().isPresent();
        } else {
            return false;
        }
	}
	
	private void removeFileWithFilePrefixeName(String filePrefixName) {
		File repertoire = new File(PATH_DIR);
        List<String> list = Arrays.asList(repertoire.list()); 
 
        if (list != null) {         
        	List<String> listFileName = list.stream().filter(fileName -> fileName.startsWith(filePrefixName)).collect(Collectors.toList());
        	if(!Objects.isNull(listFileName)) {
        		for(String f : listFileName) {
        			String path = PATH_DIR.concat("/"+f);
            		File file = new File(path);
            		file.delete();
            	}
        	}
        	
        }
              
     }
	
}
