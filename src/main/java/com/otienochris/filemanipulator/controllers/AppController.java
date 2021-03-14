package com.otienochris.filemanipulator.controllers;

import com.otienochris.filemanipulator.Models.Document;
import com.otienochris.filemanipulator.repositories.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private DocumentRepo documentRepo;

    @GetMapping
    public String viewHomePage(Model model){
        List<Document> listDocs = documentRepo.findAll();
        model.addAttribute("listDocs", listDocs);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document")MultipartFile multipartFile, RedirectAttributes ra) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        Document document = Document.builder()
                .name(filename)
                .content(multipartFile.getBytes())
                .size(multipartFile.getSize())
                .uploadTime(new Date())
                .build();
        documentRepo.save(document);

        ra.addFlashAttribute("message", "The file has been uploaded successfully.");
        return "redirect:/";
    }

    @GetMapping("/download")
    public void downloadFile(@PathParam("id") Long id, HttpServletResponse response){
        documentRepo.findById(id).
                ifPresentOrElse(
                        document -> {
                            response.setContentType("application/octet-stream");
                            String headerKey =  "Content-Disposition";
                            String headerValue = "attachment; filename=" + document.getName();
                            response.setHeader(headerKey, headerValue);

                            try {
                                ServletOutputStream outputStream = response.getOutputStream();
                                outputStream.write(document.getContent());
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },
                        () -> {
                            try {
                                throw new Exception("Could not find document with id : " + id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                });
    }
}
