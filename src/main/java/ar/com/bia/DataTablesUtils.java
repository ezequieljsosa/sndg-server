package ar.com.bia;

import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.services.UserService;
import ar.com.bia.services.exception.OrganismNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataTablesUtils {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserService userService;

	public DataTablesUtils() {
		super();
	}

	public Criteria[] createCriteriaFromQueryString(String field, String search) {
		// String search = (String) CollectionUtils.find(this.request
		// .getParameterMap().keySet(), new Predicate() {
		// public boolean evaluate(Object arg0) {
		// return ((String) arg0).startsWith("queries");
		// }
		// });
		if (search != null && !search.trim().isEmpty()) {
			String[] keywords = search.toLowerCase().split(" - ")[0].replace(" +", " ").split(" ");
			List<Criteria> criterias = new ArrayList<Criteria>();

			for (String raw_keyword : keywords) {
				String keyword = raw_keyword.toLowerCase().trim();
				if (keyword.isEmpty()) {
					continue;
				}
				criterias.add(Criteria.where(field).not().regex("^" + keyword));
			}

			return criterias.toArray(new Criteria[criterias.size()]);
		} else {
			return new Criteria[0];
		}

	}

	public String getOrganismFromColumn() throws OrganismNotFoundException {

		@SuppressWarnings("unchecked")
		Collection<String> columnParams = new ArrayList<String>(this.request.getParameterMap().keySet());
		CollectionUtils.filter(columnParams, new Predicate() {
			public boolean evaluate(Object arg0) {
				return ((String) arg0).startsWith("columns");
			}
		});
		int columnCount = columnParams.size() / 6;


		for (Integer i = 0; i < columnCount; i++) {
			String columnName = this.request.getParameter("columns[" + i.toString() + "][name]");
			if (columnName.trim().isEmpty()) {
				columnName = this.request.getParameter("columns[" + i.toString() + "][data]");
			}
			String columnFilter = this.request.getParameter("columns[" + i.toString() + "][search][value]");
			if (columnFilter.replace("^$", "").trim().isEmpty()) {
				continue;
			}
			
				columnFilter = columnFilter.replace("$", "").replace("^", "");
			if (columnName.equals("organism")){
				return columnFilter;
			}
				
			

		}

		throw new OrganismNotFoundException();

	}

	public Criteria[] createCriteriaFromQueryString2(String field, String search) {

		if (search != null && !search.trim().isEmpty()) {
			String[] keywords = search.toLowerCase().split(" - ")[0].replace(" +", " ").split(" ");
			List<Criteria> criterias = new ArrayList<Criteria>();

			for (String raw_keyword : keywords) {
				String keyword = raw_keyword.toLowerCase().trim();
				if (keyword.isEmpty()) {
					continue;
				}

				Criteria criteria = Criteria.where(field);
				if (!keyword.startsWith("!")) {
					criteria.regex("^" + keyword);
				} else {
					criteria.not().regex("^" + keyword.substring(1, keyword.length()));
				}

				criterias.add(criteria);
			}

			return criterias.toArray(new Criteria[criterias.size()]);
		} else {
			return new Criteria[0];
		}

	}

	public Criteria[] createCriteriaFromQueryStringColumns() {

		@SuppressWarnings("unchecked")
		Collection<String> columnParams = new ArrayList<String>(this.request.getParameterMap().keySet());
		CollectionUtils.filter(columnParams, new Predicate() {
			public boolean evaluate(Object arg0) {
				return ((String) arg0).startsWith("columns");
			}
		});
		int columnCount = columnParams.size() / 6;
		List<Criteria> criterias = new ArrayList<Criteria>();

		for (Integer i = 0; i < columnCount; i++) {
			String columnName = this.request.getParameter("columns[" + i.toString() + "][name]");
			if (columnName.trim().isEmpty()) {
				columnName = this.request.getParameter("columns[" + i.toString() + "][data]");
			}
			String columnFilter = this.request.getParameter("columns[" + i.toString() + "][search][value]");
			if (columnFilter.replace("^$", "").trim().isEmpty()) {
				continue;
			}
			boolean regex = Boolean
					.parseBoolean(this.request.getParameter("columns[" + i.toString() + "][search][regex]"));
			if (regex) {
				criterias.add(Criteria.where(columnName).regex(columnFilter, "i"));
			} else {
				columnFilter = columnFilter.replace("$", "").replace("^", "");
				criterias.add(Criteria.where(columnName).is(columnFilter));
			}

		}
		return criterias.toArray(new Criteria[criterias.size()]);
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

	public Criteria[] andCriteriaOperator(Criteria[] searchFilterCriteria, Criteria[] columnsSeach) {
		Criteria[] allCriterias = new Criteria[columnsSeach.length + searchFilterCriteria.length];

		int i = 0;
		for (Criteria criteria : searchFilterCriteria) {
			allCriterias[i] = criteria;
			i++;
		}
		for (Criteria criteria : columnsSeach) {
			allCriterias[i] = criteria;
			i++;
		}
		return allCriterias;
	}

	public static <E extends Object> PaginatedResult<E> queryCollection(int queryOffset, Integer perPage, Sort sortObj,
			List<String> auths, Criteria filtered, Class<E> class1, MongoOperations mongoTemplate,
			long posibleCount) {

		// long posibleCount = 0;
		// for (ObjectId auth : auths) {
		// posibleCount += mongoTemplate.count(new
		// Query(Criteria.where("auth").is(auth)), class1);
		// }
		long filteredCount = 0;
		for (String auth : auths) {
			Query query = new Query(Criteria.where("auth").is(auth));
			query.addCriteria(filtered);
			filteredCount += mongoTemplate.count(query, class1);
		}

		Query query = new Query(Criteria.where("auth").in(auths));
		query.addCriteria(filtered);

		BioPage pageObj = new BioPage(queryOffset, perPage, sortObj);
		query.with(pageObj);

		List<E> genomeList = mongoTemplate.find(query, class1);

		PaginatedResult<E> result = new PaginatedResult<E>();
		result.setRecordsTotal(posibleCount);
		result.setRecordsFiltered(filteredCount);
		result.setData(genomeList);
		return result;
	}

	public List<String> authCriteria(Principal principal) {
		String name = principal.getName();
		UserDoc user = this.userService.findUser(name);
		if (user == null) {
			throw new UsernameNotFoundException(name);
		}

		List<String> list = new ArrayList<>();
		list.add(user.getId());
		if (!user.getId().equals(UserDoc.publicUserId.toString())) {
			list.add(UserDoc.publicUserId.toString());
		}
		return list;
	}

}
