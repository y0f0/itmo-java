package info.kgeorgiy.ja.pologov.student;


import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {
    private final Comparator<Student> comparator = Comparator
            .comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            // :NOTE: Упростить
            .thenComparing((Student s1, Student s2) -> Integer.compare(s2.getId(), s1.getId()))
            .reversed();

    private <T> List<T> getStudentsField(final Collection<Student> students, final Function<Student, T> getter) {
        return students.stream()
                .map(getter)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(final List<Student> students) {
        return getStudentsField(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(final List<Student> students) {
        return getStudentsField(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(final List<Student> students) {
        return getStudentsField(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(final List<Student> students) {
        return getStudentsField(students, s -> s.getFirstName() + " " + s.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(final List<Student> students) {
        return students.stream()
                .map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(final List<Student> students) {
        return students.stream()
                .max(Student::compareTo)
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(final Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getId))
                .collect(Collectors.toList());
    }
    @Override
    public List<Student> sortStudentsByName(final Collection<Student> students) {
        return students.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<Student> findStudentsByField(final Collection<Student> students, final Predicate<Student> predicate) {
        return students.stream()
                .filter(predicate)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // :NOTE: Дубли
    @Override
    public List<Student> findStudentsByFirstName(final Collection<Student> students, final String name) {
        return findStudentsByField(students, student -> student.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(final Collection<Student> students, final String name) {
        return findStudentsByField(students, student -> student.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(final Collection<Student> students, final GroupName group) {
        return findStudentsByField(students, student -> student.getGroup().equals(group));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(final Collection<Student> students, final GroupName group) {
        return students.stream()
                .filter(student -> student.getGroup().equals(group))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName,
                        BinaryOperator.minBy(String::compareTo)));
    }
}
