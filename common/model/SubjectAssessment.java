package common.model;

import common.Tablable;

public class SubjectAssessment implements Tablable {

    private int subjectId;
    private String subjectName;
    private String assessmentId;
    private String type;
    private String topic;
    private String format;
    private String dueDate;

    public SubjectAssessment(int subjectId, String subjectName, String assessmentId, String type, String topic, String format, String dueDate) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.assessmentId = assessmentId;
        this.type = type;
        this.topic = topic;
        this.format = format;
        this.dueDate = dueDate;
    }


    @Override
    public String[] toTable() {
        return new String[]{"" + subjectId, subjectName, assessmentId, type, topic, format, dueDate};
    }
}
