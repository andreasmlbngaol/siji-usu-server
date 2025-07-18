package com.sanalab.sijiusu.siji_admin.users.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sanalab.sijiusu.core.util.Routing;
import com.sanalab.sijiusu.siji_admin.users.service.AdminUsersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Routing.ADMINS_USERS)
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @Autowired
    public AdminUsersController(AdminUsersService adminUsersService) {
        this.adminUsersService = adminUsersService;
    }

    public record AddStudentPayload(
        @NotNull String name,
        @Email String email,
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit."
        )
        @NotNull String password,
        @NotNull Integer year,
        @NotNull String nim,
        @JsonProperty("major_id")
        @NotNull Long majorId,
        @JsonProperty("academic_advisor_id")
        Long academicAdvisorId
    ) { }

    @PostMapping(Routing.STUDENTS)
    @ResponseStatus(HttpStatus.CREATED)
    public void addStudent(
        @Valid @RequestBody AddStudentPayload payload
    ) {
        adminUsersService.addStudent(
            payload.name,
            payload.email,
            payload.password,
            payload.nim,
            payload.majorId,
            payload.academicAdvisorId,
            payload.year
        );
    }

    @GetMapping(Routing.STUDENTS)
    public List<StudentDto> getAllStudents(
        @RequestParam(value = "name", required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return adminUsersService.getStudentsByNameLike(name);
        }
        return adminUsersService.getAllStudents();
    }

    @GetMapping(Routing.STUDENTS + "/{id}")
    public StudentDto getStudentById(@PathVariable Long id) {
        return adminUsersService.getStudentById(id);
    }

    public record UpdateStudentPayload(
        String name,
        @Email String email,
        String nim,
        @JsonProperty("academic_advisor_id")
        Long academicAdvisorId
    ) { }

    @PatchMapping(Routing.STUDENTS + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStudent(
        @PathVariable Long id,
        @Valid @RequestBody UpdateStudentPayload payload
    ) {
        adminUsersService.updateStudent(id, payload.name, payload.email, payload.nim, payload.academicAdvisorId);
    }


    public record LecturerSumDto(
        Long id,
        String name
    ) { }

    public record CourseSectionTakenDto(
        Long id,
        @JsonProperty("course_name")
        String courseName,
        @JsonProperty("section_name")
        String sectionName,
        String room,
        String lecturer
    ) { }

    public record StudentDto(
        Long id,
        String name,
        String email,
        String nim,
        String faculty,
        String major,
        @JsonProperty("academic_advisor")
        LecturerSumDto academicAdvisor,
        @JsonProperty("courses_taken")
        List<CourseSectionTakenDto> coursesTaken
    ) { }

    public record AddLecturerPayload(
        @NotNull String name,
        @Email String email,
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit."
        )
        @NotNull String password,
        @NotNull String nip,
        @NotNull String nidn,
        @JsonProperty("department_id")
        @NotNull Long departmentId
    ) { }

    @PostMapping(Routing.LECTURERS)
    @ResponseStatus(HttpStatus.CREATED)
    public void addLecturer(
        @Valid @RequestBody AddLecturerPayload payload
    ) {
        adminUsersService.addLecturer(
            payload.name,
            payload.email,
            payload.password,
            payload.nip,
            payload.nidn,
            payload.departmentId
        );
    }

    public record StudentSumDto(
        Long id,
        String name,
        String nim
    ) { }

    public record LecturerDto(
        Long id,
        String name,
        String email,
        String nip,
        String nidn,
        String faculty,
        String department,
        @JsonProperty("advised_students")
        List<StudentSumDto> advisedStudents,
        @JsonProperty("courses_taught")
        List<CourseSectionTakenDto> coursesTaught
    ) { }

    @GetMapping(Routing.LECTURERS)
    public List<LecturerDto> getAllLecturers(
        @RequestParam(value = "name", required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return adminUsersService.getLecturersByNameLike(name);
        }
        return adminUsersService.getAllLecturers();
    }

    @GetMapping(Routing.LECTURERS + Routing.DEPARTMENTS + "/{id}")
    public List<LecturerDto> getLecturersByDepartment(
            @PathVariable Long id
    ) {
        return adminUsersService.getLecturersByMajorId(id);
    }

    @GetMapping(Routing.LECTURERS + "/{id}")
    public LecturerDto getLecturerById(@PathVariable Long id) {
        return adminUsersService.getLecturerById(id);
    }

    public record UpdateLecturerPayload(
        String name,
        @Email String email,
        String nip,
        String nidn
//        @JsonProperty("department_id")
//        Long departmentId
    ) { }

    @PatchMapping(Routing.LECTURERS + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLecturer(
        @PathVariable Long id,
        @Valid @RequestBody UpdateLecturerPayload payload
    ) {
        adminUsersService.updateLecturer(id, payload.name, payload.email, payload.nip, payload.nidn);
    }

    public record UserDto(
        Long id,
        String name,
        String email,
        String role
    ) { }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return adminUsersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return adminUsersService.getUserById(id);
    }


}
