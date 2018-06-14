package ar.com.bia.controllers;

import ar.com.bia.BioPage;
import ar.com.bia.backend.dao.GeneProductDocumentRepository;
import ar.com.bia.backend.dao.SeqCollectionRepository;
import ar.com.bia.dto.GeneProductPaginatedResult;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.dto.StrainQuery;
import ar.com.bia.entity.*;
import ar.com.bia.entity.druggability.SeqColDruggabilityParam;
import ar.com.bia.entity.var.VarDoc;
import ar.com.bia.services.OntologyService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.AlignedSequence;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/variant")
public class VariantResource {

    @Autowired
    private SeqCollectionRepository seqCollectionRepository;

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private ObjectMapper mapperJson;

    @Autowired
    private GeneProductDocumentRepository geneProductRepository;


    private @Autowired
    HttpServletRequest request;

    @Autowired
    private SeqCollectionRepository seqCollectionRepo;

    @Autowired
    private OntologyService ontologyService;

    @RequestMapping(value = "/{project_id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String allGeneProds(@PathVariable("project_id") String projectId, Model model, Principal principal)
            throws JsonProcessingException {

        SeqCollectionDoc genome = this.seqCollectionRepo.findByProject(projectId);

        StrainProject project = genome.getStrainProjects().stream().filter(x -> x.getId().equals(projectId)).findFirst().get();
        model.addAttribute("project", mapperJson.writeValueAsString(project));
        model.addAttribute("user", principal);
        model.addAttribute("genome_id", genome.getName());
        model.addAttribute("strains", mapperJson.writeValueAsString(genome.projectStrains(project)));

        model.addAttribute("organism", genome.getName());
        List<OntologyTerm> ontologies = this.ontologyService.varOntologies(genome);

        model.addAttribute("ontologies", mapperJson.writeValueAsString(ontologies));
        List<SeqColDruggabilityParam> druggabilityParams = genome.getDruggabilityParams().stream().filter(x -> {
            return (x.getUploader() == null) || (x.getUploader().equals("demo"))
                    || (x.getUploader().equals(principal.getName()));
        }).collect(Collectors.toList());
        model.addAttribute("searchProps", mapperJson.writeValueAsString(druggabilityParams));

        return "search/Variant";
    }

    @RequestMapping(value = "/{projectId}/tree", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String tree(@PathVariable("projectId") String projectId, @RequestParam(value = "selected", defaultValue = "") String selectedTree,
                       Model model, Principal principal)
            throws IOException {

        SeqCollectionDoc genome = this.seqCollectionRepo.findByProject(projectId);
        StrainProject project = genome.getStrainProjects().stream()
                .filter(x -> x.getId().equals(projectId)).findFirst().get();


        model.addAttribute("user", principal);
        if (selectedTree.isEmpty()) {
            model.addAttribute("defaultTree", 0);
        } else {
            model.addAttribute("defaultTree", IntStream.range(0, project.getTrees().size())
                    .filter(i -> selectedTree.equals(project.getTrees().get(i).get("name")))
                    .findFirst().getAsInt());
        }


        model.addAttribute("genome", this.mapperJson.writeValueAsString(genome));
        String path = "/data/organismos/" + genome.getName() + "/projects/" + projectId + "/";
        StringJoiner trees = new StringJoiner(",");

        project.getTrees().forEach(x -> {
                    String filePath = path + x.get("name") + ".newick";
                    Path path1 = Paths.get(filePath);
                    if (Files.exists(path1)) {
                        try {

                            String newick = "'" + new String(Files.readAllBytes(path1)).replace("\n", "")  + "'";
                            trees.add(newick);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        trees.add("'No data'");
                    }


                }
        );


        model.addAttribute("project", mapperJson.writeValueAsString(project) );
        model.addAttribute("trees", "[" + trees.toString() + "]" );
        model.addAttribute("strains", mapperJson.writeValueAsString(
                new String[]{"15-6324_S3_L001", "16-12-16-138_S2_L001", "1109_S1_L001", "2003_S4_L001"}));


        return "comparative/Tree";
    }


    @RequestMapping(value = "/{genome_id}/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String variantQuery(@PathVariable("genome_id") String organism,
                               @RequestBody String strainQuery) throws JsonParseException, JsonMappingException, IOException {

        StrainQuery dto = new StrainQuery();
        if (strainQuery.isEmpty()) {
            PaginatedResult<VarDoc> result = defaultSearch(dto, organism);
            return mapperJson.writeValueAsString(result);
        }
        @SuppressWarnings("deprecation")
        String replace = URLDecoder.decode(strainQuery).replace("}=", "}").replace(".-", "");
        dto = mapperJson.readValue(replace, StrainQuery.class);
        if (dto.getProteinFilters().isEmpty() && dto.getVariant_type().isEmpty() && dto.getStrainComp().isEmpty()) {
            PaginatedResult<VarDoc> result = defaultSearch(dto, organism);

            return mapperJson.writeValueAsString(result);
        }


        PaginatedResult<VarDoc> result = new PaginatedResult<VarDoc>();
        Collection<Criteria> criterias = new ArrayList<>();
        Criteria criteria = Criteria.where("organism").is(organism).and("sample_alleles.0").exists(true);
        criteria.and("sample_alleles.samples.sample").in(dto.getStrains());

        long recordsTotal = mongoTemplate.count(new Query(criteria), VarDoc.class);


        criterias.add(criteria);
        dto.proteinFilters.stream().forEach(filter -> {
            if (filter.getName().equals("ontology")) {
                criterias.add(Criteria.where("ontologies").is(filter.getValue()));
            } else {
                criterias.add(filter.mongoFilter());
            }

        });


        if (!dto.getVariant_type().isEmpty() && !dto.getVariant_type().contains("All")) {
            criterias.add(Criteria.where("sample_alleles.variant_type").in(dto.getVariant_type()));
        }

        Criteria all = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));

        Query query = new Query(all);


        query.with(new Sort(new Order(Sort.Direction.ASC, "contig"), new Order(Sort.Direction.ASC, "pos")));

        //query.limit(dto.getPageSize());
        query.limit(1000);

        query.skip(dto.getPageNumber() * dto.getPageSize());

        List<VarDoc> data = mongoTemplate.find(query, VarDoc.class);
        StrainQuery dto2 = dto;

        if (dto2.getStrainComp().size() > 0) {
            data = data.stream().filter(var -> {
                return dto2.getStrainComp().stream().anyMatch(strainComp -> {
                    return strainComp.compare(var);
                });
            }).collect(Collectors.toList());
        }
//		data = data.stream().filter(var -> {
//			return var.strainVarType(dto2.getStrains(),dto2.getVariant_type());
//		}).collect(Collectors.toList());


        result.setData(data);
        result.setRecordsTotal(recordsTotal);
        result.setRecordsFiltered((long) data.size());

        return mapperJson.writeValueAsString(result);

    }


    private PaginatedResult<VarDoc> defaultSearch(StrainQuery dto, String organism) {
        PaginatedResult<VarDoc> result = new PaginatedResult<VarDoc>();
        Criteria criteria = Criteria.where("organism").is(organism).and("sample_alleles.0").exists(true);
        criteria.and("sample_alleles.samples.sample").in(dto.getStrains());
        Query query = new Query(criteria);
        query.with(new Sort(new Order(Sort.Direction.ASC, "contig"), new Order(Sort.Direction.ASC, "pos")));
        query.limit(dto.getPageSize());
        query.skip(dto.getPageNumber() * dto.getPageSize());
        long recordsTotal = mongoTemplate.count(query, VarDoc.class);
        result.setData(mongoTemplate.find(query, VarDoc.class));
        result.setRecordsTotal(recordsTotal);
        result.setRecordsFiltered(recordsTotal);
        return result;
    }

    @RequestMapping(value = "gtfgnfnhjg/{organism}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GeneProductPaginatedResult variants(@PathVariable("organism") String organism,
                                               @RequestParam(value = "strains", defaultValue = "strain1") List<String> strainList,
                                               @RequestParam(value = "length", defaultValue = "10") Integer perPage,
                                               @RequestParam(value = "start", defaultValue = "0") Integer offset,
                                               @RequestParam(value = "search[value]", defaultValue = "") String search) {

        GeneProductPaginatedResult result = new GeneProductPaginatedResult();
        /*
         * User authenticatedUser = (User) SecurityContextHolder.getContext()
         * .getAuthentication().getPrincipal(); String name =
         * authenticatedUser.getUsername();
         *
         * List<ObjectId> userGenomes = userRepository.findUser(name)
         * .getSeqCollections();
         */

        List<SeqCollectionDoc> strains = this.seqCollectionRepository.seqCollectionsFromOrganism(organism);

        Criteria genesOfGenomeCriteria = Criteria.where("id").in(strainList);

        result.setRecordsTotal(this.geneProductRepository.count(new Query(genesOfGenomeCriteria)));

        Criteria[] searchFilterCriteria = this.createCriteriaFromQueryString("keywords", search);

        Criteria allCriterias = new Criteria().andOperator(searchFilterCriteria);
        Query query = new Query(allCriterias);

        Sort sort = this.createSortFromQueryString();

        BioPage pageObj = new BioPage();
        pageObj.setPage(new Long(new Double(Math.ceil(offset / perPage)).longValue()).intValue());
        pageObj.setPageSize(perPage);
        pageObj.setSort(sort);

        // result.setRecordsFiltered((int)
        // this.geneProductRepository.count(query));
        query = query.with(pageObj);

        List<GeneProductDoc> genomeList = this.geneProductRepository.findAll(query);

        result.setData(genomeList);

        Map<String, String> fila1 = new HashMap<String, String>();

        fila1.put("gen", "minc1");
        fila1.put("description", "ATP-synthetase");
        for (String strain : strainList) {
            fila1.put(strain, "X");
        }

        // result.add(fila1);

        fila1 = new HashMap<String, String>();
        fila1.put("gen", "minc1");
        fila1.put("description", "zinc-finger");
        for (String strain : strainList) {
            fila1.put(strain, "X");
        }

        // result.add(fila1);

        return result;

    }

    public Criteria[] createCriteriaFromQueryString(String field, String search) {
        if (search != null && !search.trim().isEmpty()) {
            String[] keywords = search.toLowerCase().replace(" +", " ").split(" ");
            List<Criteria> criterias = new ArrayList<Criteria>();

            for (String keyword : keywords) {
                criterias.add(Criteria.where(field).regex(keyword, "i"));
            }

            return criterias.toArray(new Criteria[criterias.size()]);
        } else {
            return new Criteria[0];
        }

    }

    public Sort createSortFromQueryString() {

        @SuppressWarnings("unchecked")
        Collection<String> sorts = new ArrayList<String>(this.request.getParameterMap().keySet());
        CollectionUtils.filter(sorts, new Predicate() {
            public boolean evaluate(Object arg0) {
                return ((String) arg0).startsWith("order");
            }
        });

        int sortColumnsCount = sorts.size() / 2;
        Sort.Order[] orders = new Sort.Order[sortColumnsCount];

        for (Integer i = 0; i < sortColumnsCount * 2; i += 2) {
            String sortColumnNum = this.request.getParameter("order[" + i.toString() + "][column]");
            String sortField = this.request.getParameter("columns[" + sortColumnNum + "][data]");
            Direction sortDirectionParam = this.request.getParameter("order[" + i.toString() + "][dir]").equals("desc")
                    ? Direction.DESC : Direction.ASC;

            orders[i] = new Sort.Order(sortDirectionParam, sortField);
        }

        return new Sort(orders);
    }

    /**
     * @param genomeId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "x/{genome_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String variants(@PathVariable("genome_id") String genomeId,
                           @RequestParam(value = "keywords", defaultValue = "") String keywords) throws IOException {
        if (genomeId.trim().isEmpty()) {
            throw new RuntimeException("genome parameter in querystring cannot be empty");
        }
        // Obtiene todas las proteinas del organismo y las cepas
        Map<String, GeneProductDoc> all_proteins = metagenome(genomeId, keywords);

        StringBuilder sb = new StringBuilder();

        sb.append("strain\tsymbol\thas_variant\n");

        // Map {id de genoma: organismo}
        Map<String, String> id_organism = new HashMap<String, String>();

        // Obtiene el genoma de referencia
        SeqCollectionDoc ref_genome = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(genomeId)),
                SeqCollectionDoc.class);
        id_organism.put(ref_genome.getId(), ref_genome.getOrganism());

        // Obtiene las cepas
        List<SeqCollectionDoc> strains_genome = this.mongoTemplate
                .find(new Query(Criteria.where("_id").in(ref_genome.getStrains())), SeqCollectionDoc.class);

        for (SeqCollectionDoc strain_genome : strains_genome) {
            id_organism.put(strain_genome.getId(), strain_genome.getOrganism());
        }

        // Por cada proteina
        for (String protein_key : all_proteins.keySet()) {
            GeneProductDoc protein = all_proteins.get(protein_key);
            // Indica que existe en el organismo que pertenece
            sb.append(protein.getOrganism() + "\t" + protein.getName() + "\ttrue\n");
            // si tiene variantes
            if (protein.getVariants() != null) {
                for (VariantEmbedDoc variant : protein.getVariants()) {
                    // indica que organismo tiene la variante
                    sb.append(id_organism.get(variant.getStrain_id()) + "\t" + protein.getName() + "\ttrue\n");
                }
            }
        }
        return sb.toString();
    }

    @RequestMapping(value = "x/{genome_id}/genes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String genes(@PathVariable("genome_id") String genomeId,
                        @RequestParam(value = "keywords", defaultValue = "") String keywords) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("symbol\tgene\tid\n");

        Map<String, GeneProductDoc> all_proteins = metagenome(genomeId, keywords);

        for (String protein_id : all_proteins.keySet()) {
            GeneProductDoc protein = all_proteins.get(protein_id);
            sb.append(protein.getName() + "\t" + protein.getGene() + "\t" + protein_id + "\n");
        }

        return sb.toString();

    }

    /**
     * A partir de un identificador de genoma, busca las cepas y retorna el
     * listado de proteinas
     *
     * @param genomeId: Identificador de genoma
     * @return Diccionario de proteinas, con clave de identificador de proteina,
     * de todas las proteinas de la referencia y sus cepas.
     */
    private Map<String, GeneProductDoc> metagenome(String genomeId, String keywords_filter) {
        // busca en la colección sequence_collection
        SeqCollectionDoc ref_genome = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(genomeId)),
                SeqCollectionDoc.class);

        // Guarda el identificador del genoma de referencia y las cepas en una
        // lista
        List<ObjectId> genomes = null ;// ref_genome.getStrains();
        genomes.add(new ObjectId(ref_genome.getId()));

        Criteria[] searchFilterCriteria = this.createCriteriaFromQueryString("keywords", keywords_filter);

        Criteria allCriterias = Criteria.where("seq_collection_id").in(genomes);
        if (searchFilterCriteria.length != 0) {
            allCriterias = allCriterias.andOperator(searchFilterCriteria);
        }

        Query query = new Query(allCriterias);

        // Con la lista de identificadores busca las proteinas
        // List<GeneProductDoc> proteins = geneProductRepository
        // .findAll(new Query(Criteria.where("seq_collection_id").in(
        // genomes)));

        List<GeneProductDoc> proteins = geneProductRepository.findAll(query);

        // Genera un diccionario con idetificador de proteina como clave, y
        // valor a la proteina
        Map<String, GeneProductDoc> all_proteins = new HashMap<String, GeneProductDoc>();
        List<String> proteins_to_remove = new ArrayList<String>();
        for (GeneProductDoc protein : proteins) {
            if (protein.getVariants() != null) {
                for (VariantEmbedDoc variant : protein.getVariants()) {
                    // Obtiene las variates para eliminarlas del listado final
                    proteins_to_remove.add(variant.getProtein_id());
                }
            }
            all_proteins.put(protein.getId(), protein);
        }

        // Elimina a las variantes del diccionario a devolver
        for (String protein : proteins_to_remove) {
            all_proteins.remove(protein);
        }
        return all_proteins;
    }

    /**
     * Devuelve el listado de cepas de un organismo
     *
     * @param genomeId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "x/{genome_id}/strains", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String strains(@PathVariable("genome_id") String genomeId) throws IOException {

        StringBuilder sb = new StringBuilder();

        SeqCollectionDoc ref_genome = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(genomeId)),
                SeqCollectionDoc.class);

        sb.append("strain\n");
        sb.append(ref_genome.getOrganism() + "\n");

        List<SeqCollectionDoc> strains_genome = this.mongoTemplate
                .find(new Query(Criteria.where("_id").in(ref_genome.getStrains())), SeqCollectionDoc.class);

        for (SeqCollectionDoc strain : strains_genome) {
            sb.append(strain.getOrganism() + "\n");
        }

        return sb.toString();

    }

    /**
     * Realiza un alineamiento de secuencias múltiples (msa) entre proteinas de
     * varias cepas de un organismo. El resultado es compatible para la
     * visualización con la libreria MSA de BioJs. http://biojs-msa.org/
     *
     * @param genomeId:  Identificador de genoma del organismo
     * @param proteinId: Identificador de proteina de referencia
     * @return JSON con entrada "name" que contiene el nombre de la proteina y
     * entrada "msa" con los datos usados en la visualización msa de
     * BioJs
     * @throws IOException
     * @throws CompoundNotFoundException
     */
    @RequestMapping(value = "x/{genome_id}/{prontein_id}/msa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> msa(@PathVariable("genome_id") String genomeId,
                                   @PathVariable("prontein_id") String proteinId) throws IOException, CompoundNotFoundException {

        // Diccionaro de retorno
        Map<String, Object> ret = new HashMap<String, Object>();
        // Lista de diccionario para devolver el msa
        List<Map<String, String>> msa = new ArrayList<Map<String, String>>();

        // Listado de secuencias para hacer msa
        List<ProteinSequence> seq_lst = new ArrayList<ProteinSequence>();

        // Diccionario a guardar en msa
        Map<String, String> value = new HashMap<String, String>();

        // obtiene la proteina
        GeneProductDoc protein = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(proteinId)),
                GeneProductDoc.class);
        String protein_name = protein.getName();
        // guarda secuencia
        seq_lst.add(new ProteinSequence(protein.getSequence()));
        // guarda registro en msa
        value.put("id", protein.getId());
        value.put("name", protein.getOrganism());
        msa.add(value);

        // si la protiena tiene variantes
        if (protein.getVariants() != null) {

            // busca las variantes
            for (VariantEmbedDoc variant : protein.getVariants()) {
                Map<String, String> v = new HashMap<String, String>();
                protein = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(variant.getProtein_id())),
                        GeneProductDoc.class);
                // guarda secuencia
                seq_lst.add(new ProteinSequence(protein.getSequence()));
                // guarda registro en msa
                v.put("id", protein.getId());
                v.put("name", protein.getOrganism());
                msa.add(v);
            }

            // Realiza alineamiento multiple
            Profile<ProteinSequence, AminoAcidCompound> profile = Alignments.getMultipleSequenceAlignment(seq_lst);
            int i = 0;
            // guarda alineamiento asociado a cada proteina
            for (AlignedSequence<ProteinSequence, AminoAcidCompound> seq : profile.getAlignedSequences()) {
                msa.get(i++).put("seq", seq.toString());
            }

        } else
            value.put("seq", protein.getSequence());

        // Arma salida a partir de msa y el nombre de la proteina
        ret.put("name", protein_name);
        ret.put("msa", msa);

        return ret;

    }

}
