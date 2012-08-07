package org.beijingair.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AirDataSearchController {
	
	@RequestMapping(method=RequestMethod.GET, value="/search")
	public String initForm(Model model) {
		model.addAttribute("searchFormInfo", new SearchFormInfo());
		return "search";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/search")
	public String updateForm(@Validated SearchFormInfo searchFormInfo, BindingResult result) {
		if (result.hasErrors())
			return "search";
		else
			return "ok";
	}
	

}
