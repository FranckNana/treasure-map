package fr.carbonit;

import java.util.List;
import java.util.Map;

import fr.carbonit.models.Carte;
import fr.carbonit.service.SearchTreasureService;
import fr.carbonit.utils.Utils;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	System.out.println("---------------- lecture du fichier ------------------\n");
		System.out.println("Veuillez choisir le fichier d'entrée ...\n");
		Thread.sleep(1000);
		Map<String, List<Object>> map = Utils.readFileAndGetInformations();
		
		System.out.println("les informations du fichiers : \n");
		
		Carte carte = (Carte)map.get("C").get(0);
		List<Object> mountain = map.get("M");
		List<Object> treasure = map.get("T");
		List<Object> adventurer = map.get("A");
		String filePath = (String) map.get("filePath").get(0);

		SearchTreasureService searchTreasure = new SearchTreasureService(carte, mountain, treasure, adventurer, filePath);
		
		searchTreasure.start();

		
    }
}
