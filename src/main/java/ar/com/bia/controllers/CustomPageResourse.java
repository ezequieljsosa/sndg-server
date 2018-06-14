package ar.com.bia.controllers;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.dto.PaginatedResult;
import ar.com.bia.entity.UserDoc;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/page")
public class CustomPageResourse {

	@Autowired
	private UserRepositoryImpl userRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private @Autowired
	HttpServletRequest request;

	@RequestMapping(value = "/{page_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"  )
	@ResponseBody
	public String page(@PathVariable("page_id") String pageId) {
		return findPage(pageId).toString();
	}
	private DBObject findPage(String pageId) {
		return this.mongoTemplate.getCollection("custom_pages").findOne(new BasicDBObject("_id", new ObjectId(pageId)));
	}
	@RequestMapping(value = "/table/{query_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResult<DBObject> table(@PathVariable("query_id") String query_id,
			@RequestParam(value = "length", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "search[value]", defaultValue = "") String search,
			@RequestParam(value = "columns") List<String> columns) {
		PaginatedResult<DBObject> paginatedResult  = new PaginatedResult<DBObject>();
		User authenticatedUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String name = authenticatedUser.getUsername();
		UserDoc user = userRepository.findUser(name);
		List<Map<String, String>> queries = user.getQueries();
		for (Map<String, String> map : queries) {
			if(map.get("name").equals(query_id)){
				
				DBObject tot_quety = (DBObject) JSON.parse(map.get("query"));
				DBObject query = tot_quety;
				
				if (!search.trim().isEmpty()){
					query=this.createCriteriaFromQueryString(columns, search);
				}
				
				DBObject sort = this.createSortFromQueryString();
				
				paginatedResult.setRecordsFiltered((long) this.mongoTemplate.getCollection(map.get("database")).count(query));
				paginatedResult.setRecordsTotal(this.mongoTemplate.getCollection(map.get("database")).count(tot_quety));
				paginatedResult.setData( new ArrayList<DBObject>());
				
				
				DBCursor result = this.mongoTemplate.getCollection(map.get("database")).find(query).sort(sort).limit(perPage).skip(offset);
				for (DBObject dbObject : result) {
					paginatedResult.getData().add(dbObject);
				}
				
		
				
				break;
			}
			
		}	
		
		return paginatedResult;
	}
	
	public DBObject createCriteriaFromQueryString(List<String> columns,String search) {
	

		BasicDBList conditions = new BasicDBList();
		DBObject criteria = new BasicDBObject("$or",conditions);
		if (search != null && !search.trim().isEmpty()) {
			String[] keywords = search.toLowerCase().replace(" +", " ")
					.split(" ");
			for (String keyword : keywords) {
				for (String column : columns) {
					conditions.add(new BasicDBObject( column , new BasicDBObject("$regex",keyword).append("$options", "i") ));
						
				}
			}
		}
		return criteria;
	}
	
	public DBObject createSortFromQueryString() {
		
		BasicDBList sortsResult = new BasicDBList();
		
		@SuppressWarnings("unchecked")
		Collection<String> sorts = new ArrayList<String>(this.request
				.getParameterMap().keySet());
		CollectionUtils.filter(sorts, new Predicate() {
			public boolean evaluate(Object arg0) {
				return ((String) arg0).startsWith("order");
			}
		});

		int sortColumnsCount = sorts.size() / 2;

		for (Integer i = 0; i < sortColumnsCount * 2; i += 2) {
			String sortColumnNum = this.request.getParameter("order["
					+ i.toString() + "][column]");
			String sortField = this.request.getParameter("columns["
					+ sortColumnNum + "][data]");
			int sortDirectionParam = this.request.getParameter(
					"order[" + i.toString() + "][dir]").equals("desc") ? 1
					: -1;

			sortsResult.add(new BasicDBObject(sortField,sortDirectionParam));
		}

		return sortsResult;
	}
	
	
	
}
