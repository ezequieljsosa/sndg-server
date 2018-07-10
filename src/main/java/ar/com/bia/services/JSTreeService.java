package ar.com.bia.services;

import ar.com.bia.backend.dao.GODocumentRepository;
import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.dto.GOTermTuple;
import ar.com.bia.dto.HierarchicNodeTreeSearchResult;
import ar.com.bia.dto.jstree.JSTreeNodeAbstract;
import ar.com.bia.dto.jstree.JSTreeNodeAjax;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.SeqCollectionDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class JSTreeService {

	public static final String INITIAL_JSTREE_NODE_ID = "#";
	
	@Autowired
	private GeneProductDocumentRepository geneProductRepository;

	private @Autowired
	GODocumentRepository repositoryGO;
	

	private @Autowired
	MongoTemplate mongoTemplate;
	
	public List<JSTreeNodeAbstract> nodeChildren(String goTerm, boolean includeEmpty, SeqCollectionDoc seqCollection) {
		List<JSTreeNodeAbstract> list = new ArrayList<JSTreeNodeAbstract>();

		HierarchicNodeTreeSearchResult result = this.openTreeNode(
				seqCollection, goTerm.equals(INITIAL_JSTREE_NODE_ID) ? "root" : goTerm);

		// Collections.sort(successors);

		for (GOTermTuple tuple : result.getDataTuples()) {

			if (!includeEmpty && tuple.getCount() == 0)
				continue;

			JSTreeNodeAjax jsTreeNode = new JSTreeNodeAjax();
			jsTreeNode.setId(tuple.getId());
			jsTreeNode.setText(tuple.getName() + " -> "
					+ tuple.getId().toUpperCase() + " (" + tuple.getCount()
					+ ")");
			jsTreeNode.setChildren(true);
			jsTreeNode.setParent(goTerm);
			jsTreeNode.setType("term");

			list.add(jsTreeNode);
		}
		for (GeneProductDoc gene : result.getAnnotatedGenes()) {
			JSTreeNodeAjax jsTreeNode = new JSTreeNodeAjax();
			jsTreeNode.setId(gene.getId());
			jsTreeNode.setText(gene.getName() + ": " + gene.getDescription()); 
			jsTreeNode.setChildren(false);
			jsTreeNode.setType("annotation");
			jsTreeNode.setParent(goTerm);

			list.add(jsTreeNode);
		}

		return list;
	}
	
	public HierarchicNodeTreeSearchResult openTreeNode(SeqCollectionDoc genome,
			String openNode) {

		HierarchicNodeTreeSearchResult result = new HierarchicNodeTreeSearchResult();
		// Buscar hijos
		List<String> children = null;

		String nodeName = openNode.toLowerCase();
		children = this.repositoryGO.findTerm("go", nodeName,
				false).getSuccessors();

		// TODO: quitar, poner todo en minuscula en la carga de datos
		@SuppressWarnings("rawtypes")
		Collection children_lowecase = CollectionUtils.collect(children,
				new Transformer() {

					@Override
					public Object transform(Object input) {
						return input.toString().toLowerCase();
					}
				});

		// TODO: Puede applicarse mapReduce para esto?
		// List<String> terms = this.repositoryGO.succesors(child);
		// Criteria[] criterias = new Criteria[terms.size()];
		// for (int i = 0; i < terms.size(); i++) {
		// criterias[i] = new Criteria("keywords").is(terms.get(i)
		// .toLowerCase());
		// }
		// long count2 = this.repositoryGene.count(new
		// Query(genomeCriteria(genome)
		// .orOperator(criterias)));
		BasicDBObject search = new BasicDBObject("term", new BasicDBObject(
				"$in", children_lowecase));
		search.put("seq_collection_id", new ObjectId(genome.getId()));
		DBCursor counts = this.mongoTemplate.getCollection("col_ont_idx").find(
				search);

		for (DBObject dbObject : counts) {
			String term = dbObject.get("term").toString();
			String name = dbObject.get("name").toString();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			Double parseDouble = Double.parseDouble(dbObject.get("count")
					.toString());
			result.getDataTuples().add(
					new GOTermTuple(term, name, parseDouble.longValue()));
		}
		if ( ! "root".equals(nodeName) && !"go:0008150".equals(nodeName) && !"go:0005575".equals(nodeName) && !"go:0003674".equals(nodeName) ) {
			// Buscar Anotaciones del nodo actual
			Pattern ontologyName = Pattern.compile(openNode, Pattern.CASE_INSENSITIVE);
			Criteria criteriaAnotaciones = genomeCriteria(genome.getId()).and(
					"ontologies").is(ontologyName);

			result.setAnnotatedGenes(this.geneProductRepository
					.findAll(new Query(criteriaAnotaciones)));
		}
		return result;
	}

	public Criteria genomeCriteria(String genome) {
		return Criteria.where("seq_collection_id").is(new ObjectId(genome));
	}
}
