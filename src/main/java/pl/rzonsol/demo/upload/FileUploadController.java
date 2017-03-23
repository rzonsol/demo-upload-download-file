package pl.rzonsol.demo.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.rzonsol.demo.storage.StorageFileNotFoundException;
import pl.rzonsol.demo.storage.StorageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by rzonsol on 23.03.2017.
 */

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files")
    @ResponseBody
    public List<String> listUploadFilesJson() {
        List<String> files = storageService.loadAll().map(path ->
                MvcUriComponentsBuilder
                        .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
                        .build().toString()
        ).collect(Collectors.toList());
        return files;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService
                .loadAll()
                .map(path ->
                        MvcUriComponentsBuilder
                                .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
                                .build().toString()
                ).collect(Collectors.toList()));
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filname=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String handelFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "You Succesfull upload " + file.getOriginalFilename() + " !");
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
