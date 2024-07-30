package org.sample.students.dal.model;

import java.util.Objects;

public class Student implements Comparable<Student> {
    private String id;
    private String name;
    private Integer score;

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Student student) {
        if (this.getScore() > student.getScore()) {
            return -1;
        } else if (this.getScore() < student.getScore()) {
            return 1;
        } else {
            return this.getId().compareTo(student.getId());
        }
    }

    public Student() {

    }

    public Student(String id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
