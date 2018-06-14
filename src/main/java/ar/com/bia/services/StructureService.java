package ar.com.bia.services;

import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.StructureRepository;
import ar.com.bia.dto.PocketData;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.Location;
import ar.com.bia.entity.SeqFeature;
import ar.com.bia.entity.SeqFeatureEmbedDoc;
import ar.com.bia.entity.aln.SimpleAligment;
import ar.com.bia.pdb.ChainDoc;
import ar.com.bia.pdb.HmmScanResultFeature;
import ar.com.bia.pdb.StructureDoc;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class StructureService {

	@Autowired
	private ObjectMapper mapperJson;

	@Autowired
	private StructureRepository structureRepository;

	@Autowired
	private GeneProductDocumentRepository geneProdRepository;

	public List<StructureDoc> structures(GeneProductDoc protein) {
		List<String> alias = protein.getAlias();
		if (alias == null){
			alias = protein.getGeneList().stream().map(String.class::cast).collect(Collectors.toList());
		}

		Criteria criteria = Criteria.where("organism").is(protein.getOrganism()).and("templates.aln_query.name")
				.in(alias).and("_cls").is("Structure.ModeledStructure");

		List<StructureDoc> findAll = this.structureRepository.findAll(new Query(criteria));
		List<String> addedStructs = new ArrayList<String>();
		List<SeqFeatureEmbedDoc> features = protein.getFeatures();
		for (SeqFeature feature : features) {
			if (feature.getType().equals("SO:0001079")) {
				String strId = feature.getIdentifier().substring(0, 4);
				if (!addedStructs.contains(strId)) {
					addedStructs.add(strId);
					List<StructureDoc> findAll2 = this.structureRepository.findAll(new Query(
							Criteria.where("name").in(strId).and("_cls").is("Structure.ExperimentalStructure")));
					if (findAll2.size() > 0) {
						StructureDoc s = findAll2.get(0);
						findAll.add(s);
					}

				}

			}
		}
		return findAll;
	}

	public String pdb_structure(StructureDoc structure, List<String> chains) throws FileNotFoundException, IOException {

		return FileUtils.readFileToString(new File(structure.getFilePath()));
	}

	public String pdb_structure(StructureDoc structure) throws IOException {
		return this.pdb_structure(structure, new ArrayList<String>());
	}

	public String vmd_style(List<PocketData> pocket_pdbs) {

		// str_variants = " or ".join([ "(" + ("chain " + x.split("_")[1] + "
		// and " if x.split("_")[1].strip() else "") + "resid " +
		// x.split("_")[2] + ")" for x in variant_list if x])
		StringJoiner stringJoiner = new StringJoiner("");
		stringJoiner.add("set id [[atomselect 0 \"protein\"] molid]\n" + "mol delrep 0 $id\n"
				+ "mol representation \"NewRibbons\"\n" +

				"mol material \"Opaque\"\n" + "mol color Chain\n" + "mol selection \"protein\"\n" + "mol addrep $id\n" + 
		"mol representation \"VDW\"\n" + "mol color Element\n"
		+ "mol selection \"not protein and not resname HOH and not resname STP\"\n" + "mol addrep $id\n");

		pocket_pdbs.stream().forEach(x -> {
			StringJoiner sj = new StringJoiner(" ");
			if(x.getProperties().get("Druggability Score") >= 0.2){
			
				x.getAtoms().stream().forEach(idx -> sj.add(idx.toString()));
				String rep = 
						"mol representation \"VDW\"\n" + "mol color Element\n" + "mol selection \" resname  STP and resid  " + x.getNumber().toString() + "\"\n" + 
						
						"mol addrep $id\n" + 
						"mol representation \"Bonds\"\n" + "mol color Element\n" + "mol selection \" index " + sj.toString()
						+ "\"\n" + "mol addrep $id\n";
				stringJoiner.add(rep);
				
			}
			

		});
		return stringJoiner.toString();
	}

	public File zipFile(StructureDoc structure) throws IOException {

		List<PocketData> pocket_pdbs = this.pockets_pdb(structure, null);
		String raw_str = this.pdb_structure(structure, null);
		StringJoiner joiner = new StringJoiner("\n");
		String[] split = raw_str.split("\n");
		Arrays.asList(split).stream().filter(x -> !x.trim().equals("TER") && !x.trim().equals("END")).map(x -> x.trim())
				.forEach(joiner::add);
		List<PocketData> collect = pocket_pdbs.stream().filter(x -> x.getProperties().get("Druggability Score") >= 0.2).collect(Collectors.toList());
		collect.stream().forEach(x -> {
			
			x.getAs_lines().stream().map(y -> y.replace("\n", "")).forEach(joiner::add);
			});

		String pdb = joiner.toString() + "\nTER\nEND";

		Path pdbFile = Paths.get("/tmp/" + structure.getName() + ".pdb");
		Files.write(pdbFile, pdb.getBytes(), StandardOpenOption.CREATE,
		         StandardOpenOption.TRUNCATE_EXISTING);

		Path tclFile = Paths.get("/tmp/" + structure.getName() + ".tcl");
		Files.write(tclFile, this.vmd_style(pocket_pdbs).getBytes(), StandardOpenOption.CREATE,
		         StandardOpenOption.TRUNCATE_EXISTING);

		File file = new File("/tmp/" + structure.getName() + ".zip");
		if (file.exists()) {
			file.delete();
		}
			ZipOutputStream out = null;
			try {
				out = new ZipOutputStream(new FileOutputStream(file));

				addFileToZip(out, pdbFile.toFile());
				addFileToZip(out, tclFile.toFile());

			} catch (Exception ex) {
				file.delete();
			} finally {

				out.close();

			}

		
		return file;
	}

	private void addFileToZip(ZipOutputStream zipStream, File fileToAdd) throws IOException {
		FileInputStream in = null;

		in = new FileInputStream(fileToAdd.getAbsoluteFile());

		try {
			zipStream.putNextEntry(new ZipEntry(fileToAdd.getName()));
			byte[] b = new byte[1024];
			int count;
			while ((count = in.read(b)) > 0) {
				zipStream.write(b, 0, count);
			}
		} finally {
			in.close();
		}

	}

	public List<HmmScanResultFeature> model_features(StructureDoc structure, String organism) throws IOException {

		ArrayList<HmmScanResultFeature> arrayList = new ArrayList<HmmScanResultFeature>();

		// FIXME
		if (structure.getName().length() == 4) {
			return arrayList;
		}

		List<GeneProductDoc> findAll = new ArrayList<GeneProductDoc>();
		for (SimpleAligment template : structure.getTemplates()) {
			List<GeneProductDoc> findAll2 = this.geneProdRepository
					.findAll(new Query(Criteria.where("name").in(template.getAln_query().getName()).and("organism").is(organism)) );
			if (findAll2.size() > 0) {
				findAll.add(findAll2.get(0));
			}

		}

		if (!findAll.isEmpty()) {
			for (GeneProductDoc geneProductDoc : findAll) {

				List<SeqFeatureEmbedDoc> features = geneProductDoc.getFeatures();
				if (features != null) {
					for (SeqFeature feature : features) {

						Location referencedLocus = feature.getLocation();

						HmmScanResultFeature structureFeature = new HmmScanResultFeature(feature.getIdentifier(),
								referencedLocus.getStart(), referencedLocus.getEnd(), feature.getId(),
								feature.getDescription(), "uh?");
						structureFeature.setChain(" ");

						arrayList.add(structureFeature);

					}
				}
			}

		}

		return arrayList;

	}

	public List<StructureDoc> pdbs(String query) throws IOException {

		List<String> pdbs = new ArrayList<String>();
		for (String string : query.split(",")) {
			String code = string.trim().split("_")[0];
			if (code.length() == 4) {
				pdbs.add(code);
			}
		}
		if (pdbs.isEmpty())
			return new ArrayList<StructureDoc>();

		return this.structureRepository.findAll(new Query(Criteria.where("_id").in(pdbs)));

	}

	public StructureDoc structure(String structureName) {
		return this.structureRepository.findByName(structureName);
	}

	public StructureDoc structure(String structureName, List<String> chains) {
		StructureDoc structure = this.structureRepository.findByName(structureName);
		List<ChainDoc> toRemove = new ArrayList<>();
		for (ChainDoc chain : structure.getChains()) {
			if (!chains.isEmpty() && !chains.contains(chain.getName())) {

				// FIXME hacer un metodo directo
				toRemove.add(chain);
			}
		}
		for (ChainDoc chain : toRemove) {
			structure.getChains().remove(chain);
		}
		return structure;
	}

	// TODO este metodo tendria que ser responsabilida de la estructura???
	public List<GeneProductDoc> proteins(StructureDoc structure) {
		List<GeneProductDoc> proteins = new ArrayList<>();

		if (structure.isModel()) {
			for (SimpleAligment aln : structure.getTemplates()) {
				GeneProductDoc protein = this.geneProdRepository.findByName(aln.getAln_query().getName());
				if (protein != null) {
					proteins.add(protein);
				}

			}

		} else {
			for (ChainDoc chain : structure.getChains()) {

				GeneProductDoc protein = this.geneProdRepository
						.findByName(structure.getName() + "_" + chain.getName());
				if (protein != null) {
					proteins.add(protein);
				}
			}
		}
		return proteins;
	}

	public List<PocketData> pockets_pdb(StructureDoc structure, List<String> chains)
			throws JsonParseException, JsonMappingException, IOException {
		// "/data/organismos/" +
		// structure.getOrganism() + "/estructura/"
		// + structure.getPipeline() + "/pockets/" + structure.getName() +
		// ".json"
		File pocketsJson = new File(structure.getFilePocketPath());

		Class<?> clz;
		try {
			clz = Class.forName("ar.com.bia.dto.PocketData");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		JavaType type = mapperJson.getTypeFactory().constructCollectionType(List.class, clz);
		List<PocketData> result = mapperJson.readValue(pocketsJson, type);
		return result;
	}

	// public String pdb_heatatom(StructureDoc structure) throws IOException {
	// BufferedReader br = new BufferedReader(new
	// FileReader(structure.getFilePath()));
	// try {
	// StringBuilder strBuilder = new StringBuilder();
	// String line = "\'";
	// while ((line = br.readLine()) != null) {
	//
	// if ((line.length() > 6) && line.substring(0, 6).equals("HETATM")) {
	// strBuilder.append(line);
	// strBuilder.append("\n");
	// }
	// }
	// return strBuilder.toString() + "\'";
	// } finally {
	// br.close();
	// }
	// }

}
