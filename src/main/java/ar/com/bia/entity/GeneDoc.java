package ar.com.bia.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author eze
 *
 */
public class GeneDoc extends SeqFeatureEmbedDoc {


	private String name;
	@Field("seq_collection_id")
	private ObjectId seqCollectionId;
	private String seq;

	
	
	@Field("product_id")
	private String productId;



	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getSeqCollectionId() {
		return seqCollectionId;
	}

	public void setSeqCollectionId(ObjectId seqCollectionId) {
		this.seqCollectionId = seqCollectionId;
	}

	@JsonProperty
	public String strCollectionId() {
		if (this.seqCollectionId != null)
			return this.seqCollectionId.toStringMongod();
		else {
			return "";
		}
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}


	
	
	
	

}
