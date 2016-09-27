package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.CommentDto;

@Action("CommentsGetOfCbtion")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^commentDtos.*,^resStatus.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class CommentsGetOfCbtion extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input parameters  */
	private int cbtionId;
	public int getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(int cbtionId) {
		this.cbtionId = cbtionId;
	}

	/* Output parameters  */
	private List<CommentDto> commentDtos = new ArrayList<CommentDto>();
	public List<CommentDto> getCommentDtos() {
		return commentDtos;
	}
	public void setCommentDtos(List<CommentDto> commentDtos) {
		this.commentDtos = commentDtos;
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		commentDtos = dbServices.cbtionGetCommentsDtos(cbtionId);
		
		resStatus.setMsg("comments retrieved");
		resStatus.setSuccess(true);
		
     	return SUCCESS;
    }
}
