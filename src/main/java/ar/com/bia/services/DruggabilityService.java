package ar.com.bia.services;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.dto.druggability.DruggabilityParam;
import ar.com.bia.dto.druggability.DruggabilitySearch;
import ar.com.bia.entity.PropertyUpload;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.druggability.SeqColDruggabilityParam;
import ar.com.bia.services.exception.PropFileLoadException;
import au.com.bytecode.opencsv.CSVReader;
import com.mongodb.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DruggabilityService {

	private static final String GENE_FIELD_IMPORT = "id";

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private GeneProductDocumentRepository geneProductRepository;

	public List<Map<String, Object>> druggabilityList(String genomeName, DruggabilitySearch ds, int limit) {

		AggregationOutput aOuput = createDruggabilityCollection(genomeName, ds, limit);

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		// BasicDBList orderBy = new BasicDBList();
		// orderBy.add(new BasicDBObject("score",-1));
		// Iterator<DBObject> iterator =
		// mongoTemplate.getCollection("score").find().sort(orderBy).limit(limit);;

		Set<String> lastMap = null;
		// This inverts the order...
		for (final DBObject dbObject : aOuput.results()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map2 = dbObject.toMap();
			results.add(map2);
			lastMap = map2.keySet();

		}
		for (String geneName : ds.getGenes()) {
			long count = this.geneProductRepository.count(new Query(Criteria.where(GENE_FIELD_IMPORT).is(geneName)));
			if (count == 0) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				for (String key : lastMap) {
					map2.put(key, "?");
				}
				map2.put("name", geneName);
				results.add(map2);
			}
		}
		return results;
	}

	/**
	 * 
	 * 
	 * 
	 * @param genomeName
	 * @param ds
	 * @param limit
	 * @return
	 */
	private AggregationOutput createDruggabilityCollection(String genomeName, DruggabilitySearch ds, int limit) {
		DBCollection collection = mongoTemplate.getCollection("proteins");

		List<DBObject> projects = initFormula(ds.getFormula(), ds.getFilters());
		List<DBObject> geneFilters = initGeneFilter(ds.getGenes());
		List<DBObject> paramFilters = initFilter(ds.getFilters());

		BasicDBObject filter = new BasicDBObject("$match",
				new BasicDBObject("keywords", "has_structure").append("organism", genomeName));

		List<DBObject> pipeline = new ArrayList<DBObject>();
		pipeline.addAll(projects);
		pipeline.addAll(paramFilters);
		pipeline.addAll(geneFilters);

		pipeline.add(new BasicDBObject("$sort", new BasicDBObject("score", -1)));
		pipeline.add(new BasicDBObject("$limit", limit));

		DBObject[] array = pipeline.toArray(new DBObject[pipeline.size()]);
		return collection.aggregate(filter, array);
	}

	private List<DBObject> initGeneFilter(List<String> genes) {
		List<DBObject> matches = new ArrayList<DBObject>();
		if (genes.size() > 0) {
			BasicDBList geneList = new BasicDBList();
			for (String geneName : genes) {
				geneList.add(geneName);
			}
			BasicDBObject geneListDB = new BasicDBObject("$match",
					new BasicDBObject("name", new BasicDBObject("$in", geneList)));
			matches.add(geneListDB);
		}
		return matches;
	}

	private List<DBObject> initFilter(List<DruggabilityParam> filters) {
		List<DBObject> matches = new ArrayList<DBObject>();

		for (DruggabilityParam dp : filters) {
			BasicDBObject matchOp = new BasicDBObject();

			DBObject match = new BasicDBObject("$match", matchOp);
			matches.add(match);

			String prop = dp.getName();
			if (dp.getCategory().equals("structure")) {
				prop = "structure." + dp.getName();
			}
			if (dp.getCategory().equals("pocket")) {
				prop = "structure.pocket." + dp.getName();
			}
			if (dp.getType().equals("has")) {
				matchOp.append(prop, dp.getOperation().equals("has"));
			} else if (dp.getType().equals("choises")) {
				matchOp.append(prop, dp.getOptions());
			} else if (dp.getType().equals("variable")) {
				String operation = "$lt";
				if (dp.getOperation().equals(">")) {
					operation = "$gt";
				}
				matchOp.append(prop, new BasicDBObject(operation, dp.getCoefficient()));
			} else {
				throw new RuntimeException("Valor incorrecto de parametro");
			}
		}
		return matches;
	}

	private List<DBObject> initFormula(List<DruggabilityParam> dps, List<DruggabilityParam> dpsFilters) {
		List<DBObject> projects = new ArrayList<DBObject>();
		BasicDBObject baseProject = new BasicDBObject("name", "$name").append("search", 1);

		DBObject unwindStruct = null;
		DBObject unwindPocket = null;

		Map<DruggabilityParam, String> projectedFields = new HashMap<DruggabilityParam, String>();

		for (DruggabilityParam dp : dps) {

			String project = "search." + dp.getType() + "." + dp.getName();
			String projected = dp.getName();
			if (dp.getCategory().equals("structure")) {
				unwindStruct = new BasicDBObject("$unwind", "$search.structures");
				project = "search.structures." + dp.getType() + "." + dp.getName();
				projected = "structure." + dp.getName();
			} else if (dp.getCategory().equals("pocket")) {
				unwindStruct = new BasicDBObject("$unwind", "$search.structures");
				unwindPocket = new BasicDBObject("$unwind", "$search.structures.pockets");
				project = "search.structures.pockets." + dp.getType() + "." + dp.getName();
				projected = "structure.pocket." + dp.getName();
			}
			projectedFields.put(dp, projected);
			baseProject.put(projected, "$" + project);
		}

		for (DruggabilityParam dp : dpsFilters) {

			String project = "search." + dp.getType() + "." + dp.getName();
			String projected = dp.getName();
			if (dp.getCategory().equals("structure")) {
				unwindStruct = new BasicDBObject("$unwind", "$search.structures");
				project = "search.structures." + dp.getType() + "." + dp.getName();
				projected = "structure." + dp.getName();
			} else if (dp.getCategory().equals("pocket")) {
				unwindStruct = new BasicDBObject("$unwind", "$search.structures");
				unwindPocket = new BasicDBObject("$unwind", "$search.structures.pockets");
				project = "search.structures.pockets." + dp.getType() + "." + dp.getName();
				projected = "structure.pocket." + dp.getName();
			}
			projectedFields.put(dp, projected);
			baseProject.put(projected, "$" + project);
		}

		if (unwindPocket != null) {
			baseProject.put("structure.name", "$search.structures.structure");
			projects.add(unwindStruct);
			projects.add(unwindPocket);
		} else if (unwindStruct != null) {
			baseProject.put("structure.name", "$search.structures.structure");
			projects.add(unwindStruct);
		}

		BasicDBObject scoreInit = (BasicDBObject) baseProject.clone();
		scoreInit.put("score", new BasicDBObject("$literal", 0));
		projects.add(new BasicDBObject("$project", scoreInit));

		for (DruggabilityParam dp : dps) {
			BasicDBObject projectOp = (BasicDBObject) baseProject.clone();
			DBObject projection = new BasicDBObject("$project", projectOp);
			projects.add(projection);

			if (dp.getType().equals("has")) {
				addHasParam(projectedFields.get(dp), dp, projectOp);
			} else if (dp.getType().equals("choises")) {
				addChoiseParam(projectedFields.get(dp), dp, projectOp);
			} else if (dp.getType().equals("variable")) {
				addVariableParam(projectedFields.get(dp), dp, projectOp);
			} else {
				throw new RuntimeException("Valor incorrecto de parametro");
			}
		}

		return projects;
	}

	private void addVariableParam(String projected, DruggabilityParam dp, BasicDBObject projectOp) {
		// {score: {$sum:[$score, {$multiply:[ "$score", {coef} ]}]}

		BasicDBList multiplyList = new BasicDBList();
		BasicDBList ifNullList = new BasicDBList();
		ifNullList.add("$" + projected);
		ifNullList.add(0);
		BasicDBObject ifNull = new BasicDBObject("$ifNull", ifNullList);
		multiplyList.add(ifNull);
		multiplyList.add(dp.getCoefficient());

		BasicDBList sumList = new BasicDBList();
		sumList.add("$score");
		sumList.add(new BasicDBObject("$multiply", multiplyList));

		projectOp.put("score", new BasicDBObject("$add", sumList));
	}

	private void addHasParam(String projected, DruggabilityParam dp, BasicDBObject projectOp) {
		// {score: {$if: ["${param}" , {$add:[ "$score", {coef} ]}, 0] }}

		BasicDBList sumList = new BasicDBList();
		sumList.add("$score");
		sumList.add(dp.getCoefficient());

		BasicDBList condList = new BasicDBList();
		condList.add("$" + projected);
		condList.add(new BasicDBObject("$add", sumList));
		condList.add("$score");
		DBObject cond = new BasicDBObject("$cond", condList);
		projectOp.put("score", cond);
	}

	private void addChoiseParam(String projected, DruggabilityParam dp, BasicDBObject projectOp) {
		// {score: {$cond: [ {$eq:["${param}","{choise}"] }, {$sum:[ "$score",
		// {coef} ]}, "$score" ] }}

		BasicDBList eqList = new BasicDBList();
		eqList.add("$" + projected);
		eqList.add(dp.getOptions());

		BasicDBList sumList = new BasicDBList();
		sumList.add("$score");
		sumList.add(dp.getCoefficient());

		BasicDBList condList = new BasicDBList();
		condList.add(new BasicDBObject("$eq", eqList));
		condList.add(new BasicDBObject("$add", sumList));
		condList.add("$score");

		DBObject cond = new BasicDBObject("$cond", condList);

		projectOp.put("score", cond);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> mapReduce(DruggabilitySearch ds, int limit, Principal principal) {

		String map = "function Map() {emit(this.name,this.pepe); }";
		String reduce = "function Reduce (key, values) { var size = values.length; var result = (Array.sum(values))/size; return result;    }";

		Query query = new Query(Criteria.where("keywords").is("has_structure"));
		// Criteria auth = dataTablesUtils.authCriteria(principal);

		// DBCollection collection = template.getCollection("");
		// MapReduceCommand command = new MapReduceCommand(inputCollection, map,
		// reduce, outputCollection, type, query);
		// collection.mapReduce(command);

		// query.addCriteria(Criteria.where("sensor.$id").is(sensorId).andOperator(Criteria.where("date").gt(date)));

		MapReduceOptions mapReduceOptions = new MapReduceOptions().outputCollection("job1");
		@SuppressWarnings("rawtypes")
		MapReduceResults<Map> result = mongoTemplate.mapReduce(query, "proteins", map, reduce, mapReduceOptions,
				Map.class);
		// (query,"measurements", map, reduce, Measurement.class);

		// PaginatedResult<GeneProductDoc> pr = new
		// PaginatedResult<GeneProductDoc>();
		// pr.setRecordsFiltered(template.count(query, GeneProductDoc.class));
		// //pr.setRecordsTotal(template.count(new Query(auth),
		// GeneProductDoc.class));
		//
		// pr.setData(new ArrayList<GeneProductDoc>());

		// List<GeneProductDoc> data = new ArrayList<GeneProductDoc>();
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		int i = 0;
		for (Map<String, String> geneProductDoc : result) {
			data.add(geneProductDoc);
			if (i >= limit) {
				break;
			}
		}

		return data;
	}

	public List<Map<String, Object>> druggabilityTable(SeqCollectionDoc genome) {

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

		// if (genome.getDruggabilityParams() != null) {
		// DBCollection collection = mongoTemplate.getCollection("proteins");
		// List<DBObject> projects = new ArrayList<DBObject>();
		// BasicDBObject baseProject = new
		// BasicDBObject("choises.type","druggability_group");
		//
		// for (SeqColDruggabilityParam dp : genome.getDruggabilityParams()) {
		// if (dp.getName().equals("protein")) {
		// for (DruggabilityParameter dparam : dp.getHas()) {
		// baseProject.put(dparam.getName(), "$search.has." + dparam.getName());
		// }
		// for (DruggabilityParameter dparam : dp.getChoises()) {
		// baseProject.put(dparam.getName(), "$search.choises." +
		// dparam.getName());
		// }
		//
		// }
		// }
		// projects.add(new BasicDBObject("$project", baseProject));
		//
		// BasicDBObject filter = new BasicDBObject("$match",
		// new BasicDBObject("keywords", "has_structure").append("organism",
		// genome.getName()));
		//
		// List<DBObject> pipeline = new ArrayList<DBObject>(projects);
		// DBObject[] array = pipeline.toArray(new DBObject[pipeline.size()]);
		// AggregationOutput aOuput = collection.aggregate(filter, array);
		//
		// for (final DBObject dbObject : aOuput.results()) {
		// @SuppressWarnings("unchecked")
		// Map<String, Object> map2 = dbObject.toMap();
		// results.add(map2);
		// }
		// }

		return results;
	}

	public PropertyUpload loadPropFiles(String collectionName, InputStream resourceAsStream, String uploader)
			throws PropFileLoadException {

		SeqCollectionDoc seqCollection = this.mongoTemplate
				.findOne(new Query(Criteria.where("name").is(collectionName)), SeqCollectionDoc.class);

		// List<String> mandatoryColumns = Arrays.asList(new
		// String[]{"gene",""})
		List<String> errors = new ArrayList<String>();

		PropertyUpload upload = new PropertyUpload();
		upload.setTimestamp(new Date());
		upload.setUploader(uploader);
		upload.setErrors(errors);

		Integer linenum = 0;
		String csv_raw;
		String[] csv;
		try {
			csv_raw = IOUtils.toString(resourceAsStream, "UTF-8");
			csv = csv_raw.split("\n");
		} catch (IOException e) {
			throw new PropFileLoadException("error reading file", e);
		}

		try {
			CSVReader csvReader = new CSVReader(new StringReader(csv_raw), '\t');
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				// System.out.println(nextLine[0] + nextLine[1] + "etc...");
			}
			csvReader.close();
		} catch (Exception ex) {
			throw new PropFileLoadException("Invalid csv format");
		}

		List<String> header = null;

		for (String line : csv) {
			linenum++;
			if (!line.startsWith("#")) {
				header = Arrays.asList(line.split("\t"));
				break;
			}
		}

		List<String> headerProperties = header.stream().filter(p -> !p.equals(GENE_FIELD_IMPORT))
				.collect(Collectors.toList());
		upload.setProperties(headerProperties);

		Map<String, Set<String>> values = new HashMap<>();
		headerProperties.stream().forEach(p -> values.put(p, new HashSet<String>()));
		List<String> numericFields = new ArrayList<>();
		headerProperties.stream().forEach(p -> numericFields.add(p));

		if (!header.contains(GENE_FIELD_IMPORT)) {
			throw new PropFileLoadException("protein_id field not found");
		}

		List<Map<String, Object>> preLoad = new ArrayList<>();

		for (String line : Arrays.copyOfRange(csv, linenum, csv.length)) {
			linenum++;
			String fields[] = line.split("\t");
			String gene = fields[0].trim();

			if (gene.isEmpty()) {
				errors.add(linenum.toString() + " gene field is empty");
				continue;
			}
			if (header.size() != fields.length) {
				errors.add(linenum.toString() + " the line (" + fields.length + ") and the header(" + header.size()
						+ ") have diferent column count");
				continue;
			}

			long count = this.geneProductRepository
					.count(new Query(Criteria.where("organism").is(collectionName).and("alias").is(gene)));

			if (count == 0) {
				errors.add(linenum.toString() + " " + gene + " does not exists in " + collectionName);
				continue;
			}
			Map<String, Object> record = new HashMap<>();
			record.put(GENE_FIELD_IMPORT, gene);
			preLoad.add(record);

			for (int i = 0; i <= header.size() - 2; i++) {
				Object value = fields[i + 1];
				String fieldName = header.get(i + 1);

				try {
					if (!value.equals("NaN")) {
						value = Double.parseDouble(value.toString());
					}
				} catch (NumberFormatException ex) {
					if (numericFields.contains(fieldName)) {
						numericFields.remove(fieldName);
					}

				}
				record.put(fieldName, value);
				if (!numericFields.contains(fieldName)) {
					values.get(fieldName).add(value.toString());
				}

			}

		}
		for (Map<String, Object> record : preLoad) {
			DBObject q = new BasicDBObject("organism", collectionName);
			q.put("alias", record.get(GENE_FIELD_IMPORT));
			DBObject findOne = this.mongoTemplate.getCollection("proteins").findOne(q, new BasicDBObject("_id", 1));
			Object proteinId = findOne.get("_id");
			// List<GeneProductDoc> findAll =
			// this.geneProductRepository.findAll(new Query(
			// Criteria.where("organism").is(collectionName).and("alias").is(record.get(GENE_FIELD_IMPORT))));

			// GeneProductDoc geneProd = findAll.get(0);
			// String id = geneProd.getId();
			BulkWriteOperation bulk = this.mongoTemplate.getCollection("proteins").initializeUnorderedBulkOperation();
			for (String propertyName : headerProperties) {

				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("_type", uploader);
				// properties.put("_type", "metadata");
				// geneProd.getProperties().stream().filter(p ->
				// p.get("_type").equals(uploader) &&
				// p.get("property").equals(propertyName)).forEach(map -> {
				// properties.put(propertyName, record.get(propertyName) );
				//
				// });
				properties.put("property", propertyName);
				properties.put("value", record.get(propertyName));

				BasicDBObject addSearchToProtein = this.geneProductRepository.addSearchToProtein(uploader, propertyName,
						record.get(propertyName));
				bulk.find(new BasicDBObject("_id", proteinId)).update(addSearchToProtein);
				BasicDBObject removePropFromProtein = this.geneProductRepository.removePropFromProtein(uploader,
						propertyName);
				bulk.find(new BasicDBObject("_id", proteinId)).update(removePropFromProtein);
				BasicDBObject addPropToProtein = this.geneProductRepository.addPropToProtein(properties);
				bulk.find(new BasicDBObject("_id", proteinId)).update(addPropToProtein);

			}
			bulk.execute();
			;

		}
		headerProperties.stream().forEach(p -> {
			List<String> options = values.get(p).stream().collect(Collectors.toList());
			Optional<SeqColDruggabilityParam> currentDp = seqCollection.getDruggabilityParams().stream()
					.filter(dp -> dp.getName().equals(p) && dp.getUploader().equals(uploader)).findFirst();
			String dpType = (numericFields.contains(p)) ? "number" : "value";
			currentDp.ifPresent(dp -> {

				dp.setOptions(options);
				dp.setType(dpType);
			});
			if (!currentDp.isPresent()) {
				SeqColDruggabilityParam seqColDruggabilityParam = new SeqColDruggabilityParam();
				seqColDruggabilityParam.setName(p);
				seqColDruggabilityParam.setOptions(options);
				seqColDruggabilityParam.setUploader(uploader);
				seqColDruggabilityParam.setTarget("protein");
				seqColDruggabilityParam.setType(dpType);
				seqCollection.getDruggabilityParams().add(seqColDruggabilityParam);
			}
		});
		seqCollection.getUploads().add(upload);
		this.mongoTemplate.save(seqCollection);
		return upload;

	}

}
