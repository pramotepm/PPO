package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import structures.Peptide;

public class BlastpParser {
	private String proteinID = null;
	private LinkedList<Peptide> peptides = new LinkedList<Peptide>();

	public String getProteinID() {
		return proteinID;
	}
	
	private void setProteinID(String proteinID) {
		this.proteinID = proteinID;
	}
	
	public Peptide[] getPeptides() {
		return peptides.toArray(new Peptide[peptides.size()]);
	}
	
	private void setPeptide(LinkedList<Peptide> peptides) {
		this.peptides = peptides;
	}
	
	private void reset() {
		setProteinID(null);
		setPeptide(new LinkedList<Peptide>());		
	}
	
	private Peptide getHitsInformation(Node hits) {
		Peptide ap = new Peptide(); 
		NodeList hitsList = hits.getChildNodes();
		for (int i = 0; i < hitsList.getLength(); i++) {
			Node hits_child = hitsList.item(i);
			switch (hits_child.getNodeName()) {
				case "Hit_accession":
					// Get accession
					ap.setAccession(hits_child.getTextContent());
				break;
				case "Hit_len":
					// Get length
					ap.setLength(Integer.parseInt(hits_child.getTextContent()));
				break;
				case "Hit_hsps":
					NodeList hits_hspsList = hits_child.getChildNodes();
					for (int j = 0; j < hits_hspsList.getLength(); j++) {
						Node hits_hsps = hits_hspsList.item(j);
						if (hits_hsps.getNodeName().equals("Hsp")) {
							NodeList hspList = hits_hsps.getChildNodes();
							for (int k = 0; k < hspList.getLength(); k++) {
								switch (hspList.item(k).getNodeName()) {
									case "Hsp_query-from":
										// Get query from
										ap.setAlign_start(Integer.parseInt(hspList.item(k).getTextContent()));
									break;
									case "Hsp_query-to":
										// Get query to
										ap.setAlign_end(Integer.parseInt(hspList.item(k).getTextContent()));
									break;
								}
							}
						}
					}
				break;
			}
		}
		return ap;
	}
	
	public void parse(String XMLFilePath) throws ParserConfigurationException, SAXException, IOException {
		reset();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		EntityResolver resolver = new EntityResolver() {
		    public InputSource resolveEntity(String publicId, String systemId) throws FileNotFoundException {
		        if (publicId.equals("-//NCBI//NCBI BlastOutput/EN")) {
		            InputStream in = BlastpParser.class.getResourceAsStream("/NCBI_BlastOutput.dtd");
		            if (in == null)
		            	System.out.println("Not Found !");
		            return new InputSource(in);
		        }
		        return null;
		    }
		};
		builder.setEntityResolver(resolver);
		
		File f = new File(XMLFilePath);
		Document document = builder.parse(f);
		Element root = document.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		// Get XML node "BlastOutput_iterations"
		Node BlastOutput_iterations = nodeList.item(17);
		
		// Get XML node "BlastOutput_iterations"."Iteration"
		NodeList IterationList = BlastOutput_iterations.getChildNodes().item(1).getChildNodes();
		
		for (int i = 0; i < IterationList.getLength(); i++) {
			Node item = IterationList.item(i);
			switch (item.getNodeName()) {
				case "Iteration_query-def":
					proteinID = item.getTextContent();
				break;
				case "Iteration_hits":
					NodeList Iteration_hitsList = item.getChildNodes();
					for (int j = 0; j < Iteration_hitsList.getLength(); j++) {
						Node hitsNode = Iteration_hitsList.item(j);
						switch (hitsNode.getNodeName()) {
							case "Hit":
								peptides.add(getHitsInformation(hitsNode));
							break;
						}
					}
				break;
				case "Iteration_stat":
						/*
						 * Statistical Information
						 * 	- E-value
						 *  - Entropy
						 *  - Lambda
						 *  - Kappa
						 */
				break;				
				default:
				break;
			}
		}
	}	
	
	public void print() {
		System.out.println("[Protein]");
		System.out.println("  Protein ID : " + this.getProteinID());
		System.out.println("[Peptides]");
		System.out.println("-------");
		for (Peptide a : this.getPeptides()) {
			System.out.println("  Accession : " + a.getAccession());
			System.out.println("  Length : " + a.getLength());
			System.out.println("  Start : " + a.getAlign_start());
			System.out.println("  End : " + a.getAlign_end());
			System.out.println("-------");
		}
	}
	
	public static void main(String[] args) {
		try {
			BlastpParser x = new BlastpParser();
			x.parse("/Users/pramote/Desktop/exp/as_seq_out/5HT3D_HUMAN.4");
			x.print();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}