package IE;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaMapping {
	//initialise MetaMap api
	private static MetaMapApi api;
	
	public MetaMapping() throws MalformedURLException
	{
		
		/**
		 * set parameters (set parameters in conf/metamap.properties)
		 */
		api = new MetaMapApiImpl();
		api.setHost("gnode1.mib.man.ac.uk");
		api.setPort(8066);
	    
	    //get and set parameters
		//List<String> theOptions = FileOps.getFileContentAsList(new File("conf/metamap.parameters").toURI().toURL());
		List<String> theOptions = new ArrayList<String>();
	    theOptions.add("-y");  // turn on Word Sense Disambiguation
	    theOptions.add("-i");
	    theOptions.add("-l");
	   // theOptions.add("-R SNOMEDCT,ICD10CM,ICD9CM,ICF,ICF-CY,RXNORM");
		for(String opt: theOptions)
	    	api.setOptions(opt);

	}
	
	private static Map<Object, Object> getClassification(String term) throws Exception
	{
		//String[] classfication = new String[3]; 
		Map<Object, Object> mp = new HashMap<Object, Object>();

		//Certain characters may cause MetaMap to throw an exception; 
		//filter terms before passing to mm.
		term = term.replaceAll("'", "");
		term = term.replaceAll("\"", "");

		List<Result> resultList = api.processCitationsFromString(term);
	    //Result result = resultList.get(0);
		int i = 0;
	    for(Result result:resultList){
		for (Utterance utterance: result.getUtteranceList()) 
		{	for (PCM pcm: utterance.getPCMList()) {
	          /*for (Ev ev: pcm.getCandidatesInstance().getEvList()) {
		           System.out.println(" Candidate:");
		            System.out.println("  Score: " + ev.getScore());
		            System.out.println("  Concept Id: " + ev.getConceptId());
		            System.out.println("  Concept Name: " + ev.getConceptName());
		            System.out.println("  Preferred Name: " + ev.getPreferredName());
		            System.out.println("  Matched Words: " + ev.getMatchedWords());
		            System.out.println("  Semantic Types: " + ev.getSemanticTypes().get(0));
		            System.out.println("  MatchMap: " + ev.getMatchMap());
		            System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
		            System.out.println("  is Head?: " + ev.isHead());
		            System.out.println("  is Overmatch?: " + ev.isOvermatch());
		            System.out.println("  Sources: " + ev.getSources());
		            System.out.println("  Positional Info: " + ev.getPositionalInfo());
		          }*/
			
	          for (Mapping map: pcm.getMappingList()) {
	            for (Ev mapEv: map.getEvList()) {	  
	           
	            	mp.put(i++, mapEv.getConceptId());
	            	mp.put(i++, mapEv.getMatchedWords());
	            	mp.put(i++, mapEv.getPositionalInfo());
	            	mp.put(i++, mapEv.getSemanticTypes().get(0)); //get only first SemType //.toString() for all if applicable [ SemType1, SemType2, etc.]
	            	mp.put(i++, mapEv.getConceptName());
	            	mp.put(i++, mapEv.getPreferredName());
	            	// add further attributes here (and in run(gateDoc) --> gateMap.put(///))
	            }
	          }
	    	}
		}
	    }
	return mp;
	}
	
}
