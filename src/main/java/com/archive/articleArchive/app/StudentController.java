package com.archive.articleArchive.app;

import com.archive.articleArchive.app.models.Article;
import com.archive.articleArchive.app.models.Conference;
import com.archive.articleArchive.app.models.Contest;
import com.archive.articleArchive.app.models.Publication;
import com.archive.articleArchive.app.models.ScientificEvent;
import com.archive.articleArchive.app.models.Student;
import com.archive.articleArchive.app.models.Supervisor;
import com.archive.articleArchive.app.models.Thesis;
import com.archive.articleArchive.app.repository.PublicationRepository;
import com.archive.articleArchive.app.repository.ScientificEventRepository;
import com.archive.articleArchive.app.repository.StudentRepository;
import com.archive.articleArchive.app.repository.SupervisorRepository;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final ScientificEventRepository eventRepository;
    private final PublicationRepository publicationRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository,
                             SupervisorRepository supervisorRepository,
                             ScientificEventRepository eventRepository,
                             PublicationRepository publicationRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.eventRepository = eventRepository;
        this.publicationRepository = publicationRepository;
    }

    // 1. Просмотр списка студентов
    @GetMapping
    public String listStudents(@RequestParam(required = false) String search,
                               @RequestParam(required = false) String faculty,
                               @RequestParam(required = false) String group,
                               Model model) {
        List<Student> students;

        if (search != null && !search.isEmpty()) {
            students = studentRepository.findByFullNameContainingIgnoreCase(search);
        } else if (faculty != null && !faculty.isEmpty()) {
            students = studentRepository.findByFaculty(faculty);
        } else if (group != null && !group.isEmpty()) {
            students = studentRepository.findByGroupName(group);
        } else {
            students = studentRepository.findAll();
        }

        model.addAttribute("students", students);
        model.addAttribute("faculties", studentRepository.findDistinctFaculties());
        model.addAttribute("groups", studentRepository.findDistinctGroupNames());
        return "students/list";
    }

    // 2. Просмотр конкретного студента
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        model.addAttribute("student", student);
        model.addAttribute("events", eventRepository.findByStudentId(id));
        model.addAttribute("publications", publicationRepository.findByStudentId(id));
        model.addAttribute("supervisors", supervisorRepository.findAll());

        // Формы для добавления
        model.addAttribute("newConference", new Conference());
        model.addAttribute("newContest", new Contest());
        model.addAttribute("newArticle", new Article());
        model.addAttribute("newThesis", new Thesis());

        return "students/view";
    }

    // 3. Добавление нового студента
    @PostMapping
    public String addStudent(@Valid @ModelAttribute Student student,
                             BindingResult result,
                             @RequestParam(required = false) Long supervisorId,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("supervisors", supervisorRepository.findAll());
            return "students/list";
        }

        if (supervisorId != null) {
            Supervisor supervisor = supervisorRepository.findById(supervisorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found"));
            student.setSupervisor(supervisor);
        }

        studentRepository.save(student);
        return "redirect:/students";
    }

    // 4. Обновление данных студента
    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute Student studentDetails,
                                BindingResult result,
                                @RequestParam(required = false) Long supervisorId) {
        if (result.hasErrors()) {
            return "redirect:/students/" + id + "?error=update";
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        student.setFullName(studentDetails.getFullName());
        student.setCourse(studentDetails.getCourse());
        student.setGroupName(studentDetails.getGroupName());
        student.setFaculty(studentDetails.getFaculty());

        if (supervisorId != null) {
            Supervisor supervisor = supervisorRepository.findById(supervisorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found"));
            student.setSupervisor(supervisor);
        } else {
            student.setSupervisor(null);
        }

        studentRepository.save(student);
        return "redirect:/students/" + id;
    }

    // 5. Добавление конференции
    @PostMapping("/{id}/add-event/conference")
    public String addConference(@PathVariable Long id,
                                @Valid @ModelAttribute("newConference") Conference conference,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/students/" + id + "?error=conference";
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        conference.setStudent(student);
        eventRepository.save(conference);
        return "redirect:/students/" + id;
    }

    // 6. Добавление конкурса
    @PostMapping("/{id}/add-event/contest")
    public String addContest(@PathVariable Long id,
                             @Valid @ModelAttribute("newContest") Contest contest,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/students/" + id + "?error=contest";
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        contest.setStudent(student);
        eventRepository.save(contest);
        return "redirect:/students/" + id;
    }

    // 7. Добавление статьи
    @PostMapping("/{id}/add-publication/article")
    public String addArticle(@PathVariable Long id,
                             @Valid @ModelAttribute("newArticle") Article article,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/students/" + id + "?error=article";
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        article.setStudent(student);
        publicationRepository.save(article);
        return "redirect:/students/" + id;
    }

    // 8. Добавление тезисов
    @PostMapping("/{id}/add-publication/thesis")
    public String addThesis(@PathVariable Long id,
                            @Valid @ModelAttribute("newThesis") Thesis thesis,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/students/" + id + "?error=thesis";
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        thesis.setStudent(student);
        publicationRepository.save(thesis);
        return "redirect:/students/" + id;
    }

    // 9. Удаление мероприятия
    @PostMapping("/{studentId}/delete-event/{eventId}")
    public String deleteEvent(@PathVariable Long studentId,
                              @PathVariable Long eventId) throws BadRequestException {
        ScientificEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (!event.getStudent().getId().equals(studentId)) {
            throw new BadRequestException("Event does not belong to student");
        }

        eventRepository.delete(event);
        return "redirect:/students/" + studentId;
    }

    // 10. Удаление публикации
    @PostMapping("/{studentId}/delete-publication/{publicationId}")
    public String deletePublication(@PathVariable Long studentId,
                                    @PathVariable Long publicationId) throws BadRequestException {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        if (!publication.getStudent().getId().equals(studentId)) {
            throw new BadRequestException("Publication does not belong to student");
        }

        publicationRepository.delete(publication);
        return "redirect:/students/" + studentId;
    }

    // 11. Изменение научного руководителя
    @PostMapping("/{id}/change-supervisor")
    public String changeSupervisor(@PathVariable Long id,
                                   @RequestParam(required = false) Long supervisorId) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (supervisorId != null) {
            Supervisor supervisor = supervisorRepository.findById(supervisorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found"));
            student.setSupervisor(supervisor);
        } else {
            student.setSupervisor(null);
        }

        studentRepository.save(student);
        return "redirect:/students/" + id;
    }

    // 12. Удаление студента
    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        studentRepository.delete(student);
        return "redirect:/students";
    }

    // Обработка исключений
    @ExceptionHandler({ResourceNotFoundException.class, BadRequestException.class})
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}