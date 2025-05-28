package com.archive.articleArchive.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileRepository fileRepository;

    @GetMapping
    public String listFiles(
            @RequestParam(required = false) String searchBy,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        List<File> files;
        if (keyword != null && !keyword.isEmpty()) {
            switch (searchBy) {
                case "projectName": files = fileRepository.findByProjectNameContainingIgnoreCase(keyword); break;
                case "username": files = fileRepository.findByUsernameContainingIgnoreCase(keyword); break;
                case "url": files = fileRepository.findByUrlContainingIgnoreCase(keyword); break;
                case "faculty": files = fileRepository.findByFacultyContainingIgnoreCase(keyword); break;
                case "studentGroup": files = fileRepository.findByStudentGroupContainingIgnoreCase(keyword); break;
                case "department": files = fileRepository.findByDepartmentContainingIgnoreCase(keyword); break;
                case "projectMasterName": files = fileRepository.findByProjectMasterNameContainingIgnoreCase(keyword); break;
                default: files = fileRepository.findAll();
            }
        } else {
            files = fileRepository.findAll();
        }
        model.addAttribute("files", files);
        return "files";
    }

    @PostMapping("/add")
    public String addFile(File file) {
        fileRepository.save(file);
        return "redirect:/files";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id) {
        fileRepository.deleteById(id);
        return "redirect:/files";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<File> file = fileRepository.findById(id);
        file.ifPresent(value -> model.addAttribute("file", value));
        return "edit-file";
    }

    @PostMapping("/update/{id}")
    public String updateFile(@PathVariable Long id, File updatedFile) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            file.setProjectName(updatedFile.getProjectName());
            file.setUsername(updatedFile.getUsername());
            file.setUrl(updatedFile.getUrl());
            file.setFaculty(updatedFile.getFaculty());
            file.setStudentGroup(updatedFile.getStudentGroup());
            file.setDepartment(updatedFile.getDepartment());
            file.setProjectMasterName(updatedFile.getProjectMasterName());
            fileRepository.save(file);
        }
        return "redirect:/files";
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<File> files = fileRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Учебные работы");

            // Создание заголовков
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Название работы", "Автор", "Ссылка", "Факультет", "Группа", "Кафедра", "Руководитель"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Заполнение данными
            int rowNum = 1;
            for (File file : files) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(file.getId());
                row.createCell(1).setCellValue(file.getProjectName());
                row.createCell(2).setCellValue(file.getUsername());
                row.createCell(3).setCellValue(file.getUrl());
                row.createCell(4).setCellValue(file.getFaculty());
                row.createCell(5).setCellValue(file.getStudentGroup());
                row.createCell(6).setCellValue(file.getDepartment());
                row.createCell(7).setCellValue(file.getProjectMasterName());
            }

            // Автоматическое изменение размера столбцов
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Конвертация в массив байтов
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Настройка HTTP-ответа
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "student_works.xlsx");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        }
    }
}
