package org.collectiveone.web.controllers.views;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.collectiveone.services.DbServicesImp;
import org.collectiveone.web.dto.CbtionDto;
import org.collectiveone.web.dto.DecisionDto;
import org.collectiveone.web.dto.GoalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/views")
public class ViewsController {
	
	@Autowired
	DbServicesImp dbServices;
	
	@RequestMapping("/activityListPageR")
	public String activityList(Model model) {
		return "views/activityListPage";
	}
	
	@RequestMapping("/cbtionListPageR")
	public String cbtionListPage(Model model) {
		return "views/cbtionListPage";
	}
	
	@RequestMapping("/goalListPageR")
	public String goalListPageR(Model model) {
		return "views/goalListPage";
	}
	
	@RequestMapping("/cbtionPageR/{id}")
	public String cbtionPage(@PathVariable(value="id") Long id, Model model) {
		CbtionDto ctionDto = dbServices.cbtionGetDto(id);  
		
		model.addAttribute("cbtionTitle",ctionDto.getTitle());
		model.addAttribute("cbtionId",ctionDto.getId());
		
		return "views/cbtionPage";
	}
	
	@RequestMapping("/goalPageR")
	public String goalPage(@PathParam("goalTag") String goalTag, @PathParam("projectName") String projectName, Model model) {
		model.addAttribute("goalTag",goalTag);
		model.addAttribute("projectName",projectName);
		
		return "views/goalPage";
	}
	
	@RequestMapping("/project/{projectName}")
	public String projectPageR(@PathVariable("projectName") String projectName, Model model) {
		model.addAttribute("projectName",projectName);
		return "views/projectPage";
	}
	
	@RequestMapping("/decisionPageR/{id}")
	public String decisionPage(@PathVariable(value="id") Long id, Model model) {
		DecisionDto decision = dbServices.decisionGetDto(id);  
		model.addAttribute("decisionId",decision.getId());
		return "views/decisionPage";
	}
	
	@RequestMapping("/decisionListPageR")
	public String decisionListPage(Model model) {
		return "views/decisionListPage";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping("/cbtionNewPageR")
	public String cbtionNewPage(Model model) {
		model.addAttribute("cbtion",new CbtionDto());
		return "views/cbtionNewPage";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/cbtionNewSubmit", method = RequestMethod.POST)
	public String cbtionNewSubmit(@Valid CbtionDto cbtionDto) {
		
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		cbtionDto.setCreatorUsername(auth.getName()); 
		Long cbtionId = dbServices.cbtionCreate(cbtionDto);
		return "redirect:/views/cbtionPageR/"+cbtionId;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping("/goalNewPageR")
	public String goalNewPage(Model model) {
		model.addAttribute("goal",new GoalDto());
		return "views/goalNewPage";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goalNewSubmit", method = RequestMethod.POST)
	public String goalNewSubmit(@Valid GoalDto goalDto) {
		
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		goalDto.setCreatorUsername(auth.getName()); 
		
		dbServices.goalCreate(goalDto);
		return "redirect:/views/goalListPageR";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping("/decisionNewPageR")
	public String decisionNewPage(Model model) {
		model.addAttribute("decision",new DecisionDto());
		return "views/decisionNewPage";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/decisionNewSubmit", method = RequestMethod.POST)
	public String decisionNewSubmit(@Valid DecisionDto decisionDto) {
		
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		decisionDto.setCreatorUsername(auth.getName()); 
		
		Long decisionId = dbServices.decisionCreate(decisionDto);
		return "redirect:/views/decisionPageR/"+decisionId;
	}
	
	
}
