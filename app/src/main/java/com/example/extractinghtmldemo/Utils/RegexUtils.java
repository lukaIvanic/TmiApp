package com.example.extractinghtmldemo.Utils;

import com.example.extractinghtmldemo.enums.AttachmentType;
import com.example.extractinghtmldemo.enums.ProjectStatus;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ExternalReference;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.Project;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Attachment;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexUtils {

    public static String getRepositoryName(String html) {
        List<String> nameHtml = getRegexMatches(html, "navbar-item\">((.|\n)*?)</a");

        if (nameHtml.isEmpty()) {
            return "Repository";
        }

        return nameHtml
                .get(0)
                .trim()
                .replaceFirst("<b>", "")
                .replaceFirst("</b>", "");
    }

    public static List<Project> getProjects(String html) {
        List<Project> projects = new ArrayList<>();
        List<String> projectsHtmlRaw = getRegexMatches(html, "project((.|\n)*?)</a");

        for (int i = 1; i < projectsHtmlRaw.size(); i++) {
            Project project = new Project();
            String projectHtmlRaw = projectsHtmlRaw.get(i);


            List<String> id = getRegexMatches(projectHtmlRaw, "href=\"/tmi(.*?)\"");
            if (!id.isEmpty()) {
                project.setId(id.get(0).trim());
            }

            List<String> name = getRegexMatches(projectHtmlRaw, "name\">((.|\n)*?)</span");
            if (!name.isEmpty()) {
                project.setName(name.get(0).trim());
            }

            List<String> leader = getRegexMatches(projectHtmlRaw, "leader\">((.|\n)*?)</span");
            if (!leader.isEmpty()) {
                project.setLeader(leader.get(0).trim());
            }

            List<String> year = getRegexMatches(projectHtmlRaw, "year\">((.|\n)*?)</span");
            if (!year.isEmpty()) {
                project.setYear(year.get(0).trim());
            }

            List<String> type = getRegexMatches(projectHtmlRaw, "type\">((.|\n)*?)</span");
            if (!type.isEmpty()) {
                project.setType(type.get(0).trim());
            }

            List<String> status = getRegexMatches(projectHtmlRaw, "status is-hidden\\\">((.|\\n)*?)</span");
            if (!status.isEmpty()) {
                project.setProjectStatus(ProjectStatus.valueOf(status.get(0).trim().toUpperCase()));
            }

            projects.add(project);
        }


        return projects;
    }

    public static Project getProjectDetails(String html) {

        List<String> phaseHtmls = getRegexMatches(html, "href=\"(/(.){3}/(.){8}/(.){8}/\"(.|\n)*?)</a");

        List<Phase> phases = new ArrayList<>();

        for (String phaseHtml : phaseHtmls) {
            Phase phase = new Phase();

            List<String> phasesNamesList = getRegexMatches(phaseHtml, "me\" >((.|\n)*?)<");
            if (phasesNamesList.size() > 0) {
                phase.setPhaseName(phasesNamesList.get(0).trim());
            }

            phase.setPhaseId(phaseHtml.substring(5, 23));

            boolean d = phaseHtml.contains("contains attachments");

            phase.setHasAttachment(d);
            phases.add(phase);
        }

        List<ExternalReference> referencesList = RegexUtils.getExternalReferences(html);

        Project project = new Project("", "", phases, referencesList);

        return project;
    }


    public static List<String> getPhasesNames(String html) {
        List<String> projectNamesList = getRegexMatches(html, "me\" >((.|\n)*?)<");
        return projectNamesList.stream()
                .map(s -> s.trim())
                .collect(Collectors.toList());
    }

    public static List<String> getPhasesLinkIds(String html) {
        return getRegexMatches(html, "href=\"/tmi/((.){8}/(.){8})");
    }

    public static List<String> getPhasePdfsLinks(String html) {
        return getRegexMatches(html, "embed\\(\"(.*?)\"");
    }

    public static Phase getPhaseDetails(String html) {
        List<String> stepsHtml = getRegexMatches(html, "href=\"(/(.){3}/(.){8}/(.){8}/(.){8}/\"(.|\n)*?)</a");

        List<Step> steps = new ArrayList<>();

        for (String stepHtml : stepsHtml) {
            Step step = new Step();

            String stepId = stepHtml.substring(5, 31);
            step.setStepId(stepId);

            List<String> stepName = getRegexMatches(stepHtml, "name\" >((.|\n)*?)</span");
            if (stepName.size() > 0) {
                step.setStepName(stepName.get(0).trim());
            }

            List<String> stepStatus = getRegexMatches(stepHtml, "title=\"(.*?)\"");
            if (stepStatus.size() > 0) {
                step.setStatus(ProjectStatus.valueOf(stepStatus.get(0).trim().toUpperCase()));
            }

            step.setHasAttachment(stepHtml.contains("contains attachments"));

            steps.add(step);

        }

        List<ExternalReference> externalReferences = getExternalReferences(html);

        return new Phase("", "", steps, externalReferences);
    }


    public static Step getStepDetails(String html) {
        List<String> attachmentsHtmls = getRegexMatches(html, "href=\"(/(.){3}/(.){8}/(.){8}/(.){8}(.|\n)*?)</a");

        List<Attachment> attachmentsList = new ArrayList<>();

        for (int i = 1; i < attachmentsHtmls.size(); i++) {
            Attachment attachment = new Attachment();
            List<String> name = getRegexMatches(attachmentsHtmls.get(i), "/(.*?)\"");
            if (name.size() > 0) {
                attachment.setAttachmentName(name.get(0).substring(31, name.get(0).length()));
                attachment.setAttachmentUrlLink("https://tmi.tickmark-software.com/" + name.get(0));

                if (attachmentsHtmls.get(i).contains(".docx") || attachmentsHtmls.get(i).contains(".doc")) {
                    attachment.setAttachmentType(AttachmentType.DOCX);
                } else if (attachmentsHtmls.get(i).contains(".pdf")) {
                    attachment.setAttachmentType(AttachmentType.PDF);
                } else if(attachmentsHtmls.get(i).contains(".xlsx")) {
                    attachment.setAttachmentType(AttachmentType.XLSX);
                } else{
                    attachment.setAttachmentType(AttachmentType.UNKNOWN);
                }

                attachmentsList.add(attachment);
            }
        }

        List<ExternalReference> externalReferences = getExternalReferences(html);

        return new Step(attachmentsList, externalReferences);
    }

    public static List<ExternalReference> getExternalReferences(String html) {
        List<String> ulLists = getRegexMatches(html, "<ul>((.|\n)*?)</ul");
        if (ulLists.size() <= 1) return new ArrayList<>();
        String referenceUlList = ulLists.get(1);
        return getRegexMatches(referenceUlList, "<li>((.|\n)*?)</li>")
                .stream()
                .map((Function<String, ExternalReference>) s -> new ExternalReference(s))
                .collect(Collectors.toList());
    }


    private static List<String> getRegexMatches(String s, String regex) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        List<String> spansList = new ArrayList<>();

        while (matcher.find()) {
            String span = matcher.group(1);
            spansList.add(span);
        }
        return spansList;
    }


}
