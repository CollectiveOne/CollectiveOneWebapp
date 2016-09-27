package coproject.cpweb.actions.json;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("CommentPromote")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^resStatus.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class CommentPromote extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input parameters  */
	private int commentId;
	private boolean promoteUp;
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public boolean isPromoteUp() {
		return promoteUp;
	}
	public void setPromoteUp(boolean promoteUp) {
		this.promoteUp = promoteUp;
	}

	
	/* Output parameters  */

	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) getSession().get("userLoggedDto");
		
		if(userLoggedDtoses != null) {
			resStatus = dbServices.commentPromote(commentId, userLoggedDtoses.getId(), promoteUp);
		} else {
			resStatus.setSuccess(false);
			resStatus.setMsg("please login to promote items");
		}
		
     	return SUCCESS;
    }

}
