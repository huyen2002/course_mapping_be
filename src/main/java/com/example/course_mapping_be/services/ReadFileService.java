package com.example.course_mapping_be.services;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.annotation.RegEx;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReadFileService {
    public String readPdfFile(String url) {
        try {
            URL urlFile = new URL(url);

            InputStream in = urlFile.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();

            byte[] bytes = baos.toByteArray();
            PDDocument doc = Loader.loadPDF(bytes);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            doc.close();
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readDocFile(String url) {

        try {
            URL urlFile = new URL(url);
            URLConnection connection = urlFile.openConnection();
            InputStream inputStream = connection.getInputStream();

            HWPFDocument doc = new HWPFDocument(inputStream);

            WordExtractor we = new WordExtractor(doc);

            String[] paragraphs = we.getParagraphText();

            System.out.println("Total no of paragraph " + paragraphs.length);
            StringBuilder text = new StringBuilder();
            for (String para : paragraphs) {
                text.append(para);
            }
            inputStream.close();
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readDocxFile(String url) {

        try {
            URL urlFile = new URL(url);
            URLConnection connection = urlFile.openConnection();
            InputStream inputStream = connection.getInputStream();

            XWPFDocument document = new XWPFDocument(inputStream);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            System.out.println("Total no of paragraph " + paragraphs.size());
            StringBuilder text = new StringBuilder();
            for (XWPFParagraph para : paragraphs) {
                text.append(para.getText());
            }

            inputStream.close();
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readTxtFile(String url) {
        try {
            URL urlFile = new URL(url);
            URLConnection connection = urlFile.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            inputStream.close();
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readData(String url) throws MalformedURLException, UnsupportedEncodingException {
        URL urlFile = new URL(url);
        String path = urlFile.getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        String fileName = decodedPath.substring(decodedPath.lastIndexOf('/') + 1);
        System.out.println("File name: " + fileName);
        //check type of file
        String[] parts = fileName.split("\\.");
        String extension = parts[parts.length - 1];
        System.out.println("Extension: " + extension);

        return switch (extension) {
            case "pdf" -> readPdfFile(url);
            case "doc" -> readDocFile(url);
            case "docx" -> readDocxFile(url);
            case "txt" -> readTxtFile(url);
            default -> {
                System.out.println("This is not a supported file");
                yield null;
            }
        };

    }
}
