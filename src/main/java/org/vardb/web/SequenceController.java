package org.vardb.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vardb.sequences.Sequence;

@RooWebScaffold(path = "sequences", formBackingObject = Sequence.class)
@RequestMapping("/sequences")
@Controller
public class SequenceController {
}
