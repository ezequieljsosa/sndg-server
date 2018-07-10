package ar.com.bia.controllers;

import ar.com.bia.entity.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.AlignedSequence;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/comparative")
public class ComparativeAnalysisResource {
	
	@Autowired
	private MongoOperations mongoTemplate;
	

	

	/**
	 * Devuelve un tbs con celdas Heatmap (cepas vs genes)
	 * 
	 * @param comparative_analysis_id
	 * @return tbs con celdas de heatmap
	 * @throws IOException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/heatmap", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String heatmapData(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
							  @RequestParam(value = "filter-genes-strain[]", defaultValue = "" ) String[] filterGenes,
							  @RequestParam(value = "genelist", defaultValue = "") String[] genelist)
			throws IOException {
		
		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		
		StringBuilder sb = new StringBuilder();	
		sb.append("strain\tgene\tidentity\n");

		// obtiene documento de análisis comparativo
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		List<String> organisms= comparativeAnalysis.getOrganisms();

		// aplica filtros de genes por cepa
		List<OrthologsDoc> orthologsList = getFilterOrthologs(comparativeAnalysisId, filterGenes, genelist);
		
		
		// Recorre el listado de genes ortólogos
		for (OrthologsDoc orthologs : orthologsList) {
			List<GeneOrthologDoc> genes = orthologs.getGenes();
			List<Float> identities = null;
			int i = 0;
			while(identities == null){
				if (genes.get(i).getFeatureId() != null)
					// el primer organismo con la variante lo toma como referencia para las identidades
					identities = genes.get(i).getIdentities();
				i++;
			}
			for(i=0; i < genes.size(); i++ ){
				Float id; 
				// indica que organismos tiene la variante
				if (identities.get(i) != null){
					if (genes.get(i).getFeatureId() != null)
						id = identities.get(i);
					else
						id = identities.get(i) * -1;
					sb.append(organisms.get(i) + "\t" +  orthologs.getLocusTag() + "\t" + id.toString() + "\n");
				}
			}
			
		}

		return sb.toString();

	}

	/**
	 * Filas de heatmap: Listado de genes ortólogos
	 * 
	 * @param comparativeAnalysisId: Identificador del análisis comparativo
	 * @return Tbs con listado de genes
	 * @throws IOException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/heatmap/genes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String heatmapGenes(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
							   @RequestParam(value = "filter-genes-strain[]", defaultValue = "" ) String[] filterGenes,
							   @RequestParam(value = "genelist[]", defaultValue = "") String[] genelist)
			throws IOException {

		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		
		// obtiene documento de análisis comparativo
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		StringBuilder sb = new StringBuilder();	

		sb.append("gene\n");
		
		// aplica filtros de genes por cepa
		List<OrthologsDoc> orthologsList = getFilterOrthologs(comparativeAnalysisId, filterGenes, genelist);
		
		// Recorre los genes del análisis
		for (OrthologsDoc orthologs : orthologsList) {
			sb.append(orthologs.getLocusTag() + "\n");
		}

		return sb.toString();

	}

	
	/**
	 * Columnas de Heatmap: Organismos/cepas
	 * 
	 * @param comparativeAnalysisId: Identificador del análisis comparativo
	 * @return Tbs con listado de organismos
	 * @throws IOException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/heatmap/organisms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String heatmapOrganisms(@PathVariable("comparative_analysis_id") String comparativeAnalysisId)
			throws IOException {

		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		
		StringBuilder sb = new StringBuilder();	
		
		// obtiene documento de análisis comparativo
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		sb.append("strain\n");
		
		// Recorre los organismos/cepas del análisis
		for (String organism : comparativeAnalysis.getOrganisms()) {
			sb.append(organism + "\n");
		}
		
		return sb.toString();

	}
	
	/**
	 * Listado de organismos usados en el análisis
	 * 
	 * @param comparativeAnalysisId: Identificador del análisis comparativo
	 * @return json con listado de organismos
	 * @throws IOException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/organisms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Object> listOrganisms(@PathVariable("comparative_analysis_id") String comparativeAnalysisId)
			throws IOException {

		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		
		List<Object> retValue = new ArrayList <Object>();
		
		// obtiene documento de análisis comparativo
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		// Recorre los organismos/cepas del análisis
		for (String organism : comparativeAnalysis.getOrganisms()) {
			Map <String, String> value = new HashMap <String, String> ();
			// Consulta en sequence_collection para ontener el id
			SeqCollectionDoc seqCollection = this.mongoTemplate.findOne(new Query(Criteria.where("organism").is(organism)),
					SeqCollectionDoc.class);
			value.put("name", organism);
			value.put("id", seqCollection != null ? seqCollection.getId() : "" );
			retValue.add(value);
		}
		
		return retValue;
	}
	
	/**
	 * Devuelve estadísticas del análisis comparativo de ortólogos.
	 * Por cada organismo/cepa se obtiene la totalidad de genes y la cantidad de genes de la referencia.
	 * 
	 * @param comparativeAnalysisId: Identificador del análisis comparativo
	 * @return json con resumen de análisis
	 * @throws IOException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/resume", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Object> orthologsResume(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
										@RequestParam(value = "filter-genes-strain[]", defaultValue = "" ) String[] filterGenes,
										@RequestParam(value = "genelist", defaultValue = "") String[] genelist)
			throws IOException {

		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");

		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		List<Object> ret_values = new ArrayList <Object>();

		int i = 0;
		// total de genes en referencia
		int total_genes_ref = 0;
		
		// Por cada organismo/cepa
		for (String organism : comparativeAnalysis.getOrganisms()) {
			Map <String, String> value = new HashMap <String, String> ();
			
			value.put("strain", organism);
			// Consulta en sequence_collection para ontener el id del genoma del organismo/cepa
			SeqCollectionDoc seqCollection = this.mongoTemplate.findOne(new Query(Criteria.where("organism").is(organism)),
					SeqCollectionDoc.class);
			value.put("id", seqCollection != null ? seqCollection.getId() : "" );
			
			// aplica filtros de genes por cepa
			List<OrthologsDoc> orthologsList = getFilterOrthologs(comparativeAnalysisId, filterGenes, genelist);
			
			// cuenta total de genes
			int total_genes = 0;
			int total_genes_in_ref = 0;
			for (OrthologsDoc orthologs : orthologsList) {
				List<GeneOrthologDoc> genes = orthologs.getGenes();
				if (genes.get(i).getFeatureId() != null){
					total_genes++;
					// verifica si está en la referencia
					if (genes.get(0).getFeatureId() != null)
						total_genes_in_ref++;
				}
			}
			// si es la referencia
			if(i ==0)
				total_genes_ref = total_genes;
			
			// calcula el porcentaje de genes de referencia
			float genes_in_ref = 0;
			if (total_genes_ref != 0)
				genes_in_ref = 100.0f * total_genes_in_ref / total_genes_ref;
			
			value.put("total_genes", Integer.toString(total_genes));
			value.put("percent_in_ref", String.format("%.2f", genes_in_ref));
			
			ret_values.add(value);
			i++;
		}

		return ret_values;

	}
	
	/**
	 * Dado un ortólogo muestra la información del gen para cada cepa.
	 * 
	 * @param comparativeAnalysisId
	 * @param orthologId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/{locus_tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Object> orthologInfo(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
							   @PathVariable("locus_tag") String locusTag) 
			throws IOException {

		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		if (locusTag.trim().isEmpty())
			throw new RuntimeException("ortholog parameter in querystring cannot be empty");
		
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		List<String> organisms = comparativeAnalysis.getOrganisms();
		
		List<SeqFeatureEmbedDoc> orthologSeqFeatures = getOrthologSeqFeatures(comparativeAnalysisId, locusTag, organisms);
		List<Object> retValues = new ArrayList <Object>();
		
		int i = 0;
		for (SeqFeatureEmbedDoc seqFeature: orthologSeqFeatures){
			
			if (seqFeature != null){
				Map <String, String> value = new HashMap <String, String> ();
				value.put("name", seqFeature.getIdentifier());
				value.put("strain", organisms.get(i));
				value.put("type",  seqFeature.getType());
				value.put("locus_tag", seqFeature.getLocusTag());
				value.put("start", Integer.toString(seqFeature.getLocation().getStart()));
				value.put("end", Integer.toString(seqFeature.getLocation().getEnd()));
				value.put("strand", seqFeature.getLocation().getStrand());
				value.put("description", seqFeature.getDescription());
				retValues.add(value);
			}
			i++;
		}
		return retValues;

	}
	
	/**
	 * Realiza un alineamiento de secuencias múltiples (msa) entre proteinas de varias cepas de un organismo.
	 * El resultado es compatible para la visualización con la libreria MSA de BioJs. http://biojs-msa.org/
	 * 
	 * @param genomeId: Identificador de genoma del organismo
	 * @param proteinId: Identificador de proteina de referencia
	 * @return JSON con entrada "name" que contiene el nombre de la proteina y 
	 * 		entrada "msa" con los datos usados en la visualización msa de BioJs
	 * @throws IOException
	 * @throws CompoundNotFoundException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/{locus_tag}/msa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map <String, Object> orthologMsa(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
									@PathVariable("locus_tag") String locusTag) 
			throws IOException, CompoundNotFoundException {

		
		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		if (locusTag.trim().isEmpty())
			throw new RuntimeException("locusTag parameter in querystring cannot be empty");
		
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		
		// Diccionaro de retorno
		Map <String, Object> retValue = new HashMap <String, Object>();
		// Lista de diccionario para devolver el msa
		List <Map <String, String>> msa = new ArrayList <Map <String, String>>();
		
		// Listado de secuencias para hacer msa
		List<DNASequence> seqLst = new ArrayList<DNASequence>();
		
		List<String> organisms = comparativeAnalysis.getOrganisms();
		
		List<SeqFeatureEmbedDoc> orthologSeqFeatures = getOrthologSeqFeatures(comparativeAnalysisId, locusTag, organisms);
		
		int organism_index = 0;
		for (SeqFeatureEmbedDoc seqFeature: orthologSeqFeatures){
			
			if (seqFeature != null){
				Map <String, String> value = new HashMap <String, String> ();
				value.put("id", seqFeature.getId());
				value.put("name", seqFeature.getLocusTag());
				msa.add(value);
				
				
				// consulta en mongo datos de ortologo
				DBObject match = new BasicDBObject("$match", new BasicDBObject("organism", organisms.get(organism_index)));
				int start = seqFeature.getLocation().getStart();
				int length = seqFeature.getLocation().getEnd()- seqFeature.getLocation().getStart();
				
				BasicDBObject project = new BasicDBObject("$project", new BasicDBObject("seq",
																	  new BasicDBObject("$substr", new Object[] {"$seq",start, length })));
				
				Iterator<DBObject> result = this.mongoTemplate.getCollection("contig_collection")
												.aggregate( match, project).results().iterator();
				
				if (result.hasNext()){
					DBObject dbObj = result.next();
					ContigDoc contig = this.mongoTemplate.getConverter().read(ContigDoc.class, dbObj);	
					// guarda secuencia
					
					DNASequence dnaseq = new DNASequence(contig.getSequence().toUpperCase(), AmbiguityDNACompoundSet.getDNACompoundSet());
					
					// Si es strand negativo
					if (seqFeature.getLocation().getStrand().equals("-"))
						dnaseq = new DNASequence( dnaseq.getReverseComplement().getSequenceAsString().toUpperCase(), AmbiguityDNACompoundSet.getDNACompoundSet());
					
					seqLst.add(dnaseq);
				}
				else
					throw new NotFoundException("Contig not found");
			}
			organism_index++;
		}
		
		if (seqLst.size()>1){
			// Realiza alineamiento multiple
			Profile<DNASequence, NucleotideCompound> profile = Alignments.getMultipleSequenceAlignment(seqLst);
			for (int i= 0; i< seqLst.size();i++){
				AlignedSequence<DNASequence, NucleotideCompound> seq = profile.getAlignedSequence(seqLst.get(i));
				msa.get(i).put("seq", seq.toString());
			}
		}
		else
			msa.get(0).put("seq", seqLst.get(0).toString());	
		
		
		// Arma salida a partir de msa y el nombre de la proteina
		retValue.put("name", "");
		retValue.put("msa", msa);
		
		return retValue;

	}
	
	
	/**
	 * Realiza un alineamiento de secuencias múltiples (msa) entre proteinas de varias cepas de un organismo.
	 * El resultado es compatible para la visualización con la libreria MSA de BioJs. http://biojs-msa.org/
	 * 
	 * @param genomeId: Identificador de genoma del organismo
	 * @param proteinId: Identificador de proteina de referencia
	 * @return JSON con entrada "name" que contiene el nombre de la proteina y 
	 * 		entrada "msa" con los datos usados en la visualización msa de BioJs
	 * @throws IOException
	 * @throws CompoundNotFoundException
	 */
	@RequestMapping(value = "/{comparative_analysis_id}/orthologs/{locus_tag}/msa/proteins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map <String, Object> orthologMsaProtein(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
									@PathVariable("locus_tag") String locusTag) 
			throws IOException, CompoundNotFoundException {

		
		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		if (locusTag.trim().isEmpty())
			throw new RuntimeException("locusTag parameter in querystring cannot be empty");
		
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		
		// Diccionaro de retorno
		Map <String, Object> retValue = new HashMap <String, Object>();
		// Lista de diccionario para devolver el msa
		List <Map <String, String>> msa = new ArrayList <Map <String, String>>();
		
		// Listado de secuencias para hacer msa
		List<ProteinSequence> seqLst = new ArrayList<ProteinSequence>();
		
		List<String> organisms = comparativeAnalysis.getOrganisms();
		
		List<SeqFeatureEmbedDoc> orthologSeqFeatures = getOrthologSeqFeatures(comparativeAnalysisId, locusTag, organisms);
		
		for (SeqFeatureEmbedDoc seqFeature: orthologSeqFeatures){
			
			if (seqFeature != null){	
				Map <String, String> value = new HashMap <String, String> ();
				GeneProductDoc protein = this.mongoTemplate.findOne(new Query(Criteria.where("gene_id").is(new ObjectId(seqFeature.getId()))),
													 				GeneProductDoc.class);
				if (protein != null){
					value.put("id", protein.getId());
					value.put("name", seqFeature.getLocusTag());
					msa.add(value);				
					// guarda secuencia
					seqLst.add(new ProteinSequence(protein.getSequence()));
				}
			}
		}
		
		if (seqLst.size() == 0)
			return retValue;
		
		if (seqLst.size()>1){
			// Realiza alineamiento multiple
			Profile<ProteinSequence, AminoAcidCompound> profile = Alignments.getMultipleSequenceAlignment(seqLst);
			// guarda alineamiento asociado a cada proteina
			for (int i= 0; i< seqLst.size();i++){
				AlignedSequence<ProteinSequence, AminoAcidCompound> seq = profile.getAlignedSequence(seqLst.get(i));
				msa.get(i).put("seq", seq.toString());
			}
		}
		else
			msa.get(0).put("seq", seqLst.get(0).toString());	
		
		
		// Arma salida a partir de msa y el nombre de la proteina
		retValue.put("name", "");
		retValue.put("msa", msa);
		
		return retValue;
	}
	
	@RequestMapping(value = "/{comparative_analysis_id}/gene/searchList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <Map <String, String>> geneListSearch(@PathVariable("comparative_analysis_id") String comparativeAnalysisId,
												   @RequestParam(value = "q", defaultValue = "") String query) 
			throws IOException, CompoundNotFoundException {

		List <Map <String, String>> retValues = new ArrayList <Map <String, String>>();
		
		if (query.isEmpty()) {
			return retValues;
		}
		
		if (comparativeAnalysisId.trim().isEmpty())
			throw new RuntimeException("comparativeAnalysis parameter in querystring cannot be empty");
		
		ComparativeAnalysisDoc comparativeAnalysis = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(comparativeAnalysisId)),
				ComparativeAnalysisDoc.class);
		
		if (comparativeAnalysis == null)
			throw new NotFoundException("comparativeAnalysis not found");
		
		
		
		Query q = new Query(Criteria.where("comparative_analysis_id").is(new ObjectId(comparativeAnalysisId)));
		Criteria cSearch = new Criteria().orOperator(Criteria.where("genes.locus_tag").regex(query,"i"),Criteria.where("genes.name").regex(query,"i"));
		q.addCriteria(cSearch);
		
		List<OrthologsDoc> orthologs = this.mongoTemplate.find(q,OrthologsDoc.class);
		
		// Arma salida de listado de genes orthologos
		for (OrthologsDoc ortholog: orthologs){
			Map <String, String> value = new HashMap <String, String> ();
			value.put("id", ortholog.getId().toString());
			value.put("name", ortholog.getLocusTag());
			retValues.add(value);
		}
		
		return retValues;
	}

	/**
	 * 
	 * @param comparativeAnalysisId
	 * @param orthologId
	 * @param organisms
	 * @return
	 */
	private List<SeqFeatureEmbedDoc> getOrthologSeqFeatures(String comparativeAnalysisId,
															String locus_tag,
															List<String> organisms)	throws IOException {	
		
		List<SeqFeatureEmbedDoc> retValues = new ArrayList<SeqFeatureEmbedDoc>();
		
		// Obtiene los genes ortologos
		Query query = new Query(Criteria.where("comparative_analysis_id").is(new ObjectId(comparativeAnalysisId)));
		query.addCriteria(Criteria.where("locus_tag").is(locus_tag));
		OrthologsDoc ortholog = this.mongoTemplate.findOne(query,OrthologsDoc.class);
		
		if (ortholog == null)
			throw new NotFoundException("orthologId not found");

		
		
		// paremetros consulta para obtener información de cada gen
		BasicDBObject projectFeature1 = new BasicDBObject("$project",new BasicDBObject("features", 1));
		BasicDBObject projectFeature2 = new BasicDBObject("$project",
										new BasicDBObject("_id", "$features._id")
										.append("name", "$features.name")
										.append("type", "$features.type")
										.append("locus_tag", "$features.locus_tag")
										.append("position", "$features.position")
										.append("description", "$features.description")
										.append("description", "$features.description")		
								       );
		
		DBObject unwindFeature = new BasicDBObject("$unwind", "$features");
		// Por cada gen de ortologo
		int i = 0;
		for (GeneOrthologDoc gene: ortholog.getGenes()){
			ObjectId featureId = gene.getFeatureId();
			if (featureId != null){
				// consulta en mongo
				DBObject matchFeature = new BasicDBObject("$match", new BasicDBObject("features._id", featureId));
				
				DBObject matchContig = new BasicDBObject("$match", new BasicDBObject("organism", organisms.get(i)));
	
				Iterator<DBObject> r = this.mongoTemplate.getCollection("contig_collection")
										   .aggregate(matchContig, projectFeature1, unwindFeature, matchFeature, projectFeature2)
										   .results().iterator();
				if (r.hasNext()){
					DBObject dbObj = r.next();
					SeqFeatureEmbedDoc seqFeature = this.mongoTemplate.getConverter().read(SeqFeatureEmbedDoc.class, dbObj);				
					retValues.add(seqFeature);
				}
				else
					retValues.add(null);
			}
			else
				retValues.add(null);
			
			i++;
		}
		
		return retValues;
	}
	
	/**
	 * 
	 * 
	 * @param comparativeAnalysisId
	 * @param filterGenes
	 * @return
	 */
	private List<OrthologsDoc> getFilterOrthologs(String comparativeAnalysisId, String[] filterGenes, String[] geneList){
		
		Query query = new Query(Criteria.where("comparative_analysis_id").is(new ObjectId(comparativeAnalysisId)));
		
		// arma filtro de genes por cepa
		for (int i=0; i< filterGenes.length; i++){
			if (filterGenes[i].equals("present")){
				query.addCriteria(Criteria.where("genes."+ Integer.toString(i) + ".feature_id").type(7));
			}
			else if (filterGenes[i].equals("absent")){
				query.addCriteria(Criteria.where("genes."+ Integer.toString(i) + ".feature_id").type(10));
			}
		}
		
		if (geneList.length > 0)
			query.addCriteria(Criteria.where("_id").in(Arrays.asList(geneList)));
				
		List<OrthologsDoc> orthologs = this.mongoTemplate.find(query,OrthologsDoc.class);
		return orthologs;
		
	}
	
	
}
