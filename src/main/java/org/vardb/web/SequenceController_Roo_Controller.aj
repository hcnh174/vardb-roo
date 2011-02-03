// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.web;

import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.vardb.resources.Family;
import org.vardb.resources.Pathogen;
import org.vardb.sequences.Sequence;

privileged aspect SequenceController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String SequenceController.create(@Valid Sequence sequence, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("sequence", sequence);
            return "sequences/create";
        }
        sequence.persist();
        return "redirect:/sequences/" + encodeUrlPathSegment(sequence.getId().toString(), request);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String SequenceController.createForm(Model model) {
        model.addAttribute("sequence", new Sequence());
        return "sequences/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String SequenceController.show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("sequence", Sequence.findSequence(id));
        model.addAttribute("itemId", id);
        return "sequences/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String SequenceController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("sequences", Sequence.findSequenceEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Sequence.countSequences() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("sequences", Sequence.findAllSequences());
        }
        return "sequences/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String SequenceController.update(@Valid Sequence sequence, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("sequence", sequence);
            return "sequences/update";
        }
        sequence.merge();
        return "redirect:/sequences/" + encodeUrlPathSegment(sequence.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String SequenceController.updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("sequence", Sequence.findSequence(id));
        return "sequences/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String SequenceController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Sequence.findSequence(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/sequences?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("familys")
    public Collection<Family> SequenceController.populateFamilys() {
        return Family.findAllFamilys();
    }
    
    @ModelAttribute("pathogens")
    public Collection<Pathogen> SequenceController.populatePathogens() {
        return Pathogen.findAllPathogens();
    }
    
    String SequenceController.encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
