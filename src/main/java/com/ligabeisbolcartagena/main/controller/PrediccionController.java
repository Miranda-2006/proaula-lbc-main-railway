package com.ligabeisbolcartagena.main.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ligabeisbolcartagena.main.model.Temporada;
import com.ligabeisbolcartagena.main.repository.mongo.TemporadaRepository;
import com.ligabeisbolcartagena.main.service.WekaService;

@Controller
@RequestMapping("/prediccion")
public class PrediccionController {

	@Autowired
	private WekaService wekaService;
	
	@GetMapping("")
	public String index() {
	return "prediccion";
	}

	@PostMapping("/resultado")
	public String resultado(
	        @RequestParam String localVisitante,
	        @RequestParam double rachaUlt3,
	        @RequestParam double promFavor,
	        @RequestParam double promContra,
	        @RequestParam String rivalFuerza,
	        Model model) throws Exception {

	    WekaService.Prediccion pred = wekaService.predecir(localVisitante, rachaUlt3, promFavor, promContra, rivalFuerza);

	    // Mandamos al modelo los datos y porcentaje
	    model.addAttribute("prediccionClase", pred.getClase());
	    model.addAttribute("confianza", pred.getConfianza());
	    model.addAttribute("localVisitante", pred.getLocalVisitante());
	    model.addAttribute("rachaUlt3", pred.getRachaUlt3());
	    model.addAttribute("promFavor", pred.getPromFavor());
	    model.addAttribute("promContra", pred.getPromContra());
	    model.addAttribute("rivalFuerza", pred.getRivalFuerza());

	    return "prediccion";
	}


}
