package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.CommentDto;

@Action("CommentGetReplies")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^repliesDtos.*,^resStatus.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class CommentGetReplies extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input parameters  */
	private int commentId;
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	/* Output parameters  */
	private List<CommentDto> repliesDtos = new ArrayList<CommentDto>();
	public List<CommentDto> getRepliesDtos() {
		return repliesDtos;
	}
	public void setRepliesDtos(List<CommentDto> repliesDtos) {
		this.repliesDtos = repliesDtos;
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		repliesDtos = dbServices.commentGetRepliesDtos(commentId);
		
		resStatus.setMsg("replies retrieved");
		resStatus.setSuccess(true);
		
     	return SUCCESS;
    }
}
