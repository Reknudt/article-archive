package com.archive.articleArchive.app.controller;

import com.archive.articleArchive.app.models.ResourceNotFoundException;
import com.archive.articleArchive.app.models.Article;
import com.archive.articleArchive.app.models.Publication;
import com.archive.articleArchive.app.models.PublicationType;
import com.archive.articleArchive.app.models.Student;
import com.archive.articleArchive.app.models.Thesis;
import com.archive.articleArchive.app.repository.PublicationRepository;
import com.archive.articleArchive.app.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/publications")
public class PublicationController {
    private final PublicationRepository publicationRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public PublicationController(PublicationRepository publicationRepository,
                                 StudentRepository studentRepository) {
        this.publicationRepository = publicationRepository;
        this.studentRepository = studentRepository;
    }

    // 1. Просмотр всех публикаций (с фильтрами)
    @GetMapping
    public String listPublications(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PublicationType type,
            @RequestParam(required = false) String studentName,
            Model model) {

        List<Publication> publications;

        if (search != null && !search.isEmpty()) {
            publications = publicationRepository.findByTitleContainingIgnoreCase(search);
        } else if (type != null) {
            publications = publicationRepository.findByType(type);
        } else if (studentName != null && !studentName.isEmpty()) {
            publications = publicationRepository.findByStudentFullNameContainingIgnoreCase(studentName);
        } else {
            publications = publicationRepository.findAll();
        }

        model.addAttribute("publications", publications);
        model.addAttribute("publicationTypes", PublicationType.values());
        return "publications/list";
    }

    // 2. Просмотр конкретной публикации
    @GetMapping("/{id}")
    public String viewPublication(@PathVariable Long id, Model model) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found with id: " + id));

        model.addAttribute("publication", publication);
        return "publications/view";
    }

    // 3. Форма добавления статьи (GET)
    @GetMapping("/add/article")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("students", studentRepository.findAll());
        return "publications/add-article";
    }

    // 4. Добавление статьи (POST)
    @PostMapping("/add/article")
    public String addArticle(@Valid @ModelAttribute Article article,
                             BindingResult result,
                             @RequestParam Long studentId) {
        if (result.hasErrors()) {
            return "publications/add-article";
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        article.setStudent(student);

        publicationRepository.save(article);
        return "redirect:/publications";
    }

    // 5. Форма добавления тезисов (GET)
    @GetMapping("/add/thesis")
    public String showAddThesisForm(Model model) {
        model.addAttribute("thesis", new Thesis());
        model.addAttribute("students", studentRepository.findAll());
        return "publications/add-thesis";
    }

    // 6. Добавление тезисов (POST)
    @PostMapping("/add/thesis")
    public String addThesis(@Valid @ModelAttribute Thesis thesis,
                            BindingResult result,
                            @RequestParam Long studentId) {
        if (result.hasErrors()) {
            return "publications/add-thesis";
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        thesis.setStudent(student);

        publicationRepository.save(thesis);
        return "redirect:/publications";
    }

    // 7. Форма редактирования публикации
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        if (publication instanceof Article) {
            model.addAttribute("article", publication);
            model.addAttribute("students", studentRepository.findAll());
            return "publications/edit-article";
        } else {
            model.addAttribute("thesis", publication);
            model.addAttribute("students", studentRepository.findAll());
            return "publications/edit-thesis";
        }
    }

    // 8. Обновление публикации
    @PostMapping("/{id}/update")
    public String updatePublication(@PathVariable Long id,
                                    @Valid @ModelAttribute Publication publicationDetails,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return publicationDetails instanceof Article ?
                    "publications/edit-article" : "publications/edit-thesis";
        }

        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        // Обновляем общие поля
        publication.setTitle(publicationDetails.getTitle());
        publication.setOutputData(publicationDetails.getOutputData());

        // Для статей обновляем специфичные поля
        if (publication instanceof Article && publicationDetails instanceof Article) {
            ((Article) publication).setArticleType(((Article) publicationDetails).getArticleType());
        }

        // Для тезисов обновляем специфичные поля
        if (publication instanceof Thesis && publicationDetails instanceof Thesis) {
            ((Thesis) publication).setConferenceName(((Thesis) publicationDetails).getConferenceName());
            ((Thesis) publication).setConferenceDate(((Thesis) publicationDetails).getConferenceDate());
        }

        publicationRepository.save(publication);
        return "redirect:/publications/" + id;
    }

    // 9. Удаление публикации
    @PostMapping("/{id}/delete")
    public String deletePublication(@PathVariable Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        publicationRepository.delete(publication);
        return "redirect:/publications";
    }

    // 10. Скачивание публикации (пример)
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadPublication(@PathVariable Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        // В реальном приложении здесь должна быть логика получения файла
        String fileName = publication.getTitle() + ".pdf";
        ByteArrayResource resource = new ByteArrayResource(new byte[0]); // Заглушка

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleException(ResourceNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}