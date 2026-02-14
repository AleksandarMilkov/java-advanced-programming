package OopDesign.FacultyApp;

import java.util.*;
import java.util.stream.Collectors;

class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Student{
    private String id;
    private int yearsOfStudy;
    private Map<Course, Integer> courseToGrade;
    private Map<Integer, Integer> gradesPerTerm;
    private Map<Integer, List<Course>> coursesPerTerm;

    public Student(String id, int yearsOfStudy) {
        this.id = id;
        this.yearsOfStudy = yearsOfStudy;
        courseToGrade = new HashMap<>();
        gradesPerTerm = new HashMap<>();
        coursesPerTerm = new HashMap<>();
    }
    public void addGrade(Course courseName, int term, int grade) throws OperationNotAllowedException {
        if(gradesPerTerm.containsKey(term)) {
            if(gradesPerTerm.get(term) >= 3) {
                throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", this.id, term));
            }
        }
        if(term>yearsOfStudy*2) {
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, this.id));
        }
        if(!coursesPerTerm.containsKey(term)) {
            coursesPerTerm.put(term, new ArrayList<>());
        }
        coursesPerTerm.get(term).add(courseName);
        courseToGrade.put(courseName, grade);
        if(gradesPerTerm.containsKey(term)) {
            gradesPerTerm.replace(term, gradesPerTerm.get(term) + 1);
        } else {
            gradesPerTerm.put(term, 1);
        }
    }
    public boolean checkGraduate(){
        return courseToGrade.values().size() == yearsOfStudy * 6;
    }

    public String getReport(){
        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(this.id).append("\n");
        for(int i : gradesPerTerm.keySet()) {
            sb.append("Term ").append(i).append("\n");
            sb.append("Courses: ").append(this.gradesPerTerm.get(i)).append("\n");
            sb.append("Average grade for term: ").append(String.format("%.2f", getAverageGradePerTerm(i))).append("\n");
        }
        sb.append("Average grade: ").append(getAverageGrade()).append("\n");
        sb.append("Courses attended: ");
        courseToGrade.keySet().stream()
                .sorted(Comparator.comparing(Course::getCourseName))
                .forEach(k -> sb.append(k.getCourseName()).append(","));
        if(!courseToGrade.isEmpty()) {
            sb.replace(sb.length()-1, sb.length(), "");
        }
        return sb.toString();
    }

    public float getAverageGradePerTerm(int term) {
        if (!gradesPerTerm.containsKey(term) || gradesPerTerm.get(term)==0) return 5f;
        float average=0;
        for(Course c : coursesPerTerm.get(term)){
            average+=courseToGrade.get(c);
        }
        return average/gradesPerTerm.get(term);
    }

    public String getId() {
        return id;
    }

    public int getYearsOfStudy() {
        return yearsOfStudy;
    }
    public float getAverageGrade() {
        if (courseToGrade.values().isEmpty()) return 5f;
        return (float) courseToGrade.values().stream().mapToInt(g -> g).sum() /courseToGrade.values().size();
    }

    public Map<Course, Integer> getCourseToGrade() {
        return courseToGrade;
    }
    public float getGradeOfCourse(Course course) {
        return courseToGrade.get(course);
    }
}

class Course{
    private String courseName;
    private List<Student> students;

    public Course(String courseName) {
        this.courseName = courseName;
        students = new ArrayList<>();
    }
    public void addStudent(Student student) {
        students.add(student);
    }

    public String getCourseName() {
        return courseName;
    }
    public int getStudentCount() {
        return students.size();
    }
    public float getAverageGrade(){
        if(students.isEmpty()) return 5f;
        float average=0;
        for(Student s : students){
            average+=s.getGradeOfCourse(this);
        }
        return average/students.size();
    }
}

class Faculty {
    private Map<String, Student> students;
    private Map<String, Course> courses;
    private StringBuilder logs;

    public Faculty() {
        students = new HashMap<>();
        courses = new HashMap<>();
        logs = new StringBuilder();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.put(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        if(!courses.containsKey(courseName)) {
            courses.put(courseName, new Course(courseName));
        }
        Course course = courses.get(courseName);
        Student s = students.get(studentId);
        course.addStudent(s);
        s.addGrade(course, term, grade);
        checkGraduate(s);
    }
    void checkGraduate(Student s) {
        if(!s.checkGraduate()){
            return;
        }
        logs.append(String.format("Student with ID %s graduated with average grade %.2f in %d years\n", s.getId(), s.getAverageGrade(), s.getYearsOfStudy()));
        students.remove(s.getId());
    }

    String getFacultyLogs() {
        return logs.toString();
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).getReport();
    }

    void printFirstNStudents(int n) {
        List<Student> firstN = students.values().stream()
                .sorted(Comparator.comparing((Student student) -> (int) student.getCourseToGrade().keySet().size()).reversed()
                        .thenComparing(Comparator.comparing(Student::getAverageGrade).reversed()))
                .limit(n)
                .collect(Collectors.toList());
        firstN.forEach(s -> System.out.printf("Student: %s Courses passed: %d Average grade: %.2f%n", s.getId(), s.getCourseToGrade().keySet().size(), s.getAverageGrade()));
    }

    void printCourses() {
        courses.values().stream().sorted(Comparator.comparing(Course::getStudentCount).thenComparing(Comparator.comparing(Course::getAverageGrade))).forEach(s -> System.out.println(s.getCourseName()+" "+ s.getStudentCount() + " " + s.getAverageGrade()));
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
