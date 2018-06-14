package ar.com.bia.services;

import ar.com.bia.backend.dao.OntologyDocumentRepository;
import ar.com.bia.backend.dao.impl.OntologyDocIndexRepositoryImpl;
import ar.com.bia.backend.dao.impl.VarOntologyDocIndexRepositoryImpl;
import ar.com.bia.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OntologyService {

	@Autowired
	private OntologyDocumentRepository ontologyRepository;

	@Autowired
	private OntologyDocIndexRepositoryImpl ontologyIndexRepository;
	
	@Autowired
	private VarOntologyDocIndexRepositoryImpl ontologyVariantIndexRepository;

	public List<OntologyTerm> ontologies(GeneProductDoc protein) {
		List<String> ontologies = proteinOntologiesList(protein);

		return this.ontologies(ontologies, new ArrayList<String>());
	}

	public List<OntologyTerm> ontologies(List<GeneProductDoc> proteins) {
		List<String> ontologies = new ArrayList<String>();

		for (GeneProductDoc protein : proteins) {
			ontologies.addAll(this.proteinOntologiesList(protein));
		}
		Set<String> hs = new HashSet<>();
		hs.addAll(ontologies);

		return this.ontologies(hs, new ArrayList<String>());
	}

	public List<OntologyTerm> ontologies(GeneProductDoc protein, List<String> ontologies) {
		List<String> terms = proteinOntologiesList(protein);

		return this.ontologies(terms, ontologies);
	}


	
	
	
	public List<OntologyTerm>  varOntologies(SeqCollectionDoc genome) {
		Criteria criteria = Criteria.where("seq_collection_name").is(genome.getName());
		return ontologyVariantIndexRepository.findAll(new Query(criteria)).stream()
				//.map(OrgOntIndexElement.class::cast)				
				.map(x -> {
					try{
						return  new OntologyTerm(x.getOntology(),x.getTerm(),x.getName(),x.getDatabase(),x.getOrder());
					} catch(Exception ex) {
						return null;
					}
							
					
					
				})		
				.filter(x -> x != null)
				//.map(OntologyTerm.class::cast)
				.collect(Collectors.toList());
	}
	
//	public List<OntologyTerm> varPathwayOntologies(SeqCollectionDoc genome) {
//		Criteria criteria = Criteria.where("organism").is(genome.getName()).and("ontology").is("biocyc_pw");
//		List<String> ontologies = ontologyVariantIndexRepository.findAllNorRep(criteria).stream().map(x -> {
//			return x.getTerm();
//		}).collect(Collectors.toList());
//		return this.ontologies(ontologies, new ArrayList<String>());
//	}
	
	public List<OntologyTerm>  ontologies_go_ec(SeqCollectionDoc genome) {
		Criteria criteria = Criteria.where("seq_collection_name").is(genome.getName());
		return ontologyIndexRepository.findAll(new Query(criteria)).stream()
				//.map(OrgOntIndexElement.class::cast)				
				.map(x -> {
					try{
						return  new OntologyTerm(x.getOntology(),x.getTerm(),x.getName(),x.getDatabase(),x.getOrder());
					} catch(Exception ex) {
						return null;
					}
							
					
					
				})		
				.filter(x -> x != null)
				//.map(OntologyTerm.class::cast)
				.collect(Collectors.toList());
	}
	

	
	public List<OntologyTerm> pathwayOntologies(SeqCollectionDoc genome) {
		List<String> ontologies = genome.getPathways().stream().map(p -> p.getTerm()).collect(Collectors.toList());
		return this.ontologies(ontologies, new ArrayList<String>());
	}

	public List<OntologyTerm> ontologies(Collection<String> terms, Collection<String> ontologies) {
		Set<String> hs = new HashSet<>();
		for (String term : terms) {
			hs.add(term.toLowerCase());
		}

		List<OntologyTerm> result = new ArrayList<OntologyTerm>();
		
		Criteria termCriteria = Criteria.where("term").in(hs);

		Query query = new Query(termCriteria);
		result = ontologyRepository.findAll(query);
		return result;
	}

	public List<OrgOntIndexElement> ontSearch(String organism, String query, List<String> ontologies) {
		Criteria criteria = null;
		if (organism.isEmpty()) {
			criteria = new Criteria();
		} else {
			criteria = Criteria.where("seq_collection_name").is(organism);
		}
		int criteriaCount =   (int) Arrays.asList(query.split(" ")).stream().filter(x -> x.length() > 2).count();
		if (ontologies.size() > 0) {
			criteriaCount++;
		}

		Criteria[] keywordsCriteria = new Criteria[criteriaCount];
		int i = 0;
		for (String keyword : query.split(" ")) {
			if(keyword.trim().length() > 3){
				keywordsCriteria[i] = Criteria.where("keywords").regex(keyword.trim(), "i");
				i++;	
			}			
		}
		

		if (ontologies.size() > 0) {
			Criteria ontologiesCriteria = new Criteria();
			keywordsCriteria[criteriaCount - 1] = ontologiesCriteria;
			List<Criteria> ontologiesCriteriaList = new ArrayList<>();

			for (String ontology : ontologies) {
				Criteria ontologyCriteria = new Criteria();
				if (!ontology.contains(":")) {
					ontologyCriteria.and("ontology").is(ontology);
				} else {
					String database = ontology.split(":")[1];
					ontology = ontology.split(":")[0];
					ontologyCriteria.and("ontology").is(ontology).and("database").is(database);
				}
				ontologiesCriteriaList.add(ontologyCriteria);
			}
			ontologiesCriteria.orOperator(ontologiesCriteriaList.toArray(new Criteria[ontologiesCriteriaList.size()]));
		}
		criteria.andOperator(keywordsCriteria);

		// query2.limit(20);
		List<OrgOntIndexElement> findAllNorRep = ontologyIndexRepository.findAllNorRep(criteria);
		return findAllNorRep.stream().filter(x-> 
				!x.getOntology().equals("ec") 
				||  x.getTerm().endsWith(".-.-")).collect(Collectors.toList());

	}

	public List<OntologyTerm> searchTerm(String query, List<String> ontologiesListFilter) {
		Criteria[] criterias = new Criteria[2];
		criterias[0] = Criteria.where("term").regex(query, "i");
		criterias[1] = Criteria.where("name").regex(query, "i");

		Criteria criteria = new Criteria().orOperator(criterias);

		for (String ontology : ontologiesListFilter) {
			criteria.and("ontology").is(ontology);
		}

		Query query2 = new Query(criteria);
		// query2.limit(20);
		return ontologyRepository.findAll(query2);
	}

	private List<String> proteinOntologiesList(GeneProductDoc protein) {
		List<String> ontologies = new ArrayList<String>();
		if (protein.getOntologies() != null) {
			ontologies.addAll(protein.getOntologies());
			for (SeqFeature feature : protein.getFeatures()) {
				ontologies.add(feature.getIdentifier());
				ontologies.add(feature.getType());
			}
						
		}
		for (ReactionDoc reaction : protein.getReactions()) {
			ontologies.add(reaction.getName());
			ontologies.addAll(reaction.getPathways());
			reaction.getSubstrates().stream().forEach(x -> {
				ontologies.add(x.getName());
			});
			reaction.getProducts().stream().forEach(x -> {
				ontologies.add(x.getName());
			});
		}
		Optional<Map<String, String>> oChokeProp = protein.getProperties().stream().filter(x -> x.get("_type").equals("pathways") 
				&& x.containsKey("property")
				&& x.get("property").equals("chokepoint")  ).findFirst();
		oChokeProp.ifPresent(chokeProp->{
			Arrays.asList(chokeProp.get("metabolites").split(",")).forEach(x -> {
				ontologies.add( x.toLowerCase().split("\"")[1]   );
			});
			
		});

		return ontologies;
	}

	public List<OrgOntIndexElement> searchTermByKeyword(String organismName, List<String> words,
			List<String> ontologyList) {
		Criteria criteria = Criteria.where("seq_collection_name").is(organismName).and("_cls")
				.is("SeqColOntologyIndex");
		for (String word : words) {
			criteria = criteria.and("keywords").regex("^" + word.trim().toLowerCase());
		}

		for (String ontology : ontologyList) {
			criteria.and("ontology").is(ontology);
		}

		Query dbquery = new Query(criteria); // "ontologies" , new
												// BasicDBObject("$in",ontologies_list)
												// );

		return this.ontologyIndexRepository.findAll(dbquery);
	}


}
