package fr.carbonit;

import java.io.File;
import java.util.Arrays;
import java.util.List;
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
import fr.carbonit.service.SearchTreasureService;
import fr.carbonit.utils.Utils;

@TestInstance(Lifecycle.PER_CLASS)
public class SearchTreasureServiceTest {
	
	Carte carte;
	List<Object> mount;
	List<Object> treas;
	List<Object> advent;
	String PATH_DIR = "src/test/resources";
	String FILE_PREFIXE = "result_";
	
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
	void startTest() throws ExecutionException {
		SearchTreasureService searchTreasure = new SearchTreasureService(carte, mount, treas, advent, PATH_DIR);
		searchTreasure.setSkipScanner(true);
		searchTreasure.start();
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
