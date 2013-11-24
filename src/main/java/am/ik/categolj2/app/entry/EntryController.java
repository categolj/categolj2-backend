package am.ik.categolj2.app.entry;

import java.util.List;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.service.entry.EntryService;

@Controller
@RequestMapping("entries")
public class EntryController {
	@Inject
	EntryService entryService;
	@Inject
	Mapper beanMapper;

	@ModelAttribute
	public EntryForm setupForm() {
		EntryForm form = new EntryForm();
		return form;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, @PageableDefault Pageable pageable) {
		Page<Entry> page = entryService.findPage(pageable);
		model.addAttribute("page", page);
		return "entries/list";
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm() {
		return "entries/createForm";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Validated EntryForm form, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "entries/createForm";
		}
		Entry entry = beanMapper.map(form, Entry.class);
		List<Category> categories = entry.getCategory();
		entry.setCategory(null);

		Entry created = entryService.create(entry, categories);
		attributes.addFlashAttribute(created);
		return "redirect:/entries/?createComplete";
	}

	@RequestMapping(params = "createComplete", method = RequestMethod.GET)
	public String createComplete() {
		return "entries/createComplete";
	}

	@RequestMapping(value = "{entryId}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("entryId") Integer entryId,
			EntryForm form, Model model) {
		Entry entry = entryService.findOne(entryId);
		beanMapper.map(entry, form);
		model.addAttribute(entry);
		return "entries/updateForm";
	}

	@RequestMapping(value = "{entryId}", method = RequestMethod.POST)
	public String update(@PathVariable("entryId") Integer entryId,
			@Validated EntryForm form, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "entries/updateForm";
		}
		Entry entry = beanMapper.map(form, Entry.class);
		new Categories(entry.getCategory()).applyEntryId(entryId);
		Entry updated = entryService.update(entryId, entry,
				form.isUpdateLastModifiedDate(), form.isSaveInHistory());

		attributes.addFlashAttribute(updated);
		return "redirect:/entries/{entryId}?updateComplete";
	}

	@RequestMapping(value = "{entryId}", params = "updateComplete", method = RequestMethod.GET)
	public String updateComplete(@PathVariable("entryId") Integer entryId) {
		return "entries/updateComplete";
	}

	@RequestMapping(value = "{entryId}", params = "delete", method = RequestMethod.POST)
	public String delete(@PathVariable("entryId") Integer entryId) {
		entryService.delete(entryId);
		return "redirect:/entries/{entryId}?deleteComplete";
	}

	@RequestMapping(value = "{entryId}", params = "deleteComplete", method = RequestMethod.GET)
	public String deleteComplete(@PathVariable("entryId") Integer entryId) {
		return "entries/deleteComplete";
	}
}
