package com.archisacademy.employee.helpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfHelper {

    public static void generatePerformanceReport(String filePath, String employeeName, double goalCompletionRate, double taskCompletionRate, String goalGraph, String taskGraph) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Performance Report for " + employeeName, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Goal Completion Rate: " + goalCompletionRate + "%"));
            document.add(new Paragraph("Task Completion Rate: " + taskCompletionRate + "%"));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Goal Completion Graph:"));
            document.add(new Paragraph(goalGraph));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Task Completion Graph:"));
            document.add(new Paragraph(taskGraph));

            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
