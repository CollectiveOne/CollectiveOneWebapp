package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.CommentDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("CommentCbtionNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^resStatus.*"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class CommentCbtionNew extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private CommentDto commentDto = new CommentDto();
	public CommentDto getCommentDto() {
		return commentDto;
	}
	public void setCommentDto(CommentDto commentDto) {
		this.commentDto = commentDto;
	}
	
	/* Output Json  */
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.userGet(commentDto.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " comment creator is not logged in");
			return SUCCESS;
		}
		
		resStatus = dbServices.commentCbtionCreate(commentDto);
		
		return SUCCESS;
    }

}
