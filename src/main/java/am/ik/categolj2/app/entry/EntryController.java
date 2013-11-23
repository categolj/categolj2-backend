package am.ik.categolj2.app.entry;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("entries")
public class EntryController {

	public EntryForm setupForm() {
		EntryForm form = new EntryForm();
		return form;
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm() {
		return "entries/createForm";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Validated EntryForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "entries/createForm";
		}
		return "redirect:/entries/?createComplete";
	}

	@RequestMapping(params = "createComplete", method = RequestMethod.GET)
	public String createComplete() {
		return "entries/createForm";
	}
}
