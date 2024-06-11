package org.zerock.springex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.PageRequestDTO;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.service.TodoService;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @RequestMapping("/list")
    public void list(@Valid PageRequestDTO pageRequestDTO
            , BindingResult bindingResult
            , Model model) {
        log.info("todo list..............");

        if (bindingResult.hasErrors()) {
            pageRequestDTO = PageRequestDTO.builder().build();
        }

        model.addAttribute("responseDTO", todoService.getList(pageRequestDTO));
    }

    //    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @GetMapping("/register")
    public void registerGET() {
        log.info("GET todo register..........");
    }

    @PostMapping("/register")
    public String registerPost(@Valid TodoDTO todoDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("POST todo register..........");

        if (bindingResult.hasErrors()) {
            log.info("has errors.....................");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/todo/register";
        }
        log.info(todoDTO);

        todoService.register(todoDTO);

        return "redirect:/todo/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long tno, PageRequestDTO pageRequestDTO, Model model) {

        TodoDTO todoDTO = todoService.getOne(tno);
        log.info(todoDTO);

        model.addAttribute("dto", todoDTO);
    }

    @PostMapping("/remove")
    public String remove(Long tno, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        log.info("-----------------remove----------------");
        log.info("tno={}", tno);

        todoService.remove(tno);

        redirectAttributes.addAttribute("page", 1);
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());

        return "redirect:/todo/list";
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid TodoDTO todoDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("has errors.............");
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("tno", todoDTO.getTno());
            return "redirect:/todo/modify";
        }

        log.info(todoDTO);

        todoService.modify(todoDTO);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());

        return "redirect:/todo/read?tno=" + todoDTO.getTno();

    }

    @GetMapping("/ex1")
    public void ex1(String name, int age) {
        log.info("ex1...............");
        log.info("name={}", name);
        log.info("age={}", age);
    }

    @GetMapping("/ex2")
    public void ex2(@RequestParam(name = "name", defaultValue = "AAA") String name,
                    @RequestParam(name = "age", defaultValue = "20") int age) {
        log.info("ex2.................");
        log.info("name={}", name);
        log.info("age={}", age);
    }

    @GetMapping("/ex3")
    public void ex3(LocalDate dueDate) {
        log.info("ex3..........");
        log.info("dueDate={}", dueDate);
    }
}
